package nl.amsscala
package simplegame

import org.scalatest.AsyncFlatSpec

import scala.collection.mutable
import scala.concurrent.Future

class PageSuite extends AsyncFlatSpec with Page {
  type T = SimpleCanvasGame.Generic
  // All graphical features are placed just outside the playground
  lazy val gameState0 = GameState[T](canvas, doubleInitialLUnder, doubleInitialLUnder)
  // Collect all Futures of onload events
  lazy val loaders = gameState0.pageElements.map(pg => imageFuture(pg.src))
  val initialLUnder = Position(512, 480).asInstanceOf[Position[T]]
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

      def testHarness(gs: GameState[T], text: String, assertion: () => Boolean) = {
        render(gs)
        info(text)
        assert(assertion(), s"Thrown probably by value ${context2Hashcode(doubleInitialLUnder)}")
      }

      /* Composite all pictures drawn outside the play field.
       * This should result in a hashcode equal as the image of the background.
       */
      resetCanvasWH(canvas, initialLUnder)
      val loadedAndNoText0 = new GameState(canvas,
        gameState0.pageElements.zip(imageElements).map { case (el, img) => el.copy(img = img) },
        monstersHitTxt = "",
        isNewGame = false)

      testHarness(loadedAndNoText0,
        "Default initial screen everything left out",
        () => context2Hashcode(initialLUnder) == expectedHashCode("background.png"))

      // ***** Tests with double canvas size
      resetCanvasWH(canvas, doubleInitialLUnder)

      testHarness(loadedAndNoText0, "Default double size initial screen, no text",
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

      testHarness(loadedAndSomeText1, "Test double screen with score text",
        () => ref != context2Hashcode(doubleInitialLUnder))

      testHarness(loadedAndSomeText2, "Test double screen with explain text put in",
        () => ref != context2Hashcode(doubleInitialLUnder))


      // *****  Test the navigation of the Hero character graphical
      def navigateHero(gs: GameState[T], move: Position[Int]) =
      gs.copy(new Hero(initialLUnder + move.asInstanceOf[Position[T]], gs.pageElements.last.img))

      testHarness(navigateHero(loadedAndNoText0, Position(0, 0)), "Test double screen with centered hero",
        () => Seq(-1212284464 /*Chrome*/ , 981419409 /*FireFox*/).contains(context2Hashcode(doubleInitialLUnder)))

      testHarness(navigateHero(loadedAndNoText0, Position(1, 0)), "Test double screen with right displaced hero",
        () => Seq(475868743 /*Chrome*/ , -1986372876 /*FireFox*/).contains(context2Hashcode(doubleInitialLUnder)))

      testHarness(navigateHero(loadedAndNoText0, Position(-1, 0)), "Test double screen with left displaced hero",
        () => Seq(320738379 /*Chrome*/ , 214771813 /*FireFox*/).contains(context2Hashcode(doubleInitialLUnder)))

      testHarness(navigateHero(loadedAndNoText0, Position(0, 1)), "Test double screen with up displaced hero",
        () => Seq(-409947707 /*Chrome*/ , -1902498081 /*FireFox*/).contains(context2Hashcode(doubleInitialLUnder)))

      testHarness(navigateHero(loadedAndNoText0, Position(0, -1)), "Test double screen with down displaced hero",
        () => Seq(1484865515 /*Chrome*/ , 954791841 /*FireFox*/).contains(context2Hashcode(doubleInitialLUnder)))

      testHarness(loadedAndNoText0, "Test double screen reference still to same.",
        () => ref == context2Hashcode(doubleInitialLUnder))
    }
    }
  }
}
