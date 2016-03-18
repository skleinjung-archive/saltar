package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Player implements UpdateableComponent {
    public enum AnimationState {
        Idle,
        WalkingLeft,
        WalkingRight,
        Jumping
    }

    private AnimationState animationState = AnimationState.Idle;

    public AnimationState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimationState animationState) {
        this.animationState = animationState;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        Movement movement = gameObject.getComponent(Movement.class);
        Player player = gameObject.getComponent(Player.class);

        if (movement.getVelocityY() < 0) {
            player.setAnimationState(AnimationState.Jumping);
        } else if (movement.getVelocityY() > 0) {
            // falling, really
            player.setAnimationState(AnimationState.Jumping);
        } else if (movement.getVelocityX() < 0) {
            player.setAnimationState(AnimationState.WalkingLeft);
        } else if (movement.getVelocityX() > 0) {
            player.setAnimationState(AnimationState.WalkingRight);
        } else {
            player.setAnimationState(AnimationState.Idle);
        }
    }
}
