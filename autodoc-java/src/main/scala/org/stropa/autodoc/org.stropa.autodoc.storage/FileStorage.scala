package org.stropa.autodoc.org.stropa.autodoc.storage

import java.io.{File, PrintWriter}
import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.Config


class FileStorage(fileName: String) extends Storage {

  val mapper = new ObjectMapper()
  val writer = new PrintWriter(new File(fileName))

  override def write(map: Map[String, Any]) = {

    writer.write(mapper.writeValueAsString(map))
    writer.flush()

  }

}
