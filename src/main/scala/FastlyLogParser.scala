package me.moschops

import scala.io.{Codec, Source}
import java.io._
import org.joda.time.{Period, Duration, DateTime}
import org.joda.time.format.{PeriodFormatterBuilder, ISOPeriodFormat}

/**
 * sbt
 * run [path to log dir]
 */
object FastlyLogParser extends App with Logging {
  
  if(args.length == 1) {
    val start = DateTime.now()

    val logDir = args(0)
    val files = new File(logDir).listFiles().filter(f => f.isFile && f.getName.endsWith(".log")).toList

    val logEntries = files.map(Parser.parse(_)).flatten

    logger.info(s"total log entries: ${logEntries.size}")

    // do the hard work here
    Summary.print(logEntries)

    val end = DateTime.now()
    val duration = new Duration(start, end)
    logger.info(s"time to parse: ${duration.getStandardSeconds()} seconds")
    
  } else {
    logger.error("You need to specify a directory to scan for *.log")
  }
  
}

object Parser extends Logging {
  def parse(file: File): List[FastlyLogEntry] = {
    logger.info(s"parsing ${file.getAbsolutePath}")
    val lines: List[String] = Source.fromFile(file, Codec.ISO8859.name).mkString.split("\n").toList
    lines.map(FastlyLogEntry(_))
  }
}

object FileWriter extends Logging {

  def append(file: String, line: String): Unit = {
    try {
      val out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))
      out.println(line)
      out.close()
    } catch {
      case e: IOException => logger.error("", e)
    }
  }
}
