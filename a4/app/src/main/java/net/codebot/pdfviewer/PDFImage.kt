package net.codebot.pdfviewer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.annotation.RequiresApi
import java.io.File
import kotlin.math.sqrt

@SuppressLint("AppCompatCustomView")
class PDFImage  // constructor
    (context: Context?) : ImageView(context) {
    private val LOG_NAME = "pdf_image"

    // change orientation helper variables
    var oldWidth: Float? = null
    var oldHeight: Float? = null

    // status
    var currentPathId = 0

    var interactionState: InteractionState = InteractionState.HIGHLIGHT_DRAW

    private val eraseTolerance = 40
    var undoQueue = ArrayList<Actions>()
    var redoQueue = ArrayList<Actions>()
    var counter = 0
    var fistTime = true

    // drawing path
    private var currentPath: Path = Path()
    var addedPaths = ArrayList<MyPath>()

    // drawing points
    private var currentPathPoints = ArrayList<Point>()
    var addedPathPoints = ArrayList<ArrayList<Point>>()

    // zoom variables

    val LOGNAME = "panzoom"

    // drawing
    var highlightPaintBrush = Paint(Color.BLUE)
    var standardPaintBrush = Paint(Color.BLUE)

    // we save a lot of points because they need to be processed
    // during touch events e.g. ACTION_MOVE
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    var old_x1 = 0f
    var old_y1 = 0f
    var old_x2 = 0f
    var old_y2 = 0f
    var mid_x = -1f
    var mid_y = -1f
    var old_mid_x = -1f
    var old_mid_y = -1f
    var p1_id = 0
    var p1_index = 0
    var p2_id = 0
    var p2_index = 0

    // store cumulative transformations
    // the inverse matrix is used to align points with the transformations - see below
    var currentMatrix = Matrix()
    var inverse = Matrix()

    // image to display
    var bitmap: Bitmap? = null

    init {
        scaleType = ScaleType.CENTER_CROP
        highlightPaintBrush.style = Paint.Style.STROKE
        highlightPaintBrush.strokeWidth = 25f
        highlightPaintBrush.color = Color.rgb(255, 255, 224)

        standardPaintBrush.style = Paint.Style.STROKE
        standardPaintBrush.strokeWidth = 5f
        standardPaintBrush.color = Color.BLACK
    }

    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        counter += 1
        onTouchEventZoom(event)
        return true
    }

    // set image as background
    fun setImage(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var newFirstTime = false
        if (oldWidth == null){
            newFirstTime = true
        }
        if (newFirstTime){
            oldWidth = w.toFloat()
            oldHeight = h.toFloat()
            return
        }
//        currentMatrix.preScale(w.toFloat()/oldw.toFloat(), h.toFloat()/oldh.toFloat())
        val sx = w.toFloat()/oldWidth!!
        val sy = h.toFloat()/oldHeight!!
        val god = centerCropMatrix(this)
        updateChangeOrientation(sx, sx)
        oldWidth = w.toFloat()
        oldHeight = h.toFloat()
    }

    private fun updateChangeOrientation(sx: Float, sy: Float){
        val newMatrix1 = Matrix()
        newMatrix1.preScale(sx, sy)
        for (myPath in addedPaths){
            myPath.path.transform(newMatrix1)
        }
        for (pathPoint in addedPathPoints){
            for (point in pathPoint){
                point.x = point.x * sx
                point.y = point.y * sy
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.concat(currentMatrix)

        // draw background
        if (bitmap != null) {
            setImageBitmap(bitmap)
        }
        // draw lines over it
        for (path in addedPaths) {
            if (path.drawType == DrawType.Highlight) {
                canvas.drawPath(path.path, highlightPaintBrush)
            }
            else{
                canvas.drawPath(path.path, standardPaintBrush)
            }
        }

        if (InteractionState.HIGHLIGHT_DRAW == interactionState) {
            canvas.drawPath(currentPath, highlightPaintBrush)
        }
        else{
            canvas.drawPath(currentPath, standardPaintBrush)
        }
        super.onDraw(canvas)
    }

    fun undo(){
        if (undoQueue.size == 0){
            return
        }
        val lastAction = undoQueue[undoQueue.size - 1]

        when (lastAction.actionType) {
            ActionType.HighlightDraw -> {
                removeAddedPathById(lastAction.myPath.id)
            }
            ActionType.StandardDraw -> {
                removeAddedPathById(lastAction.myPath.id)
            }
            ActionType.Erase -> {
                addedPaths.add(lastAction.myPath)
                addedPathPoints.add(lastAction.pathPoints)
            }
        }

        redoQueue.add(Actions(lastAction.actionType, lastAction.myPath, lastAction.pathPoints))
        undoQueue.removeAt(undoQueue.size - 1)

        forceRedraw()
    }

    fun redo() {
        if (redoQueue.size == 0){
            return
        }
        val lastActionRemoved = redoQueue[redoQueue.size - 1]

        when (lastActionRemoved.actionType) {
            ActionType.HighlightDraw -> {
                addedPaths.add(lastActionRemoved.myPath)
                addedPathPoints.add(lastActionRemoved.pathPoints)
            }
            ActionType.StandardDraw -> {
                // same as Highlight draw
                addedPaths.add(lastActionRemoved.myPath)
                addedPathPoints.add(lastActionRemoved.pathPoints)
            }
            ActionType.Erase -> {
                removeAddedPathById(lastActionRemoved.myPath.id)
            }
        }

        redoQueue.removeAt(redoQueue.size - 1)
        undoQueue.add(Actions(lastActionRemoved.actionType, lastActionRemoved.myPath, lastActionRemoved.pathPoints))

        forceRedraw()
    }

    private fun removeAddedPathById(id: Int): Int?{
        for(index in 0 until addedPaths.size){
            if (addedPaths[index].id == id){
                addedPaths.removeAt(index)
                addedPathPoints.removeAt(index)
                return index
                break
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun set(){
        val file = File(context.filesDir, "messi")
        file.createNewFile()
        file.writeText("bolsonaro")
    }

    fun get(){
        val file = File(context.filesDir, "messi")
        println(file.readText())
    }

    fun getState(): StatePage{
        return StatePage(addedPaths, addedPathPoints, currentPathId, undoQueue, redoQueue, oldWidth, oldHeight)
    }


        fun onTouchEventZoom(event: MotionEvent): Boolean {
            var inverted = floatArrayOf()
            when (event.pointerCount) {
                1 -> {
                    p1_id = event.getPointerId(0)
                    p1_index = event.findPointerIndex(p1_id)

                    // invert using the current matrix to account for pan/scale
                    // inverts in-place and returns boolean
                    inverse = Matrix()
                    currentMatrix.invert(inverse)

                    // mapPoints returns values in-place
                    inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
                    inverse.mapPoints(inverted)
                    x1 = inverted[0]
                    y1 = inverted[1]
                    if (interactionState == InteractionState.HIGHLIGHT_DRAW ||
                            interactionState == InteractionState.STANDARD_DRAW) {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                Log.d(LOGNAME, "Action down")
                                currentPath = Path()

                                currentPath.moveTo(x1, y1)
                                currentPathPoints = ArrayList()
                                currentPathPoints.add(Point(x1, y1))
                            }
                            MotionEvent.ACTION_MOVE -> {
                                Log.d(LOGNAME, "Action move")
                                currentPath.lineTo(x1, y1)
                                // mine
                                if (currentPathPoints.size > 0){
                                    val lastPoint = currentPathPoints[currentPathPoints.size - 1]
                                    currentPathPoints.add(Point((x1 + lastPoint.x)/2,
                                        (y1 + lastPoint.y)/2))
                                }
                                currentPathPoints.add(Point(x1, y1))
                            }
                            MotionEvent.ACTION_UP -> {
                                Log.d(LOGNAME, "Action up")
                                val newPath = if (interactionState==InteractionState.HIGHLIGHT_DRAW){
                                    MyPath(currentPathId, currentPath, DrawType.Highlight)
                                } else{
                                    MyPath(currentPathId, currentPath, DrawType.Standard)
                                }
                                currentPathId += 1
                                addedPaths.add(newPath)
                                // mine

                                // update paths
                                addedPathPoints.add(currentPathPoints)

                                // undo, redo queue
                                if (interactionState == InteractionState.HIGHLIGHT_DRAW) {
                                    undoQueue.add(
                                        Actions(
                                            ActionType.HighlightDraw,
                                            newPath,
                                            currentPathPoints
                                        )
                                    )
                                }
                                else{
                                    undoQueue.add(
                                        Actions(
                                            ActionType.StandardDraw,
                                            newPath,
                                            currentPathPoints
                                        )
                                    )
                                }
                                redoQueue.clear()

                                currentPath = Path()
                            }
                        }
                    }
                    if (interactionState == InteractionState.ERASE){
                        var foundIntersection = false
                        for(index in 0 until addedPathPoints.size){
                            val pathPointss = addedPathPoints[index]
                            for (point in pathPointss){
                                val distance = distance(point, Point(x1, y1), getCurrentScales())
                                if (distance < eraseTolerance) {
                                    // undo, redo queue
                                    undoQueue.add(Actions(ActionType.Erase, addedPaths[index], pathPointss))
                                    redoQueue.clear()

                                    // update paths
                                    addedPaths.removeAt(index)
                                    addedPathPoints.removeAt(index)
                                    foundIntersection = true
                                    forceRedraw()
                                    break
                                }
                            }
                            if (foundIntersection){
                                break
                            }
                        }
                    }
                }
                2 -> {
                    // point 1
                    p1_id = event.getPointerId(0)
                    p1_index = event.findPointerIndex(p1_id)

                    // mapPoints returns values in-place
                    inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
                    inverse.mapPoints(inverted)

                    // first pass, initialize the old == current value
                    if (old_x1 < 0 || old_y1 < 0) {
                        x1 = inverted.get(0)
                        old_x1 = x1
                        y1 = inverted.get(1)
                        old_y1 = y1
                    } else {
                        old_x1 = x1
                        old_y1 = y1
                        x1 = inverted.get(0)
                        y1 = inverted.get(1)
                    }

                    // point 2
                    p2_id = event.getPointerId(1)
                    p2_index = event.findPointerIndex(p2_id)

                    // mapPoints returns values in-place
                    inverted = floatArrayOf(event.getX(p2_index), event.getY(p2_index))
                    inverse.mapPoints(inverted)

                    // first pass, initialize the old == current value
                    if (old_x2 < 0 || old_y2 < 0) {
                        x2 = inverted.get(0)
                        old_x2 = x2
                        y2 = inverted.get(1)
                        old_y2 = y2
                    } else {
                        old_x2 = x2
                        old_y2 = y2
                        x2 = inverted.get(0)
                        y2 = inverted.get(1)
                    }

                    // midpoint
                    mid_x = (x1 + x2) / 2
                    mid_y = (y1 + y2) / 2
                    old_mid_x = (old_x1 + old_x2) / 2
                    old_mid_y = (old_y1 + old_y2) / 2

                    // distance
                    val d_old =
                        Math.sqrt(Math.pow((old_x1 - old_x2).toDouble(), 2.0) + Math.pow((old_y1 - old_y2).toDouble(), 2.0))
                            .toFloat()
                    val d = Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0))
                        .toFloat()

                    // pan and zoom during MOVE event
                    if (event.action == MotionEvent.ACTION_MOVE) {
                        Log.d(LOGNAME, "Multitouch move")
                        // pan == translate of midpoint
                        val dx = mid_x - old_mid_x
                        val dy = mid_y - old_mid_y
                        currentMatrix.preTranslate(dx, dy)
                        Log.d(LOGNAME, "translate: $dx,$dy")

                        // zoom == change of spread between p1 and p2
                        var scale = d / d_old
                        scale = Math.max(0f, scale)
                        currentMatrix.preScale(scale, scale, mid_x, mid_y)
                        Log.d(LOGNAME, "scale: $scale")

                        // reset on up
                    } else if (event.action == MotionEvent.ACTION_UP) {
                        old_x1 = -1f
                        old_y1 = -1f
                        old_x2 = -1f
                        old_y2 = -1f
                        old_mid_x = -1f
                        old_mid_y = -1f
                    }
                }
                else -> {
                }
            }
            return true
        }

    private fun forceRedraw(){
        invalidate()
        postInvalidate()
        currentPath = Path()
        currentPath.moveTo(x1, y1)
    }

    private fun getCurrentScales(): Pair<Float, Float>{
        val f = FloatArray(9)
        currentMatrix.getValues(f)

        val scaleX = f[Matrix.MSCALE_X]
        val scaleY = f[Matrix.MSCALE_Y]

        return Pair(scaleX, scaleY)
    }

        fun centerCropMatrix(view: ImageView): Matrix? {
            val image = view.drawable
            val imageWidth = image.intrinsicWidth
            val imageViewWidth = view.width
            val scaleX = imageViewWidth.toFloat() / imageWidth
            val imageHeight = image.intrinsicHeight
            val imageViewHeight = view.height
            val scaleY = imageViewHeight.toFloat() / imageHeight
            val maxScale = Math.max(scaleX, scaleY)
            val width = imageWidth * maxScale
            val height = imageHeight * maxScale
            val tx = Math.round((imageViewWidth - width) / 2f)
            val ty = Math.round((imageViewHeight - height) / 2f)
            val matrix = Matrix()
            matrix.postScale(maxScale, maxScale)
            matrix.postTranslate(tx.toFloat(), ty.toFloat())
            return matrix
        }
    }
}




enum class InteractionState{
    HIGHLIGHT_DRAW,
    STANDARD_DRAW,
    ERASE,
    MANIPULATION,
}

class MyPath(
    val id: Int,
    val path: Path,
    val drawType: DrawType,
)

enum class DrawType{
    Standard,
    Highlight
}

enum class ActionType{
    HighlightDraw,
    StandardDraw,
    Erase,
}

class Actions(val actionType: ActionType, val myPath: MyPath, val pathPoints: ArrayList<Point>)

class Point(
    var x: Float,
    var y: Float)

fun distance(p1: Point, p2: Point, scales: Pair<Float, Float>): Float{
    val xdiff = (p1.x - p2.x)//*scales.first
    val ydiff = (p1.y - p2.y)//*scales.second
    return sqrt(xdiff*xdiff + ydiff*ydiff)
}