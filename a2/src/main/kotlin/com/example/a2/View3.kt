package com.example.a2

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType


class View3(private val model: Model) : MenuBar(), IView{
    private val fileMenu = Menu("File")
    private val editMenu = Menu("Edit")
    private val helpMenu = Menu("Help")
    init {
        // about item
        val aboutItem = MenuItem("About")
        aboutItem.setOnAction {
            val alert = Alert(AlertType.INFORMATION)
            alert.title = ""
            alert.headerText = ""
            val s = "Sketch It\nDavi Cavalcanti Sena\ndcsena"
            alert.contentText = s
            alert.showAndWait()
        }
        helpMenu.items.add(aboutItem)

        // edit cut item
        val cutItem = MenuItem("cut")
        cutItem.setOnAction {
            model.editCut()
        }
        val copyItem = MenuItem("copy")
        copyItem.setOnAction {
            model.editCopy()
        }
        val pasteItem = MenuItem("paste")
        pasteItem.setOnAction {
            model.editPasted()
        }

        editMenu.items.addAll(cutItem, copyItem, pasteItem)

        // new item
        val newItem = MenuItem("New")
        newItem.onAction =
            EventHandler {
                val choiceDialog = ChoiceDialog("yes", "no")
                choiceDialog.headerText = "are you sure you want to leave the current drawing?"
                choiceDialog.title = ""
                val result = choiceDialog.showAndWait()
                if (result.get() == "yes"){
                    model.newCanvas()
                }
            }

        // save item
        val saveItem = MenuItem("Save")
        saveItem.setOnAction {
            val inputDialog = TextInputDialog("")
            inputDialog.headerText = "Enter drawing name"
            inputDialog.title = ""
            val result = inputDialog.showAndWait()
            model.storeModelView(result.get())
        }

        // load item
        val loadItem = MenuItem("Load")
        loadItem.setOnAction {
            val choiceDialog = TextInputDialog("")
            choiceDialog.headerText = "Enter drawing name"
            choiceDialog.title = "are you sure you want to leave without saving?"
            val result = choiceDialog.showAndWait()
            if (result.get() != ""){
                model.loadViewModel(result.get())
            }
        }

        // quit item
        val quitItem = MenuItem("Quit")
        quitItem.setOnAction {
            val choiceDialog = ChoiceDialog("yes", "no")
            choiceDialog.headerText = "are you sure you want to quit without saving?"
            choiceDialog.title = ""
            val result = choiceDialog.showAndWait()
            if (result.get() == "yes"){
                Platform.exit()
            }
        }

        fileMenu.items.addAll(newItem, loadItem, saveItem, quitItem)
        this.menus.addAll(fileMenu, editMenu, helpMenu)
    }
    override fun update() {
    }
}
