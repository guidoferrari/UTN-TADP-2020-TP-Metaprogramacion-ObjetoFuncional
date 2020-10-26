package tadp.parser

import scala.util.Try

trait Parser[T] {
  def run(string: String): Try[T]
}

class anyChar extends Parser[Char]{
  override def run(string: String): Try[Char] = {
    Try(string.charAt(0))
  }
}

class char(char: Char) extends Parser[Char]{
  override def run(string: String): Try[Char] = {
    Try(if (string.charAt(0) != char) {throw new Error}).map{boolean => char}
  }
}

class digit extends Parser[Char] {
  override def run(string: String): Try[Char] = {
    Try(if (!string.charAt(0).isDigit) {
      throw new Error
    }).map { boolean => string.charAt(0) }
  }
}

class string(stringEsperado: String) extends Parser[String]{
  override def run(string: String): Try[String] = {
    Try(
      if (!string.startsWith(stringEsperado)){throw new Error}
    ).map{boolean => stringEsperado}
  }
}

class integer extends Parser[Integer] {
  override def run(string: String): Try[Integer] = {Try(string.toInt)}
}

class double extends Parser[Double] {
  override def run(string: String): Try[Double] = {Try(string.toDouble)}
}