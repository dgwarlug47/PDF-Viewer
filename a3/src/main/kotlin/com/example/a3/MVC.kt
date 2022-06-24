package com.example.a2
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import kotlinx.serialization.Contextual

class MVC : Application() {
    var stage: Stage? = null
    private val borderPane = BorderPane()
    fun newCanvas(){
        stage!!.scene = Scene(borderPane, 800.0, 800.0)
        stage!!.show()
    }
    override fun start(stage: Stage) {
        this.stage = stage
        newCanvas()
    }
}