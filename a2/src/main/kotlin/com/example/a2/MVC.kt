package com.example.a2
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class MVC : Application() {
    override fun start(stage: Stage) {
        val borderPane = BorderPane()

        val model = Model()
        val view1 = View1(model)
        val view2 = View2(model)
        model.addView(view1)
        model.addView(view2)

        val scrollPane = ScrollPane()

        scrollPane.content = view2

        borderPane.center = scrollPane
        borderPane.left = view1
        stage.scene = Scene(borderPane, 800.0, 800.0)
        stage.show()
    }
}