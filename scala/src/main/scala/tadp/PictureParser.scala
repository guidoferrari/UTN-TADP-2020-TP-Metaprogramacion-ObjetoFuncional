package tadp

import tadp.internal._
import tadp.parser.Parser._

import scala.util.Try

/*
  triangulo[0 @ 100, 200 @ 300, 150 @ 500]
  rectangulo[0 @ 100, 200 @ 300]
  circulo[100 @ 100, 50]

  grupo(
   triangulo[200 @ 50, 101 @ 335, 299 @ 335],
   circulo[200 @ 350, 100]
  )

   grupo(
    grupo(
   	 triangulo[250 @ 150, 150 @ 300, 350 @ 300],
   	 triangulo[150 @ 300, 50 @ 450, 250 @ 450]
    ),
    grupo(
   	 rectangulo[460 @ 90, 470 @ 100],
   	 rectangulo[450 @ 100, 480 @ 260]
    )
  )

  color[60, 150, 200](
    grupo(
   	 triangulo[200 @ 50, 101 @ 335, 299 @ 335],
   	 circulo[200 @ 350, 100]
    )
  )

  escala[2.5, 1](
	rectangulo[0 @ 100, 200 @ 300]
  )

  rotacion[45](
	rectangulo[300 @ 0, 500 @ 200]
  )

  traslacion[200, 50](
	triangulo[0 @ 100, 200 @ 300, 150 @ 500]
  )
*/

/*
  definir FORMA
  FORMA => (operacionDeForma , List[Operaciones])

  TADPDrawingAdapter.forScreen { adapter =>
    forma._2.forEach( o -> adapter.o)
    adapter.(forma._1)
  }
 */
package object PictureParser {
  type punto = (Int, Int)

  trait formaGeometrica
  case class triangulo(v1: punto, v2: punto, v3: punto) extends formaGeometrica
  case class rectangulo(v1: punto, v2: punto) extends formaGeometrica
  case class circulo(v: punto, radio: Int) extends formaGeometrica

  case class PictureParser() extends (String => Unit){
    def apply(formaDescripta: String): Unit =
    try {
      (trianguloParser() <|> rectanguloParser() <|> circuloParser()) (formaDescripta).get match{
        case (triangulo((x1, y1), (x2, y2), (x3, y3)), resto) => TADPDrawingAdapter.forScreen( adapter => adapter.triangle((x1, y1), (x2, y2), (x3, y3)))
        case (rectangulo((x1, y1), (x2, y2)), resto) => TADPDrawingAdapter.forScreen( adapter => adapter.rectangle((x1, y1), (x2, y2)))
        case (circulo((x1, y1), r), resto) => TADPDrawingAdapter.forScreen( adapter => adapter.circle((x1, y1), r))
        case _ => println("El parser falló")
      }
    } catch {
      case e: Exception => println("El parser falló")
    }
  }

  case class trianguloParser() extends Parser[triangulo] {
    override def apply(input: String): Try[ParserResult[triangulo]] = for {
      (_, resto) <- string("triangulo[")  (input)
      (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
      (((x2, y2), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
      ((x3, y3), resto) <- integer().sepBy(string(" @ "))(resto)
      (_, resto) <- char(']') (resto)
    } yield (triangulo((x1, y1), (x2, y2), (x3, y3)), resto)
  }

  case class rectanguloParser() extends Parser[rectangulo] {
    override def apply(input: String): Try[ParserResult[rectangulo]] = for {
      (_, resto) <- string("rectangulo[")  (input)
      (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
      ((x2, y2), resto) <- integer().sepBy(string(" @ ")) (resto)
      (_, resto) <- char(']') (resto)
    } yield (rectangulo((x1, y1),(x2, y2)), resto)
  }

  case class circuloParser() extends Parser[circulo] {
    override def apply(input: String): Try[ParserResult[circulo]] = for {
      (_, resto) <- string("circulo[")  (input)
      (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
      (r, resto) <- integer()(resto)
      (_, resto) <- char(']') (resto)
    } yield (circulo((x1, y1), r), resto)
  }
}


