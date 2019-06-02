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
      logger.trace(s"Writing item: $item")
      val filteredAttributes = item.attributes.filterNot{ case (k, v) => v.toString.contains("${")}
      val map: Map[String, AnyRef] =  Map("type" -> item._type) +
        ("name" -> item.name.replace("applicationConfig: [classpath:/bootstrap.yml]", "HALLO")) +
        ("id" -> item.id) +
        ( "attributes" -> filteredAttributes)

      logger.trace(s"Writing map: $map")
      storage.write(map)
    })

    graph.links.foreach(link => {
      storage.write(Map[String, AnyRef](
        "type" -> "rel",
        "from" -> link.from.id,
        "to" -> link.to.id,
        "relation" -> link.relation
      ))
    })

  }


  def writeSnapshot() = {}



}
