package org.stropa.autodoc.reporters
import org.stropa.autodoc.engine.Item


class DummyDockerDescriber extends Describer {

  override def report: Seq[Item] = Seq(Item(name = "stub_container", `type` = "docker-container"))

}
