package com.example.code

import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import java.io.File

class Player : Observer, Rectangle(CANVAS_WIDTH/2, CANVAS_HEIGHT - 20.0, 20.0, 20.0){
    val alive: Boolean = true
    val path = "space-invaders-assets/images/player.png"
    init {
        this.fill = ImagePattern(Image(File(path).toURI().toString()))
    }

    override fun update(){
        this.translateY = this.translateY - 1
    }
}