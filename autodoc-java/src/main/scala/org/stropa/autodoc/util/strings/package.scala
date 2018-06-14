package org.stropa.autodoc.util


package object strings {

  import util.matching.Regex

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

}
