package net.codebot.pdfviewer

data class StateModel (
    var map: MutableMap<Int, StatePage>
)

data class StatePage(
    val addedPaths: ArrayList<MyPath>,
    val addedPathPoints: ArrayList<ArrayList<Point>>,
    val currentId: Int,
    val undoQueue: ArrayList<Actions>,
    val redoQueue: ArrayList<Actions>,
    val oldWidth: Float?,
    val oldHeight: Float?,
)