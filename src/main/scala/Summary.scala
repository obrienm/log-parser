package me.moschops

import akka.agent.Agent
import scala.collection.immutable.HashSet
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Summary extends Logging {
  
  val filteredLogEntries = Agent[HashSet[FastlyLogEntry]](HashSet.empty)
  
  def print(logEntries: List[FastlyLogEntry]): Unit = {
    
    val redmond = logEntries.par.
      filter( entry => entry.countryCode == "US" && entry.city == "Redmond" ).
      filter{ entry => 
        entry.userAgent.toLowerCase().contains("bing") ||
        entry.userAgent.toLowerCase().contains("bot") ||
        entry.userAgent.toLowerCase().contains("msn")}
       
    redmond.foreach( entry =>
      filteredLogEntries.sendOff(entries => entries + entry)
    )
    
    filteredLogEntries.get().foreach{ entry => FileWriter.append("logs/filtered-log-entries.csv", entry.toCsv())}
    logger.info(s"filterd: ${filteredLogEntries.get().size}")
    
  }
}
