package com.gr15.pacman.model.entities;

/**
 * Util
 */
public final class EntityUtils {

    
    public static double distance(Entity arg0, Entity arg1) {
        double arg0X = arg0.getPosition().x() + arg0.getSubTileX();
        double arg0Y = arg0.getPosition().y() + arg0.getSubTileY();
        double arg1X = arg1.getPosition().x() + arg1.getSubTileX();
        double arg1Y = arg1.getPosition().y() + arg1.getSubTileY();
        
        return Math.hypot(arg0X - arg1X, arg0Y - arg1Y);
    }
    
    public static boolean hasCollided(Entity arg0, Entity arg1) {
        double arg0X = arg0.getPosition().x() + arg0.getSubTileX();
        double arg0Y = arg0.getPosition().y() + arg0.getSubTileY();
        double arg1X = arg1.getPosition().x() + arg1.getSubTileX();
        double arg1Y = arg1.getPosition().y() + arg1.getSubTileY();

        double dx = arg0X - arg1X;
        double dy = arg0Y - arg1Y;
        double distanceSquared = dx * dx + dy * dy;
        double combinedRadius = arg0.getRadius() + arg1.getRadius();

        return distanceSquared <= combinedRadius * combinedRadius;
    }
}
