package com.thrashplay.saltar.collision;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.collision.CollisionCategoryIds;

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

        int direction = getCollidingDirectionWithShortestResolutionDistance(ourBoundingBox, otherBoundingBox, position, directions);
        int category = getCollisionCategory(otherObject);

        if (category == CollisionCategoryIds.TILE) {
            switch (direction) {
                case CollisionHandler.DIRECTION_LEFT:
                    position.setX(otherBoundingBox.getRight() + 1 - (ourBoundingBox.getX() - position.getX()));
                    if (movement.getVelocityX() < 0) {
                        movement.setVelocityX(0);
                        movement.setAccelerationX(0);
                    }
                    break;

                case CollisionHandler.DIRECTION_TOP:
                    position.setY(otherBoundingBox.getBottom() + 1 - (ourBoundingBox.getY() - position.getY()));
                    if (movement.getVelocityY() < 0) {
                        movement.setVelocityY(0);
                        movement.setAccelerationY(0);
                    }
                    break;

                case CollisionHandler.DIRECTION_RIGHT:
                    position.setX(otherBoundingBox.getLeft() - ourBoundingBox.getWidth() - (ourBoundingBox.getX() - position.getX()));
                    if (movement.getVelocityX() > 0) {
                        movement.setVelocityX(0);
                        movement.setAccelerationX(0);
                    }
                    break;

                case CollisionHandler.DIRECTION_BOTTOM:
                    position.setY(otherBoundingBox.getY() - ourBoundingBox.getHeight() - (ourBoundingBox.getY() - position.getY()) + 1);
                    if (movement.getVelocityY() > 0) {
                        movement.setVelocityY(0);
                        movement.setAccelerationY(0);
                    }

                    break;
            }
        }
    }

    private int getCollisionCategory(GameObject otherObject) {
        return otherObject.getComponent(Collider.class).getCategory();
    }

    private int getCollidingDirectionWithShortestResolutionDistance(Rectangle ourBoundingBox, Rectangle otherBoundingBox, Position position, boolean[] collisionDirections) {
        float shortestDistance = Integer.MAX_VALUE;
        int direction = CollisionHandler.DIRECTION_UNKNOWN;
        float distance;

        // check left
        if (collisionDirections[CollisionHandler.DIRECTION_LEFT]) {
            distance = Math.abs(position.getX() - (otherBoundingBox.getRight() + 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_LEFT;
            }
        }

        // check top
        if (collisionDirections[CollisionHandler.DIRECTION_TOP]) {
            distance = Math.abs(position.getY() - (otherBoundingBox.getBottom() + 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_TOP;
            }
        }

        // check right
        if (collisionDirections[CollisionHandler.DIRECTION_RIGHT]) {
            distance = Math.abs(position.getX() - (otherBoundingBox.getLeft() - ourBoundingBox.getWidth() - 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_RIGHT;
            }
        }

        // check bottom
        if (collisionDirections[CollisionHandler.DIRECTION_BOTTOM]) {
            distance = Math.abs(position.getY() - (otherBoundingBox.getY() - ourBoundingBox.getHeight() - 1));
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = CollisionHandler.DIRECTION_BOTTOM;
            }
        }

        return direction;
    }
}
