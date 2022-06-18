package com.example.a1

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.stage.Stage
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class HelloApplication : Application() {
    private fun filesPathExtractors(pathCurrentDirectory: Path, showHiddenFiles: Boolean): Stream<Path> {
        //List all items in the directory. Note that we are using Java 8 streaming API to group the entries by
        //directory and files

        val streamPathFiles: Stream<Path> = if (!showHiddenFiles) {
            Files.list(pathCurrentDirectory)
                .filter { it -> !Files.isDirectory(it) && !it.toFile().name.startsWith('.') }
        } else{
            Files.list(pathCurrentDirectory)
                .filter { it -> !Files.isDirectory(it)}
        }
        return streamPathFiles
    }

    private fun directoriesPathsExtractor(pathCurrentDirectory: Path, showHiddenFiles: Boolean): Stream<Path> {
        //List all items in the directory. Note that we are using Java 8 streaming API to group the entries by
        //directory and files

        val streamPathDirectories: Stream<Path> = if (!showHiddenFiles) {
            Files.list(pathCurrentDirectory)
                .filter { it -> Files.isDirectory(it) && !it.toFile().name.startsWith('.') }
        } else{
            Files.list(pathCurrentDirectory)
                .filter { it -> Files.isDirectory(it)}
        }
        return streamPathDirectories
    }

    fun sceneGenerator(currentDirectoryName: String, applicationStatus: ApplicationStatus) {
        applicationStatus.firstButtonSelected = false
        val pathCurrentDirectory = Paths.get(currentDirectoryName)
        val currentDirectory = pathCurrentDirectory.toFile()
        val buttonTextToFile : MutableMap<String, File> = mutableMapOf()

        // Check if the Path is a directory
        if (! Files.isDirectory(pathCurrentDirectory)) {
            return
        }

        // core widgets initialization
        val border = BorderPane()
        val scene = Scene(border, 800.0, 600.0)

        // files name in the left region
        val filesNameVBox = VBox()

        // scroll panes
        val leftScrollPane = ScrollPane()
        leftScrollPane.content = filesNameVBox

        // Toggle Group
        val toggleFilesGroup = ToggleGroup()

        // CREATE WIDGETS TO DISPLAY
        // menubar
        val fileMenu = Menu("File")
        val editMenu = Menu("Edit")
        val viewMenu = Menu("View")
        val actionMenu = Menu("Action")
        val optionsMenu = Menu("Options")

        // file Menu
        val menuNew = MenuItem("New")
        val menuOpen = MenuItem("Open")
        val menuClose = MenuItem("Close")
        val menuQuit = MenuItem("Quit")
        menuQuit.setOnAction { Platform.exit() }

        val menuNextItem = MenuItem("Next")
        menuNextItem.onAction =
            EventHandler {
                val selectedToggle = toggleFilesGroup.selectedToggle
                if (selectedToggle is ToggleButton){
                    buttonTextToFile[selectedToggle.text]?.absolutePath?.let { it1 -> sceneGenerator(it1, applicationStatus) }
                }
            }

        val menuPrevItem = MenuItem("Prev")
        menuPrevItem.onAction =
            EventHandler{
                if (null != currentDirectory.parent &&
                    Paths.get(applicationStatus.homeDir).toFile().canonicalPath != currentDirectory.canonicalPath) {
                    sceneGenerator(currentDirectory.canonicalFile.parent, applicationStatus)
                }
            }

        val menuHomeItem = MenuItem("Home")
        menuHomeItem.onAction =
            EventHandler {
                sceneGenerator(applicationStatus.homeDir, applicationStatus)
            }
        fileMenu.items.addAll(menuNextItem, menuPrevItem, menuHomeItem)

        // action menu
        val menuRenameItem = MenuItem("Rename")
        menuRenameItem.onAction =
            EventHandler {
                FileEventHandler().rename(toggleFilesGroup, buttonTextToFile)
                sceneGenerator(currentDirectoryName, applicationStatus)
            }

        val menuMove = MenuItem("Move")
        menuMove.onAction =
            EventHandler {
                FileEventHandler().move(toggleFilesGroup, buttonTextToFile)
                sceneGenerator(currentDirectoryName, applicationStatus)
            }

        val menuDelete = MenuItem("Delete")
        menuDelete.onAction =
            EventHandler {
                FileEventHandler().delete(toggleFilesGroup, buttonTextToFile)
                sceneGenerator(currentDirectoryName, applicationStatus)
            }
        actionMenu.items.addAll(menuRenameItem, menuMove, menuDelete)

        // options menu
        val menuShowHiddenFileItem = MenuItem("")
        val menuDoNotShowHiddenFileItem = MenuItem("")

        val toggleGroupShow = ToggleGroup()
        val showHiddenFilesToggleButton = ToggleButton("show hidden files")
        showHiddenFilesToggleButton.setOnAction {
            applicationStatus.showHiddenFiles = true
            sceneGenerator(currentDirectoryName, applicationStatus)
        }
        showHiddenFilesToggleButton.toggleGroup = toggleGroupShow

        val doNotShowHiddenFilesToggleButton = ToggleButton("don't show hidden files")
        doNotShowHiddenFilesToggleButton.setOnAction {
            applicationStatus.showHiddenFiles = false
            sceneGenerator(currentDirectoryName, applicationStatus)
        }
        doNotShowHiddenFilesToggleButton.toggleGroup = toggleGroupShow

        menuShowHiddenFileItem.graphic = showHiddenFilesToggleButton
        menuDoNotShowHiddenFileItem.graphic = doNotShowHiddenFilesToggleButton

        if (applicationStatus.showHiddenFiles) {
            showHiddenFilesToggleButton.isSelected = true
        }
        else{
            doNotShowHiddenFilesToggleButton.isSelected = true
        }
        optionsMenu.items.addAll(menuShowHiddenFileItem, menuDoNotShowHiddenFileItem)

        // menu bar
        val menuBar = MenuBar()
        menuBar.menus.addAll(fileMenu, editMenu, viewMenu, actionMenu, optionsMenu)

        // toolbar
        val toolbar = ToolBar()

        // home button
        val homeButton = Button("Home")
        homeButton.setOnAction {sceneGenerator(applicationStatus.homeDir, applicationStatus)}

        // prev button
        val prevButton = Button("Prev")
        prevButton.setOnAction {
            if (null != currentDirectory.parent &&
                Paths.get(applicationStatus.homeDir).toFile().canonicalPath != currentDirectory.canonicalPath) {
                sceneGenerator(currentDirectory.canonicalFile.parent, applicationStatus)
            }
        }

        // next button
        val nextButton = Button("Next")
        nextButton.setOnAction {
            val selectedToggle = toggleFilesGroup.selectedToggle
            if (selectedToggle is ToggleButton){
                buttonTextToFile[selectedToggle.text]?.absolutePath?.let { it1 -> sceneGenerator(it1, applicationStatus) }
            }
        }

        // delete button
        val deleteButton = Button("Delete")
        deleteButton.setOnAction {
            FileEventHandler().delete(toggleFilesGroup, buttonTextToFile)
            sceneGenerator(currentDirectoryName, applicationStatus)
        }

        // move button
        val moveButton = Button("Move")
        moveButton.setOnAction {
            FileEventHandler().move(toggleFilesGroup, buttonTextToFile)
            sceneGenerator(currentDirectoryName, applicationStatus)
        }

        // rename button
        val renameButton = Button("Rename")
        renameButton.setOnAction {
            FileEventHandler().rename(toggleFilesGroup, buttonTextToFile)
            sceneGenerator(currentDirectoryName, applicationStatus)
        }

        // toolbar items
        toolbar.items.addAll(homeButton, prevButton, nextButton, renameButton, moveButton, deleteButton)

        // stack menu and toolbar in the top region
        val menuAndToolBarVBox = VBox(menuBar, toolbar)

        // border keys actions
        border.setOnKeyPressed {
                event ->
            run {
                if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                    if (null != currentDirectory.parent &&
                        Paths.get(applicationStatus.homeDir).toFile().canonicalPath != currentDirectory.canonicalPath) {
                        sceneGenerator(currentDirectory.canonicalFile.parent, applicationStatus)
                    }
                }
            }
        }

        // status line
        val bottomLabel = Label(pathCurrentDirectory.toFile().canonicalPath)

        // SETUP LAYOUT
        border.top = menuAndToolBarVBox
        border.left = leftScrollPane
        border.bottom = bottomLabel

        // create toggle button for directory files
        val streamPathDirectories = directoriesPathsExtractor(pathCurrentDirectory, applicationStatus.showHiddenFiles)
        for (streamPathDirectory in streamPathDirectories) {
            val file = streamPathDirectory.toFile()
            val directoryButton = DirectoryToggleButton(applicationStatus, file, toggleFilesGroup,
            border, filesNameVBox, bottomLabel)
            buttonTextToFile[directoryButton.directoryButton.text] = file
        }

        // create toggle button for all files that are not directories
        val streamPathFiles = filesPathExtractors(pathCurrentDirectory, applicationStatus.showHiddenFiles)
        for (streamPathFile in streamPathFiles) {
            val file = streamPathFile.toFile()
            val fileButton = FileToggleButton(applicationStatus, file, toggleFilesGroup, bottomLabel, filesNameVBox, border)
            buttonTextToFile[fileButton.fileButton.text] = file
        }

        // CREATE AND SHOW SCENE
        applicationStatus.stage.scene = scene
        applicationStatus.stage.title = "File Browser"
        applicationStatus.stage.show()
    }
    override fun start(stage: Stage) {
        val applicationStatus = ApplicationStatus(stage)
        sceneGenerator(applicationStatus.homeDir, applicationStatus)
    }
}