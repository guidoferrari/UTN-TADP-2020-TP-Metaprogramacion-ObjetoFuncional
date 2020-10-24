package tadp.parser

import org.scalatest.flatspec._
import org.scalatest.matchers._

class ParserSpec extends AnyFlatSpec with should.Matchers {
  it should "Parsearme exitosamente un string" in {
    val resultado = new anyChar().run("Hola")
    assert(resultado.isSuccess)
    assert(resultado.get == 'H')
  }

  it should "Devolverme failure si le mando un string vacio" in {
    val resultado = new anyChar().run("")
    assert(resultado.isFailure)
  }
}
