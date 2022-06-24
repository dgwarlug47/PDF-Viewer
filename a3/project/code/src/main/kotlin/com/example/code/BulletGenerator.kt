package com.example.code

import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane

class BulletGenerator(val enemiesBox: EnemiesBox, val collisionHandler: CollisionHandler,
    val pane: Pane, val timer: Timer) : Observer{
    private var state = 0

    fun generateBullets(){
        val bullet = Bullet()
        collisionHandler.add2(bullet)
        pane.children.add(bullet)
        timer.attach(bullet)
    }

    override fun update(){
        state += 1
        if (state%50 == 0){
            generateBullets()
        }
    }
}