package com.example.code

import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import java.io.File
import java.nio.file.Paths

class Enemy : Rectangle(0.0, 0.0, 40.0, 40.0){
    val path = "space-invaders-assets/images/enemy2.png"

    init {
        println("hey")
        println(Paths.get("").toAbsolutePath().toString() )
        this.fill = ImagePattern(Image(File(path).toURI().toString()))
    }
}