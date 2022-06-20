package com.example.a2

import javafx.scene.paint.Color

class SelectedShape(){
    var type: ShapeTypes = ShapeTypes.Rectangle
    var strokeWidth: Double = Thickness.Type1.getStyle(Thickness.Type1)
    var strokeDashArray : MutableList<Double> = mutableListOf(Style.Type1.getStyle(Style.Type1))

    // rectangle
    var x: Double = 0.0
    var y: Double = 0.0
    var height: Double = 0.0
    var width: Double = 0.0
    var fixedPointX: Double = 0.0
    var fixedPointY: Double = 0.0
    // line
    var startX: Double = 0.0
    var startY: Double = 0.0
    var endX: Double = 0.0
    var endY: Double = 0.0
    // circle
    var radius: Double = 0.0
    var centerX: Double = 0.0
    var centerY: Double = 0.0

    var stroke: Color = Color.CORAL
    var fill: Color = Color.ALICEBLUE
}

enum class ShapeTypes{
    Circle,
    Rectangle,
    Line
}

enum class Tools{
    SelectionTool,
    EraseTool,
    LineTool,
    RectangleTool,
    CircleTool,
    FillTool
}

enum class Style{
    Type1,
    Type2,
    Type3;

    fun getType(style: Double): Style{
        if (style == 1.0){
            return Type1
        }
        if (style == 15.0){
            return Type2
        }
        return Type3
    }

    fun getStyle(style: Style): Double{
        if (style == Type1){
            return 1.0
        }
        if (style == Type2){
            return 15.0
        }
        return 30.0
    }
}

enum class Thickness{
    Type1,
    Type2,
    Type3;

    fun getType(style: Double): Thickness{
        if (style == 3.0){
            return Type1
        }
        if (style == 6.0){
            return Type2
        }
        return Type3
    }

    fun getStyle(style: Thickness): Double{
        if (style == Type1){
            return 3.0
        }
        if (style == Type2){
            return 6.0
        }
        return 10.0
    }
}