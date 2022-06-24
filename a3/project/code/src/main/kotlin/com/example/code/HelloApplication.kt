package com.example.code

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.stage.Stage

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val enemiesBox = EnemiesBox()

        val pane = Pane()
        pane.children.add(enemiesBox)

        val collisionHandler = CollisionHandler(enemiesBox, pane)
        enemiesBox.giveChildrenToObserver(collisionHandler)

        val timer = Timer()

        val bulletGenerator = BulletGenerator(enemiesBox, collisionHandler, pane, timer)

        timer.attach(enemiesBox)
        timer.attach(bulletGenerator)
        timer.attach(collisionHandler)

        val borderPane = BorderPane()

        borderPane.top = pane
        val scene = Scene(borderPane, CANVAS_WIDTH, CANVAS_HEIGHT)
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}