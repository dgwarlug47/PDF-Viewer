package com.example.a2
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import kotlinx.serialization.Contextual
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MVC : Application() {
    var stage: Stage? = null
    private val borderPane = BorderPane()
    var model = Model(this)
    var view1 = View1(model)
    var view2 = View2(model)
    var view3 = View3(model)
    fun initialize(){
        model = Model(this)
        view1 = View1(model)
        view2 = View2(model)
        view3 = View3(model)
    }
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
                if (e.code == KeyCode.ESCAPE) {
                    model.escape()
                }
                if (e.code == KeyCode.DELETE || e.code == KeyCode.BACK_SPACE){
                    model.delete()
                }
            }
        }
        println(stage)
        stage!!.scene = Scene(borderPane, 800.0, 800.0)
        stage!!.show()
    }
    override fun start(stage: Stage) {
        this.stage = stage
        val sha = Sha(1.0, 2.0)
        val shape = ShapePOJO(x=7.0,life=sha)
        println("shape")
        println(shape)
        val thunder = Json.encodeToString(shape)
        println("thunkder")
        println(thunder)
        val again = Json.decodeFromString<ShapePOJO>(thunder)
        println("again")
        println(again)
    }
}