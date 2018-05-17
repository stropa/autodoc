package org.stropa.autodoc.engine

import com.typesafe.config.{Config, ConfigFactory}
import org.stropa.autodoc.org.stropa.autodoc.storage.{FileStorage, LoggingStorage, Storage}
import org.stropa.autodoc.reporters.Describer
import org.stropa.autodoc.storage.ESStorage


class AutodocEngine(var config: Config) {

  var items = List[Item]()
  var links = List[Link]()

  if (config == null) {
    config = ConfigFactory.load()
  }

  private val storageName = config.getString("storage-type")

  implicit val storage: Storage =
    if (storageName.toLowerCase() == "es")
      new ESStorage(config)
    else if (storageName.toLowerCase() == "slf4j")
      new LoggingStorage()
    else if (storageName.toLowerCase() == "file")
      new FileStorage(config)
    else throw new IllegalArgumentException(s"Unsupported storage $storageName")


  def addInfo(newItems: List[Item], newRelations: Iterable[Link]) = {
    items ++= newItems
    links ++= newRelations
  }

  def writeSnapshot() = {

    items.foreach(item => {
      storage.write(item.attributes + ("id" -> item.id) + ("name" -> item.name) + ("type" -> item._type))
    })

    links.foreach(link => {
      storage.write(Map[String, Any](
        "from" -> link.from.id,
        "to" -> link.to.id,
        "relation" -> link.relation
      ))
    })
  }


}
