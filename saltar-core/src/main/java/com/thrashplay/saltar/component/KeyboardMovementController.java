package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.GameObject;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.math.MathUtils;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class KeyboardMovementController implements UpdateableComponent {

    private InputManager inputManager;

    public KeyboardMovementController(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void update(GameObject gameObject) {
        Position position = gameObject.getComponent(Position.class);
        Movement movement = gameObject.getComponent(Movement.class);

        boolean horizontalKeyDown = false;
        if (inputManager.isKeyDown(KeyCode.KEY_LEFT_ARROW)) {
//            position.setX(position.getX() - 5);
            //movement.setAccelerationX(-1);
            movement.setVelocityX(-10);
            horizontalKeyDown = true;
        }

        if (inputManager.isKeyDown(KeyCode.KEY_RIGHT_ARROW)) {
//            position.setX(position.getX() + 5);
//            movement.setAccelerationX(1);
            movement.setVelocityX(10);
            horizontalKeyDown = true;
        }

        if (!horizontalKeyDown) {
//            movement.setVelocityX(0);
            movement.setAccelerationX(-MathUtils.sign(movement.getVelocityX() / 5));
        }

        if (inputManager.isKeyDown(KeyCode.KEY_SPACE)) {
            position.setY(position.getY() - 5);
        }
    }
}
