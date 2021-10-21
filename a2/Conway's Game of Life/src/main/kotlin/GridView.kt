import javafx.scene.Node
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class GridView(model: Model): IView, GridPane() {

    private var nodeMap = hashMapOf<Pair<Int, Int>, Rectangle>()
    var myModel = model

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
                    val y = getColumnIndex(gridNode)
                    val x = getRowIndex(gridNode)
                    model.addShape(x, y)

                    // update board
                    model.notifyViews()
                }

                val currPair = Pair(j, i) // (row, col)
                nodeMap[currPair] = gridNode
                this.add(gridNode, i, j)
            }
        }


    }

    private fun getNode(row: Int, col: Int): Rectangle? {
        return nodeMap[Pair(row, col)]
    }


    override fun update() {
        for (x in 0..49) {
            for (y in 0..74) {
                if (myModel.getBoard(x, y) == true) {
                    getNode(x, y)?.fill = Color.BLACK
                }
                else {
                    getNode(x, y)?.fill = Color.WHITE
                }
            }
        }
    }
}