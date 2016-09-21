package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode.{Down, Left, Right, Up}

import scala.collection.mutable

class GameSuite extends SuiteSpec {

  describe("A Hero") {
    describe("should tested within the limits") {
      val canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
      canvas.width = 150
      canvas.height = 100
      it("good path") {
        new Hero(Position(0,0),null).isValidPosition(canvas) shouldBe true
        new Hero(Position(150 - Hero.pxSize, 100 - Hero.pxSize),null).isValidPosition(canvas) shouldBe true
      }
      it("bad path") {
        new Hero(Position(-1, 0),null).isValidPosition(canvas) shouldBe false
        new Hero(Position(4, -1),null).isValidPosition(canvas) shouldBe false
        new Hero(Position(0, 101 - Hero.pxSize),null).isValidPosition(canvas) shouldBe false
        new Hero(Position(151 - Hero.pxSize, 0),null).isValidPosition(canvas) shouldBe false
      }

    }
  }

/*  describe("The Game") {
    describe("should tested by navigation keys") {

      val cnvs = dom.document.createElement("cnvs").asInstanceOf[dom.html.Canvas]
      cnvs.width = 1242 // 1366
      cnvs.height = 674 // 768


      val game = new GameState(cnvs, -1, false).copy(monster = Monster(0, 0)) // Keep the monster out of site

      it("good path") {
        // No keys, no movement
        game.updateGame(1D, mutable.Map.empty, cnvs) shouldBe game

        // Opposite horizontal navigation, no movement 1
        game.updateGame(1D, mutable.Map(Left -> dummyTimeStamp, Right -> dummyTimeStamp), cnvs) shouldBe game

        // Opposite horizontal navigation, no movement 2
        game.updateGame(1D, mutable.Map(Right -> dummyTimeStamp, Left -> dummyTimeStamp), cnvs) shouldBe game

        // Opposite vertical navigation, no movement 1
        game.updateGame(1D, mutable.Map(Up -> dummyTimeStamp, Down -> dummyTimeStamp), cnvs) shouldBe game

        // Opposite vertical navigation, no movement 2
        game.updateGame(1D, mutable.Map(Down -> dummyTimeStamp, Up -> dummyTimeStamp), cnvs) shouldBe game

        // All four directions, no movement
        game.updateGame(
          1D,
          mutable.Map(Up -> dummyTimeStamp, Right -> dummyTimeStamp, Left -> dummyTimeStamp, Down -> dummyTimeStamp),
          cnvs
        ) shouldBe game

        games += game.updateGame(
          1D,
          mutable.Map(Left -> dummyTimeStamp, Right -> dummyTimeStamp, Up -> dummyTimeStamp, Down -> dummyTimeStamp),
          cnvs
        )
        games.head shouldBe game
        games += game.copy(hero = new Hero(game.hero.pos - Position(Hero.speed, Hero.speed)))
        // North west navigation
        game.updateGame(1D, mutable.Map(Up -> dummyTimeStamp, Left -> dummyTimeStamp), cnvs) shouldBe games.last

        games += game.copy(hero = new Hero(game.hero.pos + Position(Hero.speed, Hero.speed)))
        // South East navigation
        game.updateGame(1D, mutable.Map(Down -> dummyTimeStamp, Right -> dummyTimeStamp), cnvs) shouldBe games.last

      }
      it("sad path") {
        // Illegal key code
        game.updateGame(1D, mutable.Map(0 -> dummyTimeStamp), cnvs) shouldBe game
      }
      it("bad path") {
        // No move due a of out cnvs limit case
        game.updateGame(1.48828125D, mutable.Map(Right -> dummyTimeStamp, Down -> dummyTimeStamp), cnvs) shouldBe game
      }


    }
  }*/
}
