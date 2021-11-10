import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage


class Main : Application() {
    override fun start(stage: Stage) {
        val model = Model(stage)

        // our layout is the root of the scene graph
        val root = BorderPane()

        // views are the children of the top-level layout
        val toolbar = ToolbarView(model, stage)
        val status = StatusView(model)
        val pane = CanvasView(model) // our container for pics? generic container (maybe scrollpane even?)

        // register views with the model
        model.addView(toolbar)
        model.addView(status)
        model.addView(pane)

        // resizing cap
        stage.minWidth = 400.0
        stage.maxWidth = 1600.0
        stage.minHeight = 300.0
        stage.maxHeight = 1200.0

        // setup and display
        //root.children.addAll(toolbar, pane, status) // gridView
        root.top = toolbar
        root.center = pane
        root.bottom = status
        stage.scene = Scene(root)
        stage.width = 1000.0
        stage.height = 750.0
        stage.title = "lightbox (yz8yang)"
        stage.show()
    }
}