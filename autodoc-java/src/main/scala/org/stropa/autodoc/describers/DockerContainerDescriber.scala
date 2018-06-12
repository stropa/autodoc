package org.stropa.autodoc.reporters

import org.stropa.autodoc.engine.{Graph, Item}


class DockerContainerDescriber(var containerNameEnvVariable: String = "docker-container-name") extends Describer {

  def this() = {
    this("docker-container-name")
  }

  override def describe: Graph = {
    val containerName = System.getenv(containerNameEnvVariable)
    Graph(
      nodes = if (containerName != null && !containerName.isEmpty)
        List(Item(_type = "docker-container", name = containerName)) else List(),
      links = List()
    )
  }
}
