package tadp.parser
import org.scalatest.flatspec._
import org.scalatest.matchers._
import tadp.parser.Parser.{char, digit, integer, string}

class ParserCombinatorSpec extends AnyFlatSpec with should.Matchers {

  it should "Combino dos parser con el <|> y debe devolver success" in {
    val parsersCombinados = char('c') <|> char('h')
    assert(parsersCombinados("hola").get == ('h', "ola"))
  }

  it should "Combino dos parser con el <|> con distintos tipos y debe devolver success" in {
    val parsersCombinados = char('h') <|> integer()
    assert(parsersCombinados("10").get == (10, ""))
  }

  it should "Combino dos parser con el <|> y debe devolver failure" in {
    val parsersCombinados = char('c') <|> char('h')
    assert(parsersCombinados("adios").isFailure)
  }

  it should "Combino dos parser con el <|> y debe devolver failure por string vacío" in {
    val parsersCombinados = char('c') <|> char('h')
    assert(parsersCombinados("").isFailure)
  }

  it should "Combino dos parser con el <> y debe devolver success" in {
    val parsersCombinados = string("hola") <> string("mundo")
    assert(parsersCombinados("holamundo").get == (("hola", "mundo"), ""))
  }

  it should "Combino dos parser con el <> y distintos tipos y debe devolver success" in {
    val parsersCombinados = char('c') <> integer()
    assert(parsersCombinados("c123").get == (('c', 123), ""))
  }

  it should "Combino dos parser con el <> y debe devolver failure" in {
    val parsersCombinados = string("hola") <> string("mundo")
    assert(parsersCombinados("holachau").isFailure)
  }

  it should "Combino dos parser con el <> y debe devolver failure por string vacío" in {
    val parsersCombinados = string("hola") <> string("mundo")
    assert(parsersCombinados("").isFailure)
  }

  it should "Combino dos parser con el ~> y debe devolver success" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("holamundo").get == ("mundo", ""))
  }

  it should "Combino dos parser con el ~> con dos tipos y debe devolver success" in {
    val parsersCombinados = string("hola") ~> integer()
    assert(parsersCombinados("hola1234").get == (1234, ""))
  }

  it should "Combino dos parser con el ~> y debe devolver failure" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("holachau").isFailure)
  }

  it should "Combino dos parser con el ~> y debe devolver failure por string vacío" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("").isFailure)
  }

  it should "Combino dos parser con el <~ y debe devolver success" in {
    val parsersCombinados = string("hola") ~> string("mundo")
    assert(parsersCombinados("holamundo").get == ("mundo", ""))
  }

  it should "Combino dos parser con el <~ con distintos tipos y debe devolver success" in {
    val parsersCombinados = string("hola") <~ integer()
    assert(parsersCombinados("hola1234").get == ("hola", ""))
  }

  it should "Combino dos parser con el <~ y debe devolver failure" in {
    val parsersCombinados = string("hola") <~ string("mundo")
    assert(parsersCombinados("holachau").isFailure)
  }

  it should "Combino dos parser con el <~ y debe devolver failure por string vacío" in {
    val parsersCombinados = string("hola") <~ string("mundo")
    assert(parsersCombinados("").isFailure)
  }

  it should "Combino dos parser con el sepBy con distintos tipos y depende de lo que parsea devuelve exito o fallo" in {
    val parsersCombinados = integer().sepBy(char('-'))
    assert(parsersCombinados("4356-1234").get == ((4356, 1234), ""))
    assert(parsersCombinados("43561234").isFailure)
    assert(parsersCombinados("-").isFailure)
  }

  it should "Modifico parser con satisfies y debe comportarse correctamente" in {
    val parserCombinados = integer().satisfies(_ == 456)
    assert(parserCombinados("456").get == (456, ""))
    assert(parserCombinados("457").isFailure)
  }

  it should "Convierto resultado de parser en opcional" in {
    val talVezIn = string("in").opt
    val precedencia = talVezIn <> string("fija")
    assert(precedencia("infija").get == ((Some("in"), "fija"), ""))
    assert(precedencia("fija").get == ((None, "fija"), ""))
  }

  it should "Modifico parser con *" in {
    assert(digit().*("999").get == (List('9', '9', '9'), ""))
    assert(digit().*("a").get == (List(), "a"))
    assert(char('a').*("aaa4").get == (List('a', 'a', 'a'), "4"))
  }

  it should "Modifico parser con +" in {
    assert(digit().+("999").get == (List('9', '9', '9'), ""))
    assert(digit().+("a").isFailure)
    assert(char('a').+("aaa4").get == (List('a', 'a', 'a'), "4"))
  }

  it should "Modifico resultado del parser con map" in {
    assert(string("1234").map(_.toInt)("1234").get == (1234, ""))
    assert(string("abcd").map(_.toList)("abcd").get == (List('a', 'b', 'c', 'd'), ""))
  }
}