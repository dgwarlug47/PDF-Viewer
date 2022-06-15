package com.example.a2

import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.control.ColorPicker
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import kotlin.math.PI
import kotlin.math.sqrt


fun radians(degrees: Double): Double {
    return degrees * (PI / 180.0)
}

fun distance(x1: Double, x2: Double, y1:Double, y2:Double): Double{
    return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
}

fun circle(): Canvas {
    val canvas = Canvas(40.0, 50.0)
    val gc = canvas.graphicsContext2D
    gc.fill = Color.CHOCOLATE
    return canvas
}

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

class View1(val model: Model): IView, StackPane(){
    val fillColorPicker = ColorPicker()
    val lineColorPicker = ColorPicker()
    val lineToolButton = ToggleButton("line")
    val selectionToolButton = ToggleButton()
    val eraseToolButton = ToggleButton("erase")
    val rectangleToolButton = ToggleButton("rectangle")
    val circleToolButton = ToggleButton()

    override fun update() {
        if (model.selectedShape != null){
            this.fillColorPicker.value = this.model.getSelectedFillColor()
            this.lineColorPicker.value = this.model.getSelectedFillColor()
        }
    }

    init {
        val vbox = VBox()
        this.children.add(vbox)
        val hbox1 = HBox()
        val hbox2 = HBox()
        val hbox3 = HBox()

        val fillColorVBox = VBox()
        val lineColorVBox = VBox()

        vbox.children.add(hbox1)
        vbox.children.add(hbox2)
        vbox.children.add(hbox3)
        vbox.children.add(fillColorVBox)
        vbox.children.add(lineColorVBox)

        eraseToolButton.setOnAction { model.setSelectedTool(Tools.EraseTool)}

        val arrowPolygon = arrow()
        arrowPolygon.onMouseClicked = EventHandler {
            model.setSelectedTool(Tools.SelectionTool)
        }

        selectionToolButton.graphic = arrowPolygon
        hbox1.children.add(selectionToolButton)

        selectionToolButton.addEventFilter(MouseEvent.MOUSE_MOVED) { e ->
            val centerHeight = selectionToolButton.height/2
            val centerWidth = selectionToolButton.width/2
            val distance = distance(centerWidth, e.x, centerHeight, e.y)
            val distance2 = distance(centerWidth, centerWidth*2, centerHeight, centerHeight*2)
            arrowPolygon.scaleX = distance/distance2
            arrowPolygon.scaleY = distance/distance2
        }

        selectionToolButton.setOnAction {
            model.setSelectedTool(Tools.SelectionTool)
        }

        hbox1.children.add(eraseToolButton)

        circleToolButton.graphic = arrow()
        hbox2.children.add(circleToolButton)
        circleToolButton.setOnAction {
            model.setSelectedTool(Tools.CircleTool)
        }

        hbox2.children.add(rectangleToolButton)
        rectangleToolButton.setOnAction {
            model.setSelectedTool(Tools.RectangleTool)
        }

        val fillColorText = Label("Fill Color")
        fillColorVBox.children.add(fillColorText)

        fillColorPicker.value = model.defaultColor
        fillColorVBox.children.add(fillColorPicker)
        fillColorPicker.setOnAction {
            model.setSelectedFillColor(fillColorPicker.value)
        }

        val lineColorText = Label("Line Color")
        lineColorVBox.children.add(lineColorText)

        lineColorPicker.value = model.defaultColor
        lineColorVBox.children.add(lineColorPicker)
        lineColorPicker.setOnAction {
            model.setSelectedLineColor(lineColorPicker.value)
        }

        hbox3.children.add(lineToolButton)
        lineToolButton.setOnAction {
            model.setSelectedTool(Tools.LineTool)
        }
    }
}