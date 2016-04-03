package com.thrashplay.saltar.collision;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class DefaultResolutionCollisionHandler implements CollisionHandler {
    @Override
    public void handleCollision(GameObject ourObject, GameObject otherObject, Rectangle ourBoundingBox, Rectangle otherBoundingBox, boolean[] directions) {
        Position position = ourObject.getComponent(Position.class);
        Movement movement = ourObject.getComponent(Movement.class);

        if (directions[CollisionHandler.DIRECTION_LEFT]) {
            position.setX(otherBoundingBox.getRight() + 1);
            if (movement.getVelocityX() < 0) {
                movement.setVelocityX(0);
            }
        } else if (directions[CollisionHandler.DIRECTION_TOP]) {
            position.setY(otherBoundingBox.getBottom() + 1);
            if (movement.getVelocityY() < 0) {
                movement.setVelocityY(0);
            }
        } else if (directions[CollisionHandler.DIRECTION_RIGHT]) {
            position.setX(otherBoundingBox.getLeft() - ourBoundingBox.getWidth() - 1);
            if (movement.getVelocityX() > 0) {
                movement.setVelocityX(0);
            }
        } else if (directions[CollisionHandler.DIRECTION_BOTTOM]) {
            position.setY(otherBoundingBox.getY() - ourBoundingBox.getHeight() + 1);
            if (movement.getVelocityY() > 0) {
                movement.setVelocityY(0);
            }
        }
    }
}
