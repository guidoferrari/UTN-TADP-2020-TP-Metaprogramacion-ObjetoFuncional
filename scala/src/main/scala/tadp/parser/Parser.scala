package tadp.parser

trait ParserResult[+T]

case class ResultadoExitoso[T](valorParseado: T, noConsumido: String) extends ParserResult[T]
case class ResultadoFallido[T](noConsumido: String) extends ParserResult[T]

abstract class Parser[+T] extends (String => ParserResult[T]) {
  def <|>[V >: T, U <: V](otroParser: Parser[U]): Parser[V] = new ParserOr[T, V, U](this, otroParser)
  def <>[U](otroParser: Parser[U]): Parser[(T,U)] = new ParserConcat[T, U](this, otroParser)
  def ~>[U](otroParser: Parser[U]): Parser[U] = new ParserRightmost[T, U](this, otroParser)
  def <~[U](otroParser: Parser[U]): Parser[T] = new ParserLeftmost[T, U](this, otroParser)
}

////////////////////////////////////// ParserCombinators

class ParserOr[T, V >: T, U <: V](parser1: Parser[T], parser2: Parser[U]) extends Parser[V]{
  override def apply(string: String): ParserResult[V] = {
    parser1(string) match {
      case ResultadoFallido(noConsumido) => parser2(noConsumido)
      case ResultadoExitoso(valorParseado, noConsumido) => ResultadoExitoso(valorParseado, noConsumido)
    }
  }
}

class ParserConcat[T, U](parser1: Parser[T], parser2: Parser[U] ) extends Parser[(T,U)]{
  override def apply(string: String): ParserResult[(T,U)] = {
    parser1(string) match {
      case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
      case ResultadoExitoso(valorParseado, noConsumido) =>
        parser2(noConsumido) match {
          case ResultadoFallido(_) => ResultadoFallido(string)
          case ResultadoExitoso(valorParseado2, noConsumido2) =>
            ResultadoExitoso((valorParseado, valorParseado2), noConsumido2)
        }
    }
  }
}

class ParserRightmost[T, U](parser1: Parser[T], parser2: Parser[U] ) extends Parser[U]{
  override def apply(string: String): ParserResult[U] = {
    parser1(string) match {
      case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
      case ResultadoExitoso(_, noConsumido) =>
        parser2(noConsumido) match {
          case ResultadoFallido(_) => ResultadoFallido(string)
          case ResultadoExitoso(valorParseado2, noConsumido2) => ResultadoExitoso(valorParseado2, noConsumido2)
        }
    }
  }
}

class ParserLeftmost[T, U](parser1: Parser[T], parser2: Parser[U] ) extends Parser[T]{
  override def apply(string: String): ParserResult[T] = {
    parser1(string) match {
      case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
      case ResultadoExitoso(valorParseado, noConsumido) =>
        parser2(noConsumido) match {
          case ResultadoFallido(_) => ResultadoFallido(string)
          case ResultadoExitoso(_, noConsumido2) => ResultadoExitoso(valorParseado, noConsumido2)
        }
    }
  }
}

////////////////////////////////////// Parsers

class anyChar extends Parser[Char]{
  override def apply(string: String): ParserResult[Char] = {
    string match {
      case "" => ResultadoFallido(string)
      case _ => ResultadoExitoso(string.take(1).charAt(0), string.substring(1))
    }
  }
}

class char(char: Char) extends Parser[Char]{
  override def apply(string: String): ParserResult[Char] = {
    string match {
      case "" => ResultadoFallido(string)
      case _ =>
        val charParseado = string.take(1).charAt(0)
        if (charParseado == char) {
          ResultadoExitoso(charParseado, string.substring(1))
        } else{
          ResultadoFallido(string)
        }
    }
  }
}

class digit extends Parser[Char] {
  override def apply(string: String): ParserResult[Char] = {
    string match {
      case "" => ResultadoFallido(string)
      case _ =>
        val char = string.take(1).charAt(0)
        if (char.isDigit){
          ResultadoExitoso(char, string.substring(1))
        } else{
          ResultadoFallido(string)
        }
    }
  }
}

class string(stringEsperado: String) extends Parser[String]{
  override def apply(string: String): ParserResult[String] = {
    string match{
      case "" => ResultadoFallido(string)
      case _ =>
        val stringAParsear = string.take(stringEsperado.length)
        if (stringAParsear.equals(stringEsperado)) {
          ResultadoExitoso(stringAParsear, string.substring(stringAParsear.length))
        } else{
          ResultadoFallido(string)
        }
    }
  }
}

class integer extends Parser[Integer] {
  override def apply(string: String): ParserResult[Integer] = {
    string.toIntOption match {
      case Some(i) => ResultadoExitoso(i, "")
      case _ => ResultadoFallido(string)
    }
  }
}

class double extends Parser[Double] {
  override def apply(string: String): ParserResult[Double] = {
    string.toDoubleOption match {
      case Some(i) => ResultadoExitoso(i, "")
      case _ => ResultadoFallido(string)
    }
  }
}