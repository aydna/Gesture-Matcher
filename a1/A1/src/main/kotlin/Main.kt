import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.IOException
import javax.swing.text.View


class Main : Application()  {

    // returns a listView of the files in dir
    private fun getFiles(dir: File, showHidden: Boolean): ListView<String> {
        val fileNames: ListView<String> = ListView<String>()
        //val filesList: mutableListOf<File> = Array<File>()
        //check if file is directory
        if (dir.isDirectory) {

            val files = if (showHidden) {
                dir.listFiles()
            } else {
                dir.listFiles(FileFilter { file -> file.name[0] != '.' })
            }

            val sorted = files.sorted()

            for (file in sorted) {
                fileNames.items.add(file.name)
                //println(file.name)
            }
        }
        return fileNames
    }

    private fun getCurrFile(dir: File, index: Int, showHidden: Boolean): File {
        val currFile = if (showHidden) {
            dir.listFiles()
        } else {
            dir.listFiles(FileFilter { file -> file.name[0] != '.' })
        }
        return currFile.sorted()[index]
    }

    @Throws(IOException::class)
    fun isFileNameValid(filename: String?): Boolean {
        val file = File(filename)
        var created = false
        return try {
            created = file.createNewFile()
            created
        } finally {
            if (created) {
                file.delete()
            }
        }
    }


    override fun start(stage: Stage) {

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()

        // top: menubar
        val menuBar1 = MenuBar()
        val fileMenu = Menu("File") // quit application
        val viewMenu = Menu("View") // Home, Prev, Next,
        val actionMenu = Menu("Actions") // rename, delete, move
        val optionMenu = Menu("Options") // hidden files toggle

        val fileQuit = MenuItem("Quit")
        val viewHome = MenuItem("Home")
        val viewPrev = MenuItem("Prev")
        val viewNext = MenuItem("Next")
        val actionRename = MenuItem("Rename")
        val actionDelete = MenuItem("Delete")
        val actionMove = MenuItem("Move")
        //options toggle
        val optionsToggle = RadioMenuItem("Show hidden files")

        menuBar1.menus.add(fileMenu)
        menuBar1.menus.add(viewMenu)
        menuBar1.menus.add(actionMenu)
        menuBar1.menus.add(optionMenu)

        fileMenu.items.add(fileQuit)
        viewMenu.items.add(viewHome)
        viewMenu.items.add(viewPrev)
        viewMenu.items.add(viewNext)
        actionMenu.items.add(actionRename)
        actionMenu.items.add(actionDelete)
        actionMenu.items.add(actionMove)
        optionMenu.items.add(optionsToggle)

        val homeIcon = ImageView(Image("homeIcon.png", 15.0, 15.0, false, false))
        val homeButton = Button("Home")
        homeButton.graphic = homeIcon

        val prevIcon = ImageView(Image("prev.png", 15.0, 15.0, false, false))
        val prevButton = Button("Previous")
        prevButton.graphic = prevIcon

        val nextIcon = ImageView(Image("next.png", 15.0, 15.0, false, false))
        val nextButton = Button("Next")
        nextButton.graphic = nextIcon

        val delIcon = ImageView(Image("delete.png", 15.0, 15.0, false, false))
        val delButton = Button("Delete")
        delButton.graphic = delIcon

        val renameIcon = ImageView(Image("rename.png", 15.0, 15.0, false, false))
        val renameButton = Button("Rename")
        renameButton.graphic = renameIcon

        val moveIcon = ImageView(Image("move.png", 15.0, 15.0, false, false))
        val moveButton = Button("Move")
        moveButton.graphic = moveIcon

        val topContainer = VBox()  //Creates a container to hold all Menu Objects.
        val buttonContainer = HBox() // for buttons layout

        buttonContainer.children.add(homeButton)
        buttonContainer.children.add(prevButton)
        buttonContainer.children.add(nextButton)
        buttonContainer.children.add(delButton)
        buttonContainer.children.add(renameButton)
        buttonContainer.children.add(moveButton)

        topContainer.children.add(menuBar1)
        topContainer.children.add(buttonContainer)

        // label for display
        var rLabel = Label()
        //rLabel.setMaxSize(550.0, 300.0)

        var textArea = TextArea()
        textArea.isEditable = false
        textArea.isWrapText = true

        // global stuff
        var currDir = File("${System.getProperty("user.dir")}/test/")
        var currPath: String = currDir.absolutePath

        var statusBar1 = Label(currPath)
        var showHidden = false // show hidden files or not


        // left: tree
        val fileTree: ListView<String> = getFiles(currDir, showHidden)


        fun updateListView(tmpdir: ListView<String>) {
            for (file in tmpdir.items) {
                fileTree.items.add(file)
                //println(file)
            }
            layout.left = fileTree // update view
        }

        // check special file
        fun updateSpecialFile(currFile: File) {

            if (currFile.canRead()) {
                val picFileExt: Array<String> = arrayOf("jpeg", "jpg", "bmp", "png")
                val txtFileExt: Array<String> = arrayOf("txt", "md")
                if (currFile.extension in picFileExt) {
                    // is a picture file; display it
                    val input = FileInputStream(currFile.absolutePath)
                    val fileImg = ImageView(Image(input, 550.0, 400.0, true, true))
                    fileImg.fitWidthProperty().bind(rLabel.widthProperty())
                    fileImg.fitHeightProperty().bind(rLabel.heightProperty())
                    rLabel.graphic = fileImg
                    layout.right = fileImg

                } else if (currFile.extension in txtFileExt) {
                    // is txt file, display the txt
                    textArea.text = currFile.readText()
                    layout.right = textArea
                } else {
                    layout.right = Label() // not relevant
                }
            }
            else {
                layout.right = Label()
            }
        }


        // handle mouse/enter clicked action on file
        optionsToggle.setOnAction {
            showHidden = !showHidden
            // update fileTree
            fileTree.items.clear()
            val tmpDir = getFiles(currDir, showHidden)
            updateListView(tmpDir)

        }

        fileQuit.setOnAction {
            Platform.exit()
        }

        fileTree.setOnKeyPressed { event ->
            // update status bar
            if (event.code === KeyCode.DOWN || event.code === KeyCode.UP) {

                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = getCurrFile(currDir, currItem, showHidden)
                currPath = currFile.absolutePath
                statusBar1 = Label(currPath) // update status bar
                layout.bottom = statusBar1
            }

            else if (event.code === KeyCode.ENTER) {

                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = getCurrFile(currDir, currItem, showHidden)
                //println(currFile)
                currPath = currFile.absolutePath
                statusBar1 = Label(currPath) // update status bar
                layout.bottom = statusBar1

                // Update directory
                if (currFile.isDirectory) {
                    currDir = currFile
                    currPath = currFile.absolutePath // ignore that underline for now lol

                    // update fileTree
                    fileTree.items.clear()
                    val tmpDir = getFiles(currFile, showHidden)
                    updateListView(tmpDir)
                    layout.right = Label() // reset right-view

                }
                else {
                    updateSpecialFile(currFile)
                }
            }

            // go back up a directory
            else if (event.code === KeyCode.DELETE || event.code === KeyCode.BACK_SPACE) {

                if (currDir.parent != null) {
                    currDir = currDir.parentFile //move to parent folder
                    currPath = currDir.absolutePath
                    statusBar1 = Label(currPath) // update status bar
                    layout.bottom = statusBar1

                    // update fileTree
                    fileTree.items.clear()
                    val tmpDir = getFiles(currDir, showHidden)
                    updateListView(tmpDir)
                    layout.right = Label() // reset right-view
                }

            }
        }

        fileTree.setOnMouseClicked { event ->

            val currItem = fileTree.selectionModel.selectedIndex
            val currFile = getCurrFile(currDir, currItem, showHidden)
            //println(currFile)
            currPath = currFile.absolutePath
            statusBar1 = Label(currPath) // update status bar
            layout.bottom = statusBar1

            // Update directory
            if (currFile.isDirectory && event.clickCount == 2) {
                currDir = currFile

                // update fileTree
                fileTree.items.clear()
                val tmpDir = getFiles(currFile, showHidden)
                updateListView(tmpDir)
                layout.right = Label() // reset right-view

            }
            else {
                updateSpecialFile(currFile)
            }
        }

        // button click events (menu)
        fun homeButton() {
            // take us home to the test directory
            currDir = File("${System.getProperty("user.dir")}/test/")
            currPath = currDir.absolutePath
            statusBar1 = Label(currPath)
            layout.bottom = statusBar1

            // update fileTree
            fileTree.items.clear()
            val tmpDir = getFiles(currDir, showHidden)
            updateListView(tmpDir)
            layout.right = Label() // reset right-view
        }

        homeButton.setOnMouseClicked {
            homeButton()
        }
        viewHome.setOnAction {
            homeButton()
        }


        fun nextButton() {
            if (!fileTree.selectionModel.isEmpty) {
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = getCurrFile(currDir, currItem, showHidden)
                //println(currFile)

                // Update directory
                if (currFile.isDirectory) {
                    currDir = currFile

                    // update fileTree
                    fileTree.items.clear()
                    val tmpDir = getFiles(currFile, showHidden)
                    updateListView(tmpDir)
                    layout.right = Label() // reset right-view
                }
            }
        }
        nextButton.setOnMouseClicked {
            nextButton()
        }
        viewNext.setOnAction {
            nextButton()
        }


        fun prevButton() {
            if (currDir.parent != null) {
                currDir = currDir.parentFile //move to parent folder
                currPath = currDir.absolutePath
                statusBar1 = Label(currPath) // update status bar
                layout.bottom = statusBar1

                // update fileTree
                fileTree.items.clear()
                val tmpDir = getFiles(currDir, showHidden)
                updateListView(tmpDir)
                layout.right = Label() // reset right-view
            }
        }
        prevButton.setOnMouseClicked {
            prevButton()
        }
        viewPrev.setOnAction {
            prevButton()
        }


        fun renameButton() {
            if (!fileTree.selectionModel.isEmpty) {
                val dialog = TextInputDialog()
                dialog.title = "Rename File"
                dialog.contentText = "Please enter the new file name:"

                val result = dialog.showAndWait()
                if (result.isPresent && isFileNameValid(result.get())) {

                    val currItem = fileTree.selectionModel.selectedIndex
                    val currFile = getCurrFile(currDir, currItem, showHidden)
                    //println(currFile)

                    val tmpFile = File("${currDir.absolutePath}/${result.get()}")


                    currFile.renameTo(tmpFile) // rename

                    // update view
                    currPath = currFile.absolutePath
                    statusBar1 = Label(currPath) // update status bar
                    layout.bottom = statusBar1

                    fileTree.items.clear()
                    val tmpDir = getFiles(currDir, showHidden)
                    updateListView(tmpDir)

                } else {
                    val alertDialog = Alert(Alert.AlertType.ERROR)
                    alertDialog.title = "Invalid Operation"
                    alertDialog.contentText = "Cannot rename file"
                    alertDialog.show()
                }
            }
        }
        renameButton.setOnMouseClicked {
            renameButton()
        }
        actionRename.setOnAction {
            renameButton()
        }


        fun delButton() {
            if (!fileTree.selectionModel.isEmpty) {

                // test if selected file is valid to be deleted
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = getCurrFile(currDir, currItem, showHidden)
                if (currFile.parentFile.canWrite()) {

                    val dialog = Alert(Alert.AlertType.CONFIRMATION)
                    dialog.title = "Confirmation of Deletion"
                    dialog.headerText = "Deleting selected file"
                    dialog.contentText = "Are you sure?"

                    val result = dialog.showAndWait()
                    if (result.get() == ButtonType.OK) {
                        // delete
                        currFile.delete()

                        //update directory
                        // update view
                        currPath = currDir.absolutePath
                        statusBar1 = Label(currPath) // update status bar
                        layout.bottom = statusBar1

                        fileTree.items.clear()
                        val tmpDir = getFiles(currDir, showHidden)
                        updateListView(tmpDir)
                        layout.right = Label() // reset right-view
                    }
                }
                else {
                    val alertDialog = Alert(Alert.AlertType.ERROR)
                    alertDialog.title = "Invalid Operation"
                    alertDialog.contentText = "Cannot delete file"
                    alertDialog.show()
                }
            }
        }
        delButton.setOnMouseClicked {
            delButton()
        }
        actionDelete.setOnAction {
            delButton()
        }


        fun moveButton() {
            if (!fileTree.selectionModel.isEmpty) {
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = getCurrFile(currDir, currItem, showHidden)

                val dialog = TextInputDialog()
                dialog.title = "Move File"
                dialog.contentText = "Please specify new location path:"

                val result = dialog.showAndWait()

                if (result.isPresent && File(result.get()).isDirectory && currFile.canWrite()) {
                    //val newPath1 = result.get() + currFile.name
                    val newPath2 = result.get() + "/" + currFile.name

                    // is valid move
                    //println(newPath2)

                    currFile.renameTo(File(newPath2))


                    // update view
                    currPath = currDir.absolutePath
                    statusBar1 = Label(currPath) // update status bar
                    layout.bottom = statusBar1

                    fileTree.items.clear()
                    val tmpDir = getFiles(currDir, showHidden)
                    updateListView(tmpDir)
                    layout.right = Label() // reset right-view
                }
                else {
                    val alertDialog = Alert(Alert.AlertType.ERROR)
                    alertDialog.title = "Invalid Operation"
                    alertDialog.contentText = "Cannot move file"
                    alertDialog.show()
                }

            }
        }
        moveButton.setOnMouseClicked {
            moveButton()
        }
        actionMove.setOnAction {
            moveButton()
        }


        // build the scene graph

        layout.top = topContainer
        layout.left = fileTree
        layout.bottom = statusBar1


        // create and show the scene
        val scene = Scene(layout)
        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene


        stage.show()
    }
}