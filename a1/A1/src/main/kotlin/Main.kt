import javafx.application.Application
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
import java.io.IOException


class Main : Application()  {

    // returns a listView of the files in dir
    private fun getFiles(dir: File): ListView<String> {
        val fileNames: ListView<String> = ListView<String>()
        //val filesList: mutableListOf<File> = Array<File>()
        //check if file is directory
        if (dir.isDirectory) {
            //filesList = dir.listFiles()
            val files: Array<File> = dir.listFiles()
            val sorted = files.sorted()
            for (file in sorted) {
                fileNames.items.add(file.name)
                println(file.name)
            }
        }
        return fileNames
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
        val fileMenu = Menu("File")
        val fileNew = MenuItem("New")

        menuBar1.menus.add(fileMenu)
        fileMenu.items.add(fileNew)

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

        // added stuff
        var currDir = File("${System.getProperty("user.dir")}/test/")
        var currPath: String = currDir.absolutePath

        var statusBar1 = Label(currPath)

        // handle default user action aka press
        fileNew.setOnAction { event ->
            println("New pressed")
        }

        // left: tree
        val fileTree: ListView<String> = getFiles(currDir)

        /*
        fileTree.selectionModel.selectedItemProperty().addListener(
            ChangeListener { ov, old_val, new_val ->
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = currDir.listFiles().sorted()[currItem]
                println(ov)
                // Update directory
                if (currFile.isDirectory) {
                    currDir = currFile
                    currPath = currFile.absolutePath // ignore that underline for now lol
                    fileTree = getFiles(currFile)
                    layout.left = fileTree // update view
                }

                }
            )
        */


        // handle mouse/enter clicked action on file

        fileTree.setOnKeyPressed { event ->
            // update status bar
            if (event.code === KeyCode.DOWN || event.code === KeyCode.UP) {

                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = currDir.listFiles().sorted()[currItem]
                currPath = currFile.absolutePath
                statusBar1 = Label(currPath) // update status bar
                layout.bottom = statusBar1
            }

            else if (event.code === KeyCode.ENTER) {

                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = currDir.listFiles().sorted()[currItem]
                println(currFile)
                currPath = currFile.absolutePath
                statusBar1 = Label(currPath) // update status bar
                layout.bottom = statusBar1

                // Update directory
                if (currFile.isDirectory) {
                    currDir = currFile
                    currPath = currFile.absolutePath // ignore that underline for now lol

                    // update fileTree
                    fileTree.items.clear()
                    val tmpDir = getFiles(currFile)
                    for (file in tmpDir.items) {
                        fileTree.items.add(file)
                    }
                    layout.left = fileTree // update view

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
                    val tmpDir = getFiles(currDir)
                    for (file in tmpDir.items) {
                        fileTree.items.add(file)
                    }
                    layout.left = fileTree // update view
                }

            }
        }

        fileTree.setOnMouseClicked { event ->

            val currItem = fileTree.selectionModel.selectedIndex
            val currFile = currDir.listFiles().sorted()[currItem]
            println(currFile)
            currPath = currFile.absolutePath
            statusBar1 = Label(currPath) // update status bar
            layout.bottom = statusBar1

            // Update directory
            if (currFile.isDirectory && event.clickCount == 2) {
                currDir = currFile

                // update fileTree
                fileTree.items.clear()
                val tmpDir = getFiles(currFile)
                for (file in tmpDir.items) {
                    fileTree.items.add(file)
                }
                layout.left = fileTree // update view

            }
        }

        // button click events (menu)

        homeButton.setOnMouseClicked {
            // take us home to the test directory
            currDir = File("${System.getProperty("user.dir")}/test/")
            currPath = currDir.absolutePath
            statusBar1 = Label(currPath)
            layout.bottom = statusBar1

            // update fileTree
            fileTree.items.clear()
            val tmpDir = getFiles(currDir)
            for (file in tmpDir.items) {
                fileTree.items.add(file)
            }
            layout.left = fileTree // update view
        }

        nextButton.setOnMouseClicked {

            if (!fileTree.selectionModel.isEmpty) {
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = currDir.listFiles().sorted()[currItem]
                println(currFile)

                // Update directory
                if (currFile.isDirectory) {
                    currDir = currFile

                    // update fileTree
                    fileTree.items.clear()
                    val tmpDir = getFiles(currFile)
                    for (file in tmpDir.items) {
                        fileTree.items.add(file)
                    }
                    layout.left = fileTree // update view

                }
            }
        }

        prevButton.setOnMouseClicked {
            if (currDir.parent != null) {
                currDir = currDir.parentFile //move to parent folder
                currPath = currDir.absolutePath
                statusBar1 = Label(currPath) // update status bar
                layout.bottom = statusBar1

                // update fileTree
                fileTree.items.clear()
                val tmpDir = getFiles(currDir)
                for (file in tmpDir.items) {
                    fileTree.items.add(file)
                }
                layout.left = fileTree // update view
            }
        }

        renameButton.setOnMouseClicked {
            if (!fileTree.selectionModel.isEmpty) {
                val dialog = TextInputDialog()
                dialog.title = "Rename File"
                dialog.contentText = "Please enter the new file name:"

                val result = dialog.showAndWait()
                if (result.isPresent && isFileNameValid(result.get())) {

                    val currItem = fileTree.selectionModel.selectedIndex
                    val currFile = currDir.listFiles().sorted()[currItem]
                    println(currFile)

                    val tmpFile = File("${currDir.absolutePath}/${result.get()}")


                    currFile.renameTo(tmpFile) // rename

                    // update view
                    currPath = currFile.absolutePath
                    statusBar1 = Label(currPath) // update status bar
                    layout.bottom = statusBar1

                    fileTree.items.clear()
                    val tmpDir = getFiles(currDir)
                    for (file in tmpDir.items) {
                        fileTree.items.add(file)
                    }
                    layout.left = fileTree // update view


                } else {
                    val alertDialog = Alert(Alert.AlertType.ERROR)
                    alertDialog.title = "Invalid Operation"
                    alertDialog.contentText = "Cannot rename file"
                    alertDialog.show()
                }
            }

        }


        delButton.setOnMouseClicked {

            if (!fileTree.selectionModel.isEmpty) {

                // test if selected file is valid to be deleted
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = currDir.listFiles().sorted()[currItem]
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
                        val tmpDir = getFiles(currDir)
                        for (file in tmpDir.items) {
                            fileTree.items.add(file)
                        }
                        layout.left = fileTree // update view
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

        moveButton.setOnMouseClicked {

            if (!fileTree.selectionModel.isEmpty) {
                val currItem = fileTree.selectionModel.selectedIndex
                val currFile = currDir.listFiles().sorted()[currItem]

                val dialog = TextInputDialog()
                dialog.title = "Move File"
                dialog.contentText = "Please specify new location path:"

                val result = dialog.showAndWait()

                if (result.isPresent && File(result.get()).isDirectory && currFile.canWrite()) {
                    //val newPath1 = result.get() + currFile.name
                    val newPath2 = result.get() + "/" + currFile.name

                    // is valid move
                    println(newPath2)

                    currFile.renameTo(File(newPath2))


                    // update view
                    currPath = currDir.absolutePath
                    statusBar1 = Label(currPath) // update status bar
                    layout.bottom = statusBar1

                    fileTree.items.clear()
                    val tmpDir = getFiles(currDir)
                    for (file in tmpDir.items) {
                        fileTree.items.add(file)
                    }
                    layout.left = fileTree // update view
                }
                else {
                    val alertDialog = Alert(Alert.AlertType.ERROR)
                    alertDialog.title = "Invalid Operation"
                    alertDialog.contentText = "Cannot move file"
                    alertDialog.show()
                }

            }
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