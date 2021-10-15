import javafx.scene.control.Label
import javafx.scene.layout.HBox

class StatusView(model: Model) : IView, HBox() {
    var frame = 0
    var currAction = ""
    val actionLabel = Label()
    val frameLabel = Label()
    var myModel = model

    init {
    }

    override fun update() {
        // react to updates from model
        // how do we get data from the model? do we need it?
        // get coordinates

        actionLabel.text = "    " + myModel.coordinates[0].toString() + ", " + myModel.coordinates[1].toString()

        this.children.add(actionLabel)
    }
}