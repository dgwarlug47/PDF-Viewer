package com.example.code

import javafx.scene.layout.HBox
import javafx.scene.shape.Rectangle

class EnemiesHBox(enemyType: EnemyType) : Observer, HBox(){
    private val enemyList : MutableList<Enemy> = mutableListOf()
    private val numEnemies = 9
    init {
        for (i in 1..numEnemies){
            val newEnemy = Enemy(enemyType)
            this.children.add(newEnemy)
            enemyList.add(newEnemy)
        }
    }
}