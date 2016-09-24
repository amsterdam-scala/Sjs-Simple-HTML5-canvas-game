package nl.amsscala
package simplegame

import org.scalatest.AsyncFlatSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFlatSpec with Page {
  // All graphical features are placed just outside the playground
  lazy val gameState0 =
  GameState[SimpleCanvasGame.Generic](canvas, doubleInitialLUnder, doubleInitialLUnder)
  // Collect all Futures of onload events
  lazy val loaders = gameState0.pageElements.map(pg => imageFuture(pg.src))
  val initialLUnder = Position(512, 480).asInstanceOf[Position[SimpleCanvasGame.Generic]]
  val doubleInitialLUnder = initialLUnder + initialLUnder

  implicit override def executionContext = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  // Don't rely the browsers defaults
  resetCanvasWH(canvas, initialLUnder)

  // You can map assertions onto a Future, then return the resulting Future[Assertion] to ScalaTest:
  it should "be loaded remote" in {
    Future.sequence(loaders).map { imageElements => {

      def context2Hashcode[T: Numeric](size: Position[T]) = {
        val UintClampedArray: mutable.Seq[Int] =
          ctx.getImageData(0, 0, size.x.asInstanceOf[Int], size.y.asInstanceOf[Int]).data
        UintClampedArray.hashCode()
      }

      def expectedHashCode = Map("background.png" -> 1425165765, "monster.png" -> -277415456, "hero.png" -> -731024817)
      //def expectedHashCode = Map("background.png" -> -1768009948, "monster.png" -> 1817836310, "hero.png" -> 1495155181)
      def getImgName(url: String) = url.split('/').last

      info("All images correct loaded")
      assert(imageElements.forall { img => {
        val pos = Position(img.width, img.height)
        resetCanvasWH(canvas, pos)
        ctx.drawImage(img, 0, 0, img.width, img.height)
        expectedHashCode(getImgName(img.src)) == context2Hashcode(pos)
      }
      })


      /* Composite all pictures drawn outside the play field.
       * This should result in a hashcode equal as the image of the background.
       */
      resetCanvasWH(canvas, initialLUnder)
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

      def testHarness(gs: GameState[SimpleCanvasGame.Generic], text: String, assertion: () => Boolean) = {
        render(gs)
        info(text)
        assert(assertion())
      }

      resetCanvasWH(canvas, doubleInitialLUnder)

      testHarness(loadedAndNoText0,
        "Default double size initial screen, no text",
        () => Seq(1355562831 /*Chrome*/ , 1668792783 /*FireFox*/).contains(context2Hashcode(doubleInitialLUnder)))
      val ref = context2Hashcode(doubleInitialLUnder) // Register the reference value

      val loadedAndSomeText1 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "Now with text which can differ between browsers",
        isNewGame = false)

      val loadedAndSomeText2 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "",
        isNewGame = true)

      testHarness(loadedAndSomeText1,
        "Test double screen with score text",
        () => ref != context2Hashcode(doubleInitialLUnder))

      testHarness(loadedAndSomeText2,
        "Test double screen with explain text put in", () => ref != context2Hashcode(doubleInitialLUnder))

      testHarness(loadedAndNoText0,
        "Test double screen reference still valid.",
        () => ref == context2Hashcode(doubleInitialLUnder))


    }
    }
  }
}
