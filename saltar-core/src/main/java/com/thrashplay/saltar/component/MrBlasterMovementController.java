package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.Joystick;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.collision.CollisionListener;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MrBlasterMovementController implements UpdateableComponent, CollisionListener {
    private InputManager inputManager;
    private Joystick joystick;

    public MrBlasterMovementController(InputManager inputManager, Joystick joystick) {
        this.inputManager = inputManager;
        this.joystick = joystick;
    }

    boolean onGround = true;
    boolean holdingJumpDown = false;

    @Override
    public void update(GameObject gameObject, float delta) {
        Movement movement = gameObject.getComponent(Movement.class);

        /*
        if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_S)) {
            movement.setVelocityX(-3f);
        } else if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_F)) {
            movement.setVelocityX(3f);
        } else {
            movement.setVelocityX(0);
        }
        */

        if (joystick.getTiltX() < 0) {
            movement.setVelocityX(Math.max(-3f, joystick.getTiltX() / 12f));
        } else if (joystick.getTiltX() > 0) {
            movement.setVelocityX(Math.min(3f, joystick.getTiltX() / 12f));
        } else {
            movement.setVelocityX(0);
        }

        if (onGround && !holdingJumpDown) {
            if (inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
                movement.setVelocityY(-10);
//                movement.setAccelerationY(0.5f);  // we have a gravity set instead
                onGround = false;
                holdingJumpDown = true;
            }
        }

        if (!inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
            if (movement.getVelocityY() < -4.125) {
                movement.setVelocityY(-2);
            }
            holdingJumpDown = false;
        }
    }

    @Override
    public void onCollision(GameObject ourObject, GameObject otherObject, boolean[] directions) {
        if (directions[CollisionHandler.DIRECTION_BOTTOM]) {
            Movement movement = ourObject.getComponent(Movement.class);
//            movement.setAccelerationX(0);
//            movement.setVelocityY(0);
            onGround = true;
        }
    }
}
