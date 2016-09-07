package nl.amsscala
package simplegame

import nl.amsscala.simplegame.SimpleCanvasGame.dimension
import org.scalajs.dom

import scala.collection.mutable

/**
 *
 * @param canvas
 * @param pageElements This member lists the page elements. They are always in this order: PlayGround, Monster and Hero.
 *                     E.g. pageElements.head is PlayGround, pageElements(1) is the Monster, pageElements.takes(2) are those both.
 * @param monstersCaught
 * @param isNewGame Flags game play is just fresh started
 * @param isGameOver Flags a new turn
 * @tparam T  Numeric generic abstraction
 */
class GameState[T: Numeric](canvas: dom.html.Canvas,
                            val pageElements: Vector[GameElement[T]],
                            val monstersCaught: Int = 0,
                            val isNewGame: Boolean = true,
                            val isGameOver: Boolean = false
                           ) {
  def playGround = pageElements.head
  def monster = pageElements(1)
  def hero = pageElements.last

  require(playGround.isInstanceOf[PlayGround[T]] &&
    monster.isInstanceOf[Monster[T]] &&
    hero.isInstanceOf[Hero[T]], "Page elements are not well listed.")

  /**
   * Process on a regular basis the arrow keys pressed.
   * @param latency
   * @param keysDown
   * @return a Hero object with an adjusted position.
   */
  def keyEffect(latency: Double, keysDown: mutable.Set[Int]): GameState[T] = {
    if (keysDown.isEmpty) this
    else {
      // Get new position according the pressed arrow keys
      val newHero = hero.asInstanceOf[Hero[T]].keyEffect(latency, keysDown)
      // Are they touching?
      val size = Hero.pxSize.asInstanceOf[T]
      if (newHero.pos.isValidPosition(dimension(canvas).asInstanceOf[Position[T]], size)) {
        if (newHero.pos.areTouching(monster.pos, size)) copy() // Reset the game when the player catches a monster
        else copy(hero = newHero) // New position for Hero with isNewGame reset to false
      }
      else this
    }
  }

  def copy() = {
    val (h, m) = (hero.asInstanceOf[Hero[T]], monster.asInstanceOf[Monster[T]])

    new GameState(canvas, Vector(playGround, m.copy(canvas), h.copy(canvas)), monstersCaught + 1, true, true)
  }

  def copy(hero: Hero[T]) =
    new GameState(canvas, pageElements.take(2) :+ hero, monstersCaught = monstersCaught, isNewGame = false)
}

object GameState {
  def apply[T: Numeric](canvas: dom.html.Canvas) = {
    new GameState[T](canvas, Vector(PlayGround[T](), Monster[T](canvas), Hero[T](canvas)), 0)
  }
}
