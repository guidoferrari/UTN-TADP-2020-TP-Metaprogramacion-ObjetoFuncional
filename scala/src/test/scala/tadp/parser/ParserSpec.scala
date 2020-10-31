package tadp.parser

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserSpec extends AnyFlatSpec with should.Matchers {
  it should "Parsearme exitosamente un string" in {
    val parser = new anyChar()
    val resultado = parser("hola")

    assert(resultado == ResultadoExitoso('h', "ola"))
  }

  it should "Devolverme failure si le mando un string vacio" in {
    val parser = new anyChar()
    val resultado = parser("")

    assert(resultado == ResultadoFallido(""))
  }

  it should "Devolverme éxito si parseo un string que tiene el caracter deseado" in {
    val parser = new char('c')
    val resultado = parser("coca")

    assert(resultado == ResultadoExitoso('c', "oca"))
  }

  it should "Devolverme failure si parseo un string que no tiene el caracter deseado" in {
    val parser = new char('c')
    val resultado = parser("papa")

    assert(resultado == ResultadoFallido("papa"))
  }

  it should "Devolverme failure si parseo un string vacio" in {
    val parser = new char('c')
    val resultado = parser("")

    assert(resultado == ResultadoFallido(""))
  }

  it should "Devolverme success si parseo un string que comienza con un digito" in {
    val parser = new digit()
    val resultado = parser("1hola")

    assert(resultado == ResultadoExitoso('1', "hola"))
  }

  it should "Devolverme failure si parseo un string que no comienza con un digito" in {
    val parser = new digit()
    val resultado = parser("hola")

    assert(resultado == ResultadoFallido("hola"))
  }

  it should "Devolverme success si parseo un string que comienza con el string esperado" in {
    val parser = new string("hola")
    val resultado = parser("hola mundo")

    assert(resultado == ResultadoExitoso("hola"," mundo"))
  }

  it should "Devolverme failure si parseo un string que no comienza con el string esperado" in {
    val parser = new string("hola")
    val resultado = parser("hol1")

    assert(resultado == ResultadoFallido("hol1"))
  }

  it should "Devolverme failure si parseo un string que es vacio" in {
    val parser = new string("hola")
    val resultado = parser("")

    assert(resultado == ResultadoFallido(""))
  }

  it should "Devolverme success si parseo un string que es un digito positivo" in {
    val parser = new integer()
    val resultado = parser("1234")

    assert(resultado == ResultadoExitoso(1234, ""))
  }

  it should "Devolverme success si parseo un string que es un digito negativo" in {
    val parser = new integer()
    val resultado = parser("-1234")

    assert(resultado == ResultadoExitoso(-1234, ""))
  }

  it should "Devolverme failure si parseo un string que no es un digito" in {
    val parser = new integer()
    val resultado = parser("hola")

    assert(resultado == ResultadoFallido("hola"))
  }

  it should "Devolverme success si parseo un string que es un double" in {
    val parser = new double()
    val resultado = parser("1234.14")

    assert(resultado == ResultadoExitoso(1234.14, ""))
  }

  it should "Devolverme failure si parseo un string que no es un double" in {
    val parser = new double()
    val resultado = parser("hola")

    assert(resultado == ResultadoFallido("hola"))
  }
}
