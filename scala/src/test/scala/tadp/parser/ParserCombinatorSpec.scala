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

  it should "Combino dos parser con el <|> con distintos tipos y debe devolver success" in {
    val parser1 = new char('h')
    val parser2 = new integer()
    val parsersCombinados = parser1 <|> parser2

    val resultado = parsersCombinados("10")

    assert(resultado == ResultadoExitoso(10, ""))
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

  it should "Combino dos parser con el <> y distintos tipos y debe devolver success" in {
    val parser1 = new char('c')
    val parser2 = new integer()
    val parsersCombinados = parser1 <> parser2

    val resultado = parsersCombinados("c123")

    assert(resultado == ResultadoExitoso(('c', 123), ""))
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

  it should "Combino dos parser con el ~> con dos tipos y debe devolver success" in {
    val parser1 = new string("hola")
    val parser2 = new integer()
    val parsersCombinados = parser1 ~> parser2

    val resultado = parsersCombinados("hola1234")

    assert(resultado == ResultadoExitoso(1234, ""))
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

  it should "Combino dos parser con el <~ con distintos tipos y debe devolver success" in {
    val parser1 = new string("hola")
    val parser2 = new integer()
    val parsersCombinados = parser1 <~ parser2

    val resultado = parsersCombinados("hola1234")

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

//    it should "Combino dos parser con el sepBy y debe devolver success" in {
//      val parsersCombinados = new integer().sepBy(new char('-'))
//
//      val resultado = parsersCombinados("4356-1234")
//
//      assert(resultado == ResultadoExitoso((4356,1234), ""))
//    }
//
//    it should "Combino dos parser con el sepBy y debe devolver failure" in {
//      val parsersCombinados = new integer().sepBy(new char('-'))
//
//      val resultado = parsersCombinados("4356 1234")
//
//      assert(resultado == ResultadoFallido("4356 1234"))
//    }
}