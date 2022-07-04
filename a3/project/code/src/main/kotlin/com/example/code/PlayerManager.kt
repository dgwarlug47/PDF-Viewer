package com.example.code

import javafx.scene.input.KeyCode
import kotlin.random.Random

class PlayerManager(var spaceInvaders: SpaceInvaders?) {
    private var observersManager: ObserversManager? = null
    private var lives = if(DEBUG) 6 else 3
    private var random1 = Random.nextFloat()
    private var player = Player((random1* (CANVAS_WIDTH-50)).toDouble(), if(!DEBUG) CANVAS_HEIGHT-40.0 else 220.0)

    fun resetPlayer(){
        lives -= 1
        if (lives == 0){
            observersManager!!.screenIsDead()
            spaceInvaders!!.setGameOverScreen()
        }
        val observersManager = player.observersManager
        random1 = Random.nextFloat()
        player = Player((random1* (CANVAS_WIDTH-50)).toDouble(), if(!DEBUG) CANVAS_HEIGHT-40.0 else 220.0)

        if (observersManager != null) {
            initWithObserversManager(observersManager)
        }
    }

    fun initWithObserversManager(observersManager: ObserversManager){
        this.observersManager = observersManager
        player.observersManager = observersManager
        observersManager.queueAddToTimer(player)
        observersManager.collisionHandler.add2(player)
        observersManager.addToPane(player)
    }

    fun keyPressed(code: KeyCode){
        player.keyPressed(code)
    }

    fun keyReleased(code: KeyCode){
        player.keyReleased(code)
    }
}