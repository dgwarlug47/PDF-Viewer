package com.example.a1

import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import java.io.File
import javafx.scene.layout.VBox

class DirectoryToggleButton(
    private val applicationStatus: ApplicationStatus, var file: File, toggleFilesGroup: ToggleGroup,
    borderPane: BorderPane, filesNameVBox: VBox, bottomLabel: Label){
    val directoryButton = ToggleButton(file.name + "/")

    init{
        filesNameVBox.children.add(directoryButton)
        if (!applicationStatus.firstButtonSelected){
            directoryButton.isSelected = true
            bottomLabel.text = file.canonicalPath
            applicationStatus.firstButtonSelected = true
        }
        directoryButton.toggleGroup = toggleFilesGroup
        directoryButton.selectedProperty()
        eventsDefinition(borderPane, bottomLabel)
    }

    private fun eventsDefinition(borderPane: BorderPane, bottomLabel: Label){
        directoryButton.setOnKeyPressed {
                event ->
            run {
                if (event.code == KeyCode.ENTER) {
                    HelloApplication().sceneGenerator(file.canonicalPath, this.applicationStatus)
                }
            }
        }
        directoryButton.setOnKeyReleased {
            borderPane.center = Pane()
            bottomLabel.text = file.canonicalPath
        }
        directoryButton.setOnMouseClicked {
                event ->
            run{
                borderPane.center = Pane()
                bottomLabel.text = file.canonicalPath
                if(event.button.equals(MouseButton.PRIMARY)){
                    if(event.clickCount == 2){
                        println("Double clicked")
                        HelloApplication().sceneGenerator(file.canonicalPath, applicationStatus)
                    }
                }
            }
        }
    }
}