package com.thrashplay.saltar.editor.screen;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.saltar.editor.model.Level;
import com.thrashplay.saltar.editor.model.Project;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GridRenderer implements RenderableComponent {
    private Project project;
    private Level level;
    private int selectedColor;
    private int color;

    public GridRenderer(Project project, Level level) {
        this(project, level, 0xff999999);
    }

    public GridRenderer(Project project, Level level, int color) {
        this(project, level, color, 0xffff0000);
    }

    public GridRenderer(Project project, Level level, int color, int selectedColor) {
        this.project = project;
        this.level = level;
        this.selectedColor = selectedColor;
        this.color = color;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        for (int y = 0; y < level.getHeight(); y += level.getTileSize()) {
            for (int x = 0; x < level.getWidth(); x += level.getTileSize()) {
                graphics.drawRect(x, y, level.getTileSize(), level.getTileSize(), color);
            }
        }

        graphics.drawRect(level.getTileSize() * project.getSelectedTileX(), level.getTileSize() * project.getSelectedTileY(), level.getTileSize(), level.getTileSize(), selectedColor);
    }
}
