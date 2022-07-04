package com.example.code

import javafx.scene.input.KeyCode

class PlayerManager(var helloApplication: HelloApplication?) {
    private var observersManager: ObserversManager? = null
    private var lives = if(DEBUG) 6 else 3
    var player = Player()

    fun resetPlayer(){
        lives -= 1
        if (lives == 0){
            observersManager!!.screenIsDead()
            helloApplication!!.setGameOverScreen()
        }
        val observersManager = player.observersManager
        player = Player()
        if (observersManager != null) {
            initWithObserversManager(observersManager)
        }
    }

    fun initWithObserversManager(observersManager: ObserversManager){
        this.observersManager = observersManager
        player.observersManager = observersManager
        observersManager.addToTimer(player)
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