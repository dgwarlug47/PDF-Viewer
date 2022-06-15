package com.example.a2

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.*
import kotlin.math.sqrt

fun distance(x1: Double, x2: Double, y1:Double, y2:Double): Double{
    return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
}

enum class Tools{
    SelectionTool,
    EraseTool,
    LineTool,
    RectangleTool,
    CircleTool
}
class Model(){
    private val views: ArrayList<IView> = ArrayList()
    val defaultColor = Color.CORAL
    var selectedTool = Tools.SelectionTool
    var selectedShape : Shape? = null
    private var shapeMarker : Shape? = null
    private var selectedFillColor : Color? = defaultColor
    private var selectedLineColor : Color? = defaultColor
    private var selectedThickness : Double = 10.0
    private var selectedStyle: Double? = null
    val backgroundColor = Color.BEIGE

    @JvmName("setSelectedTool1")
    fun setSelectedTool(tool: Tools) {
        this.selectedTool = tool
        if (this.selectedTool != Tools.SelectionTool){
            this.unMarkShape()
        }
        updateViews()
    }

    @JvmName("setSelectedShape1")
    fun setSelectedShape(shape: Shape?) {
        this.selectedShape = shape
        updateViews()
    }

    @JvmName("setSelectedColor1")
    fun setSelectedFillColor(color: Color?) {
        this.selectedFillColor = color
        updateShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedColor1")
    fun getSelectedFillColor() : Color? {
        return this.selectedFillColor
    }

    @JvmName("setSelectedLineColor1")
    fun setSelectedLineColor(color: Color?) {
        this.selectedLineColor = color
        updateShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedLineColor1")
    fun getSelectedLineColor() : Color? {
        return this.selectedLineColor
    }

    private fun markShape(){
        /*
        this.selectedShape?.stroke = Color.SNOW
        this.selectedShape?.strokeType = StrokeType.INSIDE
        this.selectedShape?.strokeWidth = 20.0
        this.selectedShape?.strokeDashArray?.addAll(25.0, 10.0)
        */
    }

    private fun unMarkShape(){
        this.selectedShape?.stroke = this.selectedLineColor
        this.selectedShape = null
    }

    fun mouseReleased(){
        unMarkShape()
    }

    private fun updateShapeBasedOnProperties(){
        this.selectedShape?.fill = this.selectedFillColor
        this.selectedShape?.stroke = this.selectedLineColor
        this.selectedShape?.strokeWidth = this.selectedThickness
        //this.selectedShape?.strokeDashArray?.addAll(this.selectedStyle)
    }

    private fun updatePropertiesBasedOnShape(){
        if (this.selectedShape != null) {
            this.selectedFillColor = this.selectedShape?.fill as Color?
            this.selectedLineColor = this.selectedShape?.stroke as Color?
            this.selectedThickness = this.selectedShape?.strokeWidth!!
            // the one with they style
        }
    }

    private fun shapeSelectedAction(shape: Shape){
        if (this.selectedTool == Tools.SelectionTool){
            this.selectedShape = shape
            updatePropertiesBasedOnShape()
            this.markShape()
        }
    }


    fun addNewShapeActions(shape: Shape){
        this.selectedShape = shape
        this.updateShapeBasedOnProperties()
        shape.onMouseClicked = EventHandler {
            run{
                this.shapeSelectedAction(shape)
                println("MESSI")
                updateViews()
            }
        }
        shape.onMouseDragReleased = EventHandler {
            run{
                println("ja escuto os teus sinais")
                this.unMarkShape()
                updateViews()
            }
        }
    }

    fun paneDragged(e: MouseEvent){
        markShape()
        if (this.selectedShape is Circle && this.selectedTool == Tools.CircleTool){
            (this.selectedShape as Circle).radius = distance(e.x, (this.selectedShape as Circle).centerX, e.y, (this.selectedShape as Circle).centerY)
        }

        if (this.selectedShape is Line && this.selectedTool == Tools.LineTool){
            (this.selectedShape as Line).endX = e.x
            (this.selectedShape as Line).endY = e.y
        }

        if (this.selectedShape is Rectangle && this.selectedTool == Tools.RectangleTool){
            val fixedPointX = (this.selectedShape as CustomizedRectangle).fixedPointX
            val fixedPointY = (this.selectedShape as CustomizedRectangle).fixedPointY
            if (e.x < fixedPointX) {
                (this.selectedShape as Rectangle).x = e.x
                (this.selectedShape as Rectangle).width = fixedPointX - e.x
            }
            else {
                (this.selectedShape as Rectangle).x = fixedPointX
                (this.selectedShape as Rectangle).width = e.x - fixedPointX
            }
            if (e.y < fixedPointY){
                (this.selectedShape as Rectangle).y = e.y
                (this.selectedShape as Rectangle).height = fixedPointY - e.y
            }
            else {
                (this.selectedShape as Rectangle).y = fixedPointY
                (this.selectedShape as Rectangle).height = e.y - fixedPointY
            }
        }
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