package com.example.code

import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane

class GameScreen(var spaceInvaders: SpaceInvaders?){
    private val pane = Pane()
    private val enemiesVBox = EnemiesVBox()
    private val timer = Timer()
    var playerManager = PlayerManager(spaceInvaders!!)
    var statusBar = StatusBar()
    val collisionHandler = CollisionHandler(spaceInvaders!!)
    private val bulletGenerator = BulletGenerator(enemiesVBox)
    private val observersManager = ObserversManager(pane, timer, enemiesVBox, collisionHandler, playerManager, statusBar, bulletGenerator)
    fun start(score: Int, level: Int) : Scene{
        statusBar.score = score
        statusBar.level = level
        enemiesVBox.level = level

        pane.prefWidth = CANVAS_WIDTH
        pane.prefHeight = CANVAS_HEIGHT

        // put observer manager
        playerManager.initWithObserversManager(observersManager)
        collisionHandler.observersManager = observersManager
        bulletGenerator.observersManager = observersManager
        enemiesVBox.observersManager = observersManager

        enemiesVBox.attach()

        pane.children.add(enemiesVBox)
        pane.style = "-fx-background-color: black;"

        timer.attach(enemiesVBox)
        timer.attach(bulletGenerator)
        timer.attach(collisionHandler)

        val borderPane = BorderPane()

        borderPane.center = pane

        borderPane.top = statusBar

        val scene = Scene(borderPane)
        scene.setOnKeyPressed {
                e ->
            run {
                playerManager.keyPressed(e.code)
            }
        }
        scene.setOnKeyReleased {
            e ->
            run {
                playerManager.keyReleased(e.code)
            }
        }
        return scene
    }
}