import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode


class ToolbarView(model: Model) : IView, ToolBar() {
    init {

        var firstTime = true
        // manual mode support
        this.setOnKeyPressed { event->
            if (event.code === KeyCode.ENTER && model.pause) {
                model.advance()
            }
            else if (event.code === KeyCode.M) {
                model.manualSwap()
                if (firstTime) {
                    firstTime = false
                    val infoBox = Alert(AlertType.INFORMATION)
                    infoBox.title = "About Manual Mode"
                    infoBox.headerText = "Entering Manual Mode"
                    infoBox.contentText = "Manual mode lets you step through the animation frame-by-frame. You can hit the M key to toggle, and use the Enter/Return key to advance frames."
                    infoBox.showAndWait()
                }
            }
        }

        val optionsToggle = Button("About Manual Mode")
        optionsToggle.graphic = ImageView(Image("info.png", 20.0, 20.0, false, false))

        val blockButton = Button("Block")
        blockButton.graphic = ImageView(Image("block.png", 20.0, 20.0, false, false))

        val beehiveButton = Button("Beehive")
        beehiveButton.graphic = ImageView(Image("beehive.png", 26.0, 20.0, false, false))

        val blinkerButton = Button("Blinker")
        blinkerButton.graphic = ImageView(Image("blinker.png", 20.0, 20.0, false, false))

        val toadButton = Button("Toad")
        toadButton.graphic = ImageView(Image("toad.png", 28.0, 20.0, false, false))

        val gliderButton = Button("Glider")
        gliderButton.graphic = ImageView(Image("glider.png", 20.0, 20.0, false, false))

        val clearButton = Button("Clear")
        clearButton.graphic = ImageView(Image("clear.png", 20.0, 20.0, false, false))

        clearButton.setOnAction { event->
            model.clearBoard()
            model.notifyViews()
        }

        blockButton.setOnAction { event ->
            model.currSelected = "block"
        }

        beehiveButton.setOnAction { event ->
            model.currSelected = "beehive"
        }

        blinkerButton.setOnAction { event ->
            model.currSelected = "blinker"
        }

        toadButton.setOnAction { event->
            model.currSelected = "toad"
        }

        gliderButton.setOnAction { event->
            model.currSelected = "glider"
        }

        optionsToggle.setOnAction {
            firstTime = false
            model.currSelected = ""
            val infoBox = Alert(AlertType.INFORMATION)
            infoBox.title = "About Manual Mode"
            infoBox.headerText = "Entering Manual Mode"
            infoBox.contentText = "Manual mode lets you step through the animation frame-by-frame. You can press the M key to toggle, and use the Enter/Return key to advance frames."
            infoBox.showAndWait()
        }

        // add buttons to toolbar
        this.items.add(blockButton)
        this.items.add(beehiveButton)
        this.items.add(blinkerButton)
        this.items.add(toadButton)
        this.items.add(gliderButton)
        this.items.add(clearButton)
        this.items.add(optionsToggle)
    }

    override fun update() {
        // Doesn't do anything
    }
}