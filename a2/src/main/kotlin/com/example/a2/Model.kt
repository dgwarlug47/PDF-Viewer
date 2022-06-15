package com.example.a2

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.*
import kotlin.math.sqrt


enum class Tools{
    SelectionTool,
    EraseTool,
    LineTool,
    RectangleTool,
    CircleTool
}
class Model(){
    private val views: ArrayList<IView> = ArrayList()
    var selectedTool = Tools.SelectionTool
    var selectedShape : Shape? = null
    var shapeMarker : Shape? = null
    var selectedFillColor : Color? = null
    var selectedLineColor : Color? = null
    var selectedThickness : Double = 20.0
    var selectedStyle: Double? = null
    var backgroundColor = Color.BEIGE

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
    fun setSelectedColor(color: Color?) {
        this.selectedFillColor = color
        this.selectedShape?.fill = color
        updateViews()
    }

    fun addView(view: IView) {
        views.add(view)
        view.update()
    }

    fun markShape(){
        if (this.selectedShape is Circle){
            val circle = (this.selectedShape as Circle)
            this.shapeMarker = Arc(circle.centerX, circle.centerY,
                circle.radius, circle.radius, 0.0, 360.0)
            (this.shapeMarker as Arc).fill = this.backgroundColor
        }
        /*
        this.selectedShape?.stroke = Color.SNOW
        this.selectedShape?.strokeType = StrokeType.INSIDE
        this.selectedShape?.strokeWidth = 20.0
        this.selectedShape?.strokeDashArray?.addAll(25.0, 10.0)
        */
    }

    private fun unMarkShape(){
        this.selectedShape?.stroke = this.selectedFillColor
        this.selectedShape = null
    }

    fun mouseReleased(){
        unMarkShape()
    }

    private fun updateShapeBasedOnProperties(){
        this.selectedShape?.fill = this.selectedFillColor
        this.selectedShape?.stroke = this.selectedLineColor
        this.selectedShape?.strokeWidth = this.selectedThickness
        this.selectedShape?.strokeDashArray?.addAll(this.selectedStyle)
    }

    private fun shapeSelectedAction(shape: Shape){
        if (this.selectedTool == Tools.SelectionTool){
            this.selectedShape = shape
            this.updateShapeBasedOnProperties()
            this.markShape()
        }
    }

    fun addNewShapeActions(shape: Shape){
        this.selectedShape = shape
        this.updateShapeBasedOnProperties()
        shape.onMouseClicked = EventHandler {
            run{
                this.shapeSelectedAction(shape)
                updateViews()
            }
        }
        shape.onMouseDragReleased = EventHandler {
            run{
                println("ja escuto os teus sinais")
                shape.stroke = this.selectedFillColor
            }
        }
    }

    fun paneDragged(e: MouseEvent){
        print("what")
        markShape()
        if (this.selectedShape is Circle && this.selectedTool == Tools.CircleTool){
            (this.selectedShape as Circle).radius = sqrt((e.y - (this.selectedShape as Circle).centerY)*(e.y - (this.selectedShape as Circle).centerY) + (e.x - (this.selectedShape as Circle).centerX)*(e.x - (this.selectedShape as Circle).centerX))
        }

        if (this.selectedShape is Line && this.selectedTool == Tools.LineTool){
            print("what2")
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

    private fun updateViews(){
        println("updating")
        for (view in this.views){
            view.update()
        }
    }

}