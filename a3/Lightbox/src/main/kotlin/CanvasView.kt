import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.io.File

class CanvasView(model: Model): IView, ScrollPane() {

    val m = model
    var p = Pane()
    var g = Group()
    var images = ArrayList<ImageView>()
    init {
        g.children.add(p)
        this.content = g
        g.isAutoSizeChildren = false
        this.hbarPolicy = ScrollBarPolicy.AS_NEEDED
        this.vbarPolicy = ScrollBarPolicy.AS_NEEDED
        //this.isPannable = true
        //p.minWidthProperty().bind(this.widthProperty().subtract(10))
        //p.minHeightProperty().bind(this.heightProperty().subtract(10))

        // deselect
        this.setOnMousePressed {
            m.isSelected = false
            m.currSelected = ImageView()
            println("trigger1")
        }
    }

    fun addImage(currImage: ImageView) {
        p.children.add(currImage)
    }

    fun delImage(currImage: ImageView) {
        p.children.remove(currImage)
    }

    override fun update() {
        if (m.fileToAdd != "") {
            addImage(m.imageToAdd)
        }
        if (m.delImg) {
            delImage(m.imageToDel)
        }
    }
}