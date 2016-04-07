package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.animation.AnimationListener;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.animation.AnimationController;
import com.thrashplay.luna.api.engine.GameObjectManager;

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

    private GameObjectManager gameObjectManager;
    private PlayerAnimationState animationState = PlayerAnimationState.IdleFacingRight;
    private boolean dying = false;

    public Player(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
    }

    public boolean isDying() {
        return dying;
    }

    public PlayerAnimationState getAnimationState() {
        return animationState;
    }

    public void onLeftPressed() {
        animationState = PlayerAnimationState.WalkingLeft;
    }

    public void onRightPressed() {
        animationState = PlayerAnimationState.WalkingRight;
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

    public void onDeath(final GameObject playerGameObject) {
        switch (animationState) {
            case WalkingLeft:
            case IdleFacingLeft:
                animationState = PlayerAnimationState.DyingLeft;
                break;

            case WalkingRight:
            case IdleFacingRight:
                animationState = PlayerAnimationState.DyingRight;
                break;
        }

        AnimationController animationController = playerGameObject.getComponent(AnimationController.class);
        animationController.addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStarted(String animationState) {
            }

            @Override
            public void onAnimationComplete(String animationState) {
                playerGameObject.setDead(true);
            }
        });

        GameObject screenFader = new GameObject("screen fader");
        screenFader.setRenderLayer(GameObject.RenderLayer.Overlay);
        screenFader.addComponent(new ScreenFader(650, 250));
        gameObjectManager.register(screenFader);

        dying = true;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        Movement movement = gameObject.getComponent(Movement.class);

        AnimationController animationController = gameObject.getComponent(AnimationController.class);
        if (!animationController.getCurrentState().equals("DyingRight") && !animationController.getCurrentState().equals("DyingLeft")) {
            // don't change the death animation
            animationController.setCurrentState(this.animationState.name());
        }

        if (dying) {
            // stop horizontal movements on death
            movement.setVelocityX(0);
            movement.setAccelerationX(0);
        }
    }
}

