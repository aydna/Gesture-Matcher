import javafx.scene.control.Label
import javafx.scene.control.ToolBar
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region

class StatusView(model: Model) : IView, HBox() {
    val actionLabel = Label()
    val m = model

    init {
        actionLabel.text = "   " + model.images.size.toString() + " images loaded"
        this.children.add(actionLabel)
    }

    override fun update() {
        actionLabel.text = "   " + m.images.size.toString() + " image(s) loaded"
    }
}