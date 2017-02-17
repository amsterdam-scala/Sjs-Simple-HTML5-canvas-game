package nl.amsscala

import scala.collection.parallel.immutable.ParSeq

/**
  * This package object provides generic class and operators for 2D `Position`s, as well as dealing with 2D areas.
  *
  * The package includes externally this package object the main traits `Page`, `Game` and class `GameState`
  * as well for the auxiliary trait `CanvasComponent` (overarching for `Hero`, `Monster` and `Playground`).
  */
package object simplegame {

  /**
    * Generic base class Position, holding the two ordinates
    *
    * @param x The abscissa
    * @param y The ordinate
    * @tparam P Numeric type
    */
  case class Position[P: Numeric](x: P, y: P) {

    import Numeric.Implicits.infixNumericOps
    import Ordering.Implicits.infixOrderingOps

    /** Binary add operator for two coordinates */
    def +(p: Position[P]) = Position(x + p.x, y + p.y)

    /** Unary add operator e,g. (a, b) + n => (a + n, b + n) */
    def +(term: P) = Position(x + term, y + term)

    /** Binary subtract operator for the difference of two coordinates */
    def -(p: Position[P]) = Position(x - p.x, y - p.y)

    /** Unary subtract operator e,g. (a, b) - n => (a - n, b - n) */
    def -(term: P) = Position(x - term, y - term)

    /** Binary multiply operator for two coordinates, multplies each of the ordinate */
    def *(p: Position[P]) = Position(x * p.x, y * p.y)

    /** Unary multiply operator e,g. (a, b) * n=> (a * n, b * n) */
    def *(factor: P) = Position(x * factor, y * factor)

    /** Binary divide operator e,g. (a, b) * n=> (a / n, b / n) */
    def /(divisor: Position[P]): Position[P] =
      Position(x.toDouble() / divisor.x.toDouble(), y.toDouble() / divisor.y.toDouble()).asInstanceOf[Position[P]]

    /** Unary divide operator e,g. (a, b) / n=> (a / n, b / n) */
    def /(divisor: P): Position[P] = this / Position(divisor, divisor)

    /**
      * Check if the square area is within the rectangle area
      *
      * @param canvasPos Position of the second square
      * @param side      side of both two squares
      * @return False  if a square out of bound
      */
    def isValidPositionEl(canvasPos: Position[P], side: P): Boolean = {
      interSectsArea(Position(0, 0).asInstanceOf[Position[P]], canvasPos, this + side, this)
    }

    private def interSectsArea(p0: Position[P], p1: Position[P], p2: Position[P], p3: Position[P]) = {
      @inline def intersectsWith(a0: P, b0: P, a1: P, b1: P) = a0 <= b1 && a1 <= b0

      // Process the x and y axes
      intersectsWith(p0.x, p1.x, p2.x, p3.x) && intersectsWith(p0.y, p1.y, p2.y, p3.y)
    }

    /**
      * Checks if two squares intersects
      *
      * @param posB Position of the second square
      * @param side side of both two squares
      * @return True if a intersection occurs
      */
    def areTouching(posB: Position[P], side: P): Boolean = interSectsArea(this, this + side, posB, posB + side)

    /**
      * Return the Moore neighborhood with the domain range of r
      *
      * It produces a list of size sqr(2r + 1)
      *
      * @see [[http://en.wikipedia.org/wiki/Moore_neighborhood Moore neighborhood]]
      * @param r Domain range
      * @return A list of surrounding coordinates
      */
    def mooreNeighborHood(r: Int): ParSeq[Position[P]] = {
      val range = (-r to r).par
      (for (x <- range; y <- range; if (x | y) != 0 || r == 0) yield Position(x, y)).map(_.asInstanceOf[Position[P]] + this)
    }
  }


}
