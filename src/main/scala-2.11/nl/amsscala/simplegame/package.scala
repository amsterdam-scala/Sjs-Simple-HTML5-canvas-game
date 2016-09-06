package nl.amsscala

/**
 * Provides generic class and operators for dealing with 2D positions. As well dealing with 2D areas.
 */
package object simplegame {

  /**
   * Generic base class Position, holding the two ordinates
   *
   * @param x  The abscissa
   * @param y  The ordinate
   * @tparam P Numeric type
   */
  protected[simplegame] case class Position[P: Numeric](x: P, y: P) {

    import Numeric.Implicits.infixNumericOps
    import Ordering.Implicits.infixOrderingOps

    /** Binaire add operator for two coordinates */
    def +(p: Position[P]) = Position(x + p.x, y + p.y)

    /** Binaire add operator e,g. (a, b) + n => (a + n, b + n) */
    def +(term: P) = Position(x + term, y + term)

    /** Binaire subtract operator for the difference of two coordinates */
    def -(p: Position[P]) = Position(x - p.x, y - p.y)

    /** Binaire subtract operator e,g. (a, b) - n => (a - n, b - n) */
    def -(term: P) = Position(x - term, y - term)

    /** Binaire multiply operator for two coordinates, multplies each of the ordinate */
    def *(p: Position[P]) = Position(x * p.x, y * p.y)

    /** Binaire multiply operator e,g. (a, b) * n=> (a * n, b * n) */
    def *(factor: P) = Position(x * factor, y * factor)

    private def interSectsArea[P: Numeric](p0: Position[P], p1: Position[P], p2: Position[P], p3: Position[P]) = {
      @inline def intersectsWith(a0: P, b0: P, a1: P, b1: P) = a0 <= b1 && a1 <= b0

      intersectsWith(p0.x, p1.x, p2.x, p3.x) &&
        intersectsWith(p0.y, p1.y, p2.y, p3.y)
    }

    /**
     * Check if the square area is within the rectangle area
     *
     * @param canvasPos Position of the second square
     * @param side      side of both two squares
     * @return          False  if a square out of bound
     */
    def isValidPosition(canvasPos: Position[P], side: P): Boolean =
      interSectsArea(Position(0, 0).asInstanceOf[Position[P]], canvasPos, this + side, this)

    /**
     * Checks that two squares intersects
     *
     * @param posB Position of the second square
     * @param side side of both two squares
     * @return     True if a intersection occurs
     */
    def areTouching(posB: Position[P], side: P): Boolean = interSectsArea(this, this + side, posB, posB + side)
  }

}
