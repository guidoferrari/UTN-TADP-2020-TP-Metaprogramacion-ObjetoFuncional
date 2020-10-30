package tadp.parser

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserSpec extends AnyFlatSpec with should.Matchers {
  it should "Parsearme exitosamente un string" in {
    val resultado = new anyChar().parse("hola")
    assert(resultado.isSuccess)
    assert(resultado.get == ('h', "ola"))
  }

  it should "Devolverme failure si le mando un string vacio" in {
    val resultado = new anyChar().parse("")
    assert(resultado.isFailure)
  }

  it should "Devolverme éxito si parseo un string que tiene el caracter deseado" in {
    val resultado = new char('c').parse("coca")
    assert(resultado.isSuccess)
    assert(resultado.get == ('c', "oca"))
  }

  it should "Devolverme failure si parseo un string que no tiene el caracter deseado" in {
    val resultado = new char('c').parse("papa")
    assert(resultado.isFailure)
  }

  it should "Devolverme failure si parseo un string vacio" in {
    val resultado = new char('c').parse("")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que comienza con un digito" in {
    val resultado = new digit().parse("1hola")
    assert(resultado.isSuccess)
    assert(resultado.get == ('1', "hola"))
  }

  it should "Devolverme failure si parseo un string que no comienza con un digito" in {
    val resultado = new digit().parse("hola")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que comienza con el string esperado" in {
    val resultado = new string("hola").parse("hola mundo")
    assert(resultado.isSuccess)
    assert(resultado.get == ("hola"," mundo"))
  }

  it should "Devolverme failure si parseo un string que no comienza con el string esperado" in {
    val resultado = new string("hola").parse("hol1")
    assert(resultado.isFailure)
  }

  it should "Devolverme failure si parseo un string que es vacio" in {
    val resultado = new string("hola").parse("")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que es un digito positivo" in {
    val resultado = new integer().parse("1234")
    assert(resultado.isSuccess)
    assert(resultado.get == (1234, ""))
  }

  it should "Devolverme success si parseo un string que es un digito negativo" in {
    val resultado = new integer().parse("-1234")
    assert(resultado.isSuccess)
    assert(resultado.get == (-1234, ""))
  }

  it should "Devolverme failure si parseo un string que no es un digito" in {
    val resultado = new integer().parse("hola")
    assert(resultado.isFailure)
  }

  it should "Devolverme success si parseo un string que es un double" in {
    val resultado = new double().parse("1234.14")
    assert(resultado.isSuccess)
    assert(resultado.get == (1234.14, ""))
  }

  it should "Devolverme failure si parseo un string que no es un double" in {
    val resultado = new double().parse("hola")
    assert(resultado.isFailure)
  }
}
