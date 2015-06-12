package nl.tudelft.context.model.graph;

import nl.tudelft.context.model.annotation.Annotation;
import nl.tudelft.context.model.annotation.AnnotationMap;

import java.util.List;
import java.util.Set;

/**
 * @author René Vennik
 * @version 1.0
 * @since 1-6-2015
 */
public abstract class DefaultNode {

    /**
     * Shift if alone in column.
     */
    boolean shift = false;

    /**
     * Set of genomes that contain this Node.
     */
    Set<String> sources;

    /**
     * The content of the current Node.
     */
    String content;

    /**
     * Get the size in nodes of the node.
     *
     * @return Size in nodes of the node
     */
    public abstract int getSize();

    /**
     * Getter for sources.
     *
     * @return sources
     */
    public Set<String> getSources() {
        return sources;
    }

    /**
     * Getter for content.
     *
     * @return DNA sequence
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for baseCounter.
     *
     * @return baseCounter
     */
    public abstract BaseCounter getBaseCounter();

    /**
     * Getter for reference start position.
     *
     * @return reference start position
     */
    public abstract int getRefStartPosition();

    /**
     * Getter for reference end position.
     *
     * @return reference end position
     */
    public abstract int getRefEndPosition();

    /**
     * Sets the annotations that belong to this node.
     *
     * @param annotationMap The list of all annotations
     */
    public abstract void setAnnotations(final AnnotationMap annotationMap);

    /**
     * Get the list of annotations that are present in this node.
     *
     * @return The list of annotations present in this node
     */
    public abstract List<Annotation> getAnnotations();

    /**
     * Shift the node.
     */
    public void shift() {
        shift = true;
    }

    /**
     * Check if the node is shifted.
     *
     * @return If node is shifted
     */
    public boolean isShift() {
        return shift;
    }

}
