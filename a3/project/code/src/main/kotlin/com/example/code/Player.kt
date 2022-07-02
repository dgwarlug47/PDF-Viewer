package com.example.code

import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import java.io.File

class Player() : Rectangle(40.0, 300.0, 40.0, 40.0), Observer{
    var observersManager: ObserversManager? = null
    private val path = "space-invaders-assets/images/player.png"
    private var currentlyMoving = false
    private var dir = 1
    private var lastTimeMissalFired = 0L
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
        if (code == KeyCode.SPACE) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastTimeMissalFired) >= 500) {
                lastTimeMissalFired = currentTime
                val bullet = Bullet(x + translateX + 10.0, y + 5.0, 20.0, 20.0, BulletOwners.Players)
                observersManager?.addToPane(bullet)
                observersManager?.addToTimer(bullet)
                observersManager?.add2ToCollisionHandler(bullet)
            }
        }
    }

    override fun update(){
        if (!currentlyMoving){
            return
        }
        if (this.x + this.translateX + this.width >= CANVAS_WIDTH) {
            if (dir == 1) {
                return
            }
        }
        if (this.x + this.translateX < 0){
            if (dir == -1){
                return
            }
        }
        translateX += (dir*3)

    }
}