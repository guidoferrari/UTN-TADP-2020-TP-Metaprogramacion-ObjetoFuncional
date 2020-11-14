package tadp.parser

import scala.util.{Failure, Success, Try}

package object Parser{
  final case class ParserException(message: String = "") extends Exception(message)
  type ParserResult[+T] = (T, String)

  abstract class Parser[T] extends (String => Try[ParserResult[T]]) {

    def <|>[R >: T, U <: R](otroParser: Parser[U]): Parser[R] = string => this(string).recoverWith { case _ => otroParser(string) }

    def <>[U](otroParser: Parser[U]): Parser[(T, U)] = string => for {
      (resultParser, restoParser) <- this(string)
      (resultOtroParser, restoOtroParser) <- otroParser(restoParser)
    } yield ((resultParser, resultOtroParser), restoOtroParser)

    def ~>[U](otroParser: Parser[U]): Parser[U] = string => for {
      ((_, resultOtroParser), restoOtroParser) <- (this <> otroParser) (string)
    } yield (resultOtroParser, restoOtroParser)

    def <~[U](otroParser: Parser[U]): Parser[T] = string => for {
      ((resultParser, _), restoOtroParser) <- (this <> otroParser) (string)
    } yield (resultParser, restoOtroParser)

    def sepBy[U](parserSeparador: Parser[U]): Parser[(T, T)] = (this <~ parserSeparador) <> this

    def satisfies(condicion: T => Boolean): Parser[T] = {
      val ret: Parser[T] = this(_).filter { case (resultado, _) => condicion(resultado) }
      ret.recuperarConParserException
    }

    def opt: Parser[Option[T]] = string => this.map(Some(_))(string).recover { case _ => (None, string) }

    def * : Parser[List[T]] = string => Success(kleeneConAcumulador((List(), string)))

    def + : Parser[List[T]] = this.*.satisfies(_.nonEmpty)

    def map[U](f: T => U): Parser[U] = string => for {
      (resultado, resto) <- this(string)
    } yield (f(resultado), resto)

    def recuperarConParserException: Parser[T] = this(_).recover { case _ => throw new ParserException }

    private def kleeneConAcumulador(accum: ParserResult[List[T]]): ParserResult[List[T]] =
      this(accum._2).fold(_ => accum,
        { case (nuevoResultado, nuevoResto) => kleeneConAcumulador((accum._1 :+ nuevoResultado, nuevoResto)) }
      )
  }

  case class anyChar() extends Parser[Char]{
    override def apply(string: String): Try[ParserResult[Char]] = {
      val resultado = Try(string.charAt(0), string.substring(1))
      recuperarConParserException
      resultado
    }
  }

  case class char(char: Char) extends Parser[Char]{
    override def apply(string: String): Try[ParserResult[Char]] = anyChar().satisfies(_ == char)(string)
  }

  case class digit() extends Parser[Char]{
    override def apply(string: String): Try[ParserResult[Char]] = anyChar().satisfies(_.isDigit)(string)
  }

  case class integer() extends Parser[Int]{
    override def apply(string: String): Try[ParserResult[Int]] = string.charAt(0) match{
      case '-' =>
        val resultado = digit().+.map(_.mkString.toInt)(string.substring(1))
        Try(resultado.get._1 * (-1), resultado.get._2)
      case _ => digit().+.map(_.mkString.toInt)(string)
      }
  }

  case class double() extends Parser[Double]{
    override def apply(string: String): Try[ParserResult[Double]] =
      try {
        val parseado = (integer().sepBy(char('.')) <|> integer())(string).get
        var resultado: Try[ParserResult[Double]] = null
        parseado match {
          case ((entero,decimal), resto) => resultado = Try(new StringBuilder().append(entero).append('.').append(decimal).result().toDouble, resto)
          case (entero, resto) => resultado = Try(entero.toString.toDouble, resto)
        }
        recuperarConParserException
        resultado
      } catch {
        case _ => Try(throw new ParserException)
      }
  }

  case class string(stringBuscado: String) extends Parser[String]{
    override def apply(string: String): Try[ParserResult[String]] = stringBuscado.toList.map(c => char(c)).foldLeft(exitosoConResultado("")) {
      (parserAccum: Parser[String], charParser) => (parserAccum <> charParser)
        .map { case (strAccum, charNuevo) => strAccum + charNuevo }
    }(string)
  }
  private def exitosoConResultado[T](value: T): Parser[T] = input => Success((value, input))
}