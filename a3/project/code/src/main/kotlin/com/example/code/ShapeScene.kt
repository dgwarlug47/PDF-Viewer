package com.example.code

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Ellipse

const val WIDTH2 = 300.0
const val HEIGHT2 = 300.0


class ShapeScene: Observer, Pane() {

    val ball = Ellipse(25.0, 25.0)

    init {
        ball.fill = Color.CHOCOLATE
        prefWidth = WIDTH2
        prefHeight = HEIGHT2
        // set starting properties of ball
        ball.centerY = HEIGHT2 / 2
        // add it to the scene
        children.add(ball)
    }

    override fun update(){
        ball.centerX += 1
    }

    /* doesn't need draw: JavaFX redraws scene graph 60 FPS */
}