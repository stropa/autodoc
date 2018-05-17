package org.stropa.autodoc.reporters

import java.util
import scala.collection.JavaConverters._

import org.stropa.autodoc.engine.Item


class DockerContainerDescriber(var containerNameEnvVariable: String = "docker-container-name") extends Describer {

  def this() = {
    this("docker-container-name")
  }

  override def report: util.List[Item] = {
    val containerName = System.getenv(containerNameEnvVariable)
    List(Item(_type = "docker-container", name=containerName)).asJava
  }
}
