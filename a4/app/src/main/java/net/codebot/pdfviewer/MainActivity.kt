package net.codebot.pdfviewer

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so we should expect people to need this.
// We may wish to provide this code.
class MainActivity : AppCompatActivity() {
    val LOGNAME = "pdf_viewer"
    val FILENAME = "shannon1948.pdf"
    val FILERESID = R.raw.shannon1948
    var currentIndex = 0
    var currentInteractionState = InteractionState.HIGHLIGHT_DRAW
    private val stateModel = StateModel(mutableMapOf())

    // manage the pages of the PDF, see below
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    private var currentPage: PdfRenderer.Page? = null

    // custom ImageView class that captures strokes and draws them over the image
    private lateinit var pageImage: PDFImage

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("my on create")

        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        println("my on resume")
        // open page 0 of the PDF
        // it will be displayed as an image in the pageImage (above)
        try {
            moveToPage(currentIndex)
        } catch (exception: IOException) {
            Log.d(LOGNAME, "Error opening PDF")
        }
    }

    override fun onStop() {
        super.onStop()
        println("my on stop")
        try {
            saveStateModel()
            closeRenderer()
        } catch (ex: IOException) {
            Log.d(LOGNAME, "Unable to close PDF renderer")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        println("on configuration changed")
        // Checks the orientation of the screen
        saveStateModel()
        moveToPage(currentIndex)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("my on save instance state")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        println("my on restore instance state")
    }

    private fun moveToPage(index: Int){
        println("my on moving to page")
        println(stateModel.map.keys)
        initializePage()

        openRenderer(this)

        loadStateModel(index)
        showPage(index)
        pageImage.interactionState = currentInteractionState

        val text = findViewById<TextView>(R.id.textView2)
        val pageCount = pdfRenderer.pageCount
        text.text = "shannon 1984, page ${index+1}/$pageCount"
    }

    private fun initializePage(){
        pageImage = PDFImage(this)

        val layout = findViewById<FrameLayout>(R.id.otherLayout)
        layout.isEnabled = true
        if (layout.size > 0) {
            layout.removeViewAt(layout.size - 1)
        }
        layout.addView(pageImage)

        //pageImage.layoutParams.height = Resources.getSystem().displayMetrics.heightPixels
        //pageImage.layoutParams.width = Resources.getSystem().displayMetrics.widthPixels

        buttonsOnClick()
    }

    private fun buttonsOnClick(){
        val undoButton = findViewById<Button>(R.id.button)

        val redoButton = findViewById<Button>(R.id.button2)

        undoButton.setOnClickListener {
            pageImage.undo()
        }

        redoButton.setOnClickListener{
            pageImage.redo()
        }

        val eraseButton = findViewById<RadioButton>(R.id.radioButton3)

        eraseButton.setOnClickListener{
            pageImage.interactionState = InteractionState.ERASE
            currentInteractionState = InteractionState.ERASE
        }

        val drawingButton = findViewById<RadioButton>(R.id.radioButton2)

        drawingButton.setOnClickListener{
            pageImage.interactionState = InteractionState.STANDARD_DRAW
            currentInteractionState = InteractionState.STANDARD_DRAW
        }

        val highlightButton = findViewById<RadioButton>(R.id.radioButton4)
        highlightButton.setOnClickListener{
            pageImage.interactionState = InteractionState.HIGHLIGHT_DRAW
            currentInteractionState = InteractionState.HIGHLIGHT_DRAW
        }

        val backButton = findViewById<Button>(R.id.button3)

        backButton.setOnClickListener{
            if (currentIndex > 0){
                saveStateModel()
                currentIndex -= 1
                moveToPage(currentIndex)
            }
        }

        val forwardButton = findViewById<Button>(R.id.button4)

        forwardButton.setOnClickListener{
            saveStateModel()
            currentIndex += 1
            moveToPage(currentIndex)
        }
    }

    @Throws(IOException::class)
    private fun openRenderer(context: Context) {
        // In this sample, we read a PDF from the assets directory.
        val file = File(context.cacheDir, FILENAME)
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
            val asset = this.resources.openRawResource(FILERESID)
            val output = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var size: Int
            while (asset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            asset.close()
            output.close()
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        pdfRenderer = PdfRenderer(parcelFileDescriptor)
    }

    // do this before you quit!
    @Throws(IOException::class)
    private fun closeRenderer() {
        currentPage?.close()
        pdfRenderer.close()
        parcelFileDescriptor.close()
        currentPage = null
    }

    private fun showPage(index: Int) {
        if (pdfRenderer.pageCount <= index) {
            return
        }
        // Close the current page before opening another one.
        currentPage?.close()

        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index)

        if (currentPage != null) {
            // Important: the destination bitmap must be ARGB (not RGB).
            val bitmap = Bitmap.createBitmap(currentPage!!.getWidth(), currentPage!!.getHeight(), Bitmap.Config.ARGB_8888)

            // Here, we render the page onto the Bitmap.
            // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
            // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
            currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Display the page
            pageImage.setImage(bitmap)
        }
    }

    private fun saveStateModel(){
        stateModel.map[currentIndex] = pageImage.getState()
    }

    private fun loadStateModel(index: Int){
        val statePage = stateModel.map[index]
        if (statePage != null) {
            pageImage.addedPaths = statePage.addedPaths
            pageImage.addedPathPoints = statePage.addedPathPoints
            pageImage.currentPathId = statePage.currentId
            pageImage.undoQueue = statePage.undoQueue
            pageImage.redoQueue = statePage.redoQueue
            pageImage.oldWidth = statePage.oldWidth
            pageImage.oldHeight = statePage.oldHeight
        }
    }
}