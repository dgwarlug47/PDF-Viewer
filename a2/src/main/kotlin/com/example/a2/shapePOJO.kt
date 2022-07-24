package com.example.a2


@kotlinx.serialization.Serializable
data class ShapePOJO(
    var type: ShapeTypes = ShapeTypes.Rectangle,
    var strokeWidth: Double = Thickness.Type1.getStyle(Thickness.Type1),
    var strokeDashArray : MutableList<Double> = mutableListOf(Style.Type1.getStyle(Style.Type1)),

    // rectangle
    var x: Double = 0.0,
    var y: Double = 0.0,
    var height: Double = 0.0,
    var width: Double = 0.0,
    var fixedPointX: Double = 0.0,
    var fixedPointY: Double = 0.0,
    // line
    var startX: Double = 0.0,
    var startY: Double = 0.0,
    var endX: Double = 0.0,
    var endY: Double = 0.0,
    // circle
    var radius: Double = 0.0,
    var centerX: Double = 0.0,
    var centerY: Double = 0.0,
    var stroke: String = "coral",
    var fill: String = "aliceblue",
    var life: Sha
)