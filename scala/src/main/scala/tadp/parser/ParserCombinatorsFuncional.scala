import ParserFuncional._

package object ParserCombinatorsFuncional{
  implicit class ParserCombinado[+T](parser: Parser[T]) {
    def <|>[V >: T, U <: V](otroParser: Parser[U]): Parser[V] = string => {
      parser(string) match {
        case ResultadoFallido(noConsumido) => otroParser(noConsumido)
        case ResultadoExitoso(valorParseado, noConsumido) => ResultadoExitoso(valorParseado, noConsumido)
      }
    }

    def <>[U](otroParser: Parser[U]): Parser[(T,U)] = string => {
      parser(string) match {
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
      parser(string) match {
        case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
        case ResultadoExitoso(_, noConsumido) =>
          otroParser(noConsumido) match {
            case ResultadoFallido(_) => ResultadoFallido(string)
            case ResultadoExitoso(valorParseado2, noConsumido2) => ResultadoExitoso(valorParseado2, noConsumido2)
          }
      }
    }

    def <~[U](otroParser: Parser[U]): Parser[T] = string => {
      parser(string) match {
        case ResultadoFallido(noConsumido) => ResultadoFallido(noConsumido)
        case ResultadoExitoso(valorParseado, noConsumido) =>
          otroParser(noConsumido) match {
            case ResultadoFallido(_) => ResultadoFallido(string)
            case ResultadoExitoso(_, noConsumido2) => ResultadoExitoso(valorParseado, noConsumido2)
          }
      }
    }
  }
}