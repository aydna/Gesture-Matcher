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

        // update canvas
        notifyViews()
        fileToAdd = "" // currently no image on the block

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
            currSelected = currImage
            //currImage.effect = DropShadow(5.0, Color.BLUE)
            println(currSelected)
            event.consume() // so pane event doesn't trigger
        }

        currImage.setOnMouseDragged { event ->
            if (state == STATE.DRAG) {
                val dx = event.sceneX - startX
                val dy = event.sceneY - startY

                if (currImage.boundsInParent.minX + dx >= 0.0 && currImage.boundsInParent.maxX + dx <= (s.width - 5)) {
                    currImage.x += dx
                    startX = event.sceneX
                }

                if (currImage.boundsInParent.minY + dy >= 0.0 && currImage.boundsInParent.maxY + dy <= (s.height - 5)) {
                    currImage.y += dy
                    startY = event.sceneY
                }
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
        s.p.prefWidth = highestX
        s.p.prefHeight = highestY
    }

    fun delImage() {
        if (isSelected) {
            delImg = true
            imageToDel = currSelected
            images.remove(imageToDel)
            findNewBound()
            notifyViews()
            delImg = false
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
        //currSelected.transforms.add(Rotate(10.0))
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