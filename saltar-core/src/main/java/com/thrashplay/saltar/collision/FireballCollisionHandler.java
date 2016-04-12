package com.thrashplay.saltar.collision;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.collision.CollisionCategoryIds;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class FireballCollisionHandler implements CollisionHandler {
    @Override
    public void handleCollision(GameObject ourObject, GameObject otherObject, Rectangle ourBoundingBox, Rectangle otherBoundingBox, boolean[] directions) {
        if (isEnemy(otherObject)) {
            ourObject.setDead(true);
            otherObject.setDead(true);
        }
    }

    private boolean isEnemy(GameObject otherObject) {
        Collider collider = otherObject.getComponent(Collider.class);
        return collider.getCategory() == CollisionCategoryIds.LIVE_ENEMY;
    }
}
