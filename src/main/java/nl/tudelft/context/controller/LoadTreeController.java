package nl.tudelft.context.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;
import nl.tudelft.context.service.LoadTreeService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jasper Boot <mrjasperboot@gmail.com>
 * @version 1.0
 * @since 3-5-2015
 */
public class LoadTreeController extends DefaultController<GridPane> implements Initializable {

    @FXML
    protected Button
            loadTree,
            load;

    @FXML
    protected TextField treeSource;

    protected LoadTreeService loadTreeService;
    protected ProgressIndicator progressIndicator;
    protected Group sequences;

    /**
     * Init a controller at load_tree.fxml.
     *
     * @param progressIndicator progress indicator of tree loading
     * @param sequences         grid to display tree
     * @throws RuntimeException
     */
    public LoadTreeController(ProgressIndicator progressIndicator, Group sequences) {

        super(new GridPane());

        this.progressIndicator = progressIndicator;
        this.sequences = sequences;

        loadFXML("/application/load_tree.fxml");

    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadTreeService = new LoadTreeService();
        loadTreeService.setOnSucceeded(event -> showTree(loadTreeService.getValue()));

        progressIndicator.visibleProperty().bind(loadTreeService.runningProperty());

        FileChooser nwkFileChooser = new FileChooser();
        nwkFileChooser.setTitle("Open Newick file");
        loadTree.setOnAction(event -> loadTreeService.setNwkFile(loadFile(nwkFileChooser, treeSource)));

        load.setOnAction(event -> loadTree());

    }

    /**
     * Load file.
     */
    protected File loadFile(FileChooser fileChooser, TextField source) {

        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (file != null) {
            source.setText(file.getName());
        } else {
            source.setText("");
        }

        return file;

    }

    /**
     * Load tree from source.
     */
    protected void loadTree() {
        loadTreeService.restart();
    }

    /**
     * Show the tree in console.
     *
     * @param tree tree to show
     */
    protected void showTree(Tree tree) {
        System.out.println("check: " + tree.getName());
        printTree(tree.getRoot(), .0);
    }

    /**
     * Print tree recursive to console.
     *
     * @param node   current node
     * @param prev_w previous position
     */
    protected void printTree(TreeNode node, double prev_w) {
        for (int i = 0; i < node.numberLeaves; i += 1) {
            if (node.getChild(i) != null) {
                double w = prev_w + node.getChild(i).getWeight() * .5e4;
                for (int h = 0; h < w; h += 1) {
                    System.out.print("  ");
                }
                System.out.println(node.getChild(i).getName());
                printTree(node.getChild(i), w);
            }
        }
    }
}
