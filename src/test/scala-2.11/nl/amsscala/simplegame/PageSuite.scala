package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalatest.AsyncFunSpec

import scala.concurrent.Future

class PageSuite extends AsyncFunSpec with Page {
  var loadedAndNoText: GameState[SimpleCanvasGame.Generic] = _


  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  describe("The images") {
    val gameState = GameState[SimpleCanvasGame.Generic](canvas,
      Position(0, 0).asInstanceOf[Position[SimpleCanvasGame.Generic]])
    // Collect all Futures of onload events
    val loaders = gameState.pageElements.map(pg => imageFuture(pg.src))

    // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
    describe("should load pictures remote") {
      Future.sequence(loaders).map { imageElements => {
        def context2DToSeq(ctx: dom.CanvasRenderingContext2D) = ctx.getImageData(0, 0, canvas.width, canvas.height).data
        def getImgName(url: String) = url.split('/').last

        canvas.width = 2000
        canvas.height = 1000

        loadedAndNoText = new GameState(canvas,
          gameState.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
          monstersHitTxt = "", isNewGame = false)

        println(loadedAndNoText.pageElements)
        it("find all images as expected")(assert(imageElements.forall { img => {
          def expectedHashCode = Map("background.png" -> 1425165765, "monster.png" -> -277415456, "hero.png" -> -731024817)

          canvas.width = img.width
          canvas.height = img.height
          ctx.drawImage(img, 0, 0, img.width, img.height)
          expectedHashCode(getImgName(img.src)) == context2DToSeq(ctx).hashCode()
        }
        }))
      }
      }
    }
    describe("println the normalized playGround") {
      /*   canvas.width = 0
         canvas.height = 0*/
      // println(loadedAndNoText)
    }

  }



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
