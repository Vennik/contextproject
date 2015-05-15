package nl.tudelft.context.controller;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.context.drawable.DrawableEdge;
import nl.tudelft.context.drawable.InfoLabel;
import nl.tudelft.context.graph.Graph;
import nl.tudelft.context.graph.Node;
import nl.tudelft.context.service.LoadGraphService;
import org.apache.commons.collections.CollectionUtils;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 7-5-2015
 */
public final class GraphController extends ViewController<AnchorPane> {

    /**
     * ProgressIndicator to show when the graph is loading.
     */
    @FXML
    ProgressIndicator progressIndicator;

    /**
     * The container for the graph.
     */
    @FXML
    Group sequences;

    /**
     * Scroll pane to monitor.
     */
    @FXML
    ScrollPane scroll;

    /**
     * Reference to the MainController of the app.
     */
    MainController mainController;

    /**
     * The service for loading the Graph.
     */
    LoadGraphService loadGraphService;

    /**
     * Sources that are displayed in the graph.
     */
    Set<String> sources;

    /**
     * Define the amount of the shift.
     */
    public static final int NODE_SPACING = 50;

    /**
     * Init a controller at graph.fxml.
     *
     * @param mainController MainController for the application
     * @param sources        Sources to display
     */
    public GraphController(final MainController mainController, final Set<String> sources) {

        super(new AnchorPane());

        this.mainController = mainController;
        this.sources = sources;
        this.loadGraphService = mainController.getWorkspace().getGraphList().get(0);

        loadFXML("/application/graph.fxml");

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

        progressIndicator.visibleProperty().bind(loadGraphService.runningProperty());
        loadGraphService.setOnSucceeded(event -> showGraph(cleanGraph(loadGraphService.getValue())));

        loadGraph();

    }

    /**
     * Load graph from source.
     */
    private void loadGraph() {

        sequences.getChildren().clear();
        loadGraphService.restart();

    }

    /**
     * Clean the graph from sources that aren't shown.
     *
     * @param graph Graph to show
     * @return Cleaned up graph
     */
    private Graph cleanGraph(final Graph graph) {

        // Remove unnecessary edges
        graph.removeAllEdges(graph.edgeSet().stream()
                .filter(edge -> {
                    Node source = graph.getEdgeSource(edge);
                    Node target = graph.getEdgeTarget(edge);
                    return !CollectionUtils.containsAny(source.getSources(), sources)
                            || !CollectionUtils.containsAny(target.getSources(), sources);
                })
                .collect(Collectors.toList()));

        // Remove unnecessary nodes
        graph.removeAllVertices(graph.vertexSet().stream()
                .filter(vertex -> !CollectionUtils.containsAny(vertex.getSources(), sources))
                .collect(Collectors.toList()));

        return graph;

    }

    /**
     * Show graph with reference points.
     *
     * @param graph Graph to show
     */
    private void showGraph(final Graph graph) {

        List<Node> start = graph.getFirstNodes();

        int i = 0;
        while (!start.isEmpty()) {
            start = showColumn(graph, start, i++);
        }

        // Bind edges
        List<DrawableEdge> edgeList = graph.edgeSet().stream()
                .map(edge -> new DrawableEdge(graph, edge))
                .collect(Collectors.toList());

        // Bind nodes
        List<InfoLabel> nodeList = graph.vertexSet().stream()
                .map(node -> new InfoLabel(mainController, graph, node))
                .collect(Collectors.toList());

        sequences.getChildren().addAll(edgeList);
        sequences.getChildren().addAll(nodeList);

        initOnTheFlyLoading(nodeList);

    }

    /**
     * Listen to position and load on the fly.
     *
     * @param nodeList Labels to to load on the fly
     */
    private void initOnTheFlyLoading(final List<InfoLabel> nodeList) {

        HashMap<Integer, List<InfoLabel>> map = new HashMap<>();
        nodeList.stream().forEach(infoLabel -> {
            int index = (int) infoLabel.translateXProperty().get() / 100;
            if (!map.containsKey(index)) {
                map.put(index, new ArrayList<>());
            }
            map.get(index).add(infoLabel);
        });

        showCurrentLabels(map);
        scroll.hvalueProperty().addListener(event -> showCurrentLabels(map));

    }

    /**
     * Show all the labels on current position.
     *
     * @param map Containing the labels indexed by position
     */
    private void showCurrentLabels(final HashMap<Integer, List<InfoLabel>> map) {

        double width = scroll.getWidth();
        double left = (scroll.getContent().layoutBoundsProperty().getValue().getWidth() - width)
                * scroll.getHvalue();
        int indexFrom = (int) Math.round(left / 100) - 1;
        int indexTo = indexFrom + (int) Math.ceil(width / 100) + 1;
        for (int index = indexFrom; index <= indexTo; index++) {
            List<InfoLabel> temp = map.remove(index);
            if (temp != null) {
                temp.stream().forEach(InfoLabel::init);
            }
        }

    }

    /**
     * Show the columns of the graph recursive.
     *
     * @param graph  containing the nodes
     * @param nodes  nodes to display
     * @param column column index
     * @return next column
     */
    private List<Node> showColumn(final Graph graph, final List<Node> nodes, final int column) {

        showNodes(nodes, column);

        return nodes.stream()
                .map(node -> graph.outgoingEdgesOf(node).stream()
                        .map(graph::getEdgeTarget)
                        .filter(x -> x.incrementIncoming() == graph.inDegreeOf(x)))
                .flatMap(l -> l)
                .collect(Collectors.toList());

    }

    /**
     * Show all nodes at a start position.
     *
     * @param nodes  nodes to draw
     * @param column column to draw at
     */
    private void showNodes(final List<Node> nodes, final int column) {
        int shift = nodes.size() * NODE_SPACING;
        int row = 0;
        for (Node node : nodes) {

            node.setTranslateX(column * 100);
            node.setTranslateY(row * 100 - shift);

            row++;

        }

    }

    @Override
    public String getBreadcrumbName() {
        return "Genome graph (" + sources.size() + ")";
    }

}
