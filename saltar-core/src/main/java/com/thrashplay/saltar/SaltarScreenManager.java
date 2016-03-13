package com.thrashplay.saltar;

import com.thrashplay.luna.api.input.TouchManager;
import com.thrashplay.luna.engine.DefaultScreenManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarScreenManager extends DefaultScreenManager {
    public SaltarScreenManager(TouchManager touchManager) {
        registerScreen("test", new TestScreen(touchManager));
    }
}
