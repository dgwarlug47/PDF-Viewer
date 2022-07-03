package com.example.code

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane

class StatusBar() : Pane(){
    var lives = 3
        set (lives){
            label2.text = "Lives $lives"
            field = lives
        }
    var score = 0
        set(score){
            field = score
            label1.text = "Score $score"
        }
    var level = 0
        set (level){
            field = level
            label3.text = "Level: $level"

        }
    private val label1 = Label("Score: $score")
    private val label2 = Label("Lives: $lives")
    private val label3 = Label("Level: $level")
    private val hBox = HBox()

    init {
        hBox.spacing = 300.0
        this.style = "-fx-background-color: black;"

        label1.style = "-fx-text-fill: white; -fx-font-size: 16px;"
        label2.style = "-fx-text-fill: white; -fx-font-size: 16px;"
        label3.style = "-fx-text-fill: white; -fx-font-size: 16px;"

        hBox.children.addAll(label1, label2, label3)
        this.children.add(hBox)
    }

    fun updateLives(){
        lives -= 1
        label2.text = "Lives: $lives"
    }

    fun enemyWasHit(){
        score += 10
        label1.text = "Score: $score"
    }
}