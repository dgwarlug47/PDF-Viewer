package com.example.code

import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.scene.media.MediaPlayer
import javafx.scene.media.Media



class CollisionHandler(var helloApplication: HelloApplication?) :Observer{
    val classLoader = Thread.currentThread().contextClassLoader
    var observersManager: ObserversManager? = null
    // shapes that include the enemies and their bullets
    private val shapes1 : MutableList<Rectangle> = mutableListOf()
    // shapes that include the player and their bullets
    private val shapes2 : MutableList<Rectangle> = mutableListOf()
    var killedEnemiesCounter = 0

    override fun update(){
        var foundIntersection = false
        var gameIsOver = false
        var gameWon = true
        var finalShape1: Rectangle? = null
        var finalShape2: Rectangle? = null
        for (shape1 in shapes1){
            if (foundIntersection){
                break
            }
            if (gameIsOver){
                break
            }
            if (shape1 is Enemy){
                if (!shape1.alive){
                    continue
                }
            }
            for (shape2 in shapes2){
                if (Shape.intersect(shape1, shape2).boundsInLocal.width != -1.0){
                    if (shape1 is Enemy) {
                        val something = classLoader.getResource("invaderkilled.wav")?.toString()
                        MediaPlayer(Media(something)).play()

                        killedEnemiesCounter += 1
                        if (killedEnemiesCounter == if (DEBUG) 15 else 50){
                            gameIsOver = true
                            break
                        }
                        if (shape2 is Player){
                            println("we went here")
                            gameIsOver = true
                            gameWon = false
                            break
                        }

                        shape1.remove()
                        observersManager!!.enemiesVBox.enemyWasHit()
                        observersManager!!.statusBar.enemyWasHit()
                    }
                    else if (shape1 is Bullet){
                        observersManager!!.removeFromPane(shape1)
                    }
                    if (shape2 is Player){
                        val something = classLoader.getResource("explosion.wav")?.toString()
                        MediaPlayer(Media(something)).play()
                        observersManager!!.resetPlayer()
                    }
                    observersManager!!.removeFromPane(shape2)

                    finalShape1 = shape1
                    finalShape2 = shape2
                    foundIntersection = true
                    break
                }
            }
        }
        shapes1.remove(finalShape1)
        shapes2.remove(finalShape2)

        if (gameIsOver){
            for (shape2 in shapes2){
                observersManager!!.removeFromPane(shape2)
            }
            observersManager!!.screenIsDead()
            if (gameWon) {
                helloApplication!!.nextLevel()
            }
            else{
                helloApplication!!.setGameOverScreen()
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