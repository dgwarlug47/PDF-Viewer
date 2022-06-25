package com.example.code

import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

class ObserversManager(val pane: Pane, val timer: Timer, val enemiesBox: EnemiesBox,
                       val collisionHandler: CollisionHandler) {

    fun addToPane(node: Node){
        pane.children.add(node)
    }

    fun addToTimer(observer: Observer){
        timer.attach(observer)
    }

    fun addToEnemyBox(node: Node){
        enemiesBox.children.add(node)
    }

    fun removeFromPane(node: Node){
        pane.children.remove(node)
    }

    fun removeFromEnemyBox(node: Node){
        enemiesBox.children.remove(node)
    }

    fun add1ToCollisionHandler(shape: Rectangle){
        collisionHandler.add1(shape)
    }

    fun add2ToCollisionHandler(shape: Rectangle){
        collisionHandler.add2(shape)
    }
}