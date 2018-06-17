package org.stropa.autodoc.engine

import java.util.UUID


case class Item(id: String,
                _type: String = "",
                name: String,
                var attributes: Map[String, Any]) {

  def this(_type: String, name: String) = {
    this(id = UUID.randomUUID().toString, name = name, _type = _type, attributes = Map())
  }

  def this(_type: String, name: String, attributes: Map[String, Any]) = {
    this(id = UUID.randomUUID().toString, name = name, _type = _type, attributes = attributes)
  }

}

object Item {
  def apply(_type: String, name: String): Item = new Item(name = name, _type = _type)

  def apply(_type: String, name: String, attributes: Map[String, Any]): Item =
    new Item(_type = _type, name = name, attributes = attributes)

}
