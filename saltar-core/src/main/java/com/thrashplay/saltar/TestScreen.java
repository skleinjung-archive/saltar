package com.thrashplay.saltar;

import com.thrashplay.luna.api.component.GameObject;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.ui.Button;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;
import com.thrashplay.saltar.component.FilledBlockRenderer;
import com.thrashplay.saltar.component.KeyboardMovementController;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TestScreen extends EntityManagerScreen {
    private MultiTouchManager multiTouchManager;
    private InputManager inputManager;

    public TestScreen(final MultiTouchManager multiTouchManager, final InputManager inputManager) {
        this.multiTouchManager = multiTouchManager;
        this.inputManager = inputManager;
    }

    @Override
    protected void doInitialize(Rectangle screenBounds) {
        entityManager.addEntity(new ClearScreen());
        entityManager.addEntity(new FpsDisplay());

        Button leftButton = new TextButton(multiTouchManager, "<", 60, screenBounds.getBottom() - 160, 100, 100);
        Button rightButton = new TextButton(multiTouchManager, ">", 195, screenBounds.getBottom() - 160, 100, 100);
        Button jumpButton = new TextButton(multiTouchManager, "^", screenBounds.getRight() - 160, screenBounds.getBottom() - 160, 100, 100);
        VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
        virtualKeyboard.registerButtonForKey(leftButton, KeyCode.KEY_LEFT_ARROW);
        virtualKeyboard.registerButtonForKey(rightButton, KeyCode.KEY_RIGHT_ARROW);
        virtualKeyboard.registerButtonForKey(jumpButton, KeyCode.KEY_SPACE);
        entityManager.addEntity(virtualKeyboard);

        inputManager.addKeyboard(virtualKeyboard);

        GameObject filledBoxSprite = new GameObject("circle");
        filledBoxSprite.addComponent(new Position(200, 200));
        filledBoxSprite.addComponent(new Movement());
        filledBoxSprite.addComponent(new FilledBlockRenderer());
        filledBoxSprite.addComponent(new KeyboardMovementController(inputManager));
        entityManager.addEntity(filledBoxSprite);
    }

    @Override
    public void shutdown() {
        entityManager.removeAll();
    }
}
