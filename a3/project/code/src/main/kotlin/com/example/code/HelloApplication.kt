package com.example.code

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val pane = Pane()
        val enemiesVBox = EnemiesVBox()
        val player = Player()
        val timer = Timer()
        val collisionHandler = CollisionHandler()
        val observersManager = ObserversManager(pane, timer, enemiesVBox, collisionHandler)
        val bulletGenerator = BulletGenerator(enemiesVBox)

        // put observer manager
        player.observersManager = observersManager
        collisionHandler.add2(player)
        collisionHandler.observersManager = observersManager
        bulletGenerator.observersManager = observersManager
        enemiesVBox.observersManager = observersManager

        //
        enemiesVBox.attach()


        pane.children.add(enemiesVBox)
        pane.children.add(player)


        timer.attach(enemiesVBox)
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