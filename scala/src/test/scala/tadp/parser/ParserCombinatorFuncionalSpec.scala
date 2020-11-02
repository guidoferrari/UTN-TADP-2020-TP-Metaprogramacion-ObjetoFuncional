package tadp.parser
import ParserFuncional._
import ParserCombinatorsFuncional._
import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserCombinatorFuncionalSpec extends AnyFlatSpec with should.Matchers {

  it should "Combino dos parser con el <|> y debe devolver success" in {
    val parsersCombinados = char('c') <|> char('h')
    assert(parsersCombinados("hola") == ResultadoExitoso('h', "ola"))
  }

  it should "Combino dos parser con el <|> con distintos tipos y debe devolver success" in {
    val parsersCombinados = char('h') <|> integer
    assert(parsersCombinados("10") == ResultadoExitoso(10, ""))
  }

  it should "Combino dos parser con el <|> y debe devolver failure" in {
    val parsersCombinados = char('c') <|> char('h')
    assert(parsersCombinados("adios") == ResultadoFallido("adios"))
  }

  it should "Combino dos parser con el <|> y debe devolver failure por string vacío" in {
    val parsersCombinados = char('c') <|> char('h')
    assert(parsersCombinados("") == ResultadoFallido(""))
  }

  it should "Combino dos parser con el <> y debe devolver success" in {
    val parsersCombinados = string("hola") <> string("mundo")
    assert(parsersCombinados("holamundo") == ResultadoExitoso(("hola", "mundo"), ""))
  }

  it should "Combino dos parser con el <> y distintos tipos y debe devolver success" in {
    val parsersCombinados = char('c') <> integer
    assert(parsersCombinados("c123") == ResultadoExitoso(('c', 123), ""))
  }

  it should "Combino dos parser con el <> y debe devolver failure" in {
    val parsersCombinados = string("hola") <> string("mundo")
    assert(parsersCombinados("holachau") == ResultadoFallido("holachau"))
  }

  it should "Combino dos parser con el <> y debe devolver failure por string vacío" in {
    val parsersCombinados = string("hola") <> string("mundo")
    assert(parsersCombinados("") == ResultadoFallido(""))
  }

  it should "Combino dos parser con el ~> y debe devolver success" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("holamundo") == ResultadoExitoso("mundo", ""))
  }

  it should "Combino dos parser con el ~> con dos tipos y debe devolver success" in {
    val parsersCombinados = string("hola") ~> integer
    assert(parsersCombinados("hola1234") == ResultadoExitoso(1234, ""))
  }

  it should "Combino dos parser con el ~> y debe devolver failure" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("holachau") == ResultadoFallido("holachau"))
  }

  it should "Combino dos parser con el ~> y debe devolver failure por string vacío" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("") == ResultadoFallido(""))
  }

  it should "Combino dos parser con el <~ y debe devolver success" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("holamundo") == ResultadoExitoso("mundo", ""))
  }

  it should "Combino dos parser con el <~ con distintos tipos y debe devolver success" in {
    val parsersCombinados = string("hola") <~ integer
    assert(parsersCombinados("hola1234") == ResultadoExitoso("hola", ""))
  }

  it should "Combino dos parser con el <~ y debe devolver failure" in {
    val parsersCombinados = string("hola") <~ string("mundo")
    assert(parsersCombinados("holachau") == ResultadoFallido("holachau"))
  }

  it should "Combino dos parser con el <~ y debe devolver failure por string vacío" in {
    val parsersCombinados = string("hola") <~ string("mundo")
    assert(parsersCombinados("") == ResultadoFallido(""))
  }
}