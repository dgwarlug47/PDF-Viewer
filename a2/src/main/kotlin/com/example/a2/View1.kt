package com.example.a2

import javafx.geometry.Insets
import javafx.scene.control.ColorPicker
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.transform.Affine
import java.io.FileInputStream
import java.nio.file.Paths
import java.security.KeyStore.TrustedCertificateEntry


fun arrow(): javafx.scene.shape.Polygon{
    val headX = 20.0
    val headY = 0.0
    val height = 20.0
    val width = 15
    val factor = 3
    val points1 = javafx.scene.shape.Polygon(
        headX, headY,
        headX-width, headY+height,
        headX-width/factor, headY+height,
        headX-width/factor, headY+height+height,
        headX+width/factor, headY+height+height,
        headX+width/factor, headY+height,
        headX+width, headY+height
    )

    val rot = Affine.rotate(10.0, 0.0, 0.0)
    points1.transforms.add(rot)
    return points1
}

fun fileStuff(currentDirectoryName: String): ImageView {
    val file = Paths.get(currentDirectoryName).toFile()
    return ImageView(Image(FileInputStream(file.absolutePath)))
}

class View1(private val model: Model): IView, StackPane(){
    private val lineToolButton = ToggleButton()
    private val selectionToolButton = ToggleButton()
    private val eraseToolButton = ToggleButton()
    private val rectangleToolButton = ToggleButton()
    private val circleToolButton = ToggleButton()
    private val fillToolButton = ToggleButton()

    private val fillColorPicker = ColorPicker()
    private val lineColorPicker = ColorPicker()

    private val thicknessButton1 = ToggleButton("t1")
    private val thicknessButton2 = ToggleButton("t2")
    private val thicknessButton3 = ToggleButton("t3")

    private val styleButton1 = ToggleButton("s1")
    private val styleButton2 = ToggleButton("s2")
    private val styleButton3 = ToggleButton("s3")

    override fun update() {
        if (model.selectedShape != null){
            println("doing the view1 update")
            this.fillColorPicker.value = this.model.getSelectedFillColor()
            this.lineColorPicker.value = this.model.getSelectedLineColor()
        }
        if (model.getSelectedStyle() == Style.Type1) {
            this.styleButton1.isSelected = true
        }
        if (model.getSelectedStyle() == Style.Type2){
            this.styleButton2.isSelected = true
        }
        if (model.getSelectedStyle() == Style.Type3){
            this.styleButton3.isSelected = true
        }
        if (model.getSelectedStyle() == Style.Type1) {
            this.styleButton1.isSelected = true
        }
        if (model.getSelectedStyle() == Style.Type2){
            this.styleButton2.isSelected = true
        }
        if (model.getSelectedStyle() == Style.Type3){
            this.styleButton3.isSelected = true
        }
    }

    init {
        fillColorPicker.prefWidth = 80.0
        lineColorPicker.prefWidth = 80.0

        val toolsToggleGroup = ToggleGroup()
        lineToolButton.toggleGroup = toolsToggleGroup
        selectionToolButton.toggleGroup = toolsToggleGroup
        eraseToolButton.toggleGroup = toolsToggleGroup
        rectangleToolButton.toggleGroup = toolsToggleGroup
        circleToolButton.toggleGroup = toolsToggleGroup

        val thicknessToggleGroup = ToggleGroup()
        thicknessButton1.toggleGroup = thicknessToggleGroup
        thicknessButton2.toggleGroup = thicknessToggleGroup
        thicknessButton3.toggleGroup = thicknessToggleGroup

        val styleToggleGroup = ToggleGroup()
        styleButton1.toggleGroup= styleToggleGroup
        styleButton2.toggleGroup = styleToggleGroup
        styleButton3.toggleGroup = styleToggleGroup

        val vbox = VBox()
        this.children.add(vbox)

        val hbox1 = HBox()
        hbox1.spacing = 5.0
        hbox1.padding = Insets(5.0)
        val hbox2 = HBox()
        hbox2.spacing = 5.0
        hbox2.padding = Insets(5.0)
        val hbox3 = HBox()
        hbox3.spacing = 5.0
        hbox3.padding = Insets(5.0)
        val hbox4 = HBox()
        hbox4.spacing = 5.0
        hbox4.padding = Insets(5.0)
        val hbox5 = HBox()
        hbox5.spacing = 5.0
        hbox5.padding = Insets(5.0)

        val fillColorVBox = VBox()
        fillColorVBox.spacing = 5.0
        fillColorVBox.padding = Insets(5.0)

        val lineColorVBox = VBox()
        lineColorVBox.spacing = 5.0
        lineColorVBox.padding = Insets(5.0)

        vbox.children.add(hbox1)
        vbox.children.add(hbox2)
        vbox.children.add(hbox3)
        vbox.children.add(fillColorVBox)
        vbox.children.add(lineColorVBox)
        vbox.children.add(hbox4)
        vbox.children.add(hbox5)


        // selectionTollButton
        selectionToolButton.graphic = fileStuff("resources/icons8-cursor-20.png")
        hbox1.children.add(selectionToolButton)

        selectionToolButton.setOnAction {
            model.setSelectedTool(Tools.SelectionTool)
        }

        // eraseToolButton
        hbox1.children.add(eraseToolButton)
        eraseToolButton.graphic = fileStuff("resources/icons8-eraser-20.png")
        eraseToolButton.setOnAction { model.setSelectedTool(Tools.EraseTool)}

        // circleToolButton
        circleToolButton.graphic = fileStuff("resources/icons8-circle-20.png")
        hbox2.children.add(circleToolButton)
        circleToolButton.setOnAction {
            model.setSelectedTool(Tools.CircleTool)
        }

        // rectangleTool Button
        hbox2.children.add(rectangleToolButton)
        rectangleToolButton.graphic = fileStuff("resources/icons8-rectangle-20.png")
        rectangleToolButton.setOnAction {
            model.setSelectedTool(Tools.RectangleTool)
        }

        // fillColorPicker
        val fillColorText = Label("Fill Color")
        fillColorVBox.children.add(fillColorText)

        fillColorPicker.value = model.defaultColor
        fillColorVBox.children.add(fillColorPicker)
        fillColorPicker.setOnAction {
            model.setSelectedFillColor(fillColorPicker.value)
        }

        // lineColorPicker
        val lineColorText = Label("Line Color")
        lineColorVBox.children.add(lineColorText)

        lineColorPicker.value = model.defaultColor
        lineColorVBox.children.add(lineColorPicker)
        lineColorPicker.setOnAction {
            model.setSelectedLineColor(lineColorPicker.value)
        }

        // lineTool Button
        hbox3.children.add(lineToolButton)
        lineToolButton.graphic = fileStuff("resources/icons8-line-20.png")
        lineToolButton.setOnAction {
            model.setSelectedTool(Tools.LineTool)
        }

        // fillTool Button
        hbox3.children.add(fillToolButton)
        fillToolButton.graphic = fileStuff("resources/icons8-bucket-20.png")
        fillToolButton.setOnAction {
            model.setSelectedTool(Tools.FillTool)
        }

        // thickness
        hbox4.children.add(thicknessButton1)
        thicknessButton1.setOnAction {
            model.setSelectedThickness(Thickness.Type1)
        }
        hbox4.children.add(thicknessButton2)
        thicknessButton2.setOnAction {
            model.setSelectedThickness(Thickness.Type2)
        }
        hbox4.children.add(thicknessButton3)
        thicknessButton3.setOnAction {
            model.setSelectedThickness(Thickness.Type3)
        }

        // style
        hbox5.children.add(styleButton1)
        styleButton1.setOnAction {
            model.setSelectedStyle(Style.Type1)
        }

        hbox5.children.add(styleButton2)
        styleButton2.setOnAction {
            model.setSelectedStyle(Style.Type2)
        }

        hbox5.children.add(styleButton3)
        styleButton3.setOnAction {
            model.setSelectedStyle(Style.Type3)
        }
    }
}