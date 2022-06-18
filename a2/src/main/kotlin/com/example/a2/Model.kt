package com.example.a2

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Model(){
    private val views: ArrayList<IView> = ArrayList()
    val defaultColor = Color.CORAL
    private var selectedTool = Tools.SelectionTool
    private var pickedFillColor : Color? = defaultColor
    private var pickedLineColor : Color? = defaultColor
    private var pickedThickness : Thickness = Thickness.Type1
    private var pickedStyle: Style? = Style.Type1
    val backgroundColor = Color.BEIGE
    var enterX : Double? = null
    var enterY : Double? = null

    // mark border
    var markBorderX:  Double? = null
    var markBorderY: Double? = null
    var markBorderWidth: Double? = null
    var markBorderHeight: Double? = null

    // selected shape
    var selectedShape: SelectedShape? = null

    @JvmName("setSelectedTool1")
    fun setSelectedTool(tool: Tools) {
        this.selectedTool = tool
        if (this.selectedTool != Tools.SelectionTool){
            this.unMarkShape()
        }
        updateViews()
    }

    @JvmName("getSelectedTool1")
    fun getSelectedTool() : Tools{
        return this.selectedTool
    }

    @JvmName("setSelectedColor1")
    fun setPickedFillColor(color: Color?) {
        this.pickedFillColor = color
        this.updateSelectedShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedColor1")
    fun getPickedFillColor() : Color? {
        return this.pickedFillColor
    }

    @JvmName("setSelectedLineColor1")
    fun setPickedLineColor(color: Color?) {
        this.pickedLineColor = color
        this.updateSelectedShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedLineColor1")
    fun getPickedLineColor() : Color? {
        return this.pickedLineColor
    }

    @JvmName("setThickness")
    fun setPickedThickness(thickness: Thickness){
        this.pickedThickness = thickness
        this.updateSelectedShapeBasedOnProperties()
        markShape()
        updateViews()
    }

    @JvmName("getSelectedThickness")
    fun getPickedThickness(): Thickness{
        return this.pickedThickness
    }

    @JvmName("setSelectedStyle")
    fun setPickedStyle(style: Style){
        this.updateSelectedShapeBasedOnProperties()
        this.pickedStyle = style
        updateViews()
    }

    @JvmName("getSelectedStyle")
    fun getPickedStyle(): Style? {
        return this.pickedStyle
    }

    private fun markShape(){
        val borderOffset = 14.0 + this.selectedShape!!.strokeWidth
        if (this.selectedShape?.type == ShapeTypes.Circle){
            this.markBorderX = this.selectedShape!!.centerX - this.selectedShape!!.radius - borderOffset
            this.markBorderY = this.selectedShape!!.centerY - this.selectedShape!!.radius - borderOffset
            this.markBorderWidth = 2*this.selectedShape!!.radius + 2 * borderOffset
            this.markBorderHeight = 2*this.selectedShape!!.radius + 2 * borderOffset
        }
        if (this.selectedShape?.type == ShapeTypes.Rectangle){
            this.markBorderX = this.selectedShape!!.x - borderOffset
            this.markBorderY = this.selectedShape!!.y - borderOffset
            this.markBorderWidth = this.selectedShape!!.width + 2 * borderOffset
            this.markBorderHeight = this.selectedShape!!.height + 2 * borderOffset
        }
        if (this.selectedShape?.type == ShapeTypes.Line){
            val startX = this.selectedShape!!.startX
            val startY = this.selectedShape!!.startY
            val endX = this.selectedShape!!.endX
            val endY = this.selectedShape!!.endY
            this.markBorderX = min(startX, endX) - borderOffset
            this.markBorderY = min(startY, endY) - borderOffset
            this.markBorderWidth = max(startX, endX) - min(startX, endX) + 2*borderOffset
            this.markBorderHeight = max(startY, endY) - min(startY, endY) + 2*borderOffset
        }
    }

    private fun unMarkShape(){
        this.selectedShape = null
        this.markBorderHeight = null
        this.markBorderY = null
        this.markBorderX = null
        this.markBorderWidth = null
    }

    fun mouseReleased(){
        if (this.selectedTool != Tools.SelectionTool) {
            unMarkShape()
        }
    }


    private fun updatePickedPropertiesBasedOnSelectedShape(){
        if (this.selectedShape != null) {
            this.pickedFillColor = this.selectedShape?.fill
            this.pickedLineColor = this.selectedShape?.stroke
            this.pickedThickness = Thickness.Type1.getType(this.selectedShape?.strokeWidth!!)
            this.pickedStyle = Style.Type1.getType(this.selectedShape?.strokeDashArray!![0])
        }
    }

    private fun updateSelectedShapeBasedOnShape(shape: Shape){
        this.selectedShape = SelectedShape()
        if (shape is Rectangle) {
            this.selectedShape?.type = ShapeTypes.Rectangle
            this.selectedShape?.x = shape.x
            this.selectedShape?.y = shape.y
            this.selectedShape?.height = shape.height
            this.selectedShape?.width = shape.width
        }
        if (shape is Line) {
            this.selectedShape?.type = ShapeTypes.Line
            this.selectedShape?.startX = shape.startX
            this.selectedShape?.startY = shape.startY
            this.selectedShape?.endX = shape.endX
            this.selectedShape?.endY = shape.endY
        }
        if (shape is Circle){
            this.selectedShape?.type = ShapeTypes.Line
            this.selectedShape?.radius = shape.radius
            this.selectedShape?.centerX = shape.centerX
            this.selectedShape?.centerY = shape.centerY
        }
        this.selectedShape?.fill = shape.fill as Color
        this.selectedShape?.stroke = shape.stroke as Color
        this.selectedShape?.strokeDashArray = shape.strokeDashArray
    }

    private fun updateSelectedShapeBasedOnProperties(){
        this.selectedShape?.fill = this.getPickedFillColor()!!
        this.selectedShape?.stroke = this.getPickedLineColor()!!
        this.selectedShape?.strokeWidth = Thickness.Type3.getStyle(this.getPickedThickness())

        var dashSize = 20.0
        if (this.getPickedStyle() == Style.Type1) {
            dashSize = 1.0
        }
        if (this.getPickedStyle() == Style.Type2){
            dashSize = 30.0
        }
        if (this.getPickedStyle() == Style.Type3){
            dashSize = 50.0
        }
        this.selectedShape?.strokeDashArray?.removeAll(this.selectedShape?.strokeDashArray!!)
        this.selectedShape?.strokeDashArray?.addAll(dashSize,dashSize,dashSize,dashSize,dashSize)
    }

    private fun shapePressedAction(shape: Shape){
        if (this.selectedTool == Tools.SelectionTool){
            if (this.selectedShape != null){
                this.unMarkShape()
            }
            updateSelectedShapeBasedOnShape(shape)
            updatePickedPropertiesBasedOnSelectedShape()
            this.markShape()
        }
    }

    fun paneSelected(e: MouseEvent){
        this.enterX = e.x
        this.enterY = e.y
    }


    fun addNewShapeActions(shape: Shape){
        shape.onMousePressed = EventHandler {
            run{
                println("shape was selected")
                this.shapePressedAction(shape)
                updateViews()
            }
        }
        shape.onMousePressed
        shape.onMouseDragReleased = EventHandler {
            run{
                this.unMarkShape()
                updateViews()
            }
        }
    }

    fun paneDragged(e: MouseEvent){
        markShape()
        if (this.selectedShape?.type == ShapeTypes.Circle && this.selectedTool == Tools.CircleTool){
            this.selectedShape?.radius = distance(e.x, this.selectedShape!!.centerX, e.y, this.selectedShape!!.centerY)
        }

        if (this.selectedShape?.type == ShapeTypes.Line && this.selectedTool == Tools.LineTool){
            this.selectedShape?.endX = e.x
            this.selectedShape?.endY = e.y
        }

        if (this.selectedShape?.type == ShapeTypes.Rectangle && this.selectedTool == Tools.RectangleTool){
            val fixedPointX = this.selectedShape!!.fixedPointX
            val fixedPointY = this.selectedShape!!.fixedPointY
            if (e.x < fixedPointX) {
                this.selectedShape?.x = e.x
                this.selectedShape?.width = fixedPointX - e.x
            }
            else {
                this.selectedShape?.x = fixedPointX
                this.selectedShape?.width = e.x - fixedPointX
            }
            if (e.y < fixedPointY){
                this.selectedShape?.y = e.y
                this.selectedShape?.height = fixedPointY - e.y
            }
            else {
                this.selectedShape?.y = fixedPointY
                this.selectedShape?.height = e.y - fixedPointY
            }
        }
        if (this.selectedTool == Tools.SelectionTool && this.selectedShape?.type == ShapeTypes.Circle){
            println("getting to the point")
            this.selectedShape?.centerX = this.selectedShape!!.centerX + e.x - this.enterX!!
            this.selectedShape?.centerY = this.selectedShape!!.centerY + e.y - this.enterY!!
            this.enterX = e.x
            this.enterY = e.y
        }
        if (this.selectedTool == Tools.SelectionTool && this.selectedShape?.type == ShapeTypes.Rectangle){
            this.selectedShape?.x = this.selectedShape!!.x + e.x - enterX!!
            this.selectedShape?.y = this.selectedShape!!.y + e.y - enterY!!
            this.enterX = e.x
            this.enterY = e.y
        }
        if (this.selectedTool == Tools.SelectionTool && this.selectedShape?.type == ShapeTypes.Line){
            this.selectedShape?.startX = this.selectedShape!!.startX + e.x - enterX!!
            this.selectedShape?.endX = this.selectedShape!!.endX + e.x - enterX!!
            this.selectedShape?.startY = this.selectedShape!!.startY + e.y - enterY!!
            this.selectedShape?.endY = this.selectedShape!!.endY + e.y - enterY!!
            this.enterX = e.x
            this.enterY = e.y
        }
        this.updateViews()
    }

    fun addView(view: IView) {
        views.add(view)
        view.update()
    }
    private fun updateViews(){
        println("updating")
        for (view in this.views){
            view.update()
        }
    }
}

fun distance(x1: Double, x2: Double, y1:Double, y2:Double): Double{
    return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
}