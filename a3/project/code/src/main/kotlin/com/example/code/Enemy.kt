package com.example.code

import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import java.io.File
import java.nio.file.Paths

enum class EnemyType{
    type1,
    type2,
    type3
}

class Enemy(val enemyType: EnemyType, val boxId: Int) : Rectangle(0.0, 0.0, 20.0, 20.0){
    var alive = true
    init {
        var enemyFlag = "1"
        if (enemyType == EnemyType.type2){
            enemyFlag = "2"
        }
        if (enemyType == EnemyType.type3){
            enemyFlag = "3"
        }
        val path = "space-invaders-assets/images/enemy$enemyFlag.png"
        this.fill = ImagePattern(Image(File(path).toURI().toString()))
    }
    fun remove(){
        this.fill = Color.BLACK
        this.alive = false
    }
}