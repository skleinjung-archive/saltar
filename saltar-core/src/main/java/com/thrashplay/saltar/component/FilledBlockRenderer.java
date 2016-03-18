package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class FilledBlockRenderer implements RenderableComponent {
    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        Position position = gameObject.getComponent(Position.class);
        graphics.fillCircle(position.getX(), position.getY(), 50, 0xffff0000);
    }
}
