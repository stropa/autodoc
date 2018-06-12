package org.stropa.autodoc.engine

import org.scalatest.FlatSpec
import org.stropa.autodoc.reporters.HostnameDescriber
import scala.collection.JavaConverters._


class AutodocEngineTest extends FlatSpec {

  behavior of "AutodocEngine"

  val doc = new AutodocEngine()

  it should "write hostname to storage" in {
    //doc.addInfo(new HostnameDescriber().describe)
    doc.writeSnapshot()
  }

}
