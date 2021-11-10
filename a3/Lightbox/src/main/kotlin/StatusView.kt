import javafx.scene.control.Label
import javafx.scene.control.ToolBar
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region

class StatusView(model: Model) : IView, HBox() {
    val actionLabel = Label()
    val frameLabel = Label()
    var myModel = model

    init {
        actionLabel.text = "test"
        //actionLabel.minHeight = 5.0
        this.children.add(actionLabel)
    }

    override fun update() {
    }
}