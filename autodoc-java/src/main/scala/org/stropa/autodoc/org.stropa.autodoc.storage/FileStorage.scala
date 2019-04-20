package org.stropa.autodoc.org.stropa.autodoc.storage

import java.io.{File, PrintWriter}

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
//import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.collection.JavaConverters._


class FileStorage(fileName: String) extends Storage {

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
  val writer = new PrintWriter(new File(fileName))

  override def write(map: Map[String, Any]) = {

    writer.write(mapper.writeValueAsString(map.asJava))
    writer.write(System.lineSeparator())
    writer.flush()

  }

}
