package com.example.code

import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle

class EnemiesBox : Observer, HBox(){
    val enemyList : MutableList<Enemy> = mutableListOf()
    var direction = 1
    val numEnemies = 9
    init {
        for (i in 1..numEnemies){
            val newEnemy = Enemy()
            this.children.add(newEnemy)
            enemyList.add(newEnemy)
        }
    }
    override fun update(){
        if (this.translateX + this.width >= CANVAS_WIDTH || this.translateX < 0){
            this.translateY = this.translateY + 1
            direction = -direction
        }
        this.translateX = this.translateX + direction*3
    }
    fun giveChildrenToObserver(collisionHandler: CollisionHandler){
        for (child in children){
            if (child is Rectangle){
                collisionHandler.add1(child)
            }
        }
    }
}