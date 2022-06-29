package com.example.code

import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle

class EnemiesHBox(enemyType: EnemyType, val boxId: Int) : Observer, HBox(){
    val enemyList : MutableList<Enemy> = mutableListOf()
    val circle = Rectangle(0.0, 0.0, 50.0, 50.0)
    val circle2 = Rectangle(0.0, 0.0, 50.0, 50.0)
    private val numEnemies = 9
    init {
        circle.fill = Color.DARKBLUE
        this.children.add(circle)
        for (i in 1..numEnemies){
            val newEnemy = Enemy(enemyType, boxId)
            this.children.add(newEnemy)
            enemyList.add(newEnemy)
        }
        this.children.add(circle2)
    }
}