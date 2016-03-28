package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.saltar.physics.Measurement;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class KeyboardMovementController implements UpdateableComponent {

    private enum Direction {
        Left,
        Right
    }

    private Measurement MIN_WALK_VELOCITY = new Measurement("00130");
    private Measurement WALK_ACCELERATION = new Measurement("00098");
    private Measurement MAX_WALK_SPEED = new Measurement("01900");
    private Measurement RELEASE_DECELERATION = new Measurement("000D0");

    private InputManager inputManager;
    private int jumpFrames = 0;

    private Direction lastKeyPressed;

    public KeyboardMovementController(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        Player player = gameObject.getComponent(Player.class);
        Movement movement = gameObject.getComponent(Movement.class);
        movement.setClampVelocityX(false);

        boolean horizontalKeyDown = false;
        if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_S)) {
//            movement.setVelocityX(-8);
            if (movement.getVelocityX() > -MIN_WALK_VELOCITY.getAsPixels()) {
                movement.setVelocityX(-MIN_WALK_VELOCITY.getAsPixels());
            }
            if (movement.getVelocityX() < -MAX_WALK_SPEED.getAsPixels()) {
                movement.setVelocityX(-MAX_WALK_SPEED.getAsPixels());
            }
            movement.setAccelerationX(-WALK_ACCELERATION.getAsPixels());
            horizontalKeyDown = true;

            player.onLeftPressed();
            lastKeyPressed = Direction.Left;
        }

        if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_F)) {
//            movement.setVelocityX(8);
            if (movement.getVelocityX() < MIN_WALK_VELOCITY.getAsPixels()) {
                movement.setVelocityX(MIN_WALK_VELOCITY.getAsPixels());
            }
            if (movement.getVelocityX() > MAX_WALK_SPEED.getAsPixels()) {
                movement.setVelocityX(MAX_WALK_SPEED.getAsPixels());
            }
            movement.setAccelerationX(WALK_ACCELERATION.getAsPixels());
            horizontalKeyDown = true;

            player.onRightPressed();
            lastKeyPressed = Direction.Right;
        }

        if (!horizontalKeyDown) {
//            movement.setVelocityX(0);
//
            if (movement.getVelocityX() > 0) {
                movement.setAccelerationX(-RELEASE_DECELERATION.getAsPixels());
                movement.setClampVelocityX(true);
            } else if (movement.getVelocityX() < 0) {
                movement.setAccelerationX(RELEASE_DECELERATION.getAsPixels());
                movement.setClampVelocityX(true);
            }

            player.onHorizontalKeyRelased();
        }

//        if (inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
//            if (jumpFrames < 10) {
//                movement.setVelocityY(-(1.0f * jumpFrames));
//            } else if (jumpFrames > 20) {
//                movement.setVelocityY(-10 + (1.0f * (jumpFrames - 20)));
//            } else {
//                movement.setVelocityY(-10);
//            }
//
//            jumpFrames++;
//        } else {
//            jumpFrames = 0;
//        }
    }
}
