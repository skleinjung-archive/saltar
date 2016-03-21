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
        IdleFacingLeft,
        IdleFacingRight,
        WalkingLeft,
        WalkingRight,
        JumpingLeft,
        JumpingRight
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

    private AnimationState animationState = AnimationState.IdleFacingRight;
    private VerticalDirection verticalDirection = VerticalDirection.Idle;
    private HorizontalDirection horizontalDirection = HorizontalDirection.Right;
    boolean isWalking = false;

    public AnimationState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimationState animationState) {
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
        animationState = AnimationState.WalkingLeft;
    }

    public void onHorizontalKeyRelased() {
        switch (animationState) {
            case WalkingLeft:
                animationState = AnimationState.IdleFacingLeft;
                break;

            case WalkingRight:
                animationState = AnimationState.IdleFacingRight;
                break;
        }
    }

    public void onRightPressed() {
        animationState = AnimationState.WalkingRight;
    }

    public void onWallCollision() {
        switch (animationState) {
            case WalkingLeft:
                animationState = AnimationState.IdleFacingLeft;
                break;

            case WalkingRight:
                animationState = AnimationState.IdleFacingRight;
                break;
        }
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        Movement movement = gameObject.getComponent(Movement.class);
        Player player = gameObject.getComponent(Player.class);

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
            player.setAnimationState(AnimationState.Jumping);
        } else if (movement.getVelocityY() > 0) {
            // falling, really
            player.setAnimationState(AnimationState.Jumping);
        } else if (movement.getVelocityX() < 0) {
            player.setAnimationState(AnimationState.WalkingLeft);
            facingLeft = true;
        } else if (movement.getVelocityX() > 0) {
            player.setAnimationState(AnimationState.WalkingRight);
            facingLeft = false;
        } else if (facingLeft) {
            player.setAnimationState(AnimationState.IdleFacingLeft);
        } else {
            player.setAnimationState(AnimationState.IdleFacingRight);
        }
        */
    }
}

