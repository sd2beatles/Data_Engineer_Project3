package com.retriveData


import ujson._
import org.joda.time.format.DateTimeFormat
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.matching.Regex
import default._

class HttpItem(symbol:String,apikey:String) {
  var value = new ListBuffer[Map[String, Any]]
  var timeZone: Option[String] = None
  val dateFormatter=DateTimeFormat.forPattern("yyyy-MM-dd")
  val timeFormatter=DateTimeFormat.forPattern("HH:mm:ss")

  val openSeconds=timeFormatter.parseDateTime("09:30:00").getSecondOfDay()
  val closeSeconds=timeFormatter.parseDateTime("16:00:00").getSecondOfDay()

  def this(apikey: String) {
    this(Default.defaultSymbol, apikey)
  }

  def getLatest(data: ujson.Obj) = {
    val latestDate: String = data.obj.keys.max
    val record = changeLabel(latestDate, data(latestDate))
    record
  }

  def castType(unit: Value): Double = {
    var value = unit.toString
    val numberPattern: Regex = "[^\"]".r
    val valueChange = numberPattern.findAllMatchIn(value).mkString("").toDouble
    valueChange
  }

  def checkTradingTime(timeStamp:String): String ={
    val splitDate=timeStamp.split(" ")
    val tradingDate=splitDate(0)
    val tradingHours=splitDate(1)
    val time=timeFormatter.parseDateTime(tradingHours).getSecondOfDay
    var TradingDateTime:String=timeStamp

    if(time<openSeconds || time>closeSeconds){
      val date=dateFormatter.parseDateTime(splitDate(0))
      if(time<openSeconds){
        TradingDateTime=date.minusDays(1).toString.substring(0,10)+" 09:30:00"
      }else{
        TradingDateTime=date.plusDays(1).toString.substring(0,10)+" 16:00:00"
      }}
    return TradingDateTime
  }



  def changeLabel(time: String, data: Value) = {
    val tradeTime:String=checkTradingTime(time)
    val record = Map("symbol" -> symbol, "time" -> tradeTime,
      "open" -> castType(data("1. open")),
      "high" -> castType(data("2. high")),
      "low" -> castType(data("3. low")),
      "close" -> castType(data("4. close")),
      "volume" -> castType(data("5. volume")))
    record
  }
  
  def retrieveIntraData(interval: Int = Default.defaultInterval, outputSize: String = Default.defaultOutputSize): Unit = {

    val intervalInput = interval.toString + "min"
    val endPoint = s"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=$symbol&interval=$intervalInput&" +
      s"outputsize=$outputSize&apikey=$apikey"


    val req = requests.get(endPoint)

    if (req.statusCode == 200) {
      val records = ujson.read(req.text)
      timeZone = Some(records("Meta Data")("6. Time Zone").toString)
      val timeSeriesKey: String = s"Time Series ($intervalInput)"
      //records--->json format //records("Time Series") ---->Map/value
      if (outputSize == "compact") {
        val latest = getLatest(records(timeSeriesKey).obj)
        value += latest
      } else {
        for ((time, data) <- records(timeSeriesKey).obj.iterator) {
          val indRecord = changeLabel(time, data)
          value += indRecord
        }}
    }
  }
}
