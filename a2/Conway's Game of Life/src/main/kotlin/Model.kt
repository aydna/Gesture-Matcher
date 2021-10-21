import javafx.animation.AnimationTimer
import javafx.animation.Animation

class Model {
    // represent my board
    val adjustmentFactor = 5 // this is purely for edge collision cases
    private val sizeOuter = 50 + (adjustmentFactor * 2)
    private val sizeInner = 75 + (adjustmentFactor * 2)

    private val views = ArrayList<IView>()
    var board = Array(sizeOuter) { BooleanArray(sizeInner) } // init to false = dead
    var coordinates = intArrayOf(0, 0)
    var currSelected = "" // the currently selected shape
    var lastSelected = "" // for status
    var frameCounter = 0
    var pause = false

    init {
        // timer fires every 1s
        val time: AnimationTimer = object : AnimationTimer() {
            private var lastUpdate: Long = 0
            override fun handle(now: Long) {
                if (now - lastUpdate >= 1000000000.0 && !pause) {
                    updateBoard()
                    frameCounter += 1
                    notifyViews()
                    lastUpdate = now
                }
            }
        }
        time.start()

    }

    // board manipulation
    // (a) add a shape
    fun addShape(x: Int, y: Int) {
        if (currSelected == "block") {
            addBlock(x, y)
            //lastSelected = currSelected
            //currSelected = ""
            coordinates[0] = y
            coordinates[1] = x
            updateBoard()
        }
        else if (currSelected == "beehive") {
            addBeehive(x, y)
            //lastSelected = currSelected
            //currSelected = ""
            coordinates[0] = y
            coordinates[1] = x
            updateBoard()
        }
        else if (currSelected == "blinker") {
            addBlinker(x, y)
            //lastSelected = currSelected
            //currSelected = ""
            coordinates[0] = y
            coordinates[1] = x
            updateBoard()
        }
        else if (currSelected == "toad") {
            addToad(x, y)
            //lastSelected = currSelected
            //currSelected = ""
            coordinates[0] = y
            coordinates[1] = x
            updateBoard()
        }
        else if (currSelected == "glider") {
            addGlider(x, y)
            //lastSelected = currSelected
            //currSelected = ""
            coordinates[0] = y
            coordinates[1] = x
            updateBoard()
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
        setBoard(x, y + 1, true)
        setBoard(x + 1, y, true)
    }

    fun addBeehive(x: Int, y: Int) {
        setBoard(x, y + 1, true)
        setBoard(x, y+ 2, true)
        setBoard(x + 1, y, true)
        setBoard(x+1, y+3, true)
        setBoard(x+2, y+1, true)
        setBoard(x+2, y+2, true)
    }

    fun addBlinker(x: Int, y: Int) {
        setBoard(x+1, y, true)
        setBoard(x+1, y+1, true)
        setBoard(x+1, y+2, true)
    }

    fun addToad(x: Int, y: Int) {
        setBoard(x, y+1, true)
        setBoard(x, y+2, true)
        setBoard(x, y+3, true)
        setBoard(x+1, y, true)
        setBoard(x+1, y+1, true)
        setBoard(x+1, y+2, true)
    }

    fun addGlider(x: Int, y: Int) {
        setBoard(x, y+2, true)
        setBoard(x+1, y, true)
        setBoard(x+1, y+2, true)
        setBoard(x+2, y+1, true)
        setBoard(x+2, y+2, true)
    }

    private fun getCount(row: Int, col: Int): Int {
        var count = 0
        for (x in row - 1..row + 1) {
            for (y in col - 1..col + 1) {
                if (x == row && y == col) continue
                if (x < 0 || x >= 60) continue
                if (y < 0 || y >= 85) continue
                if (board[x][y]) count++
            }
        }
        return count
    }

    fun updateBoard() {
        val newBoard = Array(sizeOuter) { BooleanArray(sizeInner) }

        for (row in 0 until sizeOuter - 1) {
            for (col in 0 until sizeInner - 1) {
                val neighbors = getCount(row, col)
                if (board[row][col]) {
                    newBoard[row][col] = !(neighbors < 2 || neighbors > 3)
                }
                if (!board[row][col]) {
                    newBoard[row][col] = (neighbors == 3)
                }
            }
        }
        board = newBoard
    }

    // (b) clear the board
    fun clearBoard() {
        for (x in 0 until sizeOuter) {
            for (y in 0 until sizeInner) {
                board[x][y] = false
            }
        }
        currSelected = "Cleared Board"
    }

    // Manual mode functionality
    fun manualSwap() {
        pause = !pause
    }

    fun advance() {
        updateBoard()
        frameCounter += 1
        notifyViews()
    }

    // view management
    fun addView(view: IView) {
        views.add(view)
    }


    fun notifyViews() {
        for (view in views) {
            view.update()
        }
    }
}