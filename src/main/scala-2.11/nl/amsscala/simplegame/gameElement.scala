package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode.{Down, Left, Right, Up}

import scala.collection.mutable

// TODO: http://stackoverflow.com/questions/12370244/case-class-copy-method-with-superclass

sealed trait GameElement[Numeric] {
  val pos: Position[Numeric]
  val img: dom.raw.HTMLImageElement

  def copy(img: dom.raw.HTMLImageElement): GameElement[Numeric]

  def src: String

  override def toString = s"${this.getClass.getSimpleName} $pos"

  override def equals(that: Any): Boolean = that match {
    case that: GameElement[Numeric] => this.pos == that.pos
    case _ => false
  }
}

class PlayGround[G](
                     val pos: Position[G],
                     val img: dom.raw.HTMLImageElement
                   ) extends GameElement[G] {

  def copy(img: dom.raw.HTMLImageElement): PlayGround[G] = new PlayGround(pos, img)

  def src = "img/background.png"
}

object PlayGround {
  def apply[G]() = new PlayGround[G](Position(0, 0).asInstanceOf[Position[G]], null)
}

/**
 * Monster class, holder for its coordinate
 *
 * @param pos Monsters' position
 * @tparam M Numeric generic abstraction
 */
class Monster[M](val pos: Position[M], val img: dom.raw.HTMLImageElement) extends GameElement[M] {
  /** Get a Monster at a (new) random position */
  def copy[C: Numeric](canvas: dom.html.Canvas) = new Monster(Monster.randomPosition[C](canvas), img)
  /** Load the img in the Element */
  def copy(img: dom.raw.HTMLImageElement) = new Monster(pos, img)

  def src = "img/monster.png"

}

object Monster {
  def apply[M: Numeric](canvas: dom.html.Canvas) = new Monster(randomPosition(canvas), null)

  private def randomPosition[M: Numeric](canvas: dom.html.Canvas): Position[M] = {
    @inline def compute(dim: Int) = (math.random * (dim - Hero.size)).toInt
    Position(compute(canvas.width), compute(canvas.height)).asInstanceOf[Position[M]]
  }
}

class Hero[H: Numeric](val pos: Position[H], val img: dom.raw.HTMLImageElement) extends GameElement[H] {

  def copy(img: dom.raw.HTMLImageElement) = new Hero(pos, img)

  def copy(canvas: dom.html.Canvas) = new Hero(SimpleCanvasGame.centerPosCanvas(canvas), img)

  def src = "img/hero.png"

  def keyEffect(latency: Double, keysDown: mutable.Set[Int]) = {

    // Convert pressed keyboard keys to coordinates
    def displacements: mutable.Set[Position[H]] = {
      def dirLookUp = Map(// Key to direction translation
        Left -> Position(-1, 0), Right -> Position(1, 0), Up -> Position(0, -1), Down -> Position(0, 1)
      ).withDefaultValue(Position(0, 0))

      keysDown.map { k => dirLookUp(k).asInstanceOf[Position[H]] }
    }

    def dirLookUp = Map(// Key to direction translation
      Left -> Position(-1, 0), Right -> Position(1, 0), Up -> Position(0, -1), Down -> Position(0, 1)
    ).withDefaultValue(Position(0, 0))

    // Compute next position by summing all vectors with the position where the hero is found.
    copy(displacements.fold(pos) { (z, vec) => z + vec * (Hero.speed * latency).toInt.asInstanceOf[H] })
  }

  def copy(pos: Position[H]) = new Hero(pos, img)

  protected[simplegame] def isValidPosition(canvas: dom.html.Canvas) =
    pos.isValidPosition(Position(canvas.width, canvas.height).asInstanceOf[Position[H]], Hero.size.asInstanceOf[H])
}

/** Compagnion object of class Hero */
object Hero {
  protected[simplegame] val (size, speed) = (32, 256)

  /** Hero image centered in the field */
  def apply[H: Numeric](canvas: dom.html.Canvas): Hero[H] = new Hero[H](SimpleCanvasGame.centerPosCanvas[H](canvas), null)
}
