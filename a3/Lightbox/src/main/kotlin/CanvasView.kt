import javafx.scene.Group
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class CanvasView(model: Model): IView, ScrollPane() {

    val m = model
    var p = AnchorPane()
    var g = Group()
    //var images = ArrayList<ImageView>()
    init {
        this.hbarPolicy = ScrollBarPolicy.AS_NEEDED
        this.vbarPolicy = ScrollBarPolicy.AS_NEEDED
        //this.isFitToWidth = false
        //this.isFitToHeight = true
        //p.minWidthProperty().bind(this.widthProperty().subtract(5))
        //p.minHeightProperty().bind(this.heightProperty().subtract(5))
        p.prefHeight = model.outerY
        p.prefWidth = model.outerX
        g.children.add(p)
        this.content = p
        g.isAutoSizeChildren = false

        //this.isPannable = true

        // deselect
        this.setOnMousePressed {
            m.isSelected = false
            m.currSelected.effect = null // get rid of shadow
            m.currSelected = ImageView()
            println(this.widthProperty())
            println(this.heightProperty())
            println(p.height)
            println(p.width)
        }
    }

    fun addImage(currImage: ImageView) {
        val ranX = ThreadLocalRandom.current().nextInt(130, 400)
        val ranY = ThreadLocalRandom.current().nextInt(100, 300)

        currImage.translateX = ranX.toDouble()
        currImage.translateY = ranY.toDouble()

        if (currImage.boundsInParent.maxX > m.outerX) {
            m.outerX = currImage.boundsInParent.maxX // update prefWidth
            p.prefWidth = m.outerX
        }
        if (currImage.boundsInParent.maxY > m.outerY) {
            m.outerY = currImage.boundsInParent.maxY
            p.prefHeight = m.outerY
        }
        p.children.add(currImage)
    }

    fun delImage(currImage: ImageView) {
        p.children.remove(currImage)
    }

    /*
    fun tile() {
        p.children.removeAll()
        val paneWidth = p.width
        val paneHeight = p.height
        val currX = 5.0
        val currY = 5.0
        val numFitX = p.width.div(300.0) // number of columns of images we can have

        for (i in m.images) {
            val imgHeight = i.fitHeight
            val imgWidth = i.fitWidth

            if ()

        }
    }
*/
    override fun update() {
        if (m.fileToAdd != "") {
            addImage(m.imageToAdd)
        }
        if (m.delImg) {
            delImage(m.imageToDel)
        }
    }
}