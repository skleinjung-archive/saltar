package com.thrashplay.saltar.ai;

import com.thrashplay.luna.api.animation.AnimationListener;
import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.collision.CollisionCategoryIds;
import com.thrashplay.luna.collision.CollisionListener;
import com.thrashplay.luna.api.animation.AnimationController;

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
                movement.setVelocityX(1.5f);
            }
        } else {
            movement.setVelocityX(0);
        }
    }

    private void die(final GameObject gameObject) {
        if (!dead) {
            dead = true;
            final AnimationController animationController = gameObject.getComponent(AnimationController.class);
            animationController.setCurrentState("dead");
            animationController.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStarted(String animationState) {
                    // no op
                }

                @Override
                public void onAnimationComplete(String animationState) {
                    if ("dead".equals(animationState)) {
                        animationController.removeAnimationListener(this);
                        gameObject.setDead(true);
                    }
                }
            });

            Collider collider = gameObject.getComponent(Collider.class);
            collider.setCategory(CollisionCategoryIds.DEAD_ENEMY);
        }
    }

    private boolean isPlayer(GameObject otherObject) {
        Collider collider = otherObject.getComponent(Collider.class);
        return collider.getCategory() == CollisionCategoryIds.PLAYER;
    }
}
