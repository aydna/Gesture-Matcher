import javafx.scene.control.ScrollPane
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.scene.transform.Rotate
import java.io.File

class Model() {

    var s = CanvasView(this)

    private val views = ArrayList<IView>()
    var images = ArrayList<ImageView>() // array of files (pictures)
    var fileToAdd = ""
    var imageToAdd = ImageView()
    var delImg = false
    var imageToDel = ImageView()

    var isSelected = false // is an img selected
    var currSelected = ImageView() // currently selected image
    var ds = DropShadow(5.0, Color.BLUE)

    private enum class STATE { NONE, DRAG }
    private var state = STATE.NONE
    private var startX = -1.0
    private var startY = -1.0

    val defaultRotation = 0.0 // image rotation
    val defaultScale = 1.0 // image scaling

    var rotationMap = mutableMapOf<ImageView, Double?>()
    var scaleMap = mutableMapOf<ImageView, Double?>()

    // keep track of furthest points (for scrollbar)
    var outerX = 0.0
    var outerY = 0.0

    var isCascade = true // cascade mode

    init {
    }

    fun addImage(file: File) {
        if (isImage(file)) {
            fileToAdd = file.toURI().toURL().toString()
        }
        else {
            // maybe add notification for non-valid file
            return
        }

        val currImage = ImageView(Image(fileToAdd))
        currImage.fitWidth = 300.0
        currImage.isPreserveRatio = true
        imageToAdd = currImage
        images.add(currImage) // add to images array

        currSelected.effect = null // get rid of current image selection dropshadow
        isSelected = true
        currImage.effect = ds
        currSelected = currImage

        // update canvas
        notifyViews()
        fileToAdd = "" // currently no image on the block
        if (!isCascade) {
            //tile mode
            tile()
        }

        // add dragging functionality
        currImage.setOnMousePressed { event ->
            startX = event.sceneX
            startY = event.sceneY
            state = STATE.DRAG
            currImage.toFront()
            currImage.effect = ds // dropshadow selection
            if (currSelected != currImage) {
                currSelected.effect = null // get rid of shadow
            }
            isSelected = true
            notifyViews() // enable del button
            currSelected = currImage
            //currImage.effect = DropShadow(5.0, Color.BLUE)
            println(currSelected)
            event.consume() // so pane event doesn't trigger
        }

        currImage.setOnMouseDragged { event ->
            if (state == STATE.DRAG) {
                val dx = event.sceneX - startX
                val dy = event.sceneY - startY
                val currOuterX = outerX
                val currOuterY = outerY

                // find way to get the full dimensions (bounds) of scrollpane
                if (currImage.boundsInParent.minX + dx >= 0.0 && currImage.boundsInParent.maxX + dx <= max(currOuterX, s.width - 5)) {
                    println(currOuterX)
                    currImage.x += dx
                    startX = event.sceneX
                }

                if (currImage.boundsInParent.minY + dy >= 0.0 && currImage.boundsInParent.maxY + dy <= max(currOuterY, s.height - 5)) {
                    currImage.y += dy
                    startY = event.sceneY
                }

                // switch back to cascade
                isCascade = true
                notifyViews()
            }
        }

        currImage.setOnMouseReleased {
            state = STATE.NONE
            // update furthest bounds
            findNewBound()
        }

        currImage.setOnMouseExited{
            state = STATE.NONE
        }

    }

    private fun max(a: Double, b: Double): Double {
        return if (a > b) a
        else b
    }

    fun disableDel() {
        notifyViews() // disable del button on pane click
    }

    fun findNewBound() {
        var highestX = 0.0
        var highestY = 0.0
        for (i in images) {
            if (i.boundsInParent.maxX > highestX) {
                highestX = i.boundsInParent.maxX
            }
            if (i.boundsInParent.maxY > highestY) {
                highestY = i.boundsInParent.maxY
            }
        }
        outerX = highestX
        outerY = highestY
        s.p.prefWidth = highestX
        s.p.prefHeight = highestY
    }

    fun delImage() {
        if (isSelected) {
            delImg = true
            imageToDel = currSelected
            images.remove(currSelected)
            isSelected = false
            findNewBound()
            notifyViews()
            delImg = false
            if (!isCascade) {
                //tile mode
                tile()
            }
        }
    }

    private fun isImage(file: File): Boolean {
        val picFileExt: Array<String> = arrayOf("jpeg", "jpg", "bmp", "png")
        return file.extension in picFileExt
    }

    // rotate selected image left
    fun rotate(dir: String) {
        if (!isSelected) {
            return
        }
        if (currSelected in rotationMap) {
            if (dir == "r") {
                rotationMap[currSelected] = rotationMap[currSelected]?.plus(10.0)
            }
            else {
                rotationMap[currSelected] = rotationMap[currSelected]?.minus(10.0)
            }
        }
        else {
            // add to rotationMap
            if (dir == "r") {
                rotationMap[currSelected] = 10.0
            }
            else {
                rotationMap[currSelected] = -10.0
            }
        }

        currSelected.rotate = rotationMap[currSelected]!!
        findNewBound()
        isCascade = true
        notifyViews()
    }

    // zoom-in/out scaling
    fun scale(dir: String) {
        if (!isSelected) {
            return
        }
        if (currSelected in scaleMap) {
            if (dir == "in") {
                scaleMap[currSelected] = scaleMap[currSelected]?.plus(0.25)
            }
            else {
                scaleMap[currSelected] = scaleMap[currSelected]?.minus(0.25)
            }
        }
        else {
            // add to rotationMap
            if (dir == "in") {
                scaleMap[currSelected] = 1.25
            }
            else {
                scaleMap[currSelected] = 0.75
            }
        }

        // check if image would be too small
        if (scaleMap[currSelected] == 0.0) {
            scaleMap[currSelected] = scaleMap[currSelected]?.plus(0.25) // don't allow that
        }

        currSelected.scaleX = scaleMap[currSelected]!!
        currSelected.scaleY = scaleMap[currSelected]!!
        findNewBound()
        println(outerX)
        println(outerY)
        isCascade = true
        notifyViews()
    }

    fun reset() {
        if (!isSelected) {
            return
        }
        rotationMap[currSelected] = defaultRotation
        scaleMap[currSelected] = defaultScale

        currSelected.rotate = defaultRotation
        currSelected.scaleX = defaultScale
        currSelected.scaleY = defaultScale
        findNewBound()
    }

    fun tile() {
        // reset all transformations first
        currSelected.effect = null
        for (img in images) {
            currSelected = img
            isSelected = true
            reset()
        }
        isSelected = false

        isCascade = false // switch to tile mode
        notifyViews() // disable del button

        var currX = 5.0
        val currY = 5.0
        val numFitX: Int = s.width.div(305.0).toInt() // number of columns of images we can have
        println(numFitX)
        var tileOffset = mutableListOf<Double>() // height offset
        for (k in 0..images.size) {
            tileOffset.add(0.0) // initialize all to 0
        }

        var i = 0
        while (i < images.size) {

            for (j in 0 until (numFitX.toInt())) {
                if (i >= images.size) {
                    break
                }
                if (i >= numFitX) {
                    // do diff stuff. no longer the first layer
                    val tmpHeight = 5.0 + tileOffset[i - numFitX] // check if this goes beyond pane
                    /* fk it try greedy for now
                    if (currY >= paneHeight) {
                    } */
                    images[i].translateX = 0.0
                    images[i].translateY = 0.0
                    images[i].x = currX
                    images[i].y = tmpHeight
                    println(tmpHeight)
                    println(currX)
                    tileOffset[i] = tmpHeight + images[i].boundsInParent.height
                    currX += 305.0
                }
                else {
                    images[i].translateX = 0.0
                    images[i].translateY = 0.0
                    images[i].x = currX
                    images[i].y = currY
                    currX += 305.0 // move right for next img placement; 5 units used for padding
                    tileOffset[i] = currY + images[i].boundsInParent.height // for offset
                }
                i += 1
            }
            //after a row is done
            currX = 5.0 // reset
        }
        findNewBound()
    }

    // view management
    fun addView(view: IView) {
        views.add(view)
    }

    private fun notifyViews() {
        for (view in views) {
            view.update()
        }
    }
}