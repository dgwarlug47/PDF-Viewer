package com.example.a2

import javafx.event.EventHandler
import javafx.scene.control.*

class View3(private val model: Model) : MenuBar(), IView{
    private val fileMenu = Menu("File")
    private val aboutMenu = Menu("About")
    init {
        val newItem = MenuItem("New")
        newItem.onAction =
            EventHandler {
                val choiceDialog = ChoiceDialog("yes", "no")
                choiceDialog.headerText = "are you sure you want to delete the current drawing?"
                choiceDialog.title = ""
                val result = choiceDialog.showAndWait()
                if (result.get() == "yes"){
                    model.newCanvas()
                }
            }
        val loadItem = MenuItem("Load")
        loadItem.setOnAction {

        }
        val saveItem = MenuItem("Save")
        saveItem.setOnAction {
            val inputDialog = TextInputDialog("")
            inputDialog.headerText = "Enter drawing name"
            inputDialog.title = ""
            val result = inputDialog.showAndWait()
            if(model.getDrawingNames().contains(result.get())){
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Warning"
                alert.contentText = "The drawing name that you selected is already being used"
                alert.showAndWait()
            }
            else{
                model.storeSelf(result.get())
            }
        }
        loadItem.setOnAction {
            val choiceDialog = ChoiceDialog("messi")
            for (drawingName in model.getDrawingNames()){
                choiceDialog.items.add(drawingName)
            }
            choiceDialog.headerText = "are you sure you want to delete the current drawing?"
            choiceDialog.title = ""
            val result = choiceDialog.showAndWait()
            model.loadViewModel(result.get())
        }
        fileMenu.items.addAll(newItem, loadItem, saveItem)
        this.menus.addAll(fileMenu, aboutMenu)
    }
    override fun update() {
    }
}
