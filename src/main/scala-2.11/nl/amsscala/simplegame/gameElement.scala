package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalajs.dom.html

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
 * Monster class, holder for its coordinate, copied as extentension to the Hero class
 *
 * @param pos Monsters' position
 * @tparam M Numeric generic abstraction
 */
class Monster[M](val pos: Position[M], val img: dom.raw.HTMLImageElement) extends GameElement[M] {

  def copy[M : Numeric](canvas : html.Canvas) = new Monster(Monster.random[M](canvas), img)

  def copy(img: dom.raw.HTMLImageElement) = new Monster(pos, img)

  def src = "img/monster.png"

  protected[simplegame] def isValidPosition(canvas: dom.html.Canvas) =
    pos.isValidPosition(Position(canvas.width, canvas.height).asInstanceOf[Position[M]], Hero.size.asInstanceOf[M])
}

object Monster {
  def random[M: Numeric](canvas: html.Canvas) = {
    @inline def compute(dim: Int) = (math.random * (dim - Hero.size)).toInt
    Position(compute(canvas.width), compute(canvas.height)).asInstanceOf[Position[M]]
  }

  def apply[M: Numeric](canvas: html.Canvas) = new Monster(random(canvas), null)
}

class Hero[H: Numeric](val pos: Position[H],
                       val img: dom.raw.HTMLImageElement
                      ) extends GameElement[H] {

  def copy(img: dom.raw.HTMLImageElement) = new Hero(pos, img)

  def copy(pos: Position[H]) = new Hero(pos, img)

  def copy(canvas: html.Canvas) = new Hero(Page.centerPosCanvas(canvas), img)

  def src = "img/hero.png"
}

/** Compagnion object of class Hero */
object Hero {
  val (size,speed )= (32, 256)

  /** Hero image centered in the field */
  def apply[H: Numeric](canvas: html.Canvas): Hero[H] = new Hero[H](Page.centerPosCanvas[H](canvas), null)
}
