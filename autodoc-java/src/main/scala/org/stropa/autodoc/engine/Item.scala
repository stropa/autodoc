package org.stropa.autodoc.engine

import java.util.UUID


case class Item(id: String = UUID.randomUUID().toString,
                name: String,
                _type: String = "",
                attributes: Map[String, Any] = Map())
