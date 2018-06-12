package org.stropa.autodoc.describers.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.stropa.autodoc.engine.{Graph, Item}
import org.stropa.autodoc.reporters.Describer


class SpringApplicationDescriber(@Autowired var appContext: ApplicationContext = null) extends Describer {

  override def describe: Graph = {

    Graph(
      nodes = List(Item(_type = "spring-application", name = appContext.getApplicationName)),
      links = List()
    )

  }

}
