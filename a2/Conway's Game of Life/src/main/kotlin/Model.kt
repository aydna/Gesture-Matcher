import javafx.scene.Node
import javafx.scene.layout.GridPane

class Model {
    // represent my board
    val adjustmentFactor = 5 // this is purely for edge collision cases
    private val sizeOuter = 50 + (adjustmentFactor * 2)
    private val sizeInner = 75 + (adjustmentFactor * 2)

    private val views = ArrayList<IView>()
    val board = Array(sizeOuter) { BooleanArray(sizeInner) } // init to false = dead
    var coordinates = intArrayOf(0, 0)
    var currSelected = "" // the currently selected shape

    // board manipulation
    // (a) add a shape
    fun addShape(x: Int, y: Int) {
        if (currSelected == "block") {
            addBlock(x, y)
        }
        else if (currSelected == "beehive") {
            addBeehive(x, y)
        }
        else if (currSelected == "blinker") {
            addBlinker(x, y)
        }
        else if (currSelected == "toad") {
            addToad(x, y)
        }
        else if (currSelected == "glider") {
            addGlider(x, y)
        }
    }

    fun getBoard(x: Int, y:Int): Boolean {
        return board[x + adjustmentFactor][y + adjustmentFactor]
    }

    fun setBoard(x: Int, y: Int, newVal: Boolean) {
        board[x + adjustmentFactor][y + adjustmentFactor] = newVal
    }

    fun addBlock(x: Int, y: Int) {
        setBoard(x, y, true)
        setBoard(x + 1, y + 1, true)
        setBoard(x + 1, y, true)
        setBoard(x, y + 1, true)
    }

    fun addBeehive(x: Int, y: Int) {
        setBoard(x+1, y, true)
        setBoard(x+2, y, true)
        setBoard(x, y+1, true)
        setBoard(x+3, y+1, true)
        setBoard(x+1, y+2, true)
        setBoard(x+2, y+2, true)
    }

    fun addBlinker(x: Int, y: Int) {
        setBoard(x, y+1, true)
        setBoard(x+1, y+1, true)
        setBoard(x+2, y+1, true)
    }

    fun addToad(x: Int, y: Int) {
        setBoard(x+1, y, true)
        setBoard(x+2, y, true)
        setBoard(x+3, y, true)
        setBoard(x, y+1, true)
        setBoard(x+1, y+1, true)
        setBoard(x+2, y+1, true)
    }

    fun addGlider(x: Int, y: Int) {
        setBoard(x+2, y, true)
        setBoard(x, y+1, true)
        setBoard(x+2, y+1, true)
        setBoard(x+1, y+2, true)
        setBoard(x+2, y+2, true)
    }

    // (b) clear the board

    // Add functions to update the board
    // use built-in classes for grid rather than gc and canvas


    fun updateStatus(status: StatusView) {
        val gridView = views[1] // second element in array
        // need to get coordinates

    }

    // view management
    fun addView(view: IView) {
        views.add(view)
    }

    fun removeView(view: IView) {
        views.remove(view)
    }

    fun notifyViews() {
        for (view in views) {
            view.update()
        }
    }
}