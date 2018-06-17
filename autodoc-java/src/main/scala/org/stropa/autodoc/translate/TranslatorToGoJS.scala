package org.stropa.autodoc.translate

import java.io.{File, PrintWriter}

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.io.Source


class Node(val key: String, var text: String, var color: Option[String] = None,
           var group: Option[String] = None, var isGroup: Boolean = false,
          var attributes: Option[Any] = None)

class Link(val from: String, val to: String, var label: String = "", var color: Option[String] = None)

class GojsModel(var nodes: List[Node], var links: List[Link])


object TranslatorToGoJS extends App {


  val groupingRelations = Seq("deployed on", "runs on")


  val input = args(0)
  val output = args(1)

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.setSerializationInclusion(Include.NON_ABSENT)

  val records = Source.fromFile(input).getLines().map(mapper.readValue(_, classOf[Map[String, Any]]))

  val (relations, items) = records.toList.partition(_.get("relation").isDefined)


  private val model = new GojsModel(
    nodes = items.map(m => new Node(key = m("id").toString, text = m("name").toString,
      attributes = m.get("attributes"))
    ),
    links = relations.map(m => new Link(from = m("from").toString, to = m("to").toString, label = m("relation").toString))
  )

  //val groups = collection.mutable.Map[String, Int]()

  model.links.foreach(link => {
    if (groupingRelations.contains(link.label)) {
      val toNode = model.nodes.find(_.key == link.to).get
      val fromNode = model.nodes.find(_.key == link.from).get
      toNode.isGroup = true
      fromNode.group = Some(toNode.key)
    }
  })

  model.links = model.links.filterNot(link => {
    val toNode = model.nodes.find(_.key == link.to).get
    val fromNode = model.nodes.find(_.key == link.from).get
    groupingRelations.contains(link.label) &&
      toNode.isGroup && toNode.key == fromNode.group.getOrElse("")
  })


  private val writer = new PrintWriter(new File(output))
  writer.write(mapper.writeValueAsString(model))
  writer.flush()

  //print(records)

}
