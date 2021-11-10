import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.scene.transform.Rotate
import javafx.scene.transform.Scale
import javafx.stage.Stage
import java.io.File

class Model(stage: Stage) {

    val s = stage

    private val views = ArrayList<IView>()
    var files = ArrayList<File>() // array of files (pictures)
    var fileToAdd = ""
    var imageToAdd = ImageView()
    var delImg = false
    var imageToDel = ImageView()

    var isSelected = false // is an img selected
    var currSelected = ImageView() // currently selected image

    private enum class STATE { NONE, DRAG }
    private var state = STATE.NONE
    private var startX = -1.0
    private var startY = -1.0

    init {

    }

    fun addImage(file: File) {
        if (isImage(file)) {
            files.add(file) // add to images array
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

        // update canvas
        notifyViews()
        fileToAdd = "" // currently no image on the block

        // add dragging functionality
        currImage.setOnMousePressed { event ->
            startX = event.sceneX
            startY = event.sceneY
            state = STATE.DRAG
            currImage.toFront()
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

                if (currImage.boundsInParent.minX + dx >= 0.0 && currImage.boundsInParent.maxX + dx <= (s.width - 2)) {
                    currImage.x += dx
                    startX = event.sceneX
                }

                if (currImage.boundsInParent.minY + dy >= 0.0 && currImage.boundsInParent.maxY + dy <= (s.height - 88)) {
                    currImage.y += dy
                    startY = event.sceneY
                }
            }
        }

        currImage.setOnMouseReleased {
            state = STATE.NONE
        }

        currImage.setOnMouseExited{
            state = STATE.NONE
        }

    }

    fun delImage() {
        if (isSelected) {
            delImg = true
            imageToDel = currSelected
            notifyViews()
            delImg = false
        }
    }

    private fun isImage(file: File): Boolean {
        val picFileExt: Array<String> = arrayOf("jpeg", "jpg", "bmp", "png")
        return file.extension in picFileExt
    }

    // rotate selected image left
    fun rotateLeft() {
        if (!isSelected) {
            return
        }
        currSelected.rotate = 90.0
        //currSelected.transforms.add(Scale(2.0, 2.0))
        //currSelected.scaleX = 2.0
        //currSelected.scaleY = 2.0
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