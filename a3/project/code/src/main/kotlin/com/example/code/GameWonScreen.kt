package com.example.code

import javafx.scene.Scene
import javafx.scene.layout.Pane

class GameWonScreen(spaceInvaders: SpaceInvaders): Pane() {


    init {
    }

    fun start(score: Int): Scene {
        return Scene(this)
    }
}