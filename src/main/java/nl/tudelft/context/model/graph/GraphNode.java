package nl.tudelft.context.model.graph;

import nl.tudelft.context.controller.GraphController;
import nl.tudelft.context.controller.MainController;
import nl.tudelft.context.drawable.DefaultLabel;
import nl.tudelft.context.drawable.DrawableNode;
import nl.tudelft.context.drawable.SinglePointLabel;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 1-6-2015
 */
public class GraphNode extends DefaultNode {

    /**
     * Counter for new id.
     */
    public static int counter = -1;

    /**
     * Unique id.
     */
    int id;

    /**
     * Create a unique graph node.
     */
    public GraphNode() {
        id = counter--;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getContent() {
        return "";
    }

    @Override
    public DefaultLabel getLabel(final MainController mainController, final GraphController graphController,
                                 final StackGraph stackGraph, final DrawableNode drawableNode) {
        return new SinglePointLabel(drawableNode, this);
    }

}
