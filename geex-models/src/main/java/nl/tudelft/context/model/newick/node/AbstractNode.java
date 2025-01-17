package nl.tudelft.context.model.newick.node;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import nl.tudelft.context.model.newick.selection.None;
import nl.tudelft.context.model.newick.selection.Selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Jasper Boot
 * @version 1.0
 * @since 05-06-2015
 */
public abstract class AbstractNode {

    /**
     * The name of the node.
     */
    final String name;
    /**
     * The weight of the node.
     */
    final double weight;
    /**
     * The children of the node.
     */
    List<AbstractNode> children;

    /**
     * The parent node.
     */
    Optional<AbstractNode> parent = Optional.empty();

    /**
     * The selection of the node.
     */
    ObjectProperty<Selection> selection = new SimpleObjectProperty<>(new None());

    /**
     * The sources in this node.
     */
    SimpleObjectProperty<Set<String>> sources = new SimpleObjectProperty<>(new HashSet<>());

    /**
     * Translation in the direction of the X axis.
     */
    SimpleDoubleProperty translateX = new SimpleDoubleProperty(0);

    /**
     * Translation in the direction of the Y axis.
     */
    SimpleDoubleProperty translateY = new SimpleDoubleProperty(0);

    /**
     * Builds a new node with the corresponding name and weight.
     *
     * @param name   the name of the node
     * @param weight the weight (distance from parent) of the node
     */
    public AbstractNode(final String name, final double weight) {
        this.name = name;
        this.weight = weight;
        children = new ArrayList<>(2);
    }

    /**
     * Adds a child to the node.
     *
     * @param n the node to add as a child
     */
    public abstract void addChild(final AbstractNode n);

    /**
     * Gets all the children inside this node.
     *
     * @return the children
     */
    public abstract List<AbstractNode> getChildren();

    /**
     * Gets the parent of the node.
     *
     * @return the parent of the node.
     */
    public AbstractNode getParent() {
        return parent.get();
    }

    /**
     * Sets the parent of the node.
     *
     * @param parent the parent.
     */
    public void setParent(final AbstractNode parent) {
        this.parent = Optional.of(parent);
    }

    /**
     * Gets the name of this node.
     *
     * @return the name of this node
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the weight of this node.
     *
     * @return the weight of this node.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Updates the sources that belong to the node and its children.
     */
    public abstract void updateSources();

    /**
     * Gets the sources for the graph from this node and its children.
     *
     * @return name of this node and it's children
     */
    public Set<String> getSources() {
        return sources.get();
    }

    /**
     * Gets the sources for the graph from this node and its children.
     *
     * @return name of this node and it's children
     */
    public ObjectProperty<Set<String>> getSourcesProperty() {
        return sources;
    }

    /**
     * Toggles the selection of the node. If the selection was ALL, the the new selection will be NONE; otherwise the
     * new selection will be ALL.
     */
    public void toggleSelection() {
        setSelection(selection.get().toggle());
        parent.ifPresent(AbstractNode::updateSelection);
    }

    /**
     * Gets the selection property of the node.
     *
     * @return the selection property.
     */
    public ObjectProperty<Selection> getSelectionProperty() {
        return this.selection;
    }

    /**
     * Gets the selection of the node.
     *
     * @return the selection.
     */
    public Selection getSelection() {
        return selection.getValue();
    }

    /**
     * Sets the new selection of the node. It recursively sets the selection of its children also.
     *
     * @param selection The new selection of the node and its children.
     */
    public void setSelection(final Selection selection) {
        this.selection.set(selection);
        getChildren().forEach(node -> node.setSelection(selection));
        updateSources();
    }

    /**
     * Sets the selection of the node, based on the selection of its children;
     * <p>
     * All the children's selection is ALL: ALL
     * All the children's selection is NONE: NONE
     * Otherwise: PARTIAL
     * </p>
     * <p>
     * If the node has a parent, it also calls this method on its parent.
     * </p>
     */
    public void updateSelection() {
        selection.setValue(getChildren().stream()
                .map(AbstractNode::getSelection)
                .reduce(Selection::merge).orElse(getSelection()));
        updateSources();

        parent.ifPresent(AbstractNode::updateSelection);
    }

    /**
     * Creates a clone of the current node.
     *
     * @return A clone of the current node
     */
    public abstract AbstractNode getCopy();

    /**
     * Should return a copy of all nodes that are (partially) selected.
     *
     * @return A copy of the nodes that are selected
     */
    public AbstractNode getSelectedNodes() {
        AbstractNode node = getCopy();
        getChildren().stream()
                .filter(n -> n.getSelection().isAny())
                .map(AbstractNode::getSelectedNodes)
                .forEach(opt -> {
                    node.addChild(opt);
                    opt.setParent(node);
                });
        return node;
    }

    /**
     * Returns the class name that belongs to the node.
     *
     * @return The class name
     */
    public abstract String getClassName();

    /**
     * Translates the position of the node, according to the given parameters.
     *
     * @param minWeight   The minimum horizontal distance to its parent
     * @param weightScale A scalar to multiply the weight with
     * @param yPos        The y-position of the node
     */
    public void translate(final int minWeight, final double weightScale, final int yPos) {
        setTranslateX(minWeight + weight * weightScale
                + parent.orElse(new DummyNode()).translateXProperty().doubleValue());
        setTranslateY(yPos);
    }

    @Override
    public String toString() {
        return "Node<" + name + "," + weight + ">";
    }

    /**
     * @return translateX property
     */
    public final ObservableDoubleValue translateXProperty() {
        return translateX;
    }

    /**
     * @return translateY property
     */
    public final ObservableDoubleValue translateYProperty() {
        return translateY;
    }

    /**
     * Update translate x.
     *
     * @param x new x
     */
    public final void setTranslateX(final double x) {
        translateX.set(x);
    }

    /**
     * Update translate y.
     *
     * @param y new y
     */
    public final void setTranslateY(final double y) {
        translateY.set(y);
    }
}
