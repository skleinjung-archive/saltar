package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.graphics.Graphics;

import java.util.LinkedList;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class DebugStringRenderer implements RenderableComponent{
    public interface DebugStringProvider {
        String[] getDebugStrings(GameObjectManager gameObjectManager);
    }

    private GameObjectManager gameObjectManager;

    public DebugStringRenderer(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        int fontSize = 12;
        int y = 20;

        for (DebugStringProvider provider : gameObject.getComponents(DebugStringProvider.class, new LinkedList<DebugStringProvider>())) {
            String[] strings = provider.getDebugStrings(gameObjectManager);
            for (int i = 0; i < strings.length; i++) {
                graphics.drawString(strings[i], 20, y, 0xffffffff, fontSize, Graphics.HorizontalAlignment.Left, Graphics.VerticalAlignment.Top);
                y += 15;
            }
        }
    }
}
