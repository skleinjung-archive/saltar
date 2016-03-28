package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PlayerMovementStatsRenderer implements RenderableComponent {
    private GameObjectManager gameObjectManager;

    public PlayerMovementStatsRenderer(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        GameObject player = gameObjectManager.getGameObject("player");
        Movement movement = player.getComponent(Movement.class);

        int fontSize = 12;
        graphics.drawString("accelerationX: " + movement.getAccelerationX(), 20, 20, 0xffffffff, fontSize, Graphics.HorizontalAlignment.Left, Graphics.VerticalAlignment.Top);
        graphics.drawString("velocityX: " + movement.getVelocityX(), 20, 35, 0xffffffff, fontSize, Graphics.HorizontalAlignment.Left, Graphics.VerticalAlignment.Top);
    }
}
