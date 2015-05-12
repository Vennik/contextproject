package nl.tudelft.context.graph;

import nl.tudelft.context.drawable.DrawableNode;

import java.util.Set;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.1
 * @since 23-4-2015
 */
public final class Node extends DrawableNode {

    int id;
    Set<String> sources;
    int refStartPosition;
    int refEndPosition;
    String content;
    BaseCounter baseCounter;

    /**
     * Create a node.
     *
     * @param id               id
     * @param refStartPosition start position in reference genome
     * @param sources          genomes that contain this node
     * @param refEndPosition   end position in reference genome
     * @param content          DNA sequence
     */
    public Node(final int id,
                final Set<String> sources,
                final int refStartPosition,
                final int refEndPosition,
                final String content) {

        this.id = id;
        this.sources = sources;
        this.refStartPosition = refStartPosition;
        this.refEndPosition = refEndPosition;
        this.content = content;
        this.baseCounter = new BaseCounter(content);

    }

    /**
     * Getter for id.
     *
     * @return id
     */
    public int getId() {

        return id;

    }

    /**
     * Getter for sources.
     *
     * @return sources
     */
    public Set<String> getSources() {

        return sources;

    }

    /**
     * Getter for reference start position.
     *
     * @return reference start position
     */
    public int getRefStartPosition() {

        return refStartPosition;

    }

    /**
     * Getter for reference end position.
     *
     * @return reference end position
     */
    public int getRefEndPosition() {

        return refEndPosition;

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
     * Getter for baseCounter
     *
     * @return baseCounter
     */
    public BaseCounter getBaseCounter() {
        return baseCounter;
    }

    /**
     * Checks if node is equal to an other node.
     * <p>
     * It checks only on id, because of performance issues.
     * </p>
     *
     * @return if node is equal to an other node
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Node) {
            Node that = (Node) other;
            return id == that.id;
        }
        return false;
    }

    /**
     * Creates an hashCode.
     *
     * @return unique hashCode by id
     */
    @Override
    public int hashCode() {
        return this.id;
    }

}
