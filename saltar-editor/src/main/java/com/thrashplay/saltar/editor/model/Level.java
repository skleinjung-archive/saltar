package com.thrashplay.saltar.editor.model;

/**
 * The current level being edited
 *
 * @author Sean Kleinjung
 */
public class Level {
    private int width;
    private int height;
    private int tileSize = 32;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
}
