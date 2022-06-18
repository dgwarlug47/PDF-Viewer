package com.example.a2
import javafx.event.EventHandler
import javafx.geometry.Insets
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

    private fun addNewShape(shape: Shape){
        this.children.add(shape)
        this.model.addNewShapeActions(shape)
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
                    this.addNewShape(circle)
                }
                if (model.getSelectedTool() == Tools.RectangleTool){
                    val rectangle = CustomizedRectangle(e.x, e.y, 20.0, 20.0)
                    this.addNewShape(rectangle)
                }
                if (model.getSelectedTool() == Tools.LineTool){
                    val line = Line(e.x, e.y, e.x+20, e.y+20)
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
                model.mouseReleased()
            }
        }
    }

    override fun update() {
        println("updating view 2")
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
                println(markBorder)
            }
        }
        if (this.viewShape == null && this.markBorder != null){
            this.children.remove(this.markBorder)
            this.markBorder = null
        }
        updateShapeBasedOnSelectedShape()
    }

    private fun updateShapeBasedOnSelectedShape(){
        this.viewShape?.fill = model.selectedShape?.fill
        this.viewShape?.stroke = model.selectedShape?.stroke
        this.viewShape?.strokeWidth = model.selectedShape!!.strokeWidth
        this.viewShape?.strokeDashArray?.removeAll(this.viewShape!!.strokeDashArray)
        this.viewShape?.strokeDashArray?.addAll(model.selectedShape?.strokeDashArray!!)
    }
}