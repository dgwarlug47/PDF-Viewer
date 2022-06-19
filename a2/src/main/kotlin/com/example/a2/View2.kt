package com.example.a2
import com.google.gson.Gson
import javafx.beans.Observable
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

class View2(private val model: Model): Pane(), IView{
    private var markBorder : Shape? = null
    private var viewShape : Shape? = null

    fun clone(cloneModel: Model): View2{
        val newView2 = View2(cloneModel)
        for (child in children){
            newView2.saveOldShape(copyNode(child) as Shape)
        }
        //val stringModel = Gson().toJson(this.children[0], Node::class.java)
        //val copyModel = Gson().fromJson<Node>(stringModel, Node::class.java)
        //newView2.children.add(copyModel)
        return newView2
    }

    private fun saveOldShape(shape: Shape){
        this.children.add(shape)
        shape.onMousePressed = EventHandler {
            run{
                if (model.getSelectedTool() == Tools.SelectionTool) {
                    viewShape = shape
                }
                if (model.getSelectedTool() == Tools.EraseTool){
                    this.children.remove(shape)
                }
                model.shapePressed(shape)
            }
        }
        shape.onMouseDragReleased = EventHandler {
            run{
                model.shapeDragReleased()
            }
        }
    }

    private fun addNewShape(shape: Shape){
        this.children.add(shape)
        initializeShape()
        model.updateSelectedShapeBasedOnShape(shape!!)
        shape.onMousePressed = EventHandler {
            run{
                if (model.getSelectedTool() == Tools.SelectionTool) {
                    viewShape = shape
                }
                if (model.getSelectedTool() == Tools.EraseTool){
                    this.children.remove(shape)
                }
                model.shapePressed(shape)
            }
        }
        shape.onMouseDragReleased = EventHandler {
            run{
                model.shapeDragReleased()
            }
        }
        update()
    }

    init{
        // background
        this.background = Background(BackgroundFill(model.backgroundColor, CornerRadii.EMPTY, Insets.EMPTY))
        val invisibleCircle = Circle(0.0, 0.0, 500.0)
        invisibleCircle.fill = model.backgroundColor
        this.children.add(invisibleCircle)

        val mousePressed  = EventHandler<MouseEvent>{
            e ->
            run{
                if (model.getSelectedTool() == Tools.CircleTool) {
                    val circle = Circle(e.x, e.y, 10.0)
                    this.viewShape = circle
                    this.addNewShape(circle)
                }
                if (model.getSelectedTool() == Tools.RectangleTool){
                    val rectangle = Rectangle(e.x, e.y, 20.0, 20.0)
                    this.viewShape = rectangle
                    this.addNewShape(rectangle)
                }
                if (model.getSelectedTool() == Tools.LineTool){
                    val line = Line(e.x, e.y, e.x+20, e.y+20)
                    this.viewShape = line
                    this.addNewShape(line)
                }
                if (model.getSelectedTool() == Tools.SelectionTool) {
                    this.model.paneSelected(e)
                }
            }
        }
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed)

        this.setOnMouseDragged {
            e ->
            run {
                model.paneDragged(e)
            }
        }
        this.setOnMouseReleased {
            run{
                model.paneMouseReleased()
            }
        }
    }

    override fun update() {
        println("updating view 2")
        if (model.selectedShape == null){
            viewShape = null
        }
        if (this.viewShape != null && model.getSelectedTool() != Tools.EraseTool) {
            this.children.remove(this.viewShape)
            this.children.add(this.viewShape)
            if (this.markBorder != null){
                this.children.remove(this.markBorder)
                this.children.add(this.markBorder)
            }
        }
        if (this.viewShape != null && model.getSelectedTool() == Tools.EraseTool){
            this.children.remove(this.viewShape)
        }
        if (this.viewShape != null && model.getSelectedTool() == Tools.SelectionTool){
            if (markBorder == null){
                markBorder = Rectangle(model.markBorderX!!, model.markBorderY!!, model.markBorderWidth!!, model.markBorderHeight!!)
                (markBorder as Rectangle).fill = Color.TRANSPARENT
                (markBorder as Rectangle).stroke = Color.GOLD
                (markBorder as Rectangle).strokeWidth = 5.0
                (markBorder as Rectangle).strokeDashArray.addAll(15.0, 15.0, 15.0, 15.0, 15.0, 15.0)
                this.children.add(markBorder)
            }
            else {
                (markBorder as Rectangle).x = model.markBorderX!!
                (markBorder as Rectangle).y = model.markBorderY!!
                (markBorder as Rectangle).width = model.markBorderWidth!!
                (markBorder as Rectangle).height = model.markBorderHeight!!
            }
        }
        if (this.viewShape == null && this.markBorder != null){
            this.children.remove(this.markBorder)
            this.markBorder = null
        }
        updateViewShapeBasedOnSelectedShape()
        println("what")
        println(viewShape?.strokeDashArray)
    }

    private fun updateViewShapeBasedOnSelectedShape(){
        this.viewShape?.fill = model.selectedShape?.fill
        this.viewShape?.stroke = model.selectedShape?.stroke
        this.viewShape?.strokeWidth = model.selectedShape!!.strokeWidth
        this.viewShape?.strokeDashArray?.removeAll(viewShape!!.strokeDashArray)
        this.viewShape?.strokeDashArray?.addAll(model.selectedShape?.strokeDashArray!!)

        if (viewShape is Rectangle) {
            (viewShape as Rectangle).x = model.selectedShape?.x!!
            (viewShape as Rectangle).y = model.selectedShape?.y!!
            (viewShape as Rectangle).height = model.selectedShape?.height!!
            (viewShape as Rectangle).width = model.selectedShape?.width!!
        }
        if (viewShape is Line) {
            (viewShape as Line).startX = model.selectedShape?.startX!!
            (viewShape as Line).startY = model.selectedShape?.startY!!
            (viewShape as Line).endX = model.selectedShape?.endX!!
            (viewShape as Line).endY = model.selectedShape?.endY!!
        }
        if (viewShape is Circle){
            (viewShape as Circle).radius = model.selectedShape?.radius!!
            (viewShape as Circle).centerX = model.selectedShape?.centerX!!
            (viewShape as Circle).centerY = model.selectedShape?.centerY!!
        }
    }
    private fun initializeShape(){
        viewShape!!.fill = model.getPickedFillColor()
        viewShape!!.stroke = model.getPickedLineColor()
        viewShape!!.strokeWidth = Thickness.Type1.getStyle(model.getPickedThickness())
        viewShape!!.strokeDashArray.removeAll(viewShape!!.strokeDashArray)
        viewShape!!.strokeDashArray.addAll(model.createDashedArrayBasedOnPickedStyle())
        println("help with god")
        println(viewShape!!.strokeDashArray)
    }
}