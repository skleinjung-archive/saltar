package com.thrashplay.saltar.ai;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.collision.CollisionCategoryIds;
import com.thrashplay.luna.collision.CollisionListener;
import com.thrashplay.luna.animation.AnimationState;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class BumpIntoThingsAndTurnAroundController implements UpdateableComponent, CollisionListener {

    boolean dead = false;
    boolean facingLeft = true;

    @Override
    public void onCollision(GameObject ourObject, GameObject otherObject, boolean[] directions) {
        if (directions[CollisionHandler.DIRECTION_LEFT]) {
            facingLeft = false;
        } else if (directions[CollisionHandler.DIRECTION_RIGHT]) {
            facingLeft = true;
        }

        if (directions[CollisionHandler.DIRECTION_TOP]) {
            if (isPlayer(otherObject)) {
                die(ourObject);
            }
        }
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        Movement movement = gameObject.getComponent(Movement.class);

        if (!dead) {
            if (facingLeft) {
                movement.setVelocityX(-1.5f);
            } else {
                movement.setVelocityX(2.5f);
            }
        } else {
            movement.setVelocityX(0);
        }
    }

    private void die(GameObject gameObject) {
        dead = true;
        AnimationState animationState = gameObject.getComponent(AnimationState.class);
        animationState.setCurrentState("dead");
    }

    private boolean isPlayer(GameObject otherObject) {
        Collider collider = otherObject.getComponent(Collider.class);
        return collider.getCategory() == CollisionCategoryIds.PLAYER;
    }
}
