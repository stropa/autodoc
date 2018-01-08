package org.stropa.autodoc.storage

import com.typesafe.config.Config
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.{RestClient, RestHighLevelClient}
import org.stropa.autodoc.org.stropa.autodoc.storage.Storage


class ESStorage(config: Config) extends Storage {

  val client = new RestHighLevelClient(
    RestClient.builder(
      new HttpHost(config.getString("elasticsearch-host"), config.getInt("elasticsearch-port"), "http")))

  def write(map: Map[String, Any]) = {
    import scala.collection.JavaConverters._
    val indexRequest = new IndexRequest("structure-data", "autodoc").source(map.asJava)
    val response = client.index(indexRequest)
  }


}
