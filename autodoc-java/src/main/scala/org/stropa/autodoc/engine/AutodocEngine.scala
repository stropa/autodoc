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

    graph.nodes.foreach(item => {
      storage.write(item.attributes + ("id" -> item.id) + ("name" -> item.name) + ("type" -> item._type))
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
