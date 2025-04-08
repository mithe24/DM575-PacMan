package com.gr15.pacman.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class AnimatedSprite extends Sprite {
    private Image[] frames;
    private int currentFrame = 0;
    private double frameTime = 0.1; // seconds per frame
    private double timeSinceLastFrame = 0.0;

    public AnimatedSprite(Image[] frames, double x, double y, double width, double height) {
        super(frames[0], x, y, width, height);
        this.frames = frames;
    }

    public void update(double deltaSeconds) {
        timeSinceLastFrame += deltaSeconds;
        if (timeSinceLastFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.length;
            super.setImage(frames[currentFrame]);
            timeSinceLastFrame = 0.0;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }
}

