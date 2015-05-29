package nl.tudelft.context.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import nl.tudelft.context.drawable.DrawableEdge;
import nl.tudelft.context.drawable.NewickLabel;
import nl.tudelft.context.model.newick.Newick;
import nl.tudelft.context.service.LoadService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 8-5-2015
 */
public final class NewickController extends ViewController<ScrollPane> {

    /**
     * This is the x disposition of the load button.
     */
    public static final double LOAD_BUTTON_OFFSET = -50;
    /**
     * ProgressIndicator to show when the tree is loading.
     */
    @FXML
    ProgressIndicator progressIndicator;

    /**
     * The container of the newick tree.
     */
    @FXML
    Group newick;

    /**
     * The main controller used to set views.
     */
    MainController mainController;

    ObjectProperty<Newick> newickObjectProperty;

    /**
     * Init a controller at newick.fxml.
     *
     * @param mainController   MainController for the application
     * @param newickIn
     */
    public NewickController(final MainController mainController, final ReadOnlyObjectProperty<Newick> newickIn) {
        super(new ScrollPane());

        this.mainController = mainController;
        newickObjectProperty = new SimpleObjectProperty<>();

        newickObjectProperty.addListener((observable, oldValue, newValue) -> {
            showTree(newValue);
        });

        newickObjectProperty.bind(newickIn);

        loadFXML("/application/newick.fxml");
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
    public void initialize(final URL location, final ResourceBundle resources) {

        progressIndicator.visibleProperty().bind(newickObjectProperty.isNull());

        mainController.newickLifted.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                root.toFront();
            } else {
                root.toBack();
            }
        });
    }

    /**
     * Show the newick in console.
     *
     * @param newick newick to show
     */
    protected void showTree(final Newick newick) {
        // Bind edges
        List<DrawableEdge> edgeList = newick.edgeSet().stream()
                .map(edge -> new DrawableEdge(newick, edge))
                .collect(Collectors.toList());

        // Bind nodes
        List<Label> nodeList = newick.vertexSet().stream()
                .map(NewickLabel::new)
                .collect(Collectors.toList());

        this.newick.getChildren().addAll(edgeList);
        this.newick.getChildren().addAll(nodeList);

        Label button = new Label("Load");
        button.setTranslateX(LOAD_BUTTON_OFFSET);
        button.setOnMouseClicked(event -> loadGraph(newick));
        root.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loadGraph(newick);
            }
        });

        this.newick.getChildren().add(button);
    }

    /**
     * Loads the graph of the selected strands.
     *
     * @param newick the tree with the nodes to show.
     */
    protected void loadGraph(final Newick newick) {
        Set<String> sources = newick.getRoot().getSources();
        if (!sources.isEmpty()) {
            mainController.setView(this,
                    new GraphController(mainController, sources,
                            mainController.getWorkspace().getGraph(),
                            mainController.getWorkspace().getAnnotation()));
        }
    }

    @Override
    public String getBreadcrumbName() {
        return "Phylogenetic tree";
    }

}
