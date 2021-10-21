import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region

class StatusView(model: Model) : IView, HBox() {
    val actionLabel = Label()
    val frameLabel = Label()
    var myModel = model

    init {

        val region = Region()
        setHgrow(region, Priority.ALWAYS)
        this.children.add(actionLabel)
        this.children.add(region)
        this.children.add(frameLabel)

    }

    override fun update() {
        // react to updates from model
        // how do we get data from the model? do we need it?
        // get coordinates
        if (myModel.currSelected == "Cleared Board" || myModel.currSelected == "") {
            actionLabel.text = "     " + myModel.currSelected
        }
        else {
            actionLabel.text =
                "    Created " + myModel.currSelected + " at (" + myModel.coordinates[0].toString() + ", " + myModel.coordinates[1].toString() + ")"
        }
        frameLabel.text = "Frame " + myModel.frameCounter + "    "
    }
}