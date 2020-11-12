package tadp

import tadp.internal._
import tadp.parser.Parser._

import scala.util.Try

/*
  triangulo[0 @ 100, 200 @ 300, 150 @ 500]
  rectangulo[0 @ 100, 200 @ 300]
  circulo[100 @ 100, 50]
*/

case class triangulo(v1: (Int, Int), v2: (Int, Int), v3: (Int, Int))

case class PictureParser() extends Parser[Unit] {

  override def apply(formaDescripta: String): Try[ParserResult[Unit]] = for {

    ((_, _), resto) <- (string("triangulo") <> char('[')) (formaDescripta)
    (((x1, y1), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
    (((x2, y2), _), resto) <- (integer().sepBy(string(" @ ")) <> string(", ")) (resto)
    (((x3, y3), _), resto) <- (integer().sepBy(string(" @ ")) <> char(']')) (resto)

  } yield (print(triangulo((x1, y1),(x2, y2),(x3, y3))), resto)

  private def print(triangulo: triangulo): Unit = {
    TADPDrawingAdapter.forScreen( adapter =>
    adapter.triangle((triangulo.v1._1, triangulo.v1._2), (triangulo.v2._1, triangulo.v2._2), (triangulo.v3._1, triangulo.v3._2))
    )
  }
}


