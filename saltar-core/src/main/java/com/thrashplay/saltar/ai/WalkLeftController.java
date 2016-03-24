package com.thrashplay.saltar.ai;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.collision.CollisionListener;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class WalkLeftController implements UpdateableComponent, CollisionListener {

    boolean isFacingLeft = true;

    @Override
    public void onCollision(GameObject ourObject, GameObject otherObject, boolean[] directions) {
        if (directions[CollisionHandler.DIRECTION_LEFT]) {
            isFacingLeft = false;
        } else if (directions[CollisionHandler.DIRECTION_RIGHT]) {
            isFacingLeft = true;
        }
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        Movement movement = gameObject.getComponent(Movement.class);
        if (isFacingLeft) {
            movement.setVelocityX(-1.5f);
        } else {
            movement.setVelocityX(2.5f);
        }
    }
}
