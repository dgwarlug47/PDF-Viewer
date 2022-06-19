package com.example.a2

import javafx.scene.Node
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle

fun copyNode(node: Node): Node{
    if (node is Rectangle){
        val copyNode = Rectangle(node.x, node.y, node.width, node.width)
        copyNode.fill = node.fill
        copyNode.stroke = node.stroke
        copyNode.strokeWidth = node.strokeWidth
        copyNode.strokeDashArray.addAll(node.strokeDashArray)
        return copyNode
    }
    if (node is Circle){
        val copyNode = Circle(node.centerX, node.centerY, node.radius)
        copyNode.fill = node.fill
        copyNode.stroke = node.stroke
        copyNode.strokeWidth = node.strokeWidth
        copyNode.strokeDashArray.addAll(node.strokeDashArray)
        return copyNode
    }
        val node = (node as Line)
        val copyNode = Line(node.startX, node.startY, node.endX, node.endY)
        copyNode.fill = node.fill
        copyNode.stroke = node.stroke
        copyNode.strokeWidth = node.strokeWidth
        copyNode.strokeDashArray.addAll(node.strokeDashArray)
        return copyNode
}