import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage


class ToolbarView(model: Model, stage: Stage) : IView, ToolBar() {

    private val m = model
    private val cascadeButton = RadioButton("Cascade")
    private val tileButton = RadioButton("Tile")
    private val delButton = Button("Delete")

    init {
        val fileChooser = FileChooser()
        fileChooser.title = "Image File"

        val addButton = Button("Add")
        addButton.graphic = ImageView(Image("add.jpg", 16.0, 16.0, true, true))

        delButton.graphic = ImageView(Image("del.png", 16.0, 16.0, true, true))

        val rLeftButton = Button("Rotate Left")
        rLeftButton.graphic = ImageView(Image("rLeft.png", 16.0, 16.0, true, true))
        val rRightButton = Button("Rotate Right")
        rRightButton.graphic = ImageView(Image("rRight.png", 15.0, 15.0, true, true))

        val zoomInButton = Button("Zoom-in")
        zoomInButton.graphic = ImageView(Image("zoomIn.png", 17.0, 17.0, true, true))
        val zoomOutButton = Button("Zoom-out")
        zoomOutButton.graphic = ImageView(Image("zoomOut1.png", 14.0, 14.0, true, true))

        val resetButton = Button("Reset")
        resetButton.graphic = ImageView(Image("reset.png", 16.0, 16.0, true, true))

        cascadeButton.graphic = ImageView(Image("cascade.png", 16.0, 16.0, true, true))
        tileButton.graphic = ImageView(Image("tile.png", 18.0, 18.0, true, true))

        addButton.setOnAction {
            val currFile = fileChooser.showOpenDialog(stage)
            if (currFile != null) {
                model.addImage(currFile)
            }
        }

        // create radio buttons

        val toggleGroup = ToggleGroup()

        //val cascadeButton = RadioButton("Cascade")
        cascadeButton.toggleGroup = toggleGroup
        cascadeButton.isSelected = true

        //val tileButton = RadioButton("Tile")
        tileButton.toggleGroup = toggleGroup


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

        cascadeButton.setOnAction {
            cascadeButton.isSelected = true
            //tileButton.isSelected = false
        }

        tileButton.setOnAction {
            //cascadeButton.isSelected = false
            tileButton.isSelected = true
            model.tile()
        }

        val separator = Separator()
        val separator1 = Separator()

        // disable delete button when not usable
        delButton.isDisable = true

        // add buttons to toolbar
        this.items.add(addButton)
        this.items.add(delButton)
        this.items.add(separator)
        this.items.add(rLeftButton)
        this.items.add(rRightButton)
        this.items.add(zoomInButton)
        this.items.add(zoomOutButton)
        this.items.add(resetButton)
        this.items.add(separator1)
        this.items.add(cascadeButton)
        this.items.add(tileButton)
    }

    override fun update() {
        if (m.isCascade) {
            cascadeButton.isSelected = true
            tileButton.isSelected = false
        }
        if (m.isSelected) {
            delButton.isDisable = false // activate delete button
        }
        else if (!m.isSelected) {
            delButton.isDisable = true
        }
    }
}