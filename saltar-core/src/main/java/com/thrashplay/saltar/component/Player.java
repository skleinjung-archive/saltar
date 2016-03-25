package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.animation.AnimationState;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Player implements UpdateableComponent {
    public enum PlayerAnimationState {
        IdleFacingLeft,
        IdleFacingRight,
        WalkingLeft,
        WalkingRight,
        JumpingLeft,
        JumpingRight,
        DyingLeft,
        DyingRight
    }

    public enum VerticalDirection {
        Up,
        Down,
        Idle
    }

    public enum HorizontalDirection {
        Left,
        Right,
        Idle
    }

    private PlayerAnimationState animationState = PlayerAnimationState.IdleFacingRight;
    private VerticalDirection verticalDirection = VerticalDirection.Idle;
    private HorizontalDirection horizontalDirection = HorizontalDirection.Right;
    boolean isWalking = false;

    public PlayerAnimationState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(PlayerAnimationState animationState) {
        this.animationState = animationState;
    }

    public VerticalDirection getVerticalDirection() {
        return verticalDirection;
    }

    public void setVerticalDirection(VerticalDirection verticalDirection) {
        this.verticalDirection = verticalDirection;
    }

    public HorizontalDirection getHorizontalDirection() {
        return horizontalDirection;
    }

    public void setHorizontalDirection(HorizontalDirection horizontalDirection) {
        this.horizontalDirection = horizontalDirection;
    }

    public void onLeftPressed() {
        animationState = PlayerAnimationState.WalkingLeft;
    }

    public void onHorizontalKeyRelased() {
        switch (animationState) {
            case WalkingLeft:
                animationState = PlayerAnimationState.IdleFacingLeft;
                break;

            case WalkingRight:
                animationState = PlayerAnimationState.IdleFacingRight;
                break;
        }
    }

    public void onRightPressed() {
        animationState = PlayerAnimationState.WalkingRight;
    }

    public void onWallCollision() {
        switch (animationState) {
            case WalkingLeft:
                animationState = PlayerAnimationState.IdleFacingLeft;
                break;

            case WalkingRight:
                animationState = PlayerAnimationState.IdleFacingRight;
                break;
        }
    }

    public void onDeath() {
        System.out.println("player is dead");
        animationState = PlayerAnimationState.DyingRight;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        Movement movement = gameObject.getComponent(Movement.class);
        Player player = gameObject.getComponent(Player.class);

        AnimationState animationState = gameObject.getComponent(AnimationState.class);
//        if (!animationState.getCurrentState().equals("DyingRight")) { // don't un-dead us
            animationState.setCurrentState(this.animationState.name());
//        }

        /*

        if (movement.getVelocityY() < 0) {
            verticalDirection = VerticalDirection.Up;
        } else if (movement.getVelocityY() > 0) {
            verticalDirection = VerticalDirection.Down;
        }

        if (movement.getVelocityX() == 0) {
            horizontalDirection = HorizontalDirection.Idle;
        }

        switch (horizontalDirection) {
            case Left:
                switch (verticalDirection) {
                    case Up:
                    case Down:
                        animationState = AnimationState.JumpingLeft;
                        break;

                    case Idle:
                        animationState = AnimationState.WalkingLeft;
                        break;
                }
                break;

            case Right:
                switch (verticalDirection) {
                    case Up:
                    case Down:
                        animationState = AnimationState.JumpingRight;
                        break;

                    case Idle:
                        animationState = AnimationState.WalkingRight;
                        break;
                }
                break;

            case Idle:
                switch (verticalDirection) {
                    case Up:
                    case Down:
                        animationState = AnimationState.JumpingRight;
                        break;

                    case Idle:
                        animationState = AnimationState.IdleFacingRight;
                        break;
                }
        }
        */

        /*
        if (movement.getVelocityY() < 0) {
            player.setCurrentState(AnimationState.Jumping);
        } else if (movement.getVelocityY() > 0) {
            // falling, really
            player.setCurrentState(AnimationState.Jumping);
        } else if (movement.getVelocityX() < 0) {
            player.setCurrentState(AnimationState.WalkingLeft);
            facingLeft = true;
        } else if (movement.getVelocityX() > 0) {
            player.setCurrentState(AnimationState.WalkingRight);
            facingLeft = false;
        } else if (facingLeft) {
            player.setCurrentState(AnimationState.IdleFacingLeft);
        } else {
            player.setCurrentState(AnimationState.IdleFacingRight);
        }
        */
    }
}

