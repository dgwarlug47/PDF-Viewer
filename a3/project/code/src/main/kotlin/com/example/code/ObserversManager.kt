package com.example.code

import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle

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

    fun removeFromEnemyVBox(node: Node){
        for (child in enemiesVBox.children){
            (child as HBox).children.remove(node)
        }
    }

    fun add1ToCollisionHandler(shape: Rectangle){
        collisionHandler.add1(shape)
    }

    fun add2ToCollisionHandler(shape: Rectangle){
        collisionHandler.add2(shape)
    }
}