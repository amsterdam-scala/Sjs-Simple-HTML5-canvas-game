package nl.amsscala
package simplegame

import org.scalajs.dom

import scala.concurrent.{Future, Promise}
import scalatags.JsDom.all._

/** Everything related to Html5 visuals */
trait Page {
  // Create the canvas and 2D context
  val canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
  val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  def center(cnvs: dom.html.Canvas) = Position(cnvs.width / 2, cnvs.height / 2)

  /**
   * Draw everything
   *
   * @param gs Game state to make graphical
   * @return The same gs
   */
  def render[T](gs: GameState[T]) = {
    // Draw each page element in the specific list order
    gs.pageElements.foreach(pe => {
      def drawImage(resize: Position[Int]) =
        ctx.drawImage(pe.img, pe.pos.x.asInstanceOf[Int], pe.pos.y.asInstanceOf[Int], resize.x, resize.y)

      drawImage(pe match {
        case _: Playground[T] => canvasDim
        case pm: GameElement[T] => dimension(pm.img) // The otherwise or default clause
      })
    })

    ctx.fillStyle = "rgb(250, 250, 250)"
    // Score
    if (!gs.monstersHitTxt.isEmpty) {
      ctx.font = "24px Helvetica"
      ctx.textAlign = "left"
      ctx.textBaseline = "top"
      ctx.fillText(gs.monstersHitTxt, 32, 32)
    }

    if (gs.isNewGame) {
      val centr = center(canvas)

      ctx.textAlign = "center"
      ctx.font = "48px Helvetica"

      ctx.fillText(
        if (gs.isGameOver) gs.gameOverTxt
        else {
          val txt = gs.explainTxt.split('\n')
          ctx.fillText(txt.last, centr.x, centr.y + 32)
          txt.head
        }, centr.x, centr.y - 48
      )
    }
    gs
  }

  def canvasDim = Position(canvas.width, canvas.height)

  @inline private def dimension(img: dom.raw.HTMLImageElement) = Position(img.width, img.height)

  canvas.textContent = "Your browser doesn't support the HTML5 CANVAS tag."
  updateCanvasWH(canvas, Position(dom.window.innerWidth, dom.window.innerHeight - 25))

  /** Convert the onload event of an img tag into a Future */
  def imageFuture(src: String): Future[dom.raw.HTMLImageElement] = {
    val img = dom.document.createElement("img").asInstanceOf[dom.raw.HTMLImageElement]

    img.setAttribute("crossOrigin", "Anonymous")
    img.src = src
    if (img.complete) Future.successful(img)
    else {
      val p = Promise[dom.raw.HTMLImageElement]()
      img.onload = { (e: dom.Event) => p.success(img) }
      p.future
    }
  }

  private def genericDetect(x: Any) = x match {
    case _: Long => "Long"
    case _: Int => "Int"
    case _: Double => "Double"
    case _ => "unknown"
  }

  @inline
  def updateCanvasWH[P :Numeric](cnvs :dom.html.Canvas, pos: Position[P]): Unit = {
    cnvs.width = pos.asInstanceOf[Position[Int]].x
    cnvs.height = pos.asInstanceOf[Position[Int]].y
  }

  dom.document.body.appendChild(div(cls := "content", style := "text-align:center; background-color:#3F8630;",
    canvas,
    a(href := "http://www.lostdecadegames.com/how-to-make-a-simple-html5-canvas-game/", "Simple HTML5 Canvas game"),
    " ported to ",
    a(href := "http://www.scala-js.org/",
      title := s"This object code is compiled with type parameter ${genericDetect(0D.asInstanceOf[SimpleCanvasGame.Generic])}.",
      "Scala.js")).render)
}
