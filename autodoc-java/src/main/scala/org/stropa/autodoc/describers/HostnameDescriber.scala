package org.stropa.autodoc.reporters

import java.net.InetAddress

import org.stropa.autodoc.engine.{Graph, Item}

class HostnameDescriber extends Describer {


  override def describe: Graph = {

    val hostName: String = InetAddress.getLocalHost.getHostName

    Graph(
      nodes = List(Item(name = hostName, _type = "host")),
      links = List()
    )
  }

}