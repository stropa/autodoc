package org.stropa.autodoc.reporters

import org.stropa.autodoc.engine.{Graph, Item}


/**
  * Simple describer that does not need any additional input and works by its own.
  */
trait Describer {

  def describe: Graph

}
