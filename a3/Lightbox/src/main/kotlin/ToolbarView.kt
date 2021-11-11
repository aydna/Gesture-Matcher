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

        val zoomInButton = Button("Zoom-in")
        val zoomOutButton = Button("Zoom-out")

        val resetButton = Button("Reset")

        addButton.setOnAction {
            val currFile = fileChooser.showOpenDialog(stage)
            model.addImage(currFile)
        }

        delButton.setOnAction { event->
            model.delImage()
        }

        rLeftButton.setOnAction {
            model.rotate("l")
        }

        rRightButton.setOnAction {
            model.rotate("r")
        }

        zoomInButton.setOnAction {
            model.scale("in")
        }

        zoomOutButton.setOnAction {
            model.scale("out")
        }

        resetButton.setOnAction {
            model.reset()
        }


        // add buttons to toolbar
        this.items.add(addButton)
        this.items.add(delButton)
        this.items.add(rLeftButton)
        this.items.add(rRightButton)
        this.items.add(zoomInButton)
        this.items.add(zoomOutButton)
        this.items.add(resetButton)
    }

    override fun update() {
        // Doesn't do anything
    }
}