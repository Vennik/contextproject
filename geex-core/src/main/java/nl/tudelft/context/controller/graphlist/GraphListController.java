package nl.tudelft.context.controller.graphlist;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import nl.tudelft.context.model.graph.StackGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author René Vennik
 * @version 1.0
 * @since 17-6-2015
 */
public class GraphListController {

    /**
     * List of graph views.
     */
    LinkedList<StackGraph> graphList = new LinkedList<>();

    /**
     * Active graph property.
     */
    ObjectProperty<StackGraph> activeGraph = new SimpleObjectProperty<>();

    /**
     * Create a graph list controller.
     *
     * @param graphs FXML Pane to add graphs labels to.
     */
    public GraphListController(final Pane graphs) {
        activeGraph.addListener((observable, oldValue, newValue) -> {
            List<GraphListItem> listItems = graphList.stream()
                    .map(graph -> new GraphListItem(graph, activeGraph))
                    .collect(Collectors.toList());
            listItems.stream()
                    .limit(graphList.indexOf(newValue) + 1)
                    .forEach(GraphListItem::setActive);
            graphs.getChildren().setAll(listItems);
        });

    }

    /**
     * Get the current active graph.
     *
     * @return The current active graph
     */
    public StackGraph getActiveGraph() {
        return activeGraph.get();
    }

    /**
     * Get the current active graph readonly property.
     *
     * @return The current active graph readonly property
     */
    public ReadOnlyObjectProperty<StackGraph> getActiveGraphProperty() {
        return activeGraph;
    }

    /**
     * Add a graph to the graph list.
     *
     * @param stackGraph Graph to add
     */
    public void add(final StackGraph stackGraph) {
        graphList.add(stackGraph);
        activeGraph.set(stackGraph);
    }

    /**
     * Zoom in.
     */
    public void zoomIn() {
        StackGraph newGraph = graphList.get(Math.max(graphList.indexOf(getActiveGraph()) - 1, 0));
        activeGraph.setValue(newGraph);
    }

    /**
     * Zoom out.
     */
    public void zoomOut() {
        StackGraph newGraph = graphList.get(Math.min(graphList.indexOf(getActiveGraph()) + 1, graphList.size() - 1));
        activeGraph.setValue(newGraph);
    }

    /**
     * Reset the view.
     */
    public void reset() {
        activeGraph.setValue(graphList.getLast());
    }

}