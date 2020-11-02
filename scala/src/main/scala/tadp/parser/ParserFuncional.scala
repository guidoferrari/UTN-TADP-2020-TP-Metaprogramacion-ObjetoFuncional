package object ParserFuncional{

  trait ParserResult[+T]

  case class ResultadoExitoso[T](valorParseado: T, noConsumido: String) extends ParserResult[T]
  case class ResultadoFallido[T](noConsumido: String) extends ParserResult[T]

  type Parser[+T] = String => ParserResult[T]

  val anyChar: Parser[Char] = {
    case string@"" => ResultadoFallido(string)
    case string => ResultadoExitoso(string.charAt(0), string.substring(1))
  }

  val char: Char => Parser[Char] = char => anyChar.andThen({
    case ResultadoExitoso(valorParseado, noConsumido) if valorParseado == char => ResultadoExitoso(valorParseado, noConsumido)
    case ResultadoExitoso(valorParseado, noConsumido) => ResultadoFallido(s"$valorParseado$noConsumido")
    case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
  })

  val digit: Parser[Char] = anyChar.andThen({
    case ResultadoExitoso(valorParseado, noConsumido) if valorParseado.isDigit => ResultadoExitoso(valorParseado, noConsumido)
    case ResultadoExitoso(valorParseado, noConsumido) => ResultadoFallido(s"$valorParseado$noConsumido")
    case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
  })

  val string: String => Parser[String] = string => recibido => {
    if (recibido.take(string.length()).equals(string)){
      ResultadoExitoso(recibido.take(string.length()), recibido.substring(string.length()))
    }
    else {
      ResultadoFallido(recibido)
    }
  }

  val integer: Parser[Integer] = string => {
    string.toIntOption match {
      case Some(i) => ResultadoExitoso(i, "")
      case _ => ResultadoFallido(string)
    }
  }

  val double: Parser[Double] = string => {
    string.toDoubleOption match {
      case Some(d) => ResultadoExitoso(d, "")
      case _ => ResultadoFallido(string)
    }
  }
}