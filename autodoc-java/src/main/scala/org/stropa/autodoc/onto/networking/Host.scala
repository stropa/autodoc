package org.stropa.autodoc.onto.networking

import org.stropa.autodoc.engine.Item
import org.stropa.autodoc.onto.Category


class Host(name: String) extends Item(_type = "host", name) with Category {

  override def inputs: Set[String] = Set("host[ed|s] on")

  override def outputs: Set[String] = Set("hosts")

}
