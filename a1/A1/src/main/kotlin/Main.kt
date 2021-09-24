import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.io.File


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


    override fun start(stage: Stage) {

        // create the root of the scene graph
        // BorderPane supports placing children in regions around the screen
        val layout = BorderPane()

        // top: menubar
        val menuBar = MenuBar()
        val fileMenu = Menu("File")
        val fileNew = MenuItem("New")

        menuBar.menus.add(fileMenu)
        fileMenu.items.add(fileNew)

        // added stuff
        var currDir: File = File("${System.getProperty("user.dir")}/test/")
        var currPath: String = currDir.absolutePath

        var statusBar1 = Label(currPath)

        // handle default user action aka press
        fileNew.setOnAction { event ->
            println("New pressed")
        }

        // left: tree
        var fileTree: ListView<String> = getFiles(currDir)

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

        // build the scene graph
        layout.top = menuBar
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