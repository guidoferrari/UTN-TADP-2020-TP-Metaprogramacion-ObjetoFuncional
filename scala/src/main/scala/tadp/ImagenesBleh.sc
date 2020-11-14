import tadp.internal.TADPDrawingAdapter
import tadp.parser.Parser.{Parser, ParserResult, char, integer, string}

import scala.util.Try

type punto = (Int, Int)

trait formaGeometrica
case class triangulo(v1: punto, v2: punto, v3: punto) extends formaGeometrica
case class rectangulo(v1: punto, v2: punto) extends formaGeometrica
case class circulo(v: punto, radio: Int) extends formaGeometrica

case class trianguloParser() extends Parser[triangulo] {
  override def apply(input: String): Try[ParserResult[triangulo]] = for {
    (_, resto) <- string("triangulo[")  (input)
    ((x1, y1), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
    ((x2, y2), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
    ((x3, y3), resto) <- integer().sepBy(string(" @ "))(resto)
    (_, resto) <- char(']') (resto)
  } yield (triangulo((x1, y1), (x2, y2), (x3, y3)), resto)
}

case class rectanguloParser() extends Parser[rectangulo] {
  override def apply(input: String): Try[ParserResult[rectangulo]] = for {
    (_, resto) <- string("rectangulo[")  (input)
    ((x1, y1), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
    ((x2, y2), resto) <- integer().sepBy(string(" @ ")) (resto)
    (_, resto) <- char(']') (resto)
  } yield (rectangulo((x1, y1),(x2, y2)), resto)
}

case class circuloParser() extends Parser[circulo] {
  override def apply(input: String): Try[ParserResult[circulo]] = for {
    (_, resto) <- string("circulo[")  (input)
    ((x1, y1), resto) <- (integer().sepBy(string(" @ ")) <~ string(", ")) (resto)
    (r, resto) <- integer()(resto)
    (_, resto) <- char(']') (resto)
  } yield (circulo((x1, y1), r), resto)
}

case class PictureParser() extends (String => Unit){
  def apply(formaDescripta: String): Unit =
    try {
      (trianguloParser() <|> rectanguloParser() <|> circuloParser()) (formaDescripta).get match{
      case (triangulo((x1, y1), (x2, y2), (x3, y3)), resto) => TADPDrawingAdapter.forScreen( adapter => adapter.triangle((x1, y1), (x2, y2), (x3, y3)))
      case (rectangulo((x1, y1), (x2, y2)), resto) => TADPDrawingAdapter.forScreen( adapter => adapter.rectangle((x1, y1), (x2, y2)))
      case (circulo((x1, y1), r), resto) => TADPDrawingAdapter.forScreen( adapter => adapter.circle((x1, y1), r))
      case _ => println("El parser falla")
    }
  } catch {
    case e: Exception => println("El parser falla")
  }
}

//PictureParser()("triangulo[0 @ 100, 200 @ 300, 150 @ 500]")
//PictureParser()("rectangulo[0 @ 100, 200 @ 300]")
PictureParser()("circulo[100 @ 100, 50]")