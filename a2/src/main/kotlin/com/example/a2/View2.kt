package com.example.a2
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape

class View2(private val model: Model): Pane(), IView{

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

        this.setOnMousePressed{
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
                    print("here")
                    val line = Line(e.x, e.y, e.x+20, e.y+20)
                    this.addNewShape(line)
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
                model.mouseReleased()
            }
        }
    }

    override fun update() {
        if (model.selectedShape != null && model.getSelectedTool() != Tools.EraseTool) {
            this.children.remove(model.selectedShape)
            this.children.add(model.selectedShape)
        }
        if (model.selectedShape != null && model.getSelectedTool() == Tools.EraseTool){
            this.children.remove(model.selectedShape)
        }
    }
}