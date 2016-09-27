package nl.amsscala
package simplegame

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode.{Down, Left, Right, Up}

import scala.collection.mutable

class GameSuite extends SuiteSpec with Page{
  type T =SimpleCanvasGame.T
  canvas.width = 1242 // 1366
  canvas.height = 674 // 768

  val game =GameState(canvas, Position(0,0), center(canvas))

  println(game)

    describe("The Game") {
      describe("should tested by navigation keys") {

        it("good path") {
          // No keys, no movement
          game.keyEffect(1D, mutable.Set.empty) shouldBe game

          // Opposite horizontal navigation, no movement 1
          game.keyEffect(1D, mutable.Set(Left , Right)) shouldBe game

          // Opposite horizontal navigation, no movement 2
          game.keyEffect(1D, mutable.Set(Right , Left )) shouldBe game

          // Opposite vertical navigation, no movement 1
          game.keyEffect(1D, mutable.Set(Up , Down)) shouldBe game

          // Opposite vertical navigation, no movement 2
          game.keyEffect(1D, mutable.Set(Down, Up )) shouldBe game

          // All four directions, no movement
          game.keyEffect(1D, mutable.Set(Up , Right , Left, Down )) shouldBe game

          game.keyEffect(1D, mutable.Set(Left )).hero.pos - Position(621,337) shouldBe Position(-256,0)
//          game.keyEffect(1D, mutable.Set(Right )).hero.pos - Position(621,337) shouldBe Position(256,0)
          game.keyEffect(1D, mutable.Set(Up )).hero.pos - Position(621,337) shouldBe Position(0,-256)
          game.keyEffect(1D, mutable.Set(Down )).hero.pos - Position(621,337) shouldBe Position(0,256)

          // North west navigation, etc
          game.keyEffect(1D, mutable.Set(Left, Up )).hero.pos - Position(621,337) shouldBe Position(-256,-256)
          game.keyEffect(1D, mutable.Set(Up, Right )).hero.pos - Position(621,337) shouldBe Position(256,-256)
          game.keyEffect(1D, mutable.Set(Down, Right )).hero.pos - Position(621,337) shouldBe Position(256,256)
          game.keyEffect(1D, mutable.Set(Down, Left )).hero.pos - Position(621,337) shouldBe Position(-256,256)


          /*  games += game.newGame(hero = new Hero(game.hero.pos - Position(Hero.speed, Hero.speed)))
          // North west navigation
          game.updateGame(1D, mutable.Map(Up -> dummyTimeStamp, Left -> dummyTimeStamp), cnvs) shouldBe games.last

          games += game.newGame(hero = new Hero(game.hero.pos + Position(Hero.speed, Hero.speed)))
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


      } */}
    }}}

