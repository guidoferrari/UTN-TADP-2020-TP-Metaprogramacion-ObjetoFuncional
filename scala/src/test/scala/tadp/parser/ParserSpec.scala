package tadp.parser

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserSpec extends AnyFlatSpec with should.Matchers {
  it should "Parsearme exitosamente un string" in {
    val resultado = new anyChar().run("hola")
    assert(resultado.isSuccess)
    assert(resultado.get == 'h')
  }

  it should "Devolverme failure si le mando un string vacio" in {
    val resultado = new anyChar().run("")
    assert(resultado.isFailure)
  }

  it should "Devolverme éxito si parseo un string que tiene el caracter deseado" in {
    val resultado = new char('c').run("coca")
    assert(resultado.isSuccess)
    assert(resultado.get == 'c')
  }

  it should "Devolverme failure si parseo un string que no tiene el caracter deseado" in {
    val resultado = new char('c').run("papa")
    assert(resultado.isFailure)
  }

  it should "Devolverme failure si parseo un string vacio" in {
    val resultado = new char('c').run("")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que comienza con un digito" in {
    val resultado = new digit().run("1hola")
    assert(resultado.isSuccess)
    assert(resultado.get == '1')
  }

  it should "Devolverme failure si parseo un string que no comienza con un digito" in {
    val resultado = new digit().run("hola")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que comienza con el string esperado" in {
    val resultado = new string("hola").run("hola mundo")
    assert(resultado.isSuccess)
    assert(resultado.get == "hola")
  }

  it should "Devolverme failure si parseo un string que no comienza con el string esperado" in {
    val resultado = new string("hola").run("hol1")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que es un digito positivo" in {
    val resultado = new integer().run("1234")
    assert(resultado.isSuccess)
    assert(resultado.get == 1234)
  }

  it should "Devolverme success si parseo un string que es un digito negativo" in {
    val resultado = new integer().run("-1234")
    assert(resultado.isSuccess)
    assert(resultado.get == -1234)
  }

  it should "Devolverme failure si parseo un string que no es un digito" in {
    val resultado = new integer().run("hola")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que es un double" in {
    val resultado = new double().run("1234.14")
    assert(resultado.isSuccess)
    assert(resultado.get == 1234.14)
  }

  it should "Devolverme failure si parseo un string que no es un double" in {
    val resultado = new double().run("hola")
    assert(resultado.isFailure)
  }
}
