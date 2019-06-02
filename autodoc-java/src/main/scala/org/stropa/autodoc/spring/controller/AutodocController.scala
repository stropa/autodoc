package org.stropa.autodoc.spring.controller

import java.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}
import org.stropa.autodoc.spring.AutodocSpringAdapter

import collection.JavaConverters._



@RestController
@RequestMapping(Array("/autodoc"))
class AutodocController(@Autowired(required = false) adapter: AutodocSpringAdapter) {

  @GetMapping(Array("/data"))
  def readDocs(): util.List[util.Map[String, Object]] = {
    if (adapter != null) {
      adapter.readDocs
    } else List().asJava
  }


}
