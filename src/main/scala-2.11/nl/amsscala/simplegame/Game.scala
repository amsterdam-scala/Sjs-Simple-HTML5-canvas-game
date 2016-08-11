package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode.{Down, Left, Right, Up}
import org.scalajs.dom.html

import scala.collection.mutable
import scala.scalajs.js

trait Game {

  def play(canvas: html.Canvas, headless: Boolean) {
    // Keyboard events store
    val keysPressed = mutable.Map.empty[Int, (Double, GameState)]
    var prev = js.Date.now()
    var oldUpdated: Option[GameState] = None

    // The main game loop
    def gameLoop = () => {
      val now = js.Date.now()
      val delta = now - prev
      val updated = oldUpdated.getOrElse(new GameState(canvas, -1)).updater(delta / 1000, keysPressed, canvas)

      if (oldUpdated.isEmpty || (oldUpdated.get.hero.pos != updated.hero.pos)) {
        oldUpdated = SimpleCanvasGame.render(updated)
      }

      prev = now
    }

    // Let's play this game!
    if (!headless) {
      dom.window.setInterval(gameLoop, 20)

      dom.window.addEventListener("keydown", (e: dom.KeyboardEvent) =>
        e.keyCode match {
          case Left | Right | Up | Down if oldUpdated.isDefined => keysPressed += e.keyCode -> (js.Date.now(), oldUpdated.get)
          case _ =>
        }, useCapture = false)

      dom.window.addEventListener("keyup", (e: dom.KeyboardEvent) => {
        keysPressed -= e.keyCode
      }, useCapture = false)
    }
  }
}

case class GameState(hero: Hero[Int], monster: Monster[Int], monstersCaught: Int = 0) {
  /** Update game objects
    *
    * @param modifier
    * @param keysDown
    * @param canvas
    * @return
    */
  def updater(modifier: Double, keysDown: mutable.Map[Int, (Double, GameState)], canvas: dom.html.Canvas): GameState = {
    def modif = (Hero.speed * modifier).toInt
    def directions = Map(Left -> Position(-1, 0), Right -> Position(1, 0), Up -> Position(0, -1), Down -> Position(0, 1))

    val newHero = new Hero(keysDown.map(k => directions(k._1)). // Convert pressed keyboard keys to coordinates
      fold(hero.pos) { (z, i) => z + i * modif }) // Compute new position by adding and multiplying.
    if (newHero.isValidPosition(canvas))
    // Are they touching?
      if (newHero.pos.areTouching(monster.pos, Hero.size)) // Reset the game when the player catches a monster
        new GameState(canvas, monstersCaught)
      else copy(hero = newHero)
    else this
  }

  /** Auxiliary constructor
    *
    * @param canvas
    * @param oldScore
    * @return
    */
  def this(canvas: dom.html.Canvas, oldScore: Int) =
  this(new Hero(Position(canvas.width / 2, canvas.height / 2)),
    // Throw the monster somewhere on the screen randomly
    new Monster(Position(
      Hero.size + (math.random * (canvas.width - 64)).toInt,
      Hero.size + (math.random * (canvas.height - 64)).toInt)),
    oldScore + 1)
}

class Monster[T: Numeric](val pos: Position[T]) {
  def this(x: T, y: T) = this(Position(x, y))
}

class Hero[A: Numeric](val pos: Position[A]) {
  def this(x: A, y: A) = this(Position(x, y))

  def isValidPosition(canvas: dom.html.Canvas): Boolean = pos.isInTheCanvas(canvas, Hero.size.asInstanceOf[A])
}

object Hero {
  val size = 32
  val speed = 256
}
