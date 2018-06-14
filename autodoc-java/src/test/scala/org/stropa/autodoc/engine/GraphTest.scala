package org.stropa.autodoc.engine

import org.scalatest.FlatSpec


class GraphTest extends FlatSpec {

  behavior of "Graph"

  it should "get node by '_type:' reference" in {

    val items = List(
      Item(_type = "cat", name = "name"),
      Item(_type = "dog", name = "name_2"),
      Item(_type = "snake", name = "name_3")
    )
    val links = List()
    val g = Graph(items, links)
    val node = g.node("_type:cat")

    assert(node.isDefined)
    assert(node.get == items.head)
  }


}
