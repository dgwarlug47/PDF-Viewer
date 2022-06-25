package com.example.code

import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane

class BulletGenerator() : Observer{
    var observersManager: ObserversManager? = null
    private var state = 0

    private fun generateBullets(){
        val bullet = Bullet()
        observersManager?.add2ToCollisionHandler(bullet)
        observersManager?.addToPane(bullet)
        observersManager?.addToTimer(bullet)
    }

    override fun update(){
        state += 1
        if (state%50 == 0){
            generateBullets()
        }
    }
}