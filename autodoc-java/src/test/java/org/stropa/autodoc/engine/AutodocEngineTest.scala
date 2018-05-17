package org.stropa.autodoc.engine

import org.scalatest.FlatSpec
import org.stropa.autodoc.reporters.HostnameDescriber
import scala.collection.JavaConverters._


class AutodocEngineTest extends FlatSpec {

  behavior of "AutodocEngine"

  val doc = new AutodocEngine(null)

  it should "write hostname to storage" in {
    doc.addInfo(new HostnameDescriber().report.asScala.toList, List())
    doc.writeSnapshot()
  }

}
