package tadp

import tadp.internal._
import tadp.parser.Parser._

import scala.util.Try

/*
  triangulo[0 @ 100, 200 @ 300, 150 @ 500]
  rectangulo[0 @ 100, 200 @ 300]
  circulo[100 @ 100, 50]
*/

/*
  definir FORMA
  FORMA => (operacionDeForma , List[Operaciones])

  TADPDrawingAdapter.forScreen { adapter =>
    forma._2.forEach( o -> adapter.o)
    adapter.(forma._1)
  }
 */

case class triangulo(v1: (Int, Int), v2: (Int, Int), v3: (Int, Int))

case class PictureParser() extends Parser[Unit] {

//  override def apply(formaDescripta: String): Try[ParserResult[Unit]] = for {
//    ((_, _), resto) <- (string("triangulo") <> char('[')) (formaDescripta)
//    (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
//    (((x2, y2), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
//    (((x3, y3), _), resto) <- (integer().sepBy(string(" @ ")) <> char(']')) (resto)
//  } yield (print(triangulo((x1, y1),(x2, y2),(x3, y3))), resto)
  type formaGeometrica = ()

  override def apply(formaDescripta: String): Try[ParserResult[Unit]] = {
    val formaYResto: (String, String) = string("triangulo") <|> string("cuadrado") (formaDescripta).get

    Try(TADPDrawingAdapter.forScreen( adapter => {
        formaYResto._1 match{
          case "triangulo" => dibujarTriangulo(formaYResto._2, adapter)
          case "cuadrado" => parsearCuadrado(formaYResto._2)
        }
        adapter.end()
      }
    ),"")
  }

  private def dibujarTriangulo(formaAParsear: String, adapter: TADPDrawingAdapter): Try[TADPDrawingAdapter] = for {
      (_, resto) <- char('[') (formaAParsear)
      (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
      (((x2, y2), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
      (((x3, y3), _), resto) <- (integer().sepBy(string(" @ ")) <> char(']')) (resto)
  } yield adapter.triangle((x1, y1),(x2, y2),(x3, y3))

  private def parsearCuadrado(formaAParsear: String): Try[ParserResult[Unit]] = for {
    (_, resto) <- char('[') (formaAParsear)
    (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
    (((x2, y2), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
    (((x3, y3), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
    (((x4, y4), _), resto) <- (integer().sepBy(string(" @ ")) <> char(']')) (resto)
  } yield (print(triangulo((x1, y1),(x2, y2),(x3, y3))), resto)
}



