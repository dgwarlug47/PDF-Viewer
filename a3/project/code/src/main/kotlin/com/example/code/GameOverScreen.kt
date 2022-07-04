package com.example.code

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text


class GameOverScreen(private val spaceInvaders: SpaceInvaders) : BorderPane(){
    private val label4 = Label("")
    private val label2 = Label("R - Restart Game")
    private val label3 = Label("Q - Quit Game")
    private val text = Text("Game Over")

    private val vbox1 = VBox()

    init {
        prefWidth = 300.0
        prefHeight = 300.0

        // font for text
        text.font = Font.font("System", FontWeight.BOLD, FontPosture.REGULAR, 25.0)
        text.style = "-fx-font-weight: bold"

        val root = Group(text)
        vbox1.children.add(root)

        vbox1.children.add(label4)
        vbox1.children.add(label2)
        vbox1.children.add(label3)
        vbox1.spacing = 20.0
        vbox1.alignment = Pos.CENTER

        this.center = vbox1
    }

    fun start(score: Int, message: String): Scene{
        text.text = message

        label4.text = "Final Score: $score"

        val scene = Scene(this)
        scene.setOnKeyPressed {
                e ->
            run{
                if (e.code == KeyCode.R){
                    spaceInvaders.setInitialScreen()
                }
                if (e.code == KeyCode.Q){
                    Platform.exit()
                }
            }
        }
        return scene
    }
}