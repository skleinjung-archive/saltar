package com.thrashplay.saltar.desktop;

import com.thrashplay.luna.api.engine.Luna;
import com.thrashplay.luna.desktop.LunaCanvas;
import com.thrashplay.luna.desktop.LunaWindow;
import com.thrashplay.luna.engine.LunaGameConfig;
import com.thrashplay.saltar.SaltarScreenManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Main {
    public static void main(String[] args) {
        new LunaWindow("Saltar", 960, 540) {
            @Override
            protected LunaGameConfig createGameConfig(LunaCanvas canvas, Luna luna) {
                LunaGameConfig gameConfig = new LunaGameConfig();
                gameConfig.setScreenManager(new SaltarScreenManager(luna.getTouchManager(), luna.getInputManager()));
                gameConfig.setDefaultScreen("test");
                return gameConfig;
            }
        };
    }
}

