package org.stropa.autodoc.reporters

import java.net.InetAddress

import org.stropa.autodoc.engine.Item

class HostnameReporter extends AutonomousReporter {

    def report: Item = {
      val hostName: String = InetAddress.getLocalHost.getHostName
      Item(name = hostName, `type` = "host")
    }

  }