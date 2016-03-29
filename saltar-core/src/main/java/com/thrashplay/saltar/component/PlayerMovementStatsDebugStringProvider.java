package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PlayerMovementStatsDebugStringProvider implements DebugStringRenderer.DebugStringProvider {

    @Override
    public String[] getDebugStrings(GameObjectManager gameObjectManager) {
        GameObject player = gameObjectManager.getGameObject("player");
        Movement movement = player.getComponent(Movement.class);

        String velocityXString = "velocityX: " + movement.getVelocityX();
        if (movement.getMaximumVelocityX() != 0) {
            velocityXString += " (" + movement.getMaximumVelocityX();
        }

        String velocityYString = "velocityY: " + movement.getVelocityY();
        if (movement.getMaximumVelocityY() != 0) {
            velocityYString += " (" + movement.getMaximumVelocityY();
        }

        return new String[] {
                "accelerationX: " + movement.getAccelerationX(),
                "accelerationY: " + movement.getAccelerationY(),
                velocityXString,
                velocityYString
        };
    }
}
