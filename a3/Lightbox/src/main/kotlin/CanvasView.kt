import javafx.scene.Group
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
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

        p.prefHeight = model.outerY
        p.prefWidth = model.outerX
        this.content = p

        // deselect
        this.setOnMousePressed {
            m.isSelected = false
            m.currSelected.effect = null // get rid of shadow
            m.currSelected = ImageView()
            m.disableDel() // disable del button
        }
    }

    private fun addImage(currImage: ImageView) {
        val ranX = ThreadLocalRandom.current().nextInt(130, 300)
        val ranY = ThreadLocalRandom.current().nextInt(100, 250)

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
        m.findNewBound()
        p.children.add(currImage)
    }

    private fun delImage(currImage: ImageView) {
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