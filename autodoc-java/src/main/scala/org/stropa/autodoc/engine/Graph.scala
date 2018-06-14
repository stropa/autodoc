package org.stropa.autodoc.engine

import org.stropa.autodoc.util.strings._


case class Graph(nodes: List[Item], links: List[Link]) {


  def merge(anotherGraph: Graph): Graph = {
    Graph(nodes ++ anotherGraph.nodes, links ++ anotherGraph.links)
  }

  def node(ref: String) = {
    nodes.find(node => {
      ref match {
        case r"_type:.*" => node._type == ref.replace("_type:", "")
        case r"name:.*" => node.name == ref.replace("name:", "")
        case _ => false
      }
    })
  }

  def node(matcher: Item => Boolean): Option[Item] = nodes.find(matcher)

}
