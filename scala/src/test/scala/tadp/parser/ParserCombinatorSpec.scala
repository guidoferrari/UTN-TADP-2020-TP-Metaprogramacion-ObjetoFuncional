package tadp.parser

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserCombinatorSpec extends AnyFlatSpec with should.Matchers {

  it should "Combino dos parser con el <|> y debe devolver success" in {
    val parser1 = new char('c')
    val parser2 = new char('h')
    val parsersCombinados = parser1 <|> parser2

    val resultado = parsersCombinados.parse("hola")

    assert(resultado.isSuccess)
    assert(resultado.get == ('h', "ola"))
  }

  it should "Combino dos parser con el <|> y debe devolver failure" in {
    val parser1 = new char('c')
    val parser2 = new char('h')
    val parsersCombinados = parser1 <|> parser2

    val resultado = parsersCombinados.parse("adios")

    assert(resultado.isFailure)
  }

  it should "Combino dos parser con el <> y debe devolver success" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <> parser2

    val resultado = parsersCombinados.parse("holamundo")

    assert(resultado.isSuccess)
    assert(resultado.get == (("hola", "mundo"), ""))
  }

  it should "Combino dos parser con el <> y debe devolver failure" in {
    val parser1 = new string("hola")
    val parser2 = new string("mundo")
    val parsersCombinados = parser1 <> parser2

    val resultado = parsersCombinados.parse("holachau")

    assert(resultado.isFailure)
  }
}
