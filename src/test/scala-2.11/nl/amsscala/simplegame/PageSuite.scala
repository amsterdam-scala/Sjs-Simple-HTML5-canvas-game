package nl.amsscala
package simplegame

import org.scalatest.AsyncFlatSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFlatSpec with Page {
  lazy val gameState0 = GameState[SimpleCanvasGame.Generic](canvas, initialLUnder)
  // Collect all Futures of onload events
  lazy val loaders = gameState0.pageElements.map(pg => imageFuture(pg.src))
  val initialLUnder = Position(512, 480).asInstanceOf[Position[SimpleCanvasGame.Generic]]

  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  // Don't be engaged with browsers defaults
  updateCanvasWH(canvas, initialLUnder)

  // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
  it should "be loaded remote" in {
    Future.sequence(loaders).map { imageElements => {
      def context2Hashcode[T: Numeric](size: Position[T]) = {
        updateCanvasWH(canvas, size)
        val UintClampedArray: mutable.Seq[Int]=
          ctx.getImageData(0, 0, size.x.asInstanceOf[Int], size.y.asInstanceOf[Int]).data
        UintClampedArray.hashCode()
      }

      def expectedHashCode = Map("background.png" -> -1768009948, "monster.png" -> 1817836310, "hero.png" -> 1495155181)
      def getImgName(url: String) = url.split('/').last

      info("All images correct loaded")
      assert(imageElements.forall { img => {
        val pos = Position(img.width, img.height)
        updateCanvasWH(canvas, pos)
        ctx.drawImage(img, 0, 0, img.width, img.height)

        expectedHashCode(getImgName(img.src)) == context2Hashcode(pos)
       }
      })


      /* Composite all pictures drawn outside the play field.
       * This should result in a hashcode equal as the image of the background.
       */
      updateCanvasWH(canvas, initialLUnder)
      val loadedAndNoText0 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "",
        isNewGame = false)
      render(loadedAndNoText0)
      info("Default initial screen everything ")
      assert(context2Hashcode(initialLUnder) == expectedHashCode("background.png"))

      /**
       * Tests with double canvas size
       *
       */
      updateCanvasWH(canvas, initialLUnder + initialLUnder)
      render(loadedAndNoText0)
      info("Default initial screen, no text")
      // Register the reference value
      val ref = context2Hashcode(initialLUnder + initialLUnder)

      info(s"Reference is $ref.") // -1396366207

      val loadedAndSomeText1 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "Now with text which can differ between browsers",
        isNewGame = false)

      updateCanvasWH(canvas, initialLUnder + initialLUnder)
      render(loadedAndSomeText1)
      info("Test with score text")
      assert(ref == context2Hashcode(initialLUnder + initialLUnder)) // ????

      val loadedAndSomeText2 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "",
        isNewGame = true)

      render(loadedAndSomeText2)
      info("Explain text put in")
      assert(ref == context2Hashcode(initialLUnder + initialLUnder)) // ????


      println("loadedAndNoText0", loadedAndNoText0)
      println("loadedAndSomeText1", loadedAndSomeText1)
      println("loadedAndSomeText2", loadedAndSomeText2)

      render(loadedAndNoText0)
      info("All reset?")
      assert(ref == context2Hashcode(initialLUnder + initialLUnder))


      //

    }
    }
  }
}
