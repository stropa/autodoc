package org.stropa.autodoc.org.stropa.autodoc.storage


trait Storage {

  def write(map: Map[String, Any]): Unit

}
