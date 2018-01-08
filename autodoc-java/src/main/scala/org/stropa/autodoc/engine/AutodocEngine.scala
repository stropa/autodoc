package org.stropa.autodoc.engine

import com.typesafe.config.{Config, ConfigFactory}
import org.stropa.autodoc.org.stropa.autodoc.storage.Storage
import org.stropa.autodoc.reporters.{AutonomousReporter, HostnameReporter}
import org.stropa.autodoc.storage.ESStorage

import scala.collection.mutable


class AutodocEngine(var config: Config) {

  val items = mutable.ListBuffer[Item]()
  val links = mutable.ListBuffer[Link]()

  if (config == null) {
    config = ConfigFactory.load()
  }

  private val storageName = config.getString("storage-type")

  implicit val storage: Storage = if (storageName.toLowerCase() == "es") {
    new ESStorage(config)
  } else throw new IllegalArgumentException(s"Unsupported storage $storageName")


  def writeDocs(reporters: java.util.List[AutonomousReporter]) = {
    import scala.collection.JavaConverters._
    items.append(reporters.asScala.map(_.report) :_*)
    links.append(items.sliding(2).map(pair => Link(pair.head, "in", pair.last)).toSeq :_*)

    items.foreach(item => {
      storage.write(item.attributes + ("id" -> item.id) + ("name" -> item.name) + ("type" -> item.`type`))
    })
    links.foreach(link => {
      storage.write(Map[String, Any](
        "from" -> link.from.id,
        "to" -> link.to.id,
        "relation" -> link.relation
      ))
    })
  }



  def reportHostName = {
   new HostnameReporter().report
  }



}
