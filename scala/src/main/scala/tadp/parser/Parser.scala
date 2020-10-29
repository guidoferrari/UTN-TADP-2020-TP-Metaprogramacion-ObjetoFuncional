package tadp.parser

import scala.util.Try

abstract class Parser[T] {
  def parse(string: String): Try[T]

  def <|>(otroParser: Parser[T]): Parser[T] = {
    new parseOr[T](this, otroParser)
  }
}

class parseOr[T](parser1: Parser[T], parser2: Parser[T] ) extends Parser[T]{
  override def parse(string: String): Try[T] = {
    parser1.parse(string).orElse(parser2.parse(string))
  }
}

class anyChar extends Parser[Char]{
  override def parse(string: String): Try[Char] = {
    Try(string.charAt(0))
  }
}

class char(char: Char) extends Parser[Char]{
  override def parse(string: String): Try[Char] = {
    Try(if (string.charAt(0) != char) {throw new Error}).map{boolean => char}
  }
}

class digit extends Parser[Char] {
  override def parse(string: String): Try[Char] = {
    Try(if (!string.charAt(0).isDigit) {
      throw new Error
    }).map { boolean => string.charAt(0) }
  }
}

class string(stringEsperado: String) extends Parser[String]{
  override def parse(string: String): Try[String] = {
    Try(
      if (!string.startsWith(stringEsperado)){throw new Error}
    ).map{boolean => stringEsperado}
  }
}

class integer extends Parser[Integer] {
  override def parse(string: String): Try[Integer] = {Try(string.toInt)}
}

class double extends Parser[Double] {
  override def parse(string: String): Try[Double] = {Try(string.toDouble)}
}