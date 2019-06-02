package org.stropa.autodoc.engine

import java.util.Optional

import org.springframework.context.ApplicationContext
import org.stropa.autodoc.describers.spring.SpringApplicationDescriber
import org.stropa.autodoc.org.stropa.autodoc.storage.{FileStorage, Storage}
import org.stropa.autodoc.reporters.{Describer, DockerContainerDescriber, HostnameDescriber}

import scala.collection.JavaConverters._

class AutodocJavaEngine(context: java.util.Map[String, Any]) {

  val engine = new AutodocEngine()

  val basicDescribers = defaultDescribers()
  engine.describers ++= basicDescribers

  val linkSpecs: List[LinkSpec] = defaultLinkSpecs()



  def doc(whatToDoc: String) = {
    engine.doc(whatToDoc)
  }

  def addInfo(newItems: java.util.List[Item], newRelations: java.util.List[Link]) = {
    engine.addInfo(newItems.asScala.toList, newRelations.asScala.toList)
  }

  def writeDocs(storage: Storage) = {
    engine.writeDocs(storage)
  }

  def node(ref: String): Optional[Item] = {
    Optional.ofNullable(engine.graph.node(ref).orNull)
  }

  def link(from: Optional[Item], relation: String, to: Optional[Item]) = {
    if (from.isPresent && to.isPresent && relation != null && !relation.isEmpty) {
      engine.link(from.get(), relation, to.get())
    }
  }

  def addLinkSpec(fromRef: String, relation: String, toRef: String): Unit = {

  }

  def defaultDescribers(): Map[String, Describer] = {
    Map(
      "hostname" -> new HostnameDescriber,
      "docker-container" -> new DockerContainerDescriber,
      "application" -> new SpringApplicationDescriber(context.asScala.toMap)
    )
  }

  def defaultDocs(): List[LinkSpec] = {
    basicDescribers.keysIterator.foreach(doc)
    defaultLinkSpecs()
  }

  def defaultLinkSpecs(): List[LinkSpec] = {
    List(
      LinkSpec("_type:docker-container", "hosted-on", "_type:host"),
      LinkSpec("_type:spring-application", "runs-in", "_type:docker-container")
    )
  }


  def writeSnapshot(): Unit = {
    engine.writeSnapshot()
  }


  def describeSpringApplication(applicationContext: ApplicationContext, storage: Storage): Unit = {
    doc("hostname")
    doc("docker-container")
    doc("application")
    val docker = node("docker-container")
    val application = node("_type:spring-application")
    val host = node("_type:host")
    if (docker.isPresent) {
      link(application, "runs in", docker)
      link(docker, "deployed on", host)
    }  else  link(application, "deployed on", host)
    writeDocs(storage)
  }




}

