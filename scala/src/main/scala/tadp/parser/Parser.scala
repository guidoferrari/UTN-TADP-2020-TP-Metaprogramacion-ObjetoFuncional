package tadp.parser

trait ParserResult[+T]

case class ResultadoExitoso[T](valorParseado: T, noConsumido: String) extends ParserResult[T]
case class ResultadoFallido[T](noConsumido: String) extends ParserResult[T]

abstract class Parser[+T] extends (String => ParserResult[T]) {

  def <|>[V >: T, U <: V](otroParser: Parser[U]): Parser[V] = string => {
    this(string) match {
      case ResultadoFallido(noConsumido) => otroParser(noConsumido)
      case ResultadoExitoso(valorParseado, noConsumido) => ResultadoExitoso(valorParseado, noConsumido)
    }
  }

  def <>[U](otroParser: Parser[U]): Parser[(T,U)] = string => {
    this(string) match {
      case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
      case ResultadoExitoso(valorParseado, noConsumido) =>
        otroParser(noConsumido) match {
          case ResultadoFallido(_) => ResultadoFallido(string)
          case ResultadoExitoso(valorParseado2, noConsumido2) =>
            ResultadoExitoso((valorParseado, valorParseado2), noConsumido2)
        }
    }
  }

  def ~>[U](otroParser: Parser[U]): Parser[U] = string => {
    this(string) match {
      case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
      case ResultadoExitoso(_, noConsumido) =>
        otroParser(noConsumido) match {
          case ResultadoFallido(_) => ResultadoFallido(string)
          case ResultadoExitoso(valorParseado2, noConsumido2) => ResultadoExitoso(valorParseado2, noConsumido2)
        }
    }
  }

  def <~[U](otroParser: Parser[U]): Parser[T] = string => {
    this(string) match {
      case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
      case ResultadoExitoso(valorParseado, noConsumido) =>
        otroParser(noConsumido) match {
          case ResultadoFallido(_) => ResultadoFallido(string)
          case ResultadoExitoso(_, noConsumido2) => ResultadoExitoso(valorParseado, noConsumido2)
        }
    }
  }
}

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