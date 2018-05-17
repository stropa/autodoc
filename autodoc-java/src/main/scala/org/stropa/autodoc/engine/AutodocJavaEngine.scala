package org.stropa.autodoc.engine

import com.typesafe.config.Config
import scala.collection.JavaConverters._

class AutodocJavaEngine(config: Config) {

  val engine = new AutodocEngine(config)

  def addInfo(newItems: java.util.List[Item], newRelations: java.util.List[Link]) = {
    engine.addInfo(newItems.asScala.toList, newRelations.asScala.toList)
  }

  def writeSnapshot() = {
    engine.writeSnapshot()
  }

}
