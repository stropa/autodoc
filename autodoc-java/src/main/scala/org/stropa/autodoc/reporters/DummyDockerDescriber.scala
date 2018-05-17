package org.stropa.autodoc.reporters
import org.stropa.autodoc.engine.Item
import scala.collection.JavaConverters._


class DummyDockerDescriber extends Describer {

  override def report: java.util.List[Item] = List(Item(name = "stub_container", _type = "docker-container")).asJava

}
