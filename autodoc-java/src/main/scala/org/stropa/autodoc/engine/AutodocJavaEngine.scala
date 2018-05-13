package org.stropa.autodoc.engine

import com.typesafe.config.Config
import org.stropa.autodoc.reporters.Describer
import scala.collection.JavaConverters._

class AutodocJavaEngine(config: Config) {

  val engine = new AutodocEngine(config)

  def writeDocs(reporters: java.util.List[Describer]) = {
    engine.writeDocs(reporters.asScala.toList)
  }

}
