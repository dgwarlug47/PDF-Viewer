package com.example.a2

import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

fun getShapePOJO(shape: Shape): ShapePOJO{
    var type = ShapeTypes.Rectangle
    if (shape is Circle){
        type = ShapeTypes.Circle
    }
    if (shape is Line){
        type = ShapeTypes.Line
    }


    println("stroke shape pojo")
    val stroke = if (shape.stroke != null){
        shape.stroke.toString()
    }
    else{
        "0xff7f50ff"
    }

    val fill = if (shape.fill != null){
        shape.fill.toString()
    }
    else{
        "0xff7f50ff"
    }
    return ShapePOJO(
        type,
        shape.strokeWidth,
        shape.strokeDashArray,
        if (shape is Rectangle) shape.x else 0.0,
        if (shape is Rectangle) shape.y else 0.0,
        if (shape is Rectangle) shape.height else 0.0,
        if (shape is Rectangle) shape.width else 0.0,
        0.0,
        0.0,
        if (shape is Line) shape.startX else 0.0,
        if (shape is Line) shape.startY else 0.0,
        if (shape is Line) shape.endX else 0.0,
        if (shape is Line) shape.endY else 0.0,
        if (shape is Circle) shape.radius else 0.0,
        if (shape is Circle) shape.centerX else 0.0,
        if (shape is Circle) shape.centerY else 0.0,
        //ColorMapping().REVERSED_NAMED_COLORS[if (shape.stroke == null) Color.ALICEBLUE else (shape.stroke as Color)]!!,
        //ColorMapping().REVERSED_NAMED_COLORS[if (shape.fill == null) Color.ALICEBLUE else (shape.fill as Color)]!!
        stroke,
        fill,
        life = Sha()
    )

}

fun getShape(node: ShapePOJO): Shape{
    if (node.type == ShapeTypes.Rectangle){
        val copyNode = Rectangle(node.x, node.y, node.width, node.width)
        copyNode.fill = Color.valueOf(node.fill)
        copyNode.stroke = Color.valueOf(node.stroke)
        copyNode.strokeWidth = node.strokeWidth
        copyNode.strokeDashArray.addAll(node.strokeDashArray)
        return copyNode
    }
    if (node.type == ShapeTypes.Circle){
        println()
        val copyNode = Circle(node.centerX, node.centerY, node.radius)
        copyNode.fill = Color.valueOf(node.fill)
        copyNode.stroke = Color.valueOf(node.stroke)
        copyNode.strokeWidth = node.strokeWidth
        copyNode.strokeDashArray.addAll(node.strokeDashArray)
        return copyNode
    }
    val copyNode = Line(node.startX, node.startY, node.endX, node.endY)
    copyNode.fill = Color.valueOf(node.fill)
    copyNode.stroke = Color.valueOf(node.stroke)
    copyNode.strokeWidth = node.strokeWidth
    copyNode.strokeDashArray.addAll(node.strokeDashArray)
    return copyNode
}