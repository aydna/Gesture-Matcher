import javafx.scene.Node
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.util.*
import kotlin.collections.HashMap

class GridView(model: Model): IView, GridPane() {

    private val nodeMap = hashMapOf<Int, Node>()
    val rMultiplier = 103
    val cMultiplier = 97

    init {
        // create a grid and attach as the root node
        //val scene = Scene(pane)

        // populate the grid here
        this.isGridLinesVisible = true
        this.hgap = 1.0
        this.vgap = 1.0

        for (i in 0..74) {
            for (j in 0..49) {
                val gridNode = Rectangle(12.0, 12.0)
                gridNode.fill = Color.WHITE

                gridNode.setOnMouseClicked {
                    if (gridNode.fill == Color.WHITE) {
                        gridNode.fill = Color.BLACK
                    } else {
                        gridNode.fill = Color.WHITE
                    }
                    val x = GridPane.getColumnIndex(gridNode)
                    val y = GridPane.getRowIndex(gridNode)
                    println(getNode(x, y))
                    println(x)
                    println(y)
                    model.coordinates[0] = x
                    model.coordinates[1] = y
                    model.notifyViews()
                }

                nodeMap[i * rMultiplier + j * cMultiplier] = gridNode
                this.add(gridNode, i, j)
            }
        }


    }

    private fun getNode(row: Int, column: Int): Node? {
        return nodeMap[row * rMultiplier + column * cMultiplier]
    }


    override fun update() {
        //
    }
}