package org.stropa.autodoc.engine


case class Graph(nodes: List[Item], links: List[Link]) {

  def merge(anotherGraph: Graph): Graph = {
    Graph(nodes ++ anotherGraph.nodes, links ++ anotherGraph.links)
  }

}
