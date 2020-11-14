package tadp

import org.scalatest.flatspec._
import org.scalatest.matchers._
import tadp.PictureParser.{circulo, color, escala, grupo, parserGrafico, rectangulo, rotacion, traslacion, triangulo}

class PictureParserSpec extends AnyFlatSpec with should.Matchers {

  it should "Parsea un triángulo correctamente" in {
    val textoAParsear = "triangulo[0 @ 100, 200 @ 300, 150 @ 500]"
    val objetoEsperado = triangulo((0, 100), (200, 300), (150, 500))
    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Parsea un rectángulo correctamente" in {
    val textoAParsear = "rectangulo[0 @ 100, 200 @ 300]"
    val objetoEsperado = rectangulo((0, 100), (200, 300))
    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Parsea un círculo correctamente" in {
    val textoAParsear = "circulo[200 @ 350, 100]"
    val objetoEsperado = circulo((200, 350), 100)
    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Un grupo anidado debería generarme los objetos correctos" in {
    val textoAParsear = "grupo(grupo(triangulo[250 @ 150, 150 @ 300, 350 @ 300], triangulo[150 @ 300, 50 @ 450, 250 @ 450]), grupo(rectangulo[460 @ 90, 470 @ 100], rectangulo[450 @ 100, 480 @ 260]))"
    val objetoEsperado = grupo(List(
      grupo(List(
        triangulo((250, 150), (150, 300), (350, 300)),
        triangulo((150, 300), (50, 450), (250, 450))
      )),
      grupo(List(
        rectangulo((460, 90), (470, 100)),
        rectangulo((450, 100), (480, 260))
      ))
    ))

    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Aplicar un color debería generarme los objetos correctos" in {
    val textoAParsear = "color[60, 150, 200](grupo(triangulo[200 @ 50, 101 @ 335, 299 @ 335], circulo[200 @ 350, 100]))"
    val objetoEsperado = color(60, 150, 200,
      grupo(List(
        triangulo((200, 50), (101, 335), (299, 335)),
        circulo((200, 350), 100)
      ))
    )

    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Aplicar una escala debería generarme los objetos correctos" in {
    val textoAParsear = "escala[2.5, 1](rectangulo[0 @ 100, 200 @ 300])"
    val objetoEsperado =
      escala(2.5, 1,
        rectangulo((0, 100), (200, 300))
      )

    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Aplicar una rotación debería generarme los objetos correctos" in {
    val textoAParsear = "rotacion[45](rectangulo[300 @ 0, 500 @ 200])"
    val objetoEsperado =
      rotacion(45,
        rectangulo((300, 0), (500, 200))
      )

    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

  it should "Aplicar una traslación debería generarme los objetos correctos" in {
    val textoAParsear = "traslacion[200, 50](triangulo[0 @ 100, 200 @ 300, 150 @ 500])"
    val objetoEsperado =
      traslacion(200, 50,
        triangulo((0, 100), (200, 300), (150, 500))
      )

    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
  }

//  it should "Aplicar un color a otro color debería simplificarse y quedar el de adentro" in {
//    val textoAParsear = "color[200, 200, 200](color[150, 150, 150](rectangulo[100 @ 100, 200 @ 200]))"
//    val objetoEsperado = color(150, 150, 150, rectangulo((100, 100), (200, 200)))
//
//    assert(parserGrafico()(textoAParsear).get == (objetoEsperado, ""))
//  }
}
