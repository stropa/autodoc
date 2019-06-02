package org.stropa.autodoc.org.stropa.autodoc.storage

import grizzled.slf4j.Logging

import scala.collection.mutable

class InMemoryStorage extends Storage with ReadableStorage with Logging {

  var docs: scala.collection.mutable.MutableList[Map[String, AnyRef]] = new mutable.MutableList()

  override def write(map: Map[String, AnyRef]): Unit = {
    docs += map
  }

  override def clean(): Unit = docs = new mutable.MutableList()

  def log(): Unit = {
    logger.info("\n" + docs.mkString("\n"))
  }

  override def read(): List[Map[String, AnyRef]] = {
    docs.toList
  }

}
