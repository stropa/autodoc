package org.stropa.autodoc.reporters

import org.stropa.autodoc.engine.Item


/**
  * Simple describer that does not need any additional input and works by its own.
  */
trait Describer {

  def report: Iterable[Item]

}
