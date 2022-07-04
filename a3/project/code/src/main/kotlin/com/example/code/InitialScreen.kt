package com.example.code

import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import java.io.File

class InitialScreen(private val spaceInvaders: SpaceInvaders) : BorderPane(){
    private val logo = Rectangle(0.0, 0.0, 491.0, 211.0)
    private val vBox = VBox()
    private val vBox2 = VBox()
    private val instructions = Text("Instructions")
    private val label0 = Label("Student Name - Davi Cavalcanti Sena")
    private val labelk = Label("Student Number - 20800126")
    private val label1 = Label("ENTER - Start Game")
    private val label2 = Label("A or D move ship left or right")
    private val label3 = Label("SPACE - Fire")
    private val label4 = Label("Q - Quit Game")
    private val label5 = Label("1 or 2 or 3 - Start Game at a specific level")

    init {

        instructions.font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 50.0)
        instructions.style = "-fx-font-weight: bold"
        this.prefWidth = 550.0
        this.prefHeight = 550.0

        vBox.spacing = 50.0
        vBox.alignment = Pos.CENTER

        val path = "space-invaders-assets/images/logo.png"
        logo.fill = ImagePattern(Image(File(path).toURI().toString()))

        vBox2.alignment = Pos.CENTER
        vBox2.spacing = 10.0

        vBox.children.add(logo)
        vBox.children.add(vBox2)

        vBox2.children.add(instructions)
        vBox2.children.add(label0)
        vBox2.children.add(labelk)
        vBox2.children.add(label1)
        vBox2.children.add(label2)
        vBox2.children.add(label3)
        vBox2.children.add(label4)
        vBox2.children.add(label5)

        this.center = vBox

        this.setOnKeyPressed {
            e ->
            run{
                if (e.code == KeyCode.ENTER){
                    spaceInvaders.setGameScreen(0, 1)
                }
                if (e.code == KeyCode.DIGIT1){
                    spaceInvaders.setGameScreen(0, 1)
                }
                if (e.code == KeyCode.DIGIT2){
                    spaceInvaders.setGameScreen(0, 2)
                }
                if (e.code == KeyCode.DIGIT3){
                    spaceInvaders.setGameScreen(0,3)
                }
                if (e.code == KeyCode.Q){
                    Platform.exit()
                }
            }
        }
    }

    fun start(): Scene {
        val scene = Scene(this)

        scene.setOnKeyPressed {
                e ->
            run{
                if (e.code == KeyCode.ENTER){
                    spaceInvaders.setGameScreen(0, 1)
                }
                if (e.code == KeyCode.DIGIT1){
                    spaceInvaders.setGameScreen(0, 1)
                }
                if (e.code == KeyCode.DIGIT2){
                    spaceInvaders.setGameScreen(0, 2)
                }
                if (e.code == KeyCode.DIGIT3){
                    spaceInvaders.setGameScreen(0,3)
                }
                if (e.code == KeyCode.Q){
                    Platform.exit()
                }
            }
        }
        return scene
    }
}