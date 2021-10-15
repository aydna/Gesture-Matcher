import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage?) {
        val model = Model()

        // our layout is the root of the scene graph
        val root = VBox()

        // views are the children of the top-level layout
        val toolbar = ToolbarView(model)
        val gridView = GridView(model)
        val status = StatusView(model)

        // register views with the model
        model.addView(toolbar)
        model.addView(gridView)
        model.addView(status)

        // setup and display
        root.children.addAll(toolbar, gridView, status) // gridView
        stage?.scene = Scene(root)
        stage?.isResizable = false
        stage?.width = 974.0
        stage?.height = 736.0
        stage?.title = "Conway's Game of Life"
        stage?.show()
    }
}