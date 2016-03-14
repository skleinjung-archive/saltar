package com.thrashplay.saltar;

import com.thrashplay.luna.api.component.GameObject;
import com.thrashplay.luna.api.component.Movement;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.api.engine.Updateable;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.Graphics;
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
    public void initialize(Rectangle screenBounds) {
        entityManager.addEntity(new ClearScreen());
        entityManager.addEntity(new FpsDisplay());

        VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
//        Button leftButton = new TextButton(touchManager, "<", 25, 25, 50, 50);
//        Button rightButton = new TextButton(touchManager, ">", 80, 25, 50, 50);

        Button leftButton = new TextButton(multiTouchManager, "<", 60, screenBounds.getBottom() - 160, 100, 100);
        Button rightButton = new TextButton(multiTouchManager, ">", 195, screenBounds.getBottom() - 160, 100, 100);
        Button jumpButton = new TextButton(multiTouchManager, "^", screenBounds.getRight() - 160, screenBounds.getBottom() - 160, 100, 100);
        virtualKeyboard.registerButtonForKey(leftButton, KeyCode.KEY_S);
        virtualKeyboard.registerButtonForKey(rightButton, KeyCode.KEY_F);
        virtualKeyboard.registerButtonForKey(jumpButton, KeyCode.KEY_SPACE);
        entityManager.addEntity(virtualKeyboard);

        inputManager.addKeyboard(virtualKeyboard);

        GameObject filledBoxSprite = new GameObject();
        filledBoxSprite.addComponent(new Position(200, 200));
        filledBoxSprite.addComponent(new Movement());
        filledBoxSprite.addComponent(new FilledBlockRenderer());
        filledBoxSprite.addComponent(new KeyboardMovementController(inputManager));
        entityManager.addEntity(filledBoxSprite);

        entityManager.addEntity(new Updateable() {
            @Override
            public void update(Graphics graphics) {
                boolean leftDown = inputManager.isKeyDown(KeyCode.KEY_S);
                boolean rightDown = inputManager.isKeyDown(KeyCode.KEY_F);
                boolean spaceDown = inputManager.isKeyDown(KeyCode.KEY_SPACE);

                if (leftDown) {
                    System.out.print("KEY LEFT");
                }

                if (rightDown) {
                    if (leftDown) {
                        System.out.print(", ");
                    }
                    System.out.print("KEY RIGHT");
                }

                if (spaceDown) {
                    if (leftDown || rightDown) {
                        System.out.print(", ");
                    }
                    System.out.print("KEY JUMP");
                }

                if (leftDown || rightDown || spaceDown) {
                    System.out.println();
                }
            }
        });
    }

    @Override
    public void shutdown() {
        entityManager.removeAll();
    }
}
