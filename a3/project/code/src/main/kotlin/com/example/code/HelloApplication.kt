package com.example.code

import javafx.application.Application
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.stage.Stage

class HelloApplication: Application() {
    val classLoader = Thread.currentThread().contextClassLoader
    private var stage: Stage? = null
    private var score = 0
    private var level = 1
    private var gameScreen = GameScreen(this)
    private val initialScreen = InitialScreen(this)
    private var gameOverScreen = GameOverScreen(this)

    fun setGameOverScreen() {
        score = gameScreen.statusBar.score
        gameOverScreen = GameOverScreen(this)
        stage!!.scene = gameOverScreen.start(score, "Game Over")
        stage!!.show()
    }

    private fun setGameWonScreen(){
        score = gameScreen.statusBar.score
        gameOverScreen = GameOverScreen(this)
        stage!!.scene = gameOverScreen.start(score, "Game Won")
        stage!!.show()
    }

    fun setInitialScreen(){
        stage!!.scene = initialScreen.start()
        stage!!.show()
    }

    fun nextLevel(){
        level = gameScreen.statusBar.level
        score = gameScreen.statusBar.score

        if (level == 3){
            setGameWonScreen()
        }
        else{
            setGameScreen(score, level + 1)
        }
    }

    fun setGameScreen(score: Int, level: Int){
        gameScreen.helloApplication = null
        gameScreen.playerManager.helloApplication = null
        gameScreen.collisionHandler.helloApplication = null
        gameScreen = GameScreen(this)
        stage!!.scene = gameScreen.start(score, level)
        stage!!.isResizable = false
        stage!!.show()
    }

    override fun start(stage: Stage?) {
        this.stage = stage
        setInitialScreen()
    }
}