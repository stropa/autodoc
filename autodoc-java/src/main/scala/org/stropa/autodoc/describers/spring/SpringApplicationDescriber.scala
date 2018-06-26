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
    val mapBuffer = collection.mutable.Map[String, Map[String, Any]]()
    val propertySources = appContext.get.getEnvironment.asInstanceOf[AbstractEnvironment].getPropertySources
    val seenAlready = mutable.ArrayBuffer[PropertySource[_]]()

    propertySources.asScala.foreach(ps => {
      diveIntoPropertySource(mapBuffer, ps, seenAlready)
    })

    val environmentItem = Item(_type = "Environment", name = "Environment")
    val propsItemsAndLinks: mutable.Map[Item, Link] = mapBuffer.map {
      case (name: String, props: Map[String, Any]) =>
        val propertiesItem = Item(_type = "properties", name = name, attributes = props)
        val linkFromPropertiesToEnv = Link(propertiesItem, "properties of", environmentItem)
        (propertiesItem, linkFromPropertiesToEnv)
    }

    // finally return a graph with description
    val graph = Graph(
      nodes = List[Item](appItem) ++ List(environmentItem) ++ propsItemsAndLinks.keys,
      links = List[Link](Link(environmentItem, "properties of", appItem)) ++ propsItemsAndLinks.values
    )
    graph
  }



  private def diveIntoPropertySource(map: mutable.Map[String, Map[String, Any]],
                                     propertySource: PropertySource[_],
                                     seenAlready: mutable.ArrayBuffer[PropertySource[_]]): Unit = {
    propertySource match {
      case source: MapPropertySource =>
        map ++= Map(source.getName -> source.getSource.asScala.toMap)
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
