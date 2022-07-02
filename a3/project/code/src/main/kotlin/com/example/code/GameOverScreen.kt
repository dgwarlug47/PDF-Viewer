package com.example.code

import javafx.scene.control.Label
import javafx.scene.layout.Pane

class GameOverScreen : Pane(){
    var label1 = Label("Game Over")
    init {
        this.children.add(label1)
    }
}