package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalatest.AsyncFlatSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFlatSpec with Page {
  val gameState = GameState[SimpleCanvasGame.Generic](canvas)
  // Collect all Futures of onload events
  val loaders = gameState.pageElements.map(pg => imageFuture(pg.src))

  //def dimension(img: dom.raw.HTMLImageElement) = Position(img.width, img.height)

  // def dimension(cvs: dom.html.Canvas) = Position(cvs.width, cvs.height)

implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
 // implicit def executionContext = scala.concurrent.ExecutionContext.Implicits.global


    def expectedHashCode = Map("background.png" -> 1425165765, "monster.png" -> -277415456, "hero.png" -> -731024817)
    // def expectedHashCode = Map("background.png" -> 745767977, "monster.png" -> -157307518, "hero.png" -> -1469347267)
    def getImgName(url: String) = url.split('/').last

  // behavior of "Page converts several states of the game in visuals"
   "The images" should "loaded from the remote" in {
    // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
    Future.sequence(loaders).map { imageElements => {
      /*"Here is some code. without any error." But exhibit the same error "SECURITY_ERR: DOM Exception 18" in Travis.
      http://stackoverflow.com/questions/10673122/how-to-save-canvas-as-an-image-with-canvas-todataurl*/
      // def image(canvas: dom.html.Canvas) = canvas.toDataURL("image/png").replace("image/png", "image/octet-stream")

      // Exhibit the error "SECURITY_ERR: DOM Exception 18" in Travis-CI
      def image0(ctx: dom.CanvasRenderingContext2D): mutable.Seq[Int] =
      ctx.getImageData(0, 0, canvas.width, canvas.height).data

      println(imageElements.map{ img => {
        canvas.width = img.width
        canvas.height = img.height
        ctx.drawImage(img, 0, 0, img.width, img.height)
        (getImgName(img.src), image0(ctx).hashCode())
      }})

      assert(imageElements.forall { img => {
       // img.setAttribute("crossOrigin", "anonymous")

        canvas.width = img.width
        canvas.height = img.height
        ctx.drawImage(img, 0, 0, img.width, img.height)
        expectedHashCode(getImgName(img.src)) == image0(ctx).hashCode()
      }
      })
      } // Future(assert(true))
  }}

  /*

      Future.sequence(loaders).onSuccess {
        case load =>
            // Create GameState with loaded images
          var originGS =
            new GameState(page.canvas, gameState.pageElements.zip(load).map { case (el, imag) => el.copy(img = imag) })
          val fixedMonster = originGS.monster.asInstanceOf[Monster[Int]].copy(Position(0, 0))
          val updatedGS = originGS.copy(fixedMonster)
          page.render(updatedGS)

          val imageData/*: scala.collection.mutable.Seq[Int]*/ =
            page.ctx.getImageData(0, 0, page.canvas.width, page.canvas.height).data

          println(imageData.hashCode())
  */


  //GameState(Hero(621, 337), Monster(0, 0), 0, false)

  /*  describe("A Hero") {
describe("should tested within the limits") {
 it("good path") {page.render()


    {
      page.render(GameState(Hero(621, 337), Monster(0, 0), 0, false))
      val imageData: scala.collection.mutable.Seq[Int] =
        page.ctx.getImageData(0, 0, page.canvas.width, page.canvas.height).data

      imageData.hashCode() shouldBe -1753260013
    }

    {
      page.render(GameState(Hero(365, 81), Monster(0, 0), 0, false))

      dom.document.body.appendChild(div(
        cls := "content", style := "text-align:center; background-color:#3F8630;",
        canvas
      ).render)

      val imageData = page.ctx.getImageData(0, 0, page.canvas.width, page.canvas.height)

      imageData.data.sum shouldBe -1753260013
    }

    {
      page.render(GameState(Hero(877, 593), Monster(0, 0), 0, false))
      val imageData: scala.collection.mutable.Seq[Int] =
        page.ctx.getImageData(0, 0, page.canvas.width, page.canvas.height).data

      imageData.hashCode() shouldBe -1753260013
    }
*/


}
