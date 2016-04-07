package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.Config;
import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.collision.CollisionCategoryIds;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PlayerCollisionHandler implements CollisionHandler, RenderableComponent {

    private boolean[] collisionDirections = new boolean[4];

    @Override
    public void handleCollision(GameObject ourObject, GameObject otherObject, Rectangle ourBoundingBox, Rectangle otherBoundingBox, boolean[] directions) {
        Player player = ourObject.getComponent(Player.class);

        int category = getCollisionCategory(otherObject);

        if (category == CollisionCategoryIds.TILE) {
            if (directions[CollisionHandler.DIRECTION_LEFT]) {
                collisionDirections[DIRECTION_LEFT] = true;
                player.onWallCollision();
            }

            if (directions[CollisionHandler.DIRECTION_TOP]) {
                collisionDirections[DIRECTION_TOP] = true;
            }

            if (directions[CollisionHandler.DIRECTION_RIGHT]) {
                collisionDirections[DIRECTION_LEFT] = true;
                player.onWallCollision();
            }

            if (directions[CollisionHandler.DIRECTION_BOTTOM]) {
                collisionDirections[DIRECTION_BOTTOM] = true;
            }
        }

        if (category == CollisionCategoryIds.LIVE_ENEMY) {
            if (directions[CollisionHandler.DIRECTION_LEFT] || directions[CollisionHandler.DIRECTION_TOP] || directions[CollisionHandler.DIRECTION_RIGHT]) {
                if (isEnemy(otherObject)) {
                    player.onDeath(ourObject);
                }
            }
        }
    }

    private int getCollisionCategory(GameObject otherObject) {
        return otherObject.getComponent(Collider.class).getCategory();
    }

    private boolean isEnemy(GameObject otherObject) {
        Collider collider = otherObject.getComponent(Collider.class);
        return collider.getCategory() == CollisionCategoryIds.LIVE_ENEMY;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        if (Config.renderCollisions) {
            Position position = gameObject.getComponent(Position.class);

            if (collisionDirections[CollisionHandler.DIRECTION_LEFT]) {
                graphics.fillCircle((int) position.getX(), (int) position.getCenterY(), 9, 0xffff0000);
            }
            if (collisionDirections[CollisionHandler.DIRECTION_RIGHT]) {
                graphics.fillCircle((int) position.getRight(), (int) position.getCenterY(), 9, 0xffff0000);
            }
            if (collisionDirections[CollisionHandler.DIRECTION_TOP]) {
                graphics.fillCircle((int) position.getCenterX(), (int) position.getTop(), 9, 0xffff0000);
            }
            if (collisionDirections[CollisionHandler.DIRECTION_BOTTOM]) {
                graphics.fillCircle((int) position.getCenterX(), (int) position.getBottom(), 9, 0xffff0000);
            }
        }

        // reset collision directions for next frame
        collisionDirections[0] = collisionDirections[1] = collisionDirections[2] = collisionDirections[3] = false;
    }
}
