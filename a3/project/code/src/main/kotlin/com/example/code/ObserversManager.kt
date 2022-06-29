package com.example.code

import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

class ObserversManager(val pane: Pane, val timer: Timer, val enemiesVBox: EnemiesVBox,
                       val collisionHandler: CollisionHandler) {

    fun addToPane(node: Node){
        pane.children.add(node)
    }

    fun addToTimer(observer: Observer){
        timer.attach(observer)
    }

    fun removeFromPane(node: Node){
        pane.children.remove(node)
    }

    fun removeFromEnemyVBox(enemy: Enemy){
        println("this is hte boxid")
        println(enemy.boxId)
        for (child in enemiesVBox.children){
            val enemiesHBox = (child as EnemiesHBox)
            if (enemiesHBox.enemyList.contains(enemy)){
                enemiesHBox.children.remove(enemy as Shape)
            }
        }
    }

    fun add1ToCollisionHandler(shape: Rectangle){
        collisionHandler.add1(shape)
    }

    fun add2ToCollisionHandler(shape: Rectangle){
        collisionHandler.add2(shape)
    }
}