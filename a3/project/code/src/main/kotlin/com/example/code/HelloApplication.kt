package com.example.code

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val pane = Pane()
        val enemiesBox = EnemiesBox()
        val player = Player()
        val timer = Timer()
        val collisionHandler = CollisionHandler()
        val observersManager = ObserversManager(pane, timer, enemiesBox, collisionHandler)

        val bulletGenerator = BulletGenerator()


        // put observer manager
        player.observersManager = observersManager
        collisionHandler.observersManager = observersManager
        bulletGenerator.observersManager = observersManager


        pane.children.add(enemiesBox)
        pane.children.add(player)

        enemiesBox.giveChildrenToObserver(collisionHandler)


        timer.attach(enemiesBox)
        timer.attach(bulletGenerator)
        timer.attach(collisionHandler)
        timer.attach(player)

        val borderPane = BorderPane()

        borderPane.top = pane


        val scene = Scene(borderPane, CANVAS_WIDTH, CANVAS_HEIGHT)
        scene.setOnKeyPressed {
                e ->
            run {
                player.keyPressed(e.code)
            }
        }
        scene.setOnKeyReleased {
            e ->
            run {
                player.keyReleased(e.code)
            }
        }
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}