package org.stropa.autodoc.describers.spring

import org.springframework.context.ApplicationContext
import org.springframework.core.env._
import org.stropa.autodoc.engine.{Graph, Item, Link}
import org.stropa.autodoc.reporters.Describer

import scala.collection.mutable
import scala.collection.JavaConverters._


class SpringApplicationDescriber(context: Map[String, Any]) extends Describer {

  override def describe: Graph = {

    val appContext: Option[ApplicationContext] = context.get("applicationContext").map(_.asInstanceOf[ApplicationContext])
    if (appContext.isEmpty) return Graph(List(), List())

    // getting application name
    val appItem = Item(_type = "spring-application",
      name = appContext.map(
        c => if (c.getApplicationName.isEmpty) c.getId else c.getApplicationName).orElse(Some("spring-application")
      ).getOrElse("spring-application"))



    // getting properties
    val mapBuffer = collection.mutable.Map[String, Any]()
    val propertySources = appContext.get.getEnvironment.asInstanceOf[AbstractEnvironment].getPropertySources
    val seenAlready = mutable.ArrayBuffer[PropertySource[_]]()

    propertySources.asScala.foreach(ps => {
      diveIntoPropertySource(mapBuffer, ps, seenAlready)
    })
    val propertiesItem = Item(_type = "properties", name = "properties", attributes = mapBuffer.toMap)
    val linkFromPropertiesToApp = Link(propertiesItem, "properties of", appItem)



    // finally return a graph with description
    Graph(
      nodes = List(
        appItem, propertiesItem
      ),
      links = List(
        linkFromPropertiesToApp
      )
    )

  }



  private def diveIntoPropertySource(map: mutable.Map[String, Any],
                                     propertySource: PropertySource[_],
                                     seenAlready: mutable.ArrayBuffer[PropertySource[_]]): Unit = {
    propertySource match {
      case source: MapPropertySource =>
        map ++= source.getSource.asScala.toMap
      case source: CompositePropertySource =>

        for (ps <- source.getPropertySources.asScala) {
          if (seenAlready.contains(ps)) {
            // skip
          }
          else {
            seenAlready += ps
            diveIntoPropertySource(map, ps, seenAlready)
          }
        }
      case _ =>
    }
  }

}
