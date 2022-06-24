package com.example.code

import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import java.io.File

class Bullet : Rectangle(CANVAS_WIDTH-40.0, 200.0, 20.0, 20.0), Observer{
    val path = "space-invaders-assets/images/bullet1.png"

    init {
        this.fill = ImagePattern(Image(File(path).toURI().toString()))
    }

    override fun update() {
        this.translateY = this.translateY - 1
    }
}