package org.stropa.autodoc.engine

import com.typesafe.config.{Config, ConfigFactory}
import org.stropa.autodoc.org.stropa.autodoc.storage.Storage
import org.stropa.autodoc.reporters.Describer
import org.stropa.autodoc.storage.ESStorage


class AutodocEngine(var config: Config) {

  var items = List[Item]()
  var links = List[Link]()

  if (config == null) {
    config = ConfigFactory.load()
  }

  private val storageName = config.getString("storage-type")

  implicit val storage: Storage = if (storageName.toLowerCase() == "es") {
    new ESStorage(config)
  } else throw new IllegalArgumentException(s"Unsupported storage $storageName")


  def writeDocs(reporters: List[Describer]) = {
    val newitems: Seq[Item] = reporters.flatMap(_.report)
    items ++= newitems
    //links ++= items.sliding(2).map(pair => Link(pair.head, "in", pair.last)).toSeq

    items.foreach(item => {
      storage.write(item.attributes + ("id" -> item.id) + ("name" -> item.name) + ("type" -> item.`type`))
    })

    // ???
    links.foreach(link => {
      storage.write(Map[String, Any](
        "from" -> link.from.id,
        "to" -> link.to.id,
        "relation" -> link.relation
      ))
    })
  }



  def addFacts(facts: Iterable[Item]) = {
   items ++= facts
  }



}
