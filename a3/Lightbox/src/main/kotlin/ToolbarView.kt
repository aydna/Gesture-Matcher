import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage


class ToolbarView(model: Model, stage: Stage) : IView, ToolBar() {
    init {

        val fileChooser = FileChooser()
        fileChooser.title = "Image File"

        val addButton = Button("Add")

        val delButton = Button("Delete")

        val rLeftButton = Button("Rotate Left")
        val rRightButton = Button("Rotate Right")

        addButton.setOnAction {
            val currFile = fileChooser.showOpenDialog(stage)
            model.addImage(currFile)
        }

        delButton.setOnAction { event->
            model.delImage()
        }

        rLeftButton.setOnAction {
            model.rotateLeft()
        }

        // add buttons to toolbar
        this.items.add(addButton)
        this.items.add(delButton)
        this.items.add(rLeftButton)
    }

    override fun update() {
        // Doesn't do anything
    }
}