package tadp

import scalafx.scene.paint.Color
import tadp.PictureParser.PicturePrinter
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
  type punto = (Double, Double)

  trait imprimible {
    def print(adapter: TADPDrawingAdapter): Unit
  }

  case class triangulo(v1: punto, v2: punto, v3: punto) extends imprimible {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.triangle(v1, v2, v3)
    }
  }

  case class rectangulo(v1: punto, v2: punto) extends imprimible {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.rectangle(v1, v2)
    }
  }

  case class circulo(v: punto, radio: Int) extends imprimible {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.circle(v, radio)
    }
  }

  case class grupo(formas: List[imprimible]) extends imprimible {
    def print(adapter: TADPDrawingAdapter): Unit = {
      formas.foreach(forma => forma.print(adapter))
    }
  }

  case class color(r: Int, g: Int, b: Int, formas: List[imprimible]) extends imprimible {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.beginColor(Color.rgb(r, g, b))
      formas.foreach(forma => forma.print(adapter))
      //adapter.end()
    }
  }

  case class parserGenerico() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = {
      (trianguloParser() <|> rectanguloParser() <|> circuloParser() <|> grupoParser() <|> colorParser()) (input)
    }
  }

  case class trianguloParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("triangulo[")  (input)
      ((x1, y1), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
      ((x2, y2), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
      ((x3, y3), resto) <- integer().sepBy(string(" @ "))(resto)
      (_, resto) <- char(']') (resto)
    } yield (triangulo((x1, y1), (x2, y2), (x3, y3)), resto)
  }

  case class rectanguloParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("rectangulo[")  (input)
      ((x1, y1), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
      ((x2, y2), resto) <- integer().sepBy(string(" @ ")) (resto)
      (_, resto) <- char(']') (resto)
    } yield (rectangulo((x1, y1),(x2, y2)), resto)
  }

  case class circuloParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("circulo[")  (input)
      ((x1, y1), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
      (r, resto) <- integer()(resto)
      (_, resto) <- char(']') (resto)
    } yield (circulo((x1, y1), r), resto)
  }

  case class grupoParser() extends Parser[grupo] {
    override def apply(input: String): Try[ParserResult[grupo]] = for {
      (_, resto) <- string("grupo(") (input)
      (formaGeometrica, resto) <- (parserGenerico() <~ string(", ")).+ (resto)
      (formaSinComa, resto) <- parserGenerico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (grupo(formaGeometrica.appended(formaSinComa)), resto)
  }

  case class colorParser() extends Parser[color] {
    override def apply(input: String): Try[ParserResult[color]] = for {
      (_, resto) <- string("color[") (input)
      (((r, g), b), resto) <- (((integer() <~ string(", ")) <> (integer() <~ string(", ")) <> integer()) <~ string("](")) (resto)
      (formaGeometrica, resto) <- (parserGenerico() <~ string(", ")).* (resto)
      (formaSinComa, resto) <- parserGenerico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (color(r, g, b, formaGeometrica.appended(formaSinComa)), resto)
  }

  case class PicturePrinter() extends (String => Unit){
    def apply(formaDescripta: String): Unit = {
      val imagen = parserGenerico() (formaDescripta)

      TADPDrawingAdapter.forScreen( adapter => {
        imagen.get match {
          case (formaGeometrica, _) => formaGeometrica.print(adapter)
          case _ => println("El parser falla")
        }
      })
    }
  }
}

object app extends App {
  // grupo anidado
  //PicturePrinter()("grupo(grupo(triangulo[250 @ 150, 150 @ 300, 350 @ 300], triangulo[150 @ 300, 50 @ 450, 250 @ 450]), grupo(rectangulo[460 @ 90, 470 @ 100], rectangulo[450 @ 100, 480 @ 260]))")

  // color
  //PicturePrinter()("color[60, 150, 200](grupo(triangulo[200 @ 50, 101 @ 335, 299 @ 335], circulo[200 @ 350, 100]))")


}