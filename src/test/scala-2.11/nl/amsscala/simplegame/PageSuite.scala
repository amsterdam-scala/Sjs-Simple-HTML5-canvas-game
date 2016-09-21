package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalatest.AsyncFunSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFunSpec with Page {
  val gameState = GameState[SimpleCanvasGame.Generic](canvas)
  // Collect all Futures of onload events
  val loaders = gameState.pageElements.map(pg => imageFuture(pg.src))

  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def expectedHashCode = Map("background.png" -> 1425165765, "monster.png" -> -277415456, "hero.png" -> -731024817)

  // def expectedHashCode = Map("background.png" -> 745767977, "monster.png" -> -157307518, "hero.png" -> -1469347267)
  def getImgName(url: String) = url.split('/').last

  describe("The images should loaded from the remote") {
    // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
    describe("should load pictures remote") {
      Future.sequence(loaders).map { imageElements => {
        def context2DToSeq(ctx: dom.CanvasRenderingContext2D): mutable.Seq[Int] =
          ctx.getImageData(0, 0, canvas.width, canvas.height).data

        it("find all images as expected")(assert(imageElements.forall { img => {
          canvas.width = img.width
          canvas.height = img.height
          ctx.drawImage(img, 0, 0, img.width, img.height)
          expectedHashCode(getImgName(img.src)) == context2DToSeq(ctx).hashCode()
        }
        }))
      }
      }
    }
  }

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
