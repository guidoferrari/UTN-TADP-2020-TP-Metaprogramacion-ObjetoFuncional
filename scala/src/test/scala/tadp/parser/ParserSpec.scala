package tadp.parser
import org.scalatest.flatspec._
import org.scalatest.matchers._
import tadp.parser.Parser.{anyChar, char, digit, double, integer, string}

class ParserSpec extends AnyFlatSpec with should.Matchers {
  it should "Testeo comportamiento de anyChar" in {
    assert(anyChar()("hola").get == ('h', "ola"))
  }

  it should "Devolverme failure si le mando un string vacio" in {
    assert(anyChar()("").isFailure)
  }

  it should "Devolverme éxito si parseo un string que tiene el caracter deseado" in {
    assert(char('c')("coca").get == ('c', "oca"))
  }

  it should "Devolverme failure si parseo un string que no tiene el caracter deseado" in {
    assert(char('c')("papa").isFailure)
  }

  it should "Devolverme failure si parseo un string vacio" in {
    assert(char('c')("").isFailure)
  }

  it should "Devolverme success si parseo un string que comienza con un digito" in {
    assert(digit()("1hola").get == ('1', "hola"))
  }

  it should "Devolverme failure si parseo un string que no comienza con un digito" in {
    assert(digit()("hola").isFailure)
  }

  it should "Devolverme success si parseo un string que es el string esperado" in {
    assert(string("hola")("hola").get == ("hola", ""))
  }

  it should "Devolverme success si parseo un string que comienza con el string esperado" in {
    assert(string("hola")("hola mundo").get == ("hola"," mundo"))
  }

  it should "Devolverme failure si parseo un string que no comienza con el string esperado" in {
    assert(string("hola")("hol1").isFailure)
  }

  it should "Devolverme failure si parseo un string que es vacio" in {
    assert(string("hola")("").isFailure)
  }

  it should "Devolverme success si parseo un string que es un digito positivo" in {
    assert(integer()("1234").get == (1234, ""))
  }

  it should "Devolverme success si parseo un string que es un digito negativo" in {
    assert(integer()("-1234").get == (-1234, ""))
  }

  it should "Devolverme failure si parseo un string que no es un digito" in {
    assert(integer()("hola").isFailure)
  }

  it should "Devolverme success si parseo un string que es un double" in {
    assert(double()("1234.14").get == (1234.14, ""))
  }

  it should "Devolverme failure si parseo un string que no es un double" in {
    assert(double()("hola").isFailure)
  }
}
