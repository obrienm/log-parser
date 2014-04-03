package me.moschops

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object FastlyLogEntry {
  
  def apply(logEntry: String): FastlyLogEntry = {
    val split = logEntry.split(""" """").toList.map(_.replaceAll("\"", ""))
    parse(split.tail)
  }
  
  private def parse(fields: List[String]): FastlyLogEntry = {
   
    val cacheInteraction = fields(13) match {
      case "HIT" => Hit
      case "MISS" => Miss
      case "PASS" => Pass
    }

    val passReasonOpt = fields(14) match {
      case "rss" => Some(Rss)
      case "logged in" => Some(LoggedIn)
      case "protected paths" => Some(ProtectedPath)
      case "edition switch" => Some(EditionSwitch)
      case "poll" => Some(Poll)
      case "unsupported method" => Some(UnsupportedMethod)
      case _ => None
    }

    val platform = fields(17) match {
      case "desktop" => R2
      case "next-gen-football" => Football
      case "next-gen-router" => NextGen
      case _ => Other
    }

    new FastlyLogEntry(
      date = toDate(fields(0)),
      ip = fields(1),
      countryCode = fields(2),
      region = fields(3),
      city = fields(4),
      continentCode = fields(5),
      latitude = fields(6).split(" ")(0),
      longitude = fields(6).split(" ")(1),
      ssl = if(fields(7) == "(null)") false else true,
      host = fields(8),
      path = fields(9),
      httpMethod = fields(10),
      userAgent = fields(11),
      status = fields(12),
      cacheInteraction = cacheInteraction,
      passReason = passReasonOpt,
      hits = fields(15),
      age = fields(16),
      platform = platform,
      fastlyNode = fields(18)
    )
  }

  private def toDate(date: String): DateTime = {
    val fmt = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss ZZZ")
    fmt.parseDateTime(date)
  }
}

case class FastlyLogEntry(
                           date: DateTime,
                           ip: String,
                           countryCode: String,
                           region: String,
                           city: String,
                           continentCode: String,
                           latitude: String,
                           longitude: String,
                           ssl: Boolean,
                           host: String,
                           path: String,
                           httpMethod: String,
                           userAgent: String,
                           status: String,
                           cacheInteraction: CacheInteraction,
                           passReason: Option[PassReason],
                           hits: String,
                           age: String,
                           platform: Platform,
                           fastlyNode: String
                           ) {

  def url = {
    ssl match {
      case true => "https://" + host + path
      case _ => "http://" + host + path
    }
  }
  
  def toCsv(): String = {
    val separator = """", """"

    """"""" + date + separator +
      ip + separator +
      countryCode + separator + 
      region + separator + 
      city + separator + 
      continentCode + separator +
      latitude + separator +
      longitude + separator +
      ssl + separator +
      url + separator +
      httpMethod + separator +
      userAgent + separator +
      status + separator +
      cacheInteraction + separator +
      passReason + separator +
      hits + separator +
      age + separator +
      platform + separator +
      fastlyNode + """""""
  }
  
}

sealed trait PassReason
object Rss extends PassReason
object LoggedIn extends PassReason
object ProtectedPath extends PassReason
object EditionSwitch extends PassReason
object Poll extends PassReason
object UnsupportedMethod extends PassReason

sealed trait Platform
object R2 extends Platform
object NextGen extends Platform
object Football extends Platform
object Other extends Platform

sealed trait CacheInteraction
object Hit extends CacheInteraction
object Miss extends CacheInteraction
object Pass extends CacheInteraction
