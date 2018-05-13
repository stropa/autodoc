package org.stropa.autodoc.reporters

import java.net.InetAddress

import org.stropa.autodoc.engine.Item

class HostnameDescriber extends Describer {

    def report: Iterable[Item] = {
      val hostName: String = InetAddress.getLocalHost.getHostName
      Seq(Item(name = hostName, `type` = "host"))
    }

  }