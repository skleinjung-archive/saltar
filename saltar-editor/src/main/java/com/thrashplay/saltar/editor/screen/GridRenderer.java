package com.thrashplay.saltar.editor.screen;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.saltar.editor.model.Level;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GridRenderer implements RenderableComponent {
    private Level level;
    private int color;

    public GridRenderer(Level level) {
        this(level, 0xff999999);
    }

    public GridRenderer(Level level, int color) {
        this.level = level;
        this.color = color;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        for (int y = 0; y < level.getHeight(); y += level.getTileSize()) {
            for (int x = 0; x < level.getWidth(); x += level.getTileSize()) {
                graphics.drawRect(x, y, level.getTileSize(), level.getTileSize(), color);
            }
        }
    }
}
