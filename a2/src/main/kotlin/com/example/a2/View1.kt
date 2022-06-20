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
import javafx.scene.shape.Line
import kotlinx.serialization.Contextual
import java.io.FileInputStream
import java.nio.file.Paths

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
    private val thicknessButton1 = ToggleButton("")
    private val thicknessButton2 = ToggleButton("")
    private val thicknessButton3 = ToggleButton("")

    private val styleButton1 = ToggleButton("")
    private val styleButton2 = ToggleButton("")
    private val styleButton3 = ToggleButton("")

    override fun update() {
        if (model.selectedShape != null){
            this.fillColorPicker.value = this.model.getPickedFillColor()
            this.lineColorPicker.value = this.model.getPickedLineColor()
        }
        if (model.getPickedThickness() == Thickness.Type1){
            this.thicknessButton1.isSelected = true
        }
        if (model.getPickedThickness() == Thickness.Type2){
            this.thicknessButton2.isSelected = true
        }
        if (model.getPickedThickness() == Thickness.Type3){
            this.thicknessButton3.isSelected = true
        }
        if (model.getPickedStyle() == Style.Type1) {
            this.styleButton1.isSelected = true
        }
        if (model.getPickedStyle() == Style.Type2){
            this.styleButton2.isSelected = true
        }
        if (model.getPickedStyle() == Style.Type3){
            this.styleButton3.isSelected = true
        }
        (thicknessButton1.graphic as Line).stroke = model.getPickedLineColor()
        (thicknessButton2.graphic as Line).stroke = model.getPickedLineColor()
        (thicknessButton3.graphic as Line).stroke = model.getPickedLineColor()
        (styleButton1.graphic as Line).stroke = model.getPickedLineColor()
        (styleButton2.graphic as Line).stroke = model.getPickedLineColor()
        (styleButton3.graphic as Line).stroke = model.getPickedLineColor()

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
        //hbox4.padding = Insets(5.0)

        val hbox5 = HBox()
        hbox5.spacing = 5.0
        //hbox5.padding = Insets(5.0)

        val fillColorVBox = VBox()
        fillColorVBox.spacing = 5.0
        fillColorVBox.padding = Insets(5.0)

        val lineColorVBox = VBox()
        lineColorVBox.spacing = 5.0
        lineColorVBox.padding = Insets(5.0)

        val thicknessVbox = VBox()
        thicknessVbox.spacing = 5.0
        thicknessVbox.padding = Insets(5.0)

        val styleVbox = VBox()
        styleVbox.spacing = 5.0
        styleVbox.padding = Insets(5.0)

        vbox.children.add(hbox1)
        vbox.children.add(hbox2)
        vbox.children.add(hbox3)
        vbox.children.add(fillColorVBox)
        vbox.children.add(lineColorVBox)
        vbox.children.add(thicknessVbox)
        vbox.children.add(styleVbox)


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
            model.setPickedFillColor(fillColorPicker.value)
        }

        // lineColorPicker
        val lineColorText = Label("Line Color")
        lineColorVBox.children.add(lineColorText)

        lineColorPicker.value = model.defaultColor
        lineColorVBox.children.add(lineColorPicker)
        lineColorPicker.setOnAction {
            model.setPickedLineColor(lineColorPicker.value)
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
        thicknessVbox.children.add(Label("Thickness"))
        thicknessVbox.children.add(hbox4)

        // thickness 1
        hbox4.children.add(thicknessButton1)
        thicknessButton1.setOnAction {
            model.setPickedThickness(Thickness.Type1)
        }
        val line1 = Line(20.0, 20.0, 20.0, 58.0)
        line1.stroke = model.defaultColor
        line1.strokeWidth = Thickness.Type1.getStyle(Thickness.Type1)
        thicknessButton1.graphic = line1

        // thickness 2
        hbox4.children.add(thicknessButton2)
        thicknessButton2.setOnAction {
            model.setPickedThickness(Thickness.Type2)
        }
        val line2 = Line(20.0, 20.0, 20.0, 54.0)
        line2.stroke = model.defaultColor
        line2.strokeWidth = Thickness.Type1.getStyle(Thickness.Type2)
        thicknessButton2.graphic = line2

        // thickness 3
        hbox4.children.add(thicknessButton3)
        thicknessButton3.setOnAction {
            model.setPickedThickness(Thickness.Type3)
        }

        val line3 = Line(20.0, 20.0, 20.0, 50.0)
        line3.stroke = model.defaultColor
        line3.strokeWidth = Thickness.Type1.getStyle(Thickness.Type3)
        thicknessButton3.graphic = line3

        // style
        styleVbox.children.add(Label("Style"))
        styleVbox.children.add(hbox5)

        // style 1
        hbox5.children.add(styleButton1)
        styleButton1.setOnAction {
            model.setPickedStyle(Style.Type1)
        }

        val styleLine1 = Line(20.0, 20.0, 20.0, 120.0)
        styleLine1.stroke = model.defaultColor
        styleLine1.strokeWidth = Thickness.Type1.getStyle(Thickness.Type2)
        styleLine1.strokeDashArray.addAll(model.createDashedArrayBasedOnStyle(Style.Type1))
        styleButton1.graphic = styleLine1

        // style 2
        hbox5.children.add(styleButton2)
        styleButton2.setOnAction {
            model.setPickedStyle(Style.Type2)
        }

        val styleLine2 = Line(20.0, 20.0, 20.0, 120.0)
        styleLine2.stroke = model.defaultColor
        styleLine2.strokeWidth = Thickness.Type1.getStyle(Thickness.Type2)
        styleLine2.strokeDashArray.addAll(model.createDashedArrayBasedOnStyle(Style.Type2))
        styleButton2.graphic = styleLine2

        // style 3
        hbox5.children.add(styleButton3)
        styleButton3.setOnAction {
            model.setPickedStyle(Style.Type3)
        }

        val styleLine3 = Line(20.0, 20.0, 20.0, 120.0)
        styleLine3.stroke = model.defaultColor
        styleLine3.strokeWidth = Thickness.Type1.getStyle(Thickness.Type2)
        styleLine3.strokeDashArray.addAll(model.createDashedArrayBasedOnStyle(Style.Type3))
        styleButton3.graphic = styleLine3
    }
}