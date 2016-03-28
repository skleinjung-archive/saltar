package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.math.MathUtils;
import com.thrashplay.saltar.physics.Measurement;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class KeyboardMovementController implements UpdateableComponent {

    private static final MovementState AT_REST = new AtRest();
    private static final MovementState WALK_LEFT = new WalkLeft();
    private static final MovementState WALK_RIGHT = new WalkRight();
    private static final MovementState RUN_LEFT = new RunLeft();
    private static final MovementState RUN_RIGHT = new RunRight();
    private static final MovementState DECELERATE_LEFT = new DecelerateLeft();
    private static final MovementState DECELERATE_RIGHT = new DecelerateRight();
    private static final MovementState SKID_FACE_LEFT = new SkidFaceLeft();
    private static final MovementState SKID_FACE_RIGHT = new SkidFaceRight();

    // ground movement
    private static final Measurement MIN_WALK_VELOCITY = new Measurement("00130");
    private static final Measurement WALK_ACCELERATION = new Measurement("00098");
    private static final Measurement MAX_WALK_SPEED = new Measurement("01900");
    private static final Measurement RUN_ACCELERATION = new Measurement("000E4");
    private static final Measurement MAX_RUN_SPEED = new Measurement("02900");
    private static final Measurement RELEASE_DECELERATION = new Measurement("000D0");
    private static final Measurement SKID_DECELERATION = new Measurement("001A0");

    // air movement - vertical parameters
    private static final Measurement LOW_VERTICAL_START_VELOCITY = new Measurement("04000");
    private static final Measurement LOW_GRAVITY_JUMP_BUTTON_DOWN = new Measurement("00200");
    private static final Measurement LOW_GRAVITY_JUMP_BUTTON_RELEASED = new Measurement("00700");

    // air movement - horizontal parameters
    private static final Measurement CURRENT_SPEED_FAST_ACCELERATION_CUTOFF = new Measurement("01900");
    private static final Measurement START_SPEED_FAST_ACCELERATION_CUTOFF = new Measurement("01000");
    private static final Measurement HIGH_MAX_SPEED_CUTOFF = new Measurement("01900");
    private static final Measurement LOW_MAX_SPEED = new Measurement("01900");
    private static final Measurement HIGH_MAX_SPEED = new Measurement("02900");
    private static final Measurement LOW_ACCELERATION = new Measurement("00098");
    private static final Measurement HIGH_ACCELERATION = new Measurement("000E4");
    private static final Measurement HIGH_DECELERATION = new Measurement("000E4");
    private static final Measurement MEDIUM_DECELERATION = new Measurement("000D0");
    private static final Measurement LOW_DECELERATION = new Measurement("00098");

    private InputManager inputManager;
    private MovementState movementState;

    public KeyboardMovementController(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        Player player = gameObject.getComponent(Player.class);
        Movement movement = gameObject.getComponent(Movement.class);

        updateState(movement);
        movementState.updateMovement(movement);

        boolean horizontalKeyDown = false;
        if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_S)) {
            player.onLeftPressed();
            horizontalKeyDown = true;
        }

        if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_F)) {
            player.onRightPressed();
            horizontalKeyDown = true;
        }

        // we are in the air
        if (movement.getVelocityY() != 0) {
            if (inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
                // jump button is held down, use slower gravity
                movement.setAccelerationY(LOW_GRAVITY_JUMP_BUTTON_DOWN.getAsPixels());
            } else {
                // no jump button, full gravity
                movement.setAccelerationY(LOW_GRAVITY_JUMP_BUTTON_RELEASED.getAsPixels());
            }
        } else {
            // we are on the ground
            if (inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
                // player jumped, set initial velocity
                movement.setVelocityY(-LOW_VERTICAL_START_VELOCITY.getAsPixels());
            }
        }

        if (!horizontalKeyDown) {
            player.onHorizontalKeyRelased();
        }
    }

    private void updateState(Movement movement) {
        if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_S)) {
            if (movement.getVelocityX() <= 0) {
                // player pressed left, and we are at rest or moving left
                if (inputManager.isKeyDown(KeyCode.KEY_SHIFT)) {
                    movementState = RUN_LEFT;
                } else {
                    movementState = WALK_LEFT;
                }
            } else {
                // player pressed left, and we are moving right
                movementState = SKID_FACE_LEFT;
            }
        } else if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_F)) {
            if (movement.getVelocityX() >= 0) {
                // player pressed right, and we are at rest or moving right
                if (inputManager.isKeyDown(KeyCode.KEY_SHIFT)) {
                    movementState = RUN_RIGHT;
                } else {
                    movementState = WALK_RIGHT;
                }
            } else {
                // player pressed right, and we are moving left
                movementState = SKID_FACE_RIGHT;
            }
        } else if (movement.getVelocityX() < 0) {
            // we are moving left, but player pressed no buttons
            movementState = DECELERATE_LEFT;
        } else if (movement.getVelocityX() > 0) {
            // we are moving right, but player pressed no buttons
            movementState = DECELERATE_RIGHT;
        } else {
            // we are at rest
            movementState = AT_REST;
        }
    }

    private abstract static class MovementState {
        protected abstract void updateMovement(Movement movement);
    }

    private static class AtRest extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setVelocityX(0);
            movement.setAccelerationX(0);
        }
    }

    private static class WalkLeft extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            if (movement.getVelocityX() > -MIN_WALK_VELOCITY.getAsPixels()) {
                movement.setVelocityX(-MIN_WALK_VELOCITY.getAsPixels());
            }
            if (movement.getVelocityX() < -MAX_WALK_SPEED.getAsPixels()) {
                movement.setVelocityX(-MAX_WALK_SPEED.getAsPixels());
            }
            movement.setAccelerationX(-WALK_ACCELERATION.getAsPixels());
        }
    }

    private static class WalkRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            if (movement.getVelocityX() < MIN_WALK_VELOCITY.getAsPixels()) {
                movement.setVelocityX(MIN_WALK_VELOCITY.getAsPixels());
            }
            if (movement.getVelocityX() > MAX_WALK_SPEED.getAsPixels()) {
                movement.setVelocityX(MAX_WALK_SPEED.getAsPixels());
            }
            movement.setAccelerationX(WALK_ACCELERATION.getAsPixels());
        }
    }

    private static  class RunLeft extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            if (movement.getVelocityX() > -MIN_WALK_VELOCITY.getAsPixels()) {
                movement.setVelocityX(-MIN_WALK_VELOCITY.getAsPixels());
            }
            if (movement.getVelocityX() < -MAX_RUN_SPEED.getAsPixels()) {
                movement.setVelocityX(-MAX_RUN_SPEED.getAsPixels());
            }
            movement.setAccelerationX(-RUN_ACCELERATION.getAsPixels());
        }
    }

    private static  class RunRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            if (movement.getVelocityX() < MIN_WALK_VELOCITY.getAsPixels()) {
                movement.setVelocityX(MIN_WALK_VELOCITY.getAsPixels());
            }
            if (movement.getVelocityX() > MAX_RUN_SPEED.getAsPixels()) {
                movement.setVelocityX(MAX_RUN_SPEED.getAsPixels());
            }
            movement.setAccelerationX(RUN_ACCELERATION.getAsPixels());
        }
    }

    private static  class DecelerateLeft extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(RELEASE_DECELERATION.getAsPixels());
            if ((MathUtils.sign(movement.getVelocityX()) == MathUtils.sign(movement.getAccelerationX()))) {
                movement.setVelocityX(0);
                movement.setAccelerationX(0);
            }
        }
    }

    private static  class DecelerateRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(-RELEASE_DECELERATION.getAsPixels());
            if ((MathUtils.sign(movement.getVelocityX()) == MathUtils.sign(movement.getAccelerationX()))) {
                movement.setVelocityX(0);
                movement.setAccelerationX(0);
            }
        }
    }

    private static class SkidFaceLeft extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(-SKID_DECELERATION.getAsPixels());
            if ((MathUtils.sign(movement.getVelocityX()) == MathUtils.sign(movement.getAccelerationX()))) {
                movement.setVelocityX(0);
                movement.setAccelerationX(0);
            }
        }
    }

    private static  class SkidFaceRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(SKID_DECELERATION.getAsPixels());
            if ((MathUtils.sign(movement.getVelocityX()) == MathUtils.sign(movement.getAccelerationX()))) {
                movement.setVelocityX(0);
                movement.setAccelerationX(0);
            }
        }
    }
}
