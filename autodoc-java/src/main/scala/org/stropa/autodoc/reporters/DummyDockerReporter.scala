package org.stropa.autodoc.reporters
import org.stropa.autodoc.engine.Item


class DummyDockerReporter extends AutonomousReporter {

  override def report: Item = Item(name = "stub_container", `type` = "docker-container")

}
