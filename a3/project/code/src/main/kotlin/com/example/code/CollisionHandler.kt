package com.example.code

import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape


class CollisionHandler(private val helloApplication: HelloApplication) :Observer{
    var observersManager: ObserversManager? = null
    // shapes that include the enemies and their bullets
    private val shapes1 : MutableList<Rectangle> = mutableListOf()
    // shapes that include the player and their bullets
    private val shapes2 : MutableList<Rectangle> = mutableListOf()
    var killedEnemiesCounter = 0

    override fun update(){
        for (shape1 in shapes1){
            if (shape1 is Enemy){
                if (!shape1.alive){
                    continue
                }
            }
            for (shape2 in shapes2){
                if (Shape.intersect(shape1, shape2).boundsInLocal.width != -1.0){
                    if (shape1 is Enemy) {
                        killedEnemiesCounter += 1
                        if (killedEnemiesCounter == 50){
                            helloApplication.nextLevel()
                        }
                        shape1.remove()
                        observersManager!!.enemiesVBox.enemyWasHit()
                        observersManager!!.statusBar.enemyWasHit()
                    }
                    if (shape2 is Player){
                        observersManager!!.resetPlayer()
                    }
                    observersManager!!.removeFromPane(shape2)
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