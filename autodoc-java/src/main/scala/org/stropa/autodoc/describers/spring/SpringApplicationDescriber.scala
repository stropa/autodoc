package org.stropa.autodoc.describers.spring

import org.springframework.context.ApplicationContext
import org.stropa.autodoc.engine.{Graph, Item}
import org.stropa.autodoc.reporters.Describer


class SpringApplicationDescriber(context: Map[String, Any]) extends Describer {

  override def describe: Graph = {

    Graph(
      nodes = List(
        Item(_type = "spring-application",
          name = context.get("applicationContext").map(_.asInstanceOf[ApplicationContext].getApplicationName)
            .getOrElse("spring-application")
        )
      ),
      links = List()
    )

  }

}
