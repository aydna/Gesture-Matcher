import javafx.scene.control.Label
import javafx.scene.layout.HBox

class StatusView(model: Model) : IView, HBox() {
    var frame = 0
    var currAction = ""
    val actionLabel = Label()
    val frameLabel = Label()
    var myModel = model

    init {
        this.children.add(actionLabel)
    }

    override fun update() {
        // react to updates from model
        // how do we get data from the model? do we need it?
        // get coordinates
        if (myModel.lastSelected == "Cleared Board" || myModel.lastSelected == "") {
            actionLabel.text = "     " + myModel.lastSelected
        }
        else {
            actionLabel.text =
                "    Created " + myModel.lastSelected + " at (" + myModel.coordinates[0].toString() + ", " + myModel.coordinates[1].toString() + ")"
        }
    }
}