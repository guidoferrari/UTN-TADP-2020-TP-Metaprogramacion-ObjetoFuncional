trait ParserResult[+T]

case class ResultadoExitoso[T](valorParseado: T, noConsumido: String) extends ParserResult[T]
case class ResultadoFallido[T](noConsumido: String) extends ParserResult[T]

type Parser[+T] = String => ParserResult[T]

val anyChar: Parser[Char] = {
  case string@ "" => ResultadoFallido(string)
  case string => ResultadoExitoso(string.charAt(0), string.substring(1))
}

val boolean = anyChar("hola") == ResultadoExitoso('h', "ola")
System.out.println("El boolean es " + boolean)


