package com.thrashplay.saltar;

import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.engine.DefaultScreenManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarScreenManager extends DefaultScreenManager {
    public SaltarScreenManager(MultiTouchManager multiTouchManager, InputManager inputManager) {
        registerScreen("test", new TestScreen(multiTouchManager, inputManager));
    }
}
