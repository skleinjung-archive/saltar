package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.animation.AnimationController;
import com.thrashplay.luna.api.animation.AnimationListener;
import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.collision.CollisionCategoryIds;
import com.thrashplay.luna.collision.CollisionListener;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Player implements UpdateableComponent, CollisionListener {
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

    private static final int INVINCIBILITY_DURATION = 2000;

    private GameObjectManager gameObjectManager;
    private PlayerAnimationState animationState = PlayerAnimationState.IdleFacingRight;
    private int currentHealth = 3;
    private int maximumHealth = 3;
    private long invincibleStartTime;
    private boolean invincible = false;
    private boolean dying = false;

    public Player(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaximumHealth() {
        return maximumHealth;
    }

    public void setMaximumHealth(int maximumHealth) {
        this.maximumHealth = maximumHealth;
    }

    public boolean isDying() {
        return dying;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public PlayerAnimationState getAnimationState() {
        return animationState;
    }

    @Override
    public void onCollision(GameObject ourObject, GameObject otherObject, boolean[] directions) {
        // stop the movement animation if we collide with a wall
        if (isTile(otherObject) && (directions[CollisionHandler.DIRECTION_LEFT] || directions[CollisionHandler.DIRECTION_RIGHT])) {
            onWallCollision();
        } else if (isEnemy(otherObject) && (directions[CollisionHandler.DIRECTION_LEFT] || directions[CollisionHandler.DIRECTION_RIGHT] || directions[CollisionHandler.DIRECTION_TOP])) {
            onDamage(ourObject);
        }
    }

    private boolean isEnemy(GameObject object) {
        Collider collider = object.getComponent(Collider.class);
        return collider.getCategory() == CollisionCategoryIds.LIVE_ENEMY;
    }

    private boolean isTile(GameObject object) {
        Collider collider = object.getComponent(Collider.class);
        return collider.getCategory() == CollisionCategoryIds.TILE;
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

    public void onDamage(final GameObject playerGameObject) {
        if (!invincible) {
            currentHealth--;
            if (currentHealth < 1) {
                onDeath(playerGameObject);
            } else {
                invincible = true;
                invincibleStartTime = System.currentTimeMillis();
            }
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
        Position position = gameObject.getComponent(Position.class);
        Movement movement = gameObject.getComponent(Movement.class);

        GameObject viewport = gameObjectManager.getGameObject(GameObjectIds.ID_VIEWPORT);
        Position viewportPosition = viewport.getComponent(Position.class);

        if (position.getTop() > viewportPosition.getBottom()) {
            onDeath(gameObject);
        }

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

        if (invincible) {
            if (System.currentTimeMillis() - invincibleStartTime >= INVINCIBILITY_DURATION) {
                invincible = false;
                invincibleStartTime = 0;
            }
        }
    }
}

