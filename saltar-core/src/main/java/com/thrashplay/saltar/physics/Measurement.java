package com.thrashplay.saltar.physics;

/**
 * Adapted from: http://s276.photobucket.com/user/jdaster64/media/smb_playerphysics.png.html
 *
 * @author Sean Kleinjung
 */
public class Measurement {
    private int blocks;
    private int pixels;
    private int subpixels;
    private int subsubpixels;
    private int subsubsubpixels;

    public Measurement() {
    }

    public Measurement(String value) {
        if (value.length() != 5) {
            throw new IllegalArgumentException("Value must be a five digit hexadecimal number.");
        }

        blocks = parse(value, 0);
        pixels = parse(value, 1);
        subpixels = parse(value, 2);
        subsubpixels = parse(value, 3);
        subsubsubpixels = parse(value, 4);
    }

    /**
     * Parses a single hexadecimal digit from a larger string, returning its value.
     */
    private int parse(String value, int characterIndex) {
        return Integer.parseInt(value.substring(characterIndex, characterIndex + 1), 16);
    }

    public float getAsPixels() {
        return (blocks * 32f) + pixels + (subpixels / 16f) + (subsubpixels / 256f) + (subsubsubpixels / 4096f);
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getPixels() {
        return pixels;
    }

    public void setPixels(int pixels) {
        this.pixels = pixels;
    }

    public int getSubpixels() {
        return subpixels;
    }

    public void setSubpixels(int subpixels) {
        this.subpixels = subpixels;
    }

    public int getSubsubpixels() {
        return subsubpixels;
    }

    public void setSubsubpixels(int subsubpixels) {
        this.subsubpixels = subsubpixels;
    }

    public int getSubsubsubpixels() {
        return subsubsubpixels;
    }

    public void setSubsubsubpixels(int subsubsubpixels) {
        this.subsubsubpixels = subsubsubpixels;
    }
}