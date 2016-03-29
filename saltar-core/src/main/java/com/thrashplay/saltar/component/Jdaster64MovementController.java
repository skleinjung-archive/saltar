package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.math.MathUtils;
import com.thrashplay.luna.collision.CollisionListener;
import com.thrashplay.saltar.physics.Measurement;

/**
 * From: http://s276.photobucket.com/user/jdaster64/media/smb_playerphysics.png.html
 *
 * @author Sean Kleinjung
 */
public class Jdaster64MovementController implements CollisionListener, UpdateableComponent {

    // ground movement states
    private static final MovementState AT_REST = new AtRest();
    private static final MovementState WALK_LEFT = new WalkLeft();
    private static final MovementState WALK_RIGHT = new WalkRight();
    private static final MovementState RUN_LEFT = new RunLeft();
    private static final MovementState RUN_RIGHT = new RunRight();
    private static final MovementState DECELERATE_LEFT = new DecelerateLeft();
    private static final MovementState DECELERATE_RIGHT = new DecelerateRight();
    private static final MovementState SKID_FACE_LEFT = new SkidFaceLeft();
    private static final MovementState SKID_FACE_RIGHT = new SkidFaceRight();

    // air movement states - horizontal
    public final MovementState MOVE_LEFT = new MoveLeft();
    public final MovementState MOVE_RIGHT = new MoveRight();
    public final MovementState JUMPING_NO_HORIZONTAL_MOVEMENT = new JumpingNoHorizontalMovement();

    // ground movement parameters
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

    private enum Direction {
        Left,
        Right
    }

    private static class JumpParameters {
        private Direction initialDirection;
        private float initialSpeed;

        public JumpParameters(float initialSpeed) {
            this.initialSpeed = initialSpeed;
            if (initialSpeed < 0) {
                initialDirection = Direction.Left;
            } else {
                initialDirection = Direction.Right;
            }
        }

        public Direction getInitialDirection() {
            return initialDirection;
        }

        public float getInitialSpeed() {
            return initialSpeed;
        }
    }

    private InputManager inputManager;
    private MovementState movementState;
    private JumpParameters jump;

    public Jdaster64MovementController(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void onCollision(GameObject ourObject, GameObject otherObject, boolean[] directions) {
        if (directions[CollisionHandler.DIRECTION_BOTTOM]) {
            jump = null;

            Movement movement = ourObject.getComponent(Movement.class);
            movement.setAccelerationX(0);
            movement.setVelocityY(0);
        }
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        Player player = gameObject.getComponent(Player.class);
        Movement movement = gameObject.getComponent(Movement.class);

        updateMovementState(movement);
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
        if (jump != null) {
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
                System.out.println("setting jumpv velocity");
                movement.setVelocityY(-LOW_VERTICAL_START_VELOCITY.getAsPixels());
                jump = new JumpParameters(movement.getVelocityX());
            }
        }

        if (!horizontalKeyDown) {
            player.onHorizontalKeyRelased();
        }
    }

    private void updateMovementState(Movement movement) {
        if (jump != null) {
            if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_S)) {
                movementState = MOVE_LEFT;
            } else if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW) || inputManager.isKeyDown(KeyCode.KEY_F)) {
                movementState = MOVE_RIGHT;
            } else {
                movementState = JUMPING_NO_HORIZONTAL_MOVEMENT;
            }
        } else {
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
            } else if (movement.getVelocityX() < 0 && movementState != DECELERATE_RIGHT) {
                // we are moving left, but player pressed no buttons
                movementState = DECELERATE_LEFT;
            } else if (movement.getVelocityX() > 0 && movementState != DECELERATE_LEFT) {
                // we are moving right, but player pressed no buttons
                movementState = DECELERATE_RIGHT;
            } else {
                // we are at rest
                movementState = AT_REST;
            }
        }
    }

    private abstract static class MovementState {
        protected abstract void updateMovement(Movement movement);
    }

    private abstract static class JumpMovementState {
        protected abstract void updateMovement(Movement movement, JumpParameters jump);
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

    private static class RunLeft extends MovementState {
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

    private static class RunRight extends MovementState {
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

    private static class DecelerateLeft extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(RELEASE_DECELERATION.getAsPixels());
            if (movement.getVelocityX() >= 0) {
                movement.setVelocityX(0);
                movement.setAccelerationX(0);
            }
        }
    }

    private static class DecelerateRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(-RELEASE_DECELERATION.getAsPixels());
            if (movement.getVelocityX() <= 0) {
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

    private static class SkidFaceRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            movement.setAccelerationX(SKID_DECELERATION.getAsPixels());
            if ((MathUtils.sign(movement.getVelocityX()) == MathUtils.sign(movement.getAccelerationX()))) {
                movement.setVelocityX(0);
                movement.setAccelerationX(0);
            }
        }
    }

    // jumping horizontal movement

    private class MoveLeft extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            if (movement.getVelocityX() <= 0) {
                // gain momentum
                if (Math.abs(movement.getVelocityX()) < CURRENT_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels()) {
                    movement.setAccelerationX(-LOW_ACCELERATION.getAsPixels());
                } else {
                    movement.setAccelerationX(-HIGH_ACCELERATION.getAsPixels());
                }

                if (Math.abs(jump.getInitialSpeed()) < HIGH_MAX_SPEED_CUTOFF.getAsPixels()) {
                    if (Math.abs(movement.getVelocityX()) > LOW_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(-LOW_MAX_SPEED.getAsPixels());
                    }
                } else {
                    if (Math.abs(movement.getVelocityX()) > HIGH_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(-HIGH_MAX_SPEED.getAsPixels());
                    }
                }
            } else {
                // lose momentum
                if (Math.abs(movement.getVelocityX()) > CURRENT_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels()) {
                    // high
                    movement.setAccelerationX(-HIGH_DECELERATION.getAsPixels());
                } else if (Math.abs(movement.getVelocityX()) < CURRENT_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels() &&
                        Math.abs(jump.getInitialSpeed()) >= START_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels()) {
                    // mid
                    movement.setAccelerationX(-MEDIUM_DECELERATION.getAsPixels());
                } else {
                    // low
                    movement.setAccelerationX(-LOW_DECELERATION.getAsPixels());
                }

                if (Math.abs(jump.getInitialSpeed()) < HIGH_MAX_SPEED_CUTOFF.getAsPixels()) {
                    if (Math.abs(movement.getVelocityX()) > LOW_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(LOW_MAX_SPEED.getAsPixels());
                    }
                } else {
                    if (Math.abs(movement.getVelocityX()) > HIGH_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(HIGH_MAX_SPEED.getAsPixels());
                    }
                }
            }
        }
    }

    private class MoveRight extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            if (movement.getVelocityX() >= 0) {
                // gain momentum
                if (Math.abs(movement.getVelocityX()) < CURRENT_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels()) {
                    movement.setAccelerationX(LOW_ACCELERATION.getAsPixels());
                } else {
                    movement.setAccelerationX(HIGH_ACCELERATION.getAsPixels());
                }

                if (Math.abs(jump.getInitialSpeed()) < HIGH_MAX_SPEED_CUTOFF.getAsPixels()) {
                    if (Math.abs(movement.getVelocityX()) > LOW_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(LOW_MAX_SPEED.getAsPixels());
                    }
                } else {
                    if (Math.abs(movement.getVelocityX()) > HIGH_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(HIGH_MAX_SPEED.getAsPixels());
                    }
                }
            } else {
                // lose momentum
                if (Math.abs(movement.getVelocityX()) > CURRENT_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels()) {
                    // high
                    movement.setAccelerationX(HIGH_DECELERATION.getAsPixels());
                } else if (Math.abs(movement.getVelocityX()) < CURRENT_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels() &&
                        Math.abs(jump.getInitialSpeed()) >= START_SPEED_FAST_ACCELERATION_CUTOFF.getAsPixels()) {
                    // mid
                    movement.setAccelerationX(MEDIUM_DECELERATION.getAsPixels());
                } else {
                    // low
                    movement.setAccelerationX(LOW_DECELERATION.getAsPixels());
                }

                if (Math.abs(jump.getInitialSpeed()) < HIGH_MAX_SPEED_CUTOFF.getAsPixels()) {
                    if (Math.abs(movement.getVelocityX()) > LOW_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(-LOW_MAX_SPEED.getAsPixels());
                    }
                } else {
                    if (Math.abs(movement.getVelocityX()) > HIGH_MAX_SPEED.getAsPixels()) {
                        movement.setVelocityX(-HIGH_MAX_SPEED.getAsPixels());
                    }
                }
            }
        }
    }

    private class JumpingNoHorizontalMovement extends MovementState {
        @Override
        protected void updateMovement(Movement movement) {
            // no op
        }
    }
}
