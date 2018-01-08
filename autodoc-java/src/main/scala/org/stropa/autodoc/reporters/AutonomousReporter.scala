package org.stropa.autodoc.reporters

import org.stropa.autodoc.engine.Item


/**
  * Simple reporter that does not need any additional input and works by its own.
  */
trait AutonomousReporter {

  def report: Item

}
