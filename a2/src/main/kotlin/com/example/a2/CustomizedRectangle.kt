package com.example.a2

import javafx.scene.shape.Rectangle

class CustomizedRectangle(x: Double, y:Double, z:Double, w:Double) : Rectangle(x,y,z,w){
    val fixedPointX = x
    val fixedPointY = y
}