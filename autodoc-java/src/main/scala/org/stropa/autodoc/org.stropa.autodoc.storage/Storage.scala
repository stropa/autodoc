package org.stropa.autodoc.org.stropa.autodoc.storage


trait Storage {

  def write(map: Map[String, AnyRef]): Unit

  def clean(): Unit

}
