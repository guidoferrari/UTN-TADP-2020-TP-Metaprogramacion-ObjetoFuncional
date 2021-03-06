package tadp

import scalafx.scene.paint.Color
import tadp.PictureParser._
import tadp.internal._
import tadp.parser.Parser._

import scala.util.Try

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

  trait transformacion extends imprimible {
    def equals(forma: imprimible): Boolean
    def copyConGrupo(grupoEmbebido: imprimible): imprimible
  }

  case class color(r: Int, g: Int, b: Int, forma: imprimible) extends transformacion {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.beginColor(Color.rgb(r, g, b))
      forma.print(adapter)
    }

    def equals(forma: imprimible): Boolean = forma match {
      case color(r, g, b, _) => this.r == r && this.g == g && this.b == b
      case _ => false
    }

    def copyConGrupo(grupoEmbebido: imprimible): color = color(r, g, b, grupoEmbebido)
  }

  case class escala(h: Double, v: Double, forma: imprimible) extends transformacion {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.beginScale(h, v)
      forma.print(adapter)
    }

    def equals(forma: imprimible): Boolean = forma match {
      case escala(h, v, _) => this.h == h && this.v == v
      case _ => false
    }

    def copyConGrupo(grupoEmbebido: imprimible): escala = escala(h, v, grupoEmbebido)
  }

  case class rotacion(grados: Double, forma: imprimible) extends transformacion {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.beginRotate(grados)
      forma.print(adapter)
    }

    def equals(forma: imprimible): Boolean = forma match {
      case rotacion(grados, _) => this.grados == grados
      case _ => false
    }

    def copyConGrupo(grupoEmbebido: imprimible): rotacion = rotacion(grados, grupoEmbebido)
  }

  case class traslacion(x: Double, y: Double, forma: imprimible) extends transformacion {
    def print(adapter: TADPDrawingAdapter): Unit = {
      adapter.beginTranslate(x, y)
      forma.print(adapter)
    }

    def equals(forma: imprimible): Boolean = forma match {
      case traslacion(x, y, _) => this.x == x && this.y == y
      case _ => false
    }

    def copyConGrupo(grupoEmbebido: imprimible): traslacion = traslacion(x, y, grupoEmbebido)
  }

  case class parserGrafico() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = {
      (trianguloParser() <|> rectanguloParser() <|> circuloParser() <|> grupoParser()
        <|> colorParser() <|> escalaParser() <|> rotacionParser() <|> traslacionParser()) (input)
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

  case class grupoParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("grupo(") (input)
      (formaGeometrica, resto) <- (parserGrafico() <~ string(", ")).+ (resto)
      (formaSinComa, resto) <- parserGrafico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (simplificarGrupo(grupo(formaGeometrica.appended(formaSinComa))), resto)

    private def simplificarGrupo(grupoASimplificar: grupo): imprimible = {
      grupoASimplificar.formas.last match {
        case t: transformacion if grupoASimplificar.formas.forall(f => t.equals(f)) =>
          t.copyConGrupo(grupo(obtenerFormasDeTransformacion(grupoASimplificar.formas)))
        case _ => grupoASimplificar
      }
    }

    private def obtenerFormasDeTransformacion(lista: List[imprimible]): List[imprimible] = lista.map {
        case color(_, _, _, forma) => forma
        case rotacion(_, forma) => forma
        case escala(_, _, forma) => forma
        case traslacion(_, _, forma) => forma
        case otraForma => otraForma
      }
  }

  case class colorParser() extends Parser[color] {
    override def apply(input: String): Try[ParserResult[color]] = for {
      (_, resto) <- string("color[") (input)
      (((r, g), b), resto) <- (((integer() <~ string(", ")) <> (integer() <~ string(", ")) <> integer()) <~ string("](")) (resto)
      (formaGeometrica, resto) <- parserGrafico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (simplificarColor(color(r, g, b, formaGeometrica)), resto)

    private def simplificarColor(colorASimplificar: color): color = colorASimplificar.forma match {
        case color(r, g, b, subForma) => color(r, g, b, subForma)
        case _ => colorASimplificar
      }
  }

  case class escalaParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("escala[") (input)
      ((h, v), resto) <- variablesEscalaTraslacion(resto)
      (formaGeometrica, resto) <- parserGrafico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (simplificarEscala(escala(h, v, formaGeometrica)), resto)

    private def simplificarEscala(escalaASimplificar: escala): imprimible = escalaASimplificar.forma match {
        case escala(h, v, subForma) => escala(h * escalaASimplificar.h, v * escalaASimplificar.v, subForma)
        case _ => escalaASimplificar match {
            case escala(1.0, 1.0, subforma) => subforma
            case _ => escalaASimplificar
          }
      }
  }

  case class rotacionParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("rotacion[") (input)
      (grados, resto) <- (double() <~ string("](")) (resto)
      (formaGeometrica, resto) <- parserGrafico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (simplificarRotacion(rotacion(grados, formaGeometrica)), resto)

    private def simplificarRotacion(rotacionASimplificar: rotacion): imprimible = rotacionASimplificar.forma match {
        case rotacion(g, subForma) => rotacion(g + rotacionASimplificar.grados, subForma)
        case _ =>
          rotacionASimplificar match {
            case rotacion(0.0, subforma) => subforma
            case _ => rotacionASimplificar
          }
      }
  }

  case class traslacionParser() extends Parser[imprimible] {
    override def apply(input: String): Try[ParserResult[imprimible]] = for {
      (_, resto) <- string("traslacion[") (input)
      ((x, y), resto) <- variablesEscalaTraslacion(resto)
      (formaGeometrica, resto) <- parserGrafico() (resto)
      (_, resto) <- char(')') (resto)
    } yield (simplificarTraslacion(traslacion(x, y, formaGeometrica)), resto)

    private def simplificarTraslacion(traslacionASimplificar: traslacion): imprimible =
      traslacionASimplificar.forma match {
        case traslacion(x, y, subForma) => traslacion(x + traslacionASimplificar.x, y + traslacionASimplificar.y, subForma)
        case _ => traslacionASimplificar match {
            case traslacion(0.0, 0.0, subForma) => subForma
            case _ => traslacionASimplificar
          }
      }
  }

  def variablesEscalaTraslacion(resto: String): Try[((Double, Double), String)] = (((double() <~ string(", ")) <> double()) <~ string("](")) (resto)

  case class PicturePrinter() extends (String => Unit){
    def apply(formaDescripta: String): Unit =
      TADPDrawingAdapter.forScreen( adapter =>
        parserGrafico() (formaDescripta).get match {
          case (formas, _) => formas.print(adapter)
          case _ => println("El parser falla")
        }
      )
  }
}

//Ejemplos de impresión
object app extends App {
  // grupo anidado
  //PicturePrinter()("grupo(grupo(triangulo[250 @ 150, 150 @ 300, 350 @ 300], triangulo[150 @ 300, 50 @ 450, 250 @ 450]), grupo(rectangulo[460 @ 90, 470 @ 100], rectangulo[450 @ 100, 480 @ 260]))")

  // color
  //PicturePrinter()("color[60, 150, 200](grupo(triangulo[200 @ 50, 101 @ 335, 299 @ 335], circulo[200 @ 350, 100]))")

  // escala
  //PicturePrinter()("escala[2.5, 1](rectangulo[0 @ 100, 200 @ 300])")

  // rotacion
  //PicturePrinter()("rotacion[45](rectangulo[300 @ 0, 500 @ 200])")

  // traslacion
  //PicturePrinter()("traslacion[200, 50](triangulo[0 @ 100, 200 @ 300, 150 @ 500])")
}