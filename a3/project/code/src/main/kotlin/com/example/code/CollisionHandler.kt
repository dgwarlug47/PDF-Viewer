package com.example.code

import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape


class CollisionHandler() :Observer{
    var observersManager: ObserversManager? = null
    // shapes that include the enemies
    private val shapes1 : MutableList<Rectangle> = mutableListOf()
    // shapes that include the player and their bullets
    private val shapes2 : MutableList<Rectangle> = mutableListOf()

    override fun update(){
        for (shape1 in shapes1){
            for (shape2 in shapes2){
                if (Shape.intersect(shape1, shape2).boundsInLocal.width != -1.0){
                    observersManager?.removeFromEnemyVBox(shape1)
                    observersManager?.removeFromPane(shape2)
                    shapes1.remove(shape1)
                    shapes2.remove(shape2)
                }
            }
        }
    }

    fun add1(shape: Rectangle){
        shapes1.add(shape)
    }

    fun add2(shape: Rectangle){
        shapes2.add(shape)
    }

}