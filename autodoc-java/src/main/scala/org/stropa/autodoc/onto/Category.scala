package org.stropa.autodoc.onto


trait Category {

  def inputs: Set[String]
  def outputs: Set[String]

}
