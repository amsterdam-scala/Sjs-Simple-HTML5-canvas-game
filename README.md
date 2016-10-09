<a href="http://www.w3.org/html/logo/">
<img src="https://www.w3.org/html/logo/badge/html5-badge-h-css3-graphics-semantics.png" width="99" height="32" alt="HTML5 Powered with CSS3 / Styling, Graphics, 3D &amp; Effects, and Semantics" title="HTML5 Powered with CSS3 / Styling, Graphics, 3D &amp; Effects, and Semantics"></a>[![Scala.js](https://img.shields.io/badge/scala.js-0.6.10%2B-blue.svg?style=flat)](https://www.scala-js.org)
[![Build Status](https://travis-ci.org/amsterdam-scala/Sjs-Simple-HTML5-canvas-game.svg?branch=master)](https://travis-ci.org/amsterdam-scala/Sjs-Simple-HTML5-canvas-game)

# Simple HTML5 Canvas game ported to Scala.js
**Featuring Scala.js "in browser testing" by ScalaTest 3.x**

A Scala hardcore action Role-Playing Game (RPG) where you possess and play as a Hero. :-)

## Project
This "Simple HTML5 Canvas Game" is a Scala.js project which targets a browser capable displaying HTML5, especially the `<canvas>` element.
Stored on GitHub.com, the code is also remote tested on Travis-CI.

This quite super simple game is heavily over-engineered. It's certainly not the game that counts but the technology around it, it features:
1. HTML5 Canvas controlled by Scala.js
1. Headless canvas Selenium 2 "in browser testing" with the recently released ScalaTest 3.x
1. ScalaTest with "async" testing styles
1. Exhaustive use of a variety of Scala features, e.g.:
    * `Traits`, (`case`) `Class`es and `Object`s (singletons)
    * `Future`s
    * Type parameters (even in the frenzied Ough).
    * Algebraic Data Types and pattern matching
1. Reactive design instead of continuous polling.
1. Eliminating a continuously redrawn of the canvas saves cpu time and power.
1. Scala generated HTML.
1. Tackling CORS enabled images.
## Motivation
Scala.js compile-to-Javascript language is by its compile phase ahead of runtime errors in the future. It prevent you of nasty
runtime error because everything must be ok in the compile phase, specially the types of the functions and variables.

In the original tutorial in Javascript: [How to make a simple HTML5 Canvas game](http://www.lostdecadegames.com/how-to-make-a-simple-html5-canvas-game/),
a continuous redraw of the canvas was made, which is a simple solution, but resource costly.
## Usage
Play the [live demo](http://goo.gl/oqSFCa). Scala doc is [here](https://amsterdam-scala.github.io/Sjs-Simple-HTML5-canvas-game/docs/api/index.html#nl.amsscala.package). 

## Architecture

object SimpleCanvasGame extends JSApp with Game with Page {

Further Resources, Notes, and Considerations

Licensed under the EUPL-1.1

-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Scala                            6             96            113            261


game.js
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
JavaScript                       1             21             16             93

Scala.js minimal project
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
JavaScript                       1             26              1            572
