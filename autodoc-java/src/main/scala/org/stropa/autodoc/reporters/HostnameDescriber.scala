package org.stropa.autodoc.reporters

import java.net.InetAddress

import org.stropa.autodoc.engine.Item
import scala.collection.JavaConverters._

class HostnameDescriber extends Describer {

  def report: java.util.List[Item] = {
    val hostName: String = InetAddress.getLocalHost.getHostName
    List(Item(name = hostName, _type = "host")).asJava
  }

}