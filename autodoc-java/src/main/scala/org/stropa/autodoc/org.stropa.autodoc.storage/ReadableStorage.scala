package org.stropa.autodoc.org.stropa.autodoc.storage

trait ReadableStorage {

  def read(): List[Map[String, Any]]

}
