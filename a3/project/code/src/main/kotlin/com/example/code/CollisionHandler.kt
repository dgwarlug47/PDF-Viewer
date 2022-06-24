package com.example.code

import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape


class CollisionHandler(val enemiesBox: EnemiesBox, val pane: Pane) :Observer{
    // shapes that include the enemies
    val shapes1 : MutableList<Rectangle> = mutableListOf()
    // shapes that include the player and bullets
    val shapes2 : MutableList<Rectangle> = mutableListOf()

    override fun update(){
        for (shape1 in shapes1){
            for (shape2 in shapes2){

                if (Shape.intersect(shape1, shape2).boundsInLocal.width != -1.0){
                    println("why never here")
                    enemiesBox.children.remove(shape1)
                    enemiesBox.enemyList.remove(shape1)
                    pane.children.remove(shape2)
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