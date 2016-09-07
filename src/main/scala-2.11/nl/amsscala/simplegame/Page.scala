package nl.amsscala
package simplegame

import org.scalajs.dom

import scalatags.JsDom.all._

/** Everything related to Html5 visuals */
protected trait Page {
  // Create the canvas and 2D context
  private[simplegame] val canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
  canvas.setAttribute("crossOrigin", "anonymous")
  private[simplegame] val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  /**
   * Draw everything
   *
   * @param gs Game state to make graphical
   * @return None if not ready else the same GameState if drawn
   */
  protected[simplegame] def render[T](gs: GameState[T]) = {
    def gameOverTxt = "Game Over?"
    def explainTxt = "Use the arrow keys to\nattack the hidden monster."

    // Draw each page element in the specific list order
    gs.pageElements.foreach(pe => {
      val resize: Position[Int] = pe match {
        case _: PlayGround[T] => dimension(canvas)
        case pm: GameElement[T] => dimension(pm.img) // The otherwise or default clause
      }
      ctx.drawImage(pe.img, pe.pos.x.asInstanceOf[Int], pe.pos.y.asInstanceOf[Int], resize.x, resize.y)
    })

    // Score
    ctx.fillStyle = "rgb(250, 250, 250)"
    ctx.font = "24px Helvetica"
    ctx.textAlign = "left"
    ctx.textBaseline = "top"
    ctx.fillText(f"Goblins caught: ${gs.monstersCaught}%03d", 32, 32)

    if (gs.isNewGame) {
      ctx.textAlign = "center"
      ctx.font = "48px Helvetica"

      val center = centerPosCanvas[Int](canvas)
      ctx.fillText(
        if (gs.isGameOver) gameOverTxt
        else {
          val txt = explainTxt.split('\n')
          ctx.fillText(txt(1), center.x, center.y + 32)
          txt(0)
        }, center.x, center.y - 48
      )
    }
    gs
  }

  def dimension(img: dom.raw.HTMLImageElement) = Position(img.width, img.height)

  def dimension(cvs: dom.html.Canvas) = Position(cvs.width, cvs.height)

  def centerPosCanvas[H: Numeric](canvas: dom.html.Canvas) =
    Position(canvas.width / 2, canvas.height / 2).asInstanceOf[Position[H]]

  canvas.width = dom.window.innerWidth.toInt
  canvas.height = dom.window.innerHeight.toInt - 25
  println(s"Dimension of canvas set to ${canvas.width},${canvas.height}")
  canvas.textContent = "Your browser doesn't support the HTML5 CANVAS tag."

  private def genericDetect(x : Any) = x match {
    case _: Long => "Long"
    case _: Int => "Int"
    case _: Double => "Double"
    case _ => "unknown"
  }

  dom.document.body.appendChild(div(cls := "content", style := "text-align:center; background-color:#3F8630;",
    canvas,
    a(href := "http://www.lostdecadegames.com/how-to-make-a-simple-html5-canvas-game/", "Simple HTML5 Canvas game"),
    " ported to ",
    a(href := "http://www.scala-js.org/",
      title := s"This object code is compiled with type parameter ${genericDetect(0D.asInstanceOf[SimpleCanvasGame.Generic])}.",
      "Scala.js")).render)
}
