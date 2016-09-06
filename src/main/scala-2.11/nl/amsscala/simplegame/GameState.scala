package nl.amsscala
package simplegame

import nl.amsscala.simplegame.SimpleCanvasGame.dimension
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode.{Down, Left, Right, Up}

import scala.collection.mutable

class GameState[T: Numeric](canvas: dom.html.Canvas,
                            val pageElements: Vector[GameElement[T]],
                            val monstersCaught: Int = 0,
                            val isNewGame: Boolean = true,
                            val isGameOver: Boolean = false
                           ) {

  def keyEffect(latency: Double, keysDown: mutable.Set[Int]): GameState[T] = {

    def dirLookUp = Map(// Key to direction translation
      Left -> Position(-1, 0), Right -> Position(1, 0), Up -> Position(0, -1), Down -> Position(0, 1)
    ).withDefaultValue(Position(0, 0))

    // Convert pressed keyboard keys to coordinates
    def displacements: mutable.Set[Position[T]] = keysDown.map { k => dirLookUp(k).asInstanceOf[Position[T]] }

    if (keysDown.isEmpty) this
    else {
      val hero = pageElements.last.asInstanceOf[Hero[T]]
      val newHero =
        hero.copy(displacements.fold(hero.pos) { (z, vec) => z + vec * (Hero.speed * latency).toInt.asInstanceOf[T] })
      val size = Hero.size.asInstanceOf[T]
      // Are they touching?
      if (newHero.pos.isValidPosition(dimension(canvas).asInstanceOf[Position[T]], size)) {
        def monster = pageElements(1)
        if (newHero.pos.areTouching(monster.pos, size)) copy() // Reset the game when the player catches a monster
        else copy(hero = newHero) // New position for Hero with isNewGame reset to false
      }
      else this
    }
  }

  def copy() = {
    val (hero, monster) = (pageElements.last.asInstanceOf[Hero[T]], pageElements(1).asInstanceOf[Monster[T]])

    new GameState(canvas, Vector(pageElements.head, monster.copy(canvas), hero.copy(canvas)), monstersCaught + 1, true, true)
  }

  def copy(hero: Hero[T]) =
    new GameState(canvas, pageElements.take(2) :+ hero, monstersCaught = monstersCaught, isNewGame = false)
}

object GameState {
  def apply[T: Numeric](canvas: dom.html.Canvas) = {
    new GameState[T](canvas, Vector(PlayGround[T](), Monster[T](canvas), Hero[T](canvas)), 0)
  }
}
