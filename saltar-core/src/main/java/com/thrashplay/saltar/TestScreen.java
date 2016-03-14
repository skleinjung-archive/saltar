package com.thrashplay.saltar;

import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.api.engine.Updateable;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.TouchManager;
import com.thrashplay.luna.api.ui.Button;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TestScreen extends EntityManagerScreen {
    public TestScreen(final TouchManager touchManager, final InputManager inputManager) {
        entityManager.addEntity(new ClearScreen());
        entityManager.addEntity(new FpsDisplay());

        VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
//        Button leftButton = new TextButton(touchManager, "<", 25, 25, 50, 50);
//        Button rightButton = new TextButton(touchManager, ">", 80, 25, 50, 50);

        Button leftButton = new TextButton(touchManager, "<", 25, 25, 75, 75);
        Button rightButton = new TextButton(touchManager, ">", 115, 25, 75, 75);
        virtualKeyboard.registerButtonForKey(leftButton, KeyCode.KEY_S);
        virtualKeyboard.registerButtonForKey(rightButton, KeyCode.KEY_F);
        entityManager.addEntity(virtualKeyboard);

        inputManager.addKeyboard(virtualKeyboard);

        entityManager.addEntity(new Updateable() {
            @Override
            public void update(Graphics graphics) {
                boolean leftDown = inputManager.isKeyDown(KeyCode.KEY_S);
                boolean rightDown = inputManager.isKeyDown(KeyCode.KEY_F);

                if (leftDown) {
                    System.out.print("KEY LEFT");
                }

                if (rightDown) {
                    if (leftDown) {
                        System.out.print(", ");
                    }
                    System.out.print("KEY RIGHT");
                }

                if (leftDown || rightDown) {
                    System.out.println();
                }
            }
        });
    }
}
