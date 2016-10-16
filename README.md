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
Scala.js compile-to-Javascript language is by its compile phase ahead of runtime errors in production. It prevent you of nasty
runtime errors because everything must be ok in the compile phase, specially the types of the functions and variables.

In the original tutorial in Javascript: [How to make a simple HTML5 Canvas game](http://www.lostdecadegames.com/how-to-make-a-simple-html5-canvas-game/),
a continuous redraw of the canvas was made, which is a simple solution, but resource costly.
## Usage
Play the [live demo](http://goo.gl/oqSFCa). Scaladoc  you will find [here](https://amsterdam-scala.github.io/Sjs-Simple-HTML5-canvas-game/docs/api/index.html#nl.amsscala.package). 

## Architecture
![class diagram](https://raw.githubusercontent.com/amsterdam-scala/Sjs-Simple-HTML5-canvas-game/master/docs/HTML5CanvasGame.png)
#### Description:

By the initial call from `SimpleCanvas.main` to `Game.play` its (private) `gameLoop` will periodic started given its `framesPerSec` frequency.
Here the status of eventually pressed arrow keys will be tested and per `GameState.keyEffect` converted to a move of the `Hero`.
In an instance of `GameState` the position of the `CanvasComponent`s are immutable recorded. When a change has to be made a new instances will be 
generated with only the changed variables adjusted and leaving the rest unchanged by copying the object.

With the changes in this `CanvasComponent` a render method of `Page` is only called if the instance is found changed.

The render method repaints the canvas completely. Successively the background, monster and hero will be painted, so the last image is at the foreground.
The images are found are the respectively instances of `CanvasComponent` subclasses `Playground`, `Monster` and `Hero`.
They are asynchronously loaded once at startup by means of the use of `Future`s.

In spite of the fact that the application is one-tier on an MVC design pattern perspective, the following parts can be identified:

<table>
  <tr>
    <th>Part</th>
    <th>Class</th>
    <th>Auxiliary</th>
  </tr>
  <tr>
    <td>Model</td>
    <td>GameState</td>
    <td>Position</td>
  </tr>
  <tr>
    <td>View</td>
    <td>Page</td>
    <td>CanvasComponents (Playground, Monster and Hero)</td>
  </tr>
  <tr>
    <td>Controller</td>
    <td>Game</td>
    <td>GameState</td>
  </tr>
</table>

#### Testing


#### Further Resources
#### Notes
#### Considerations

#### Licence
Licensed under the EUPL-1.1

```-------------------------------------------------------------------------------
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

-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
JavaScript                       2            795              1          15423
HTML                             2             13             25             51
CSS                              1             14              0             49
-------------------------------------------------------------------------------
SUM:                             5            822             26          15523
-------------------------------------------------------------------------------
```
