package org.stropa.autodoc.engine

import org.scalatest.FlatSpec


class AutodocEngineTest extends FlatSpec {

  behavior of "AutodocEngine"

  val doc = new AutodocEngine(null)

  it should "write hostname to storage" in {
    doc.reportHostName
  }

}
