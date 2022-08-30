package com.example.a1

import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files

class FileToggleButton(applicationStatus: ApplicationStatus, var file: File, var toggleFilesGroup: ToggleGroup, private val bottomLabel: Label, private val filesNameVBox : VBox,
                       private val borderPane: BorderPane){
    val fileButton = ToggleButton(file.name)
    init {
        fileButton.toggleGroup = toggleFilesGroup
        filesNameVBox.children.add(fileButton)

        if (!applicationStatus.firstButtonSelected) {
            fileButton.isSelected = true
            bottomLabel.text = file.canonicalPath
            applicationStatus.firstButtonSelected = true
            this.fileKeyReleased(bottomLabel, borderPane)
        }

        fileButton.setOnKeyReleased {
            this.fileKeyReleased(bottomLabel, borderPane)
        }
        fileButton.setOnMouseClicked {
            this.fileKeyReleased(bottomLabel, borderPane)
        }
    }
    private fun fileKeyReleased(bottomLabel: Label, borderPane: BorderPane) {
        val centerScrollPane = ScrollPane()
        borderPane.center = centerScrollPane

        if (!Files.isReadable(file.toPath())) {
            centerScrollPane.content = Label("File cannot be read")
        }
        else {
            var foundAppropriateExtension = false
            bottomLabel.text = file.canonicalPath
            for (imageExtension in listOf("jpeg", "png", "jpg", "bmp"))
                if (file.name.endsWith(imageExtension)) {
                    foundAppropriateExtension = true
                    println("complete url" + file.absolutePath)
                    val image = ImageView(Image(FileInputStream(file.absolutePath)))
                    image.isPreserveRatio = true
                    val pane = Pane()
                    image.fitWidthProperty().bind(pane.widthProperty())
                    image.fitHeightProperty().bind(pane.heightProperty())
                    pane.children.add(image)
                    borderPane.center = pane
                }

            for (textExtension in listOf("md", "txt"))
                if (file.name.endsWith(textExtension)) {
                    foundAppropriateExtension = true
                    println("complete url" + file.absolutePath)
                    val text = Text(file.readText())
                    text.wrappingWidthProperty().bind(borderPane.widthProperty())
                    val label = Label(text.text)
                    label.prefHeightProperty().bind(borderPane.heightProperty())
                    label.prefWidthProperty().bind(borderPane.widthProperty())
                    centerScrollPane.content = text
                }

            if (!foundAppropriateExtension) {
                centerScrollPane.content = (Label("Unsupported Type"))
            }
        }
    }
}
