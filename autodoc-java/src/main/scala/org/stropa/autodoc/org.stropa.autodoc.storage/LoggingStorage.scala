package org.stropa.autodoc.org.stropa.autodoc.storage

import grizzled.slf4j.Logging


class LoggingStorage extends Storage with Logging {


  override def write(map: Map[String, Any]) = {
    info(map)
  }

}
