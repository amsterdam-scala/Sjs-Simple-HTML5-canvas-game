package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode.{Down, Left, Right, Up}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

/** The game with its rules. */
protected trait Game {
  private[this] val framesPerSec = 25

  /**
   * Initialize Game loop
   *
   * @param canvas   The visual html element
   * @param headless An option to run for testing
   */
  protected def play(canvas: dom.html.Canvas, headless: Boolean) {
    // Keyboard events store
    val (keysPressed,  gameState) = (mutable.Set.empty[Int], GameState[SimpleCanvasGame.Generic](canvas))
    var prevTimestamp = js.Date.now()

    // Collect all Futures of onload events
    val loaders = gameState.pageElements.map(pg => SimpleCanvasGame.imageFuture("img/", pg.src))

    Future.sequence(loaders).onSuccess {
      case load => // Create GameState with loaded images
        var prevGS =
          new GameState(canvas, gameState.pageElements.zip(load).map { case (el, img) => el.copy(img = img) })

        /** The main game loop, invoked by  */
        def gameLoop = () => {
          val nowTimestamp = js.Date.now()
          val updatedGS = prevGS.keyEffect((nowTimestamp - prevTimestamp) / 1000, keysPressed)
          prevTimestamp = nowTimestamp

          // Render of the canvas is conditional by movement of Hero
          if (prevGS.hero != updatedGS.hero.pos) prevGS = SimpleCanvasGame.render(updatedGS)
        }

        SimpleCanvasGame.render(prevGS) // First draw

        // Let's play this game!
        if (!headless) {// For test purpose, a facility to silence the listeners.
          dom.window.setInterval(gameLoop, 1000 / framesPerSec)
          // TODO: mobile application navigation

          dom.window.addEventListener("keydown", (e: dom.KeyboardEvent) =>
            e.keyCode match {
              case Left | Right | Up | Down => keysPressed += e.keyCode
              case _ =>
            }, useCapture = false)

          dom.window.addEventListener("keyup", (e: dom.KeyboardEvent) => {
            keysPressed -= e.keyCode
          }, useCapture = false)
        }
        // Listeners are now obsoleted , so they unload them all.
        load.foreach(i => i.onload = null)
    }
  }
}
