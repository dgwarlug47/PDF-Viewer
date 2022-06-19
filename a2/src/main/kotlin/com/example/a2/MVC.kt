package com.example.a2
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.PieChart.Data
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class MVC : Application() {
    private val borderPane = BorderPane()
    private val database = Database()
    var model = Model(database, this)
    var view1 = View1(model)
    var view2 = View2(model)
    private val view3 = View3(model)
    fun newCanvas(){

        model.addView(view1)
        model.addView(view2)
        model.addView(view3)

        val scrollPane = ScrollPane()

        scrollPane.content = view2

        borderPane.center = scrollPane
        borderPane.left = view1
        borderPane.top = view3
        borderPane.setOnKeyReleased {
                e ->
            run {
                println("BBBBBBB")
                println(e.code)
                if (e.code == KeyCode.ESCAPE) {
                    model.escape()
                }
                if (e.code == KeyCode.DELETE || e.code == KeyCode.BACK_SPACE){
                    println("AAAAA")
                    model.delete()
                }
            }
        }
    }
    fun loadViewModel(model: Model, view1: View1, view2: View2){
        this.model = model
        this.view1 = view1
        this.view2 = view2
        newCanvas()
    }
    override fun start(stage: Stage) {
        newCanvas()
        stage.scene = Scene(borderPane, 800.0, 800.0)
        stage.show()
    }
}