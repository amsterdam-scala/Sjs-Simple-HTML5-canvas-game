package nl.amsscala
package simplegame

import org.scalajs.dom

class gameElementSuite extends SuiteSpec {

  describe("A GameElement") {
    describe("should tested within the limits") {
      it ("should be equal") {
       // new GameElement[Int](Position(0,0),null, "") {}
      }
    }
  }

      describe("A Hero") {
    describe("should tested within the limits") {
      val canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
      canvas.width = 150
      canvas.height = 100
      it("good path") {
        Hero(Position(0, 0)).isValidPosition(canvas) shouldBe true
        Hero(Position(150 - Hero.pxSize, 100 - Hero.pxSize)).isValidPosition(canvas) shouldBe true
      }
      it("bad path") {
        Hero(Position(-1, 0)).isValidPosition(canvas) shouldBe false
        Hero(Position(4, -1)).isValidPosition(canvas) shouldBe false
        Hero(Position(0, 101 - Hero.pxSize)).isValidPosition(canvas) shouldBe false
        Hero(Position(151 - Hero.pxSize, 0)).isValidPosition(canvas) shouldBe false
      }

    }
  }
}
