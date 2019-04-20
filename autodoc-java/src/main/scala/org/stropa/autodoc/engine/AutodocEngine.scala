package org.stropa.autodoc.engine

import grizzled.slf4j.Logging
import org.stropa.autodoc.org.stropa.autodoc.storage.Storage
import org.stropa.autodoc.reporters.Describer


class AutodocEngine() extends Logging {

  def link(from: Item, relation: String, to: Item) = {
    addInfo(List(), List( Link(from, relation, to) ))
  }

  var graph: Graph = Graph(List(), List())


  def addInfo(newItems: List[Item], newLinks: List[Link]) = {
    graph = graph.merge(Graph(newItems, newLinks))
  }


  var describers = Map[String, Describer]()

  def doc(whatToDoc: String) = describers.get(whatToDoc).foreach(d => {
    graph = graph.merge(d.describe)
  })


  def writeDocs(storage: Storage) = {

    // *** DEBUG

    /*addInfo(List(
      /*
      *Item(229da843-5e72-4497-842e-6b63392e544f,
      * properties,
      * applicationConfig: [classpath:/bootstrap.yml],
      *   Map(
      *     spring.cloud.config.uri -> http://config:8888,
      *     spring.cloud.config.username -> user,
      *     spring.cloud.config.fail-fast -> true,
      *     spring.application.name -> account-service,
      *     spring.cloud.config.password -> ${CONFIG_SERVICE_PASSWORD}))
      * */
      Item(id = "229da843-5e72-4497-842e-6b63392e544f", _type = "properties",
        name = "applicationConfig: [classpath:/bootstrap.yml]",
        attributes = Map[String, Any](
          "spring.cloud.config.uri" -> "http://config:8888"
        ))
    ), List())*/

    // *** DEBUG

    graph.nodes.foreach(item => {
      logger.trace(s"Writing item: $item")
      val filteredAttributes = item.attributes.filterNot{ case (k, v) => v.toString.contains("${")}
      val map: Map[String, Any] =  Map("attributes" -> filteredAttributes) + ("id" -> item.id) + ("name" -> item.name.replace("applicationConfig: [classpath:/bootstrap.yml]", "HALLO")) +
        ("type" -> item._type)
      logger.trace(s"Writing map: $map")
      storage.write(map)
    })

    graph.links.foreach(link => {
      storage.write(Map[String, Any](
        "from" -> link.from.id,
        "to" -> link.to.id,
        "relation" -> link.relation
      ))
    })

  }


  def writeSnapshot() = {}

  /*if (config == null) {
    config = ConfigFactory.load()
  }

  private val storageName = config.getString("storage.type")
  private val storageConfig = config.getConfig("storage")

  val storage: Storage =
    if (storageName.toLowerCase() == "es")
      new ESStorage(config.getConfig("storage"))
    else if (storageName.toLowerCase() == "slf4j")
      new LoggingStorage()
    else if (storageName.toLowerCase() == "file")
      new FileStorage(storageConfig)
    else throw new IllegalArgumentException(s"Unsupported storage $storageName")*/

  /*def writeSnapshot() = {

    info("Writing autodoc snapshot...")
    info(s"items: $items")
    info(s"links: $links")

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
  }*/


}
