package tadp.parser

import scala.util.Try

trait Parser {
  def run(string: String): Try[Object]
}

class anyChar{
  def run(string: String): Try[Char] = {
    Try(string.charAt(0))
  }
}