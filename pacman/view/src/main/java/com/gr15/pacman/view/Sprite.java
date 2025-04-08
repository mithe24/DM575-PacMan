package com.gr15.pacman.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    private Image image;
    private double x, y;
    private double width, height;

    public Sprite(Image image, double x, double y, double width, double height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, x, y, width, height);
    }

    /* Getters and setters */
    public Image getImage() { return this.image; }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getWidth() { return this.width; }
    public double getHeigt() { return this.height; }

    public void setImage(Image newImage) { this.image = newImage; }
    public void setX(double newX) { this.x = newX; }
    public void setY(double newY) { this.y = newY; }
    public void setWidth(double newWidth) { this.width = newWidth; }
    public void setHeight(double newHeight) { this.height = newHeight; }
}
