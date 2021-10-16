import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ToolbarView(model: Model) : IView, ToolBar() {
    init {

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

        // add buttons to toolbar
        this.items.add(blockButton)
        this.items.add(beehiveButton)
        this.items.add(blinkerButton)
        this.items.add(toadButton)
        this.items.add(gliderButton)
        this.items.add(clearButton)

    }

    override fun update() {
        // update my button state
        // how do we get data from the model?
    }
}