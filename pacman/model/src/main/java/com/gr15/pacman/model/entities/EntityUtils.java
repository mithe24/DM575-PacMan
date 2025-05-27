package com.gr15.pacman.model.entities;

/**
 * Utility class providing helper methods 
 * for working with {@link Entity} objects.
 * 
 * <p> This class includes methods for calculating distances
 * and detecting collisions
 * between entities in the game world,
 * taking into account their sub-tile positions and radii. </p>
 *
 * <p> This class is final and cannot be instantiated. </p>
 */
public final class EntityUtils {

    /**
     * Calculates the Euclidean distance between
     * two entities based on their position.
     *
     * @param arg0 the first entity (must not be {@code null})
     * @param arg1 the second entity (mus not be {@code null})
     * @return the distance between the centers of the two entities.
     * @throws IllegalArgumentException if arg0 or arg1 is {@code null}.
     */
    public static double distance(Entity arg0, Entity arg1) {
        if (arg0 == null || arg1 == null) {
            throw new IllegalArgumentException("arg0 and arg1 must not be null");
        }

        double arg0X = arg0.getX();
        double arg0Y = arg0.getY();
        double arg1X = arg1.getX();
        double arg1Y = arg1.getY();
        
        return Math.hypot(arg0X - arg1X, arg0Y - arg1Y);
    }
    
    /**
     * Determines whether two entities have collided
     * based on circular collision detection.
     * A collision is detected if the distance between the entities' centers
     * is less than or equal to the sum of their radii.
     *
     * @param arg0 the first entity (must not be {@code null})
     * @param arg1 the second entity (must not be {@code null})
     * @return {@code true} if the entities bounding circles
     *      intersect or touch; {@code false} otherwise
     * @throws IllegalArgumentException if {@code arg0} or {@code arg1} is {@code null}
     */
    public static boolean hasCollided(Entity arg0, Entity arg1) {
        if (arg0 == null || arg1 == null) {
            throw new IllegalArgumentException("arg0 and arg1 must not be null");
        }

        double dx = arg0.getX() - arg1.getX();
        double dy = arg0.getY() - arg1.getY();
        double distanceSquared = dx * dx + dy * dy;

        double combinedRadius = arg0.getRadius() + arg1.getRadius();
        return distanceSquared <= combinedRadius * combinedRadius;
    }

    /** Private constructor to prevent external instantiation */
    private EntityUtils() {}
}
