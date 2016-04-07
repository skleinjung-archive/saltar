package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class IntroScreenMessage implements RenderableComponent {

    private String message;
    private int size;

    public IntroScreenMessage(String message) {
        this(message, 12);
    }

    public IntroScreenMessage(String message, int size) {
        this.message = message;
        this.size = size;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        graphics.drawString(message, graphics.getWidth() / 2, graphics.getHeight() / 2, 0xffffffff, size, Graphics.HorizontalAlignment.Center, Graphics.VerticalAlignment.Center);
    }
}
