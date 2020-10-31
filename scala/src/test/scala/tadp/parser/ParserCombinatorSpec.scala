package tadp.parser

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserCombinatorSpec extends AnyFlatSpec with should.Matchers {

  it should "Combino dos parser con el <|> y debe devolver success" in {
    val parser1 = new char('c')
    val parser2 = new char('h')
    val parsersCombinados = parser1 <|> parser2

    val resultado = parsersCombinados("hola")

    assert(resultado == ResultadoExitoso('h', "ola"))
  }

  it should "Combino dos parser con el <|> y debe devolver failure" in {
    val parser1 = new char('c')
    val parser2 = new char('h')
    val parsersCombinados = parser1 <|> parser2

    val resultado = parsersCombinados("adios")

    assert(resultado == ResultadoFallido("adios"))
  }

  it should "Combino dos parser con el <|> y debe devolver failure por string vacío" in {
    val parser1 = new char('c')
    val parser2 = new char('h')
    val parsersCombinados = parser1 <|> parser2

    val resultado = parsersCombinados("")

    assert(resultado == ResultadoFallido(""))
  }

  it should "Combino dos parser con el <> y debe devolver success" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <> parser2

    val resultado = parsersCombinados("holamundo")

    assert(resultado == ResultadoExitoso(("hola", "mundo"), ""))
  }

  it should "Combino dos parser con el <> y debe devolver failure" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <> parser2

    val resultado = parsersCombinados("holachau")

    assert(resultado == ResultadoFallido("holachau"))
  }

  it should "Combino dos parser con el <> y debe devolver failure por string vacío" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <> parser2

    val resultado = parsersCombinados("")

    assert(resultado == ResultadoFallido(""))
  }

  it should "Combino dos parser con el ~> y debe devolver success" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 ~> parser2

    val resultado = parsersCombinados("holamundo")

    assert(resultado == ResultadoExitoso("mundo", ""))
  }

  it should "Combino dos parser con el ~> y debe devolver failure" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 ~> parser2

    val resultado = parsersCombinados("holachau")

    assert(resultado == ResultadoFallido("holachau"))
  }

  it should "Combino dos parser con el ~> y debe devolver failure por string vacío" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 ~> parser2

    val resultado = parsersCombinados("")

    assert(resultado == ResultadoFallido(""))
  }

  it should "Combino dos parser con el <~ y debe devolver success" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <~ parser2

    val resultado = parsersCombinados("holamundo")

    assert(resultado == ResultadoExitoso("hola", ""))
  }

  it should "Combino dos parser con el <~ y debe devolver failure" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <~ parser2

    val resultado = parsersCombinados("holachau")

    assert(resultado == ResultadoFallido("holachau"))
  }

  it should "Combino dos parser con el <~ y debe devolver failure por string vacío" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <~ parser2

    val resultado = parsersCombinados("")

    assert(resultado == ResultadoFallido(""))
  }

  //  sepBy: toma dos parsers: un parser de contenido y un parser separador,
  //  parsea 1 o más veces el parser de contenido (similar a la cláusula de kleene+) pero entre cada una aplica el parser separador.
  //    Ejemplo:
  //  val numeroDeTelefono = integer.sepBy(char('-'))
  //  debería funcionar si le paso “4356-1234” pero no si le paso “4356 1234”.
  // val asd = string("hola").sepBy(char('-'))
  // asd.parse("hola-hola")

//  it should "Combino dos parser con el sepBy y debe devolver success" in {
//    val parsersCombinados = integer.sepBy(char('-'))
//
//    val resultado = parsersCombinados.parse("4356-1234")
//
//    assert(resultado.isSuccess)
//    assert(resultado.get((4356,'-',1234), ""))
//  }
//
//  it should "Combino dos parser con el sepBy y debe devolver failure" in {
//    val parsersCombinados = integer.sepBy(char('-'))
//
//    val resultado = parsersCombinados.parse("4356 1234")
//
//    assert(resultado.isFailure)
//  }
}
