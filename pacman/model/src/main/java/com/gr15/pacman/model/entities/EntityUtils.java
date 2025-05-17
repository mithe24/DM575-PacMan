package com.gr15.pacman.model.entities;

/**
 * Utility class providing helper methods 
 * for working with {@link Entity} objects.
 * 
 * <p>
 * This class includes methods for calculating distances
 * and detecting collisions
 * between entities in the game world,
 * taking into account their sub-tile positions and radii.
 * </p>
 *
 * <p>This class is final and cannot be instantiated.</p>
 */

public final class EntityUtils {

    /**
     * Calculates the Euclidean distance between
     * two entities based on their position and sub-tile offsets.
     *
     * @param arg0 the first entity.
     * @param arg1 the second entity.
     * @return the distance between the centers of the two entities.
     */
    public static double distance(Entity arg0, Entity arg1) {
        double arg0X = arg0.getPositionX();
        double arg0Y = arg0.getPositionY();
        double arg1X = arg1.getPositionX();
        double arg1Y = arg1.getPositionY();
        
        return Math.hypot(arg0X - arg1X, arg0Y - arg1Y);
    }
    
    /**
     * Determines whether two entities have collided
     * based on the distance between their centers and their combined radii.
     *
     * @param arg0 the first entity.
     * @param arg1 the second entity.
     * @return true if the entities have collided; false otherwise.
     */
    public static boolean hasCollided(Entity arg0, Entity arg1) {
        double arg0X = arg0.getPositionX();
        double arg0Y = arg0.getPositionY();
        double arg1X = arg1.getPositionX();
        double arg1Y = arg1.getPositionY();

        double dx = arg0X - arg1X;
        double dy = arg0Y - arg1Y;
        double distanceSquared = dx * dx + dy * dy;
        double combinedRadius = arg0.getRadius() + arg1.getRadius();

        return distanceSquared <= combinedRadius * combinedRadius;
    }
}
