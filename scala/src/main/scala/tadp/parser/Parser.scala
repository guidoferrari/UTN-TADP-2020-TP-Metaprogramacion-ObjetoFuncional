package tadp.parser

import scala.util.Try

trait Parser[T] {
  def run(string: String): Try[T]
}

class anyChar extends Parser[Char]{
  def run(string: String): Try[Char] = {
    Try(string.charAt(0))
  }
}

class char(char: Char) extends Parser[Char]{
  def run(string: String): Try[Char] = {
    Try(if (string.charAt(0) != char) {throw new Error}).map{boolean => char}
  }
}