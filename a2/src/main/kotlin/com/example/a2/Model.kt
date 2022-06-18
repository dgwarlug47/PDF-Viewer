package com.example.a2

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.*
import kotlin.math.sqrt

fun distance(x1: Double, x2: Double, y1:Double, y2:Double): Double{
    return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
}

enum class Style{
    Type1,
    Type2,
    Type3;

    fun getType(style: Double): Style{
        if (style == 1.0){
            return Type1
        }
        if (style == 20.0){
            return Type2
        }
        return Type3
    }

    fun getStyle(style: Style): Double{
        if (style == Type1){
            return 1.0
        }
        if (style == Type2){
            return 20.0
        }
            return 50.0
    }
}

enum class Thickness{
    Type1,
    Type2,
    Type3;

    fun getType(style: Double): Thickness{
        if (style == 10.0){
            return Type1
        }
        if (style == 40.0){
            return Type2
        }
        return Type3
    }

    fun getStyle(style: Thickness): Double{
        if (style == Type1){
            return 10.0
        }
        if (style == Type2){
            return 30.0
        }
        return 60.0
    }
}

enum class Tools{
    SelectionTool,
    EraseTool,
    LineTool,
    RectangleTool,
    CircleTool,
    FillTool
}
class Model(){
    private val views: ArrayList<IView> = ArrayList()
    val defaultColor = Color.CORAL
    private var selectedTool = Tools.SelectionTool
    var selectedShape : Shape? = null
    private var shapeMarker : Shape? = null
    private var selectedFillColor : Color? = defaultColor
    private var selectedLineColor : Color? = defaultColor
    private var selectedThickness : Thickness = Thickness.Type1
    private var selectedStyle: Style? = Style.Type1
    val backgroundColor = Color.BEIGE

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

    @JvmName("setThickness")
    fun setSelectedThickness(thickness: Thickness){
        this.selectedThickness = thickness
        updateShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedThickness")
    fun getThickness(): Thickness{
        return this.selectedThickness
    }

    @JvmName("setSelectedStyle")
    fun setSelectedStyle(style: Style){
        this.selectedStyle = style
        updateShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedStyle")
    fun getSelectedStyle(): Style? {
        return this.selectedStyle
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
        this.selectedShape?.strokeWidth = Thickness.Type2.getStyle(this.selectedThickness)

        var dashSize = 20.0

        if (this.selectedStyle == Style.Type1) {
            dashSize = 1.0
        }
        if (this.selectedStyle == Style.Type2){
            dashSize = 30.0
        }
        if (this.selectedStyle == Style.Type3){
            dashSize = 50.0
        }
        this.selectedShape?.strokeDashArray?.removeAll(this.selectedShape?.strokeDashArray!!)
        this.selectedShape?.strokeDashArray?.addAll(dashSize,dashSize,dashSize,dashSize,dashSize)
    }

    private fun updatePropertiesBasedOnShape(){
        if (this.selectedShape != null) {
            this.selectedFillColor = this.selectedShape?.fill as Color?
            this.selectedLineColor = this.selectedShape?.stroke as Color?
            this.selectedThickness = Thickness.Type1.getType(this.selectedShape?.strokeWidth!!)
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