package com.thrashplay.saltar;

import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.TouchManager;
import com.thrashplay.luna.input.TextVirtualKey;
import com.thrashplay.luna.input.VirtualKey;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TestScreen extends EntityManagerScreen {
    public TestScreen(TouchManager touchManager) {
        entityManager.addEntity(new ClearScreen());
        entityManager.addEntity(new FpsDisplay());

        VirtualKeyboard virtualKeyboard = new VirtualKeyboard(touchManager);

        VirtualKey leftKey = new TextVirtualKey(touchManager, KeyCode.KEY_S, "B8C jy", 50, 50, 25);
        VirtualKey rightKey = new TextVirtualKey(touchManager, KeyCode.KEY_F, ">", 150, 50, 25);

        virtualKeyboard.addKey(leftKey);
        virtualKeyboard.addKey(rightKey);

        entityManager.addEntity(virtualKeyboard);
    }
}
