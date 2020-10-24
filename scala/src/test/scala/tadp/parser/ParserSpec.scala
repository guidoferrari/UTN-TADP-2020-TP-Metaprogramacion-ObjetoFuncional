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
}
