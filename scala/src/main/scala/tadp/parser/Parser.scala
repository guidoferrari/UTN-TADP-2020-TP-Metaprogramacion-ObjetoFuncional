package tadp.parser


import scala.util.Try

abstract class Parser[T] {
  def parse(string: String): Try[(T, String)]

  def <|>(otroParser: Parser[T]): Parser[T] = {
    new ParserOr[T](this, otroParser)
  }

  def <>(otroParser: Parser[T]): Parser[(T,T)] = {
    new ParserConcat[T](this, otroParser)
  }

  def ~>(otroParser: Parser[T]): Parser[T] = {
    new ParserRightmost[T](this, otroParser)
  }

  def <~(otroParser: Parser[T]): Parser[T] = {
    new ParserLeftmost[T](this, otroParser)
  }
}

class ParserOr[T](parser1: Parser[T], parser2: Parser[T] ) extends Parser[T]{
  override def parse(string: String): Try[(T, String)] = {
    parser1.parse(string).orElse(parser2.parse(string))
  }
}

class ParserConcat[T](parser1: Parser[T], parser2: Parser[T] ) extends Parser[(T,T)]{
  override def parse(string: String): Try[((T, T), String)] = {
    Try({
        val resultado = parser1.parse(string)
        val resultado2 = parser2.parse(resultado.get._2)
        ((resultado.get._1, resultado2.get._1), resultado2.get._2)
      }
    )
  }
}

class ParserRightmost[T](parser1: Parser[T], parser2: Parser[T] ) extends Parser[T]{
  override def parse(string: String): Try[(T, String)] = {
    Try({
      val resultado2 = parser2.parse(parser1.parse(string).get._2)
      (resultado2.get._1, resultado2.get._2)
      }
    )
  }
}

class ParserLeftmost[T](parser1: Parser[T], parser2: Parser[T] ) extends Parser[T]{
  override def parse(string: String): Try[(T, String)] = {
    Try({
      val resultado = parser1.parse(string)
      val resultado2 = parser2.parse(resultado.get._2)
      (resultado.get._1, resultado2.get._2)
    }
    )
  }
}

class anyChar extends Parser[Char]{
  override def parse(string: String): Try[(Char, String)] = {
    Try((string.take(1).charAt(0), string.substring(1)))
  }
}

class char(char: Char) extends Parser[Char]{
  override def parse(string: String): Try[(Char, String)] = {
    Try(
      string match {
        case "" => throw new Error
        case _ =>
          val charParseado = string.take(1).charAt(0)
          if (charParseado == char) {
            (charParseado, string.substring(1))
          } else{
            throw new Error
          }
      }
    )
  }
}

class digit extends Parser[Char] {
  override def parse(string: String): Try[(Char, String)] = {
    Try(
      string match {
        case "" => throw new Error
        case _ =>
          val char = string.take(1).charAt(0)
          if (char.isDigit){
            (char, string.substring(1))
          } else{
            throw new Error
          }
      }
    )
  }
}

class string(stringEsperado: String) extends Parser[String]{
  override def parse(string: String): Try[(String, String)] = {
    Try(
      string match{
        case "" => throw new Error
        case _ =>
          val stringAParsear = string.take(stringEsperado.length)
          if (stringAParsear.equals(stringEsperado)) {
            (stringAParsear, string.substring(stringAParsear.length))
          } else{
            throw new Error
          }
      }
    )
  }
}

class integer extends Parser[Integer] {
  override def parse(string: String): Try[(Integer, String)] = {Try((string.toInt, ""))}
}

class double extends Parser[Double] {
  override def parse(string: String): Try[(Double, String)] = {Try((string.toDouble, ""))}
}