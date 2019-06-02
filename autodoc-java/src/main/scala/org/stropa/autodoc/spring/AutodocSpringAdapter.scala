package org.stropa.autodoc.spring

import java.util

import grizzled.slf4j.Logging
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.stropa.autodoc.engine.AutodocJavaEngine
import org.stropa.autodoc.org.stropa.autodoc.storage.InMemoryStorage

import collection.JavaConverters._

class AutodocSpringAdapter extends ApplicationContextAware with Logging {

  var doc: AutodocJavaEngine = _
  val storage = new InMemoryStorage


  override def setApplicationContext(applicationContext: ApplicationContext): Unit = {
    logger.info("Writing structure docs")
    val context: util.HashMap[String, Any] = new util.HashMap[String, Any]
    context.put("applicationContext", applicationContext)
    doc = new AutodocJavaEngine(context)

    doc.describeSpringApplication(applicationContext, storage)

  }

  def readDocs: java.util.List[java.util.Map[String, Object]] = {
    storage.read().map(m => {
      m.mapValues {
        case map: Map[Any, Any] => map.asJava
        case list: List[Any] => list.asJava
        case any: Any => any
      }.asJava
    }).asJava
  }

}
