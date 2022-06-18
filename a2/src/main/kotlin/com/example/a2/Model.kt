package com.example.a2

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.*
import kotlin.math.max
import kotlin.math.min
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
    var enterX : Double? = null
    var enterY : Double? = null

    // mark border
    var markBorderX:  Double? = null
    var markBorderY: Double? = null
    var markBorderWidth: Double? = null
    var markBorderHeight: Double? = null

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
        println("selecting thickness")
        this.selectedThickness = thickness
        updateShapeBasedOnProperties()
        markShape()
        updateViews()
    }

    @JvmName("getSelectedThickness")
    fun getThickness(): Thickness{
        return this.selectedThickness
    }

    @JvmName("setSelectedStyle")
    fun setSelectedStyle(style: Style){
        println("selecting style")
        this.selectedStyle = style
        updateShapeBasedOnProperties()
        updateViews()
    }

    @JvmName("getSelectedStyle")
    fun getSelectedStyle(): Style? {
        return this.selectedStyle
    }

    private fun markShape(){
        val borderOffset = 14.0 + this.selectedShape!!.strokeWidth
        if (this.selectedShape is Circle){
            println("marking shape")
            this.markBorderX = (this.selectedShape as Circle).centerX - (this.selectedShape as Circle).radius - borderOffset
            this.markBorderY = (this.selectedShape as Circle).centerY - (this.selectedShape as Circle).radius - borderOffset
            this.markBorderWidth = 2*(this.selectedShape as Circle).radius + 2 * borderOffset
            this.markBorderHeight = 2*(this.selectedShape as Circle).radius + 2 * borderOffset
        }
        if (this.selectedShape is Rectangle){
            this.markBorderX = (this.selectedShape as Rectangle).x - borderOffset
            this.markBorderY = (this.selectedShape as Rectangle).y - borderOffset
            this.markBorderWidth = (this.selectedShape as Rectangle).width + 2 * borderOffset
            this.markBorderHeight = (this.selectedShape as Rectangle).height + 2 * borderOffset
        }
        if (this.selectedShape is Line){
            val startX = (this.selectedShape as Line).startX
            val startY = (this.selectedShape as Line).startY
            val endX = (this.selectedShape as Line).endX
            val endY = (this.selectedShape as Line).endY
            this.markBorderX = min(startX, endX) - borderOffset
            this.markBorderY = min(startY, endY) - borderOffset
            this.markBorderWidth = max(startX, endX) - min(startX, endX) + 2*borderOffset
            this.markBorderHeight = max(startY, endY) - min(startY, endY) + 2*borderOffset
        }
    }

    private fun unMarkShape(){
        println("un marking shape")
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

    private fun updateShapeBasedOnProperties(){
        println("updating shape based on properties")
        println(this.selectedShape)
        this.selectedShape?.fill = this.selectedFillColor
        this.selectedShape?.stroke = this.selectedLineColor
        this.selectedShape?.strokeWidth = Thickness.Type3.getStyle(this.selectedThickness)

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
            this.selectedStyle = Style.Type1.getType(this.selectedShape?.strokeDashArray!![0])
        }
    }

    private fun shapePressedAction(shape: Shape){
        if (this.selectedTool == Tools.SelectionTool){
            if (this.selectedShape != null){
                this.unMarkShape()
            }
            this.selectedShape = shape
            updatePropertiesBasedOnShape()
            this.markShape()
        }
    }

    fun paneSelected(e: MouseEvent){
        this.enterX = e.x
        this.enterY = e.y
    }


    fun addNewShapeActions(shape: Shape){
        this.selectedShape = shape
        this.updateShapeBasedOnProperties()
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
        if (this.selectedTool == Tools.SelectionTool && this.selectedShape is Circle){
            println("getting to the point")
            (this.selectedShape as Circle).centerX = (this.selectedShape as Circle).centerX + e.x - this.enterX!!
            (this.selectedShape as Circle).centerY = (this.selectedShape as Circle).centerY + e.y - this.enterY!!
            this.enterX = e.x
            this.enterY = e.y
        }
        if (this.selectedTool == Tools.SelectionTool && this.selectedShape is Rectangle){
            (this.selectedShape as Rectangle).x = (this.selectedShape as Rectangle).x + e.x - enterX!!
            (this.selectedShape as Rectangle).y = (this.selectedShape as Rectangle).y + e.y - enterY!!
            this.enterX = e.x
            this.enterY = e.y
        }
        if (this.selectedTool == Tools.SelectionTool && this.selectedShape is Line){
            (this.selectedShape as Line).startX = (this.selectedShape as Line).startX + e.x - enterX!!
            (this.selectedShape as Line).endX = (this.selectedShape as Line).endX + e.x - enterX!!
            (this.selectedShape as Line).startY = (this.selectedShape as Line).startY + e.y - enterY!!
            (this.selectedShape as Line).endY = (this.selectedShape as Line).endY + e.y - enterY!!
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