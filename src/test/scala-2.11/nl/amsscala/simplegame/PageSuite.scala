package nl.amsscala
package simplegame

import org.scalatest.AsyncFlatSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFlatSpec with Page {
  implicit override def executionContext = scala.concurrent.ExecutionContext.Implicits.global

  behavior of "Page converts several states of the game in visuals"
  "The images" should "loaded from the remote" in {

    val gameState = GameState[SimpleCanvasGame.Generic](canvas)
    // Collect all Futures of onload events
    val loaders = gameState.pageElements.map(pg =>
      // imageFuture("""http://lambdalloyd.net23.net/SimpleGame/views/img/""" + pg.src)
       imageFuture("""img/""" + pg.src)
    )

    def expectedHashCode = Map("background.png" -> 1425165765, "monster.png" -> -277415456, "hero.png" -> -731024817)
    def getImgName(url: String) = url.split('/').last

    // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
    Future.sequence(loaders) map { imageElements => {
        assert(imageElements.forall { img => {
        canvas.width = img.width
        canvas.height = img.height
        ctx.drawImage(img, 0, 0, img.width, img.height)
        def imageData: scalajs.js.Array[Int] = ctx.getImageData(0, 0, canvas.width, canvas.height).data
          expectedHashCode(getImgName(img.src)) == imageData.hashCode()
          true
        }
      })
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
