package com.example.a2
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import kotlinx.serialization.Contextual

class View2(private val model: Model): Pane(), IView{
    private var markBorder : Shape? = null
    private var viewShape : Shape? = null

    private fun registerShapeActions(shape: Shape){
        shape.onMousePressed = EventHandler {
            run{
                if (model.getSelectedTool() == Tools.SelectionTool) {
                    viewShape = shape
                    model.shapePressed(shape)
                }
                if (model.getSelectedTool() == Tools.EraseTool){
                    this.children.remove(shape)
                    model.shapePressed(shape)
                }
                if (model.getSelectedTool() == Tools.FillTool){
                    viewShape = shape
                    model.shapePressedWhileFillTool(shape)
                }
            }
        }
        shape.onMouseDragReleased = EventHandler {
            run{
                model.shapeDragReleased()
            }
        }
    }
    fun saveOldShape(shape: Shape){
        this.children.add(shape)
        registerShapeActions(shape)
    }

    private fun addNewShape(shape: Shape){
        this.children.add(shape)
        initializeShape()
        model.updateSelectedShapeBasedOnShape(shape)
        registerShapeActions(shape)
        update()
    }

    init{
        // background
        this.background = Background(BackgroundFill(model.backgroundColor, CornerRadii.EMPTY, Insets.EMPTY))
        val invisibleCircle = Circle(-700.0, -700.0, 500.0)
        invisibleCircle.fill = model.backgroundColor
        this.children.add(invisibleCircle)

        this.setOnMousePressed{
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
                    var hitChild = false
                    for (child in children){
                        if ((child as Shape).fill == Color.BEIGE){
                            continue
                        }
                        if (child.contains(e.x, e.y)){
                            hitChild = true
                        }
                    }
                    this.model.paneSelected(e, hitChild)
                }
            }
        }

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
        if (model.selectedShape == null){
            viewShape = null
        }
        if (model.getSelectedTool() == Tools.SelectionTool){
            if (model.editCutPressed){
                if (this.markBorder != null){
                    this.children.remove(this.markBorder)
                }
                model.editCutPressed = false
                children.remove(viewShape)
                model.copiedShape = viewShape?.let { copyNode(it) } as Shape
                updateViewShapeBasedOnSelectedShape()
                return
            }
            if (model.editCopyPressed){
                model.editCopyPressed = false
                model.copiedShape = viewShape?.let { copyNode(it) } as Shape
                updateViewShapeBasedOnSelectedShape()
                return
            }
            if (model.editPastePressed){
                if (!children.contains(model.copiedShape)) {
                    saveOldShape(model.copiedShape as Shape)
                }
                model.editPastePressed = false
                updateViewShapeBasedOnSelectedShape()
                return
            }
        }
        if (this.viewShape != null && model.getSelectedTool() != Tools.EraseTool) {
            this.children.remove(this.viewShape)
            this.children.add(this.viewShape)
            if (this.markBorder != null){
                this.children.remove(this.markBorder)
                this.children.add(this.markBorder)
            }
        }
        if (viewShape != null && model.getSelectedTool() == Tools.EraseTool){
            children.remove(viewShape)
            viewShape = null
        }
        if (viewShape != null && model.getSelectedTool() == Tools.SelectionTool && model.deletePressed){
            model.deletePressed = false
            children.remove(viewShape)
            viewShape = null
        }
        if (viewShape != null && model.getSelectedTool() == Tools.SelectionTool){
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
        if (viewShape == null && markBorder != null){
            children.remove(markBorder)
            markBorder = null
        }
        updateViewShapeBasedOnSelectedShape()
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

        println("stroke shape pojo")
        if (viewShape?.stroke != null){
        println(viewShape?.stroke.toString())
        }
    }
    private fun initializeShape(){
        viewShape!!.fill = model.getPickedFillColor()
        viewShape!!.stroke = model.getPickedLineColor()
        viewShape!!.strokeWidth = Thickness.Type1.getStyle(model.getPickedThickness())
        viewShape!!.strokeDashArray.removeAll(viewShape!!.strokeDashArray)
        viewShape!!.strokeDashArray.addAll(model.createDashedArrayBasedOnPickedStyle())
    }
}