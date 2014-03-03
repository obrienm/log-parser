package me.moschops

import org.slf4j.LoggerFactory

trait Logging {
  val logger = LoggerFactory.getLogger(this.getClass)
}
