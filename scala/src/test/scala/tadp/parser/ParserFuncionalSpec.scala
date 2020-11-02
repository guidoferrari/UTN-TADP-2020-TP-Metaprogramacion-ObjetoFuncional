package tadp.parser
import ParserFuncional._
import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserFuncionalSpec extends AnyFlatSpec with should.Matchers {
  it should "Parsearme exitosamente un string" in {
    assert(anyChar("hola") == ResultadoExitoso('h', "ola"))
  }

  it should "Devolverme failure si le mando un string vacio" in {
    assert(anyChar("") == ResultadoFallido(""))
  }

  it should "Devolverme éxito si parseo un string que tiene el caracter deseado" in {
    assert(char('c')("coca") == ResultadoExitoso('c', "oca"))
  }

  it should "Devolverme failure si parseo un string que no tiene el caracter deseado" in {
    assert(char('c')("papa") == ResultadoFallido("papa"))
  }

  it should "Devolverme failure si parseo un string vacio" in {
    assert(char('c')("") == ResultadoFallido(""))
  }

  it should "Devolverme success si parseo un string que comienza con un digito" in {
    assert(digit("1hola") == ResultadoExitoso('1', "hola"))
  }

  it should "Devolverme failure si parseo un string que no comienza con un digito" in {
    assert(digit("hola") == ResultadoFallido("hola"))
  }

  it should "Devolverme success si parseo un string que comienza con el string esperado" in {
    assert(string("hola")("hola mundo") == ResultadoExitoso("hola"," mundo"))
  }

  it should "Devolverme failure si parseo un string que no comienza con el string esperado" in {
    assert(string("hola")("hol1") == ResultadoFallido("hol1"))
  }

  it should "Devolverme failure si parseo un string que es vacio" in {
    assert(string("hola")("") == ResultadoFallido(""))
  }

  it should "Devolverme success si parseo un string que es un digito positivo" in {
    assert(integer("1234") == ResultadoExitoso(1234, ""))
  }

  it should "Devolverme success si parseo un string que es un digito negativo" in {
    assert(integer("-1234") == ResultadoExitoso(-1234, ""))
  }

  it should "Devolverme failure si parseo un string que no es un digito" in {
    assert(integer("hola") == ResultadoFallido("hola"))
  }

  it should "Devolverme success si parseo un string que es un double" in {
    assert(double("1234.14") == ResultadoExitoso(1234.14, ""))
  }

  it should "Devolverme failure si parseo un string que no es un double" in {
    assert(double("hola") == ResultadoFallido("hola"))
  }
}
