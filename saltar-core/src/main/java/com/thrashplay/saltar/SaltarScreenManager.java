package com.thrashplay.saltar;

import com.thrashplay.luna.api.engine.Screen;
import com.thrashplay.luna.api.engine.ScreenManager;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarScreenManager implements ScreenManager {
    private Screen currentScreen = new TestScreen();

    @Override
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    @Override
    public void update(Graphics graphics) {

    }
}
