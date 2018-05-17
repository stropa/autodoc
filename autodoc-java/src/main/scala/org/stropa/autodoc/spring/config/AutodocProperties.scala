package org.stropa.autodoc.spring.config


/** A container to get all properties for autodoc namespace from spring-boot as Map  */
class AutodocProperties(val autodoc: java.util.Map[String, String]) {

  def this() = {
    this(new java.util.HashMap[String, String]())
  }

}