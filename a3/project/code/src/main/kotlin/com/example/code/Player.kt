package com.example.code

import javafx.animation.TranslateTransition
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.io.File

class Player() : Rectangle(CANVAS_WIDTH/2, 500.0, 40.0, 40.0), Observer{
    var observersManager: ObserversManager? = null
    val path = "space-invaders-assets/images/player.png"
    var currentlyMoving = false
    var dir = 1
    init {
        this.fill = ImagePattern(Image(File(path).toURI().toString()))
    }

    fun keyReleased(code: KeyCode){
        if ((code == KeyCode.A && dir == -1) || (code == KeyCode.D && dir == 1)){
            currentlyMoving = false
        }
    }

    fun keyPressed(code : KeyCode){
        if (code == KeyCode.A) {
            dir = -1
            currentlyMoving = true
        }
        if (code == KeyCode.D){
            dir = 1
            currentlyMoving = true
        }
        if (code == KeyCode.SPACE){
            val bullet = Bullet()
            observersManager?.addToPane(bullet)

        }
    }

    override fun update(){
        if (!currentlyMoving){
            return
        }
        translateX += (dir*3)

    }
}