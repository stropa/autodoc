package org.stropa.autodoc.engine

import com.typesafe.config.Config
import org.stropa.autodoc.describers.spring.SpringApplicationDescriber
import org.stropa.autodoc.org.stropa.autodoc.storage.Storage
import org.stropa.autodoc.reporters.{Describer, DockerContainerDescriber, HostnameDescriber}

import scala.collection.JavaConverters._

class AutodocJavaEngine(config: Config) {

  val engine = new AutodocEngine()

  def addInfo(newItems: java.util.List[Item], newRelations: java.util.List[Link]) = {
    engine.addInfo(newItems.asScala.toList, newRelations.asScala.toList)
  }

  def writeSnapshot() = {
    engine.writeSnapshot()
  }
}

object AutodocJavaEngine {

  val engine = new AutodocEngine()

  val availableDescribers: collection.mutable.Map[String, Describer] = collection.mutable.Map()
  availableDescribers ++= defaultDescribersSetup()
  engine.describers ++= availableDescribers

  def doc(whatToDoc: String) = {
    engine.doc(whatToDoc)
  }

  def addInfo(newItems: java.util.List[Item], newRelations: java.util.List[Link]) = {
    engine.addInfo(newItems.asScala.toList, newRelations.asScala.toList)
  }

  def writeDocs(storage: Storage) = {
    engine.writeDocs(storage)
  }


  def defaultDescribersSetup(): Map[String, Describer] = {
    Map(
      "hostname" -> new HostnameDescriber,
      "docker-container" -> new DockerContainerDescriber,
      "application" -> new SpringApplicationDescriber
    )
  }

}
