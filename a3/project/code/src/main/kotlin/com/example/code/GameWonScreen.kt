package com.example.code

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

class GameWonScreen(helloApplication: HelloApplication): Pane() {


    init {
    }

    fun start(score: Int): Scene {
        val scene = Scene(this)
        return scene
    }
}