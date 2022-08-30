package com.example.a1

import javafx.scene.control.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun deleteDirectory(directory: File) {
    // if the file is directory or not
    if (directory.isDirectory) {
        val files = directory.listFiles();
        // if the directory contains any file
        if (files != null) {
            for (file in files) {
                // recursive call if the subdirectory is non-empty
                deleteDirectory(file);
            }
        }
    }

    if (!directory.delete()) {
        println("Directory not deleted");
    }
}

class FileEventHandler {
    /*
    Given a toggleGroup, and a buttonTextToFile, show a dialog for the user to rename
    the file that was selected on the toggleGroup.
     */
    fun rename(toggleGroup: ToggleGroup, buttonTextToFile: MutableMap<String, File>) {
        val selectedToggle: Toggle? = toggleGroup.selectedToggle
        if (selectedToggle != null && selectedToggle is ToggleButton) {
            val inputDialog = TextInputDialog("")
            inputDialog.headerText = "Enter new file name"
            inputDialog.title = ""
            val result = inputDialog.showAndWait()
            println("result " + result.get())
            if (result.isPresent) {
                val oldFile = buttonTextToFile[selectedToggle.text]
                val newFileName = (oldFile?.parentFile?.absolutePath ?: "") + "/" + result.get()
                println("newFileName $newFileName")

                val validPath = "" != result.get() && !(Files.exists(Paths.get(newFileName)))
                if (validPath) {
                    try {
                        val newFile = File(newFileName)
                        println(newFile.absolutePath)
                        selectedToggle.text = result.get()
                        val success = oldFile?.renameTo(newFile)
                        if (!success!!){
                            val alert = Alert(Alert.AlertType.ERROR)
                            alert.title = "Warning"
                            alert.contentText = "The new file name you added is invalid"
                            alert.showAndWait()
                            return
                        }
                        return
                    } catch (_: Exception) {
                    } catch (_: java.lang.RuntimeException) {
                    }
                }
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Warning"
                alert.contentText = "The new file name you added is invalid"
                alert.showAndWait()
            }
        }
    }

    /*
    Given a toggleGroup, and a buttonTextToFile, show a dialog for the user to move
    the file that was selected on the toggleGroup.
     */
    fun move(toggleGroup: ToggleGroup, buttonTextToFile: MutableMap<String, File>) {
        val selectedToggle: Toggle? = toggleGroup.selectedToggle
        if (selectedToggle != null && selectedToggle is ToggleButton) {
            val oldFilePath = buttonTextToFile[selectedToggle.text]?.parentFile?.canonicalPath
            val oldFileName = buttonTextToFile[selectedToggle.text]?.name
            val inputDialog = TextInputDialog(oldFilePath)
            inputDialog.headerText = "Enter new file location"
            inputDialog.title = ""
            val result = inputDialog.showAndWait()
            if (result.isPresent) {
                val parentPath = Paths.get(result.get())
                val newFileName = result.get() + '/' + oldFileName
                val newFilePath = Paths.get(newFileName)
                val validPath = Files.exists(parentPath) && !Files.exists(newFilePath)
                try {
                    if (validPath) {
                        val oldFile = buttonTextToFile[selectedToggle.text]
                        val newFile = File(newFileName)
                        val success = oldFile?.renameTo(newFile)
                        if (!success!!){
                            val alert = Alert(Alert.AlertType.ERROR)
                            alert.title = "Warning"
                            alert.contentText = "The new path location you added is invalid"
                            alert.showAndWait()
                        }
                        return
                    }
                } catch (_: Exception) {
                } catch (_: java.lang.RuntimeException) {
                }
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Warning"
                alert.contentText = "The new path location you added is invalid"
                alert.showAndWait()
            }
        }
    }

    /*
    Given a toggleGroup, and a buttonTextToFile, show a dialog for the user to delete
    the file that was selected on the toggleGroup.
     */
    fun delete(toggleFilesGroup: ToggleGroup, buttonTextToFile: MutableMap<String, File>) {
        val selectedToggle: Toggle? = toggleFilesGroup.selectedToggle
        if (selectedToggle != null && selectedToggle is ToggleButton) {
            val choiceDialog = ChoiceDialog("yes", "no")
            choiceDialog.headerText = "are you sure you want to delete the file?"
            choiceDialog.title = ""
            val result = choiceDialog.showAndWait()
            if (result.get() == "no") {
                return
            } else {
                val file = buttonTextToFile[selectedToggle.text]
                if (file?.isDirectory == true) {
                    deleteDirectory(file)
                } else {
                    file?.delete()
                }
            }
        }
    }
}