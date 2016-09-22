package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalatest.AsyncFlatSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFlatSpec with Page {
  val initialLUnder = Position(512, 480).asInstanceOf[Position[SimpleCanvasGame.Generic]]
  lazy val gameState0 = GameState[SimpleCanvasGame.Generic](canvas,
    initialLUnder + Position(1, 1).asInstanceOf[Position[SimpleCanvasGame.Generic]])
  // Collect all Futures of onload events
  lazy val loaders = gameState0.pageElements.map(pg => imageFuture(pg.src))

  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  // Don't be engaged with browsers defaults
  updateCanvasWH(canvas, initialLUnder)

  // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
  it should "be loaded remote" in {
    Future.sequence(loaders).map { imageElements => {
      def context2DToSeq(ctx: dom.CanvasRenderingContext2D): mutable.Seq[Int] =
        ctx.getImageData(0, 0, canvas.width, canvas.height).data
      def expectedHashCode = Map("background.png" -> 1425165765, "monster.png" -> -277415456, "hero.png" -> -731024817)
      def getImgName(url: String) = url.split('/').last

      //        println(imageElements.map { img => {
      //          canvas.width = img.width
      //          canvas.height = img.height
      //          ctx.drawImage(img, 0, 0, img.width, img.height)
      //          (getImgName(img.src), context2DToSeq(ctx).hashCode())
      //        }
      //        })
      info("All images correct loaded")
      assert(imageElements.forall { img => {
        updateCanvasWH(canvas, Position(img.width, img.height))
        ctx.drawImage(img, 0, 0, img.width, img.height)
        expectedHashCode(getImgName(img.src)) == context2DToSeq(ctx).hashCode()
      }
      })

      val loadedAndNoText0 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "",
        isNewGame = false)

      updateCanvasWH(canvas, Position(512 * 2, 480 * 2).asInstanceOf[Position[SimpleCanvasGame.Generic]])
      render(loadedAndNoText0)
      info("Default initial screen, no text")
      val ref = context2DToSeq(ctx).hashCode()

      val loadedAndSomeText1 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "Now with text which can differ between browsers",
        isNewGame = false)

      render(loadedAndSomeText1)
      info("Test with score text")
      assert(ref != context2DToSeq(ctx).hashCode())

      val loadedAndSomeText2 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "",
        isNewGame = true)

      render(loadedAndSomeText2)
      info("Explain text put in")
      assert(ref != context2DToSeq(ctx).hashCode())

      println("loadedAndNoText0",loadedAndNoText0)
      println("loadedAndSomeText1",loadedAndSomeText1)
      println("loadedAndSomeText2",loadedAndSomeText2)

      render(loadedAndNoText0)
      info("All reset?")
      assert(ref == context2DToSeq(ctx).hashCode())

      //

    }
    }
  }


}
