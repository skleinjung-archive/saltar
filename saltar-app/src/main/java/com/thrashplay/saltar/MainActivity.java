package com.thrashplay.saltar;

import com.thrashplay.luna.android.engine.LunaGame;
import com.thrashplay.luna.api.engine.Luna;
import com.thrashplay.luna.engine.LunaGameConfig;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MainActivity extends LunaGame {

    @Override
    protected LunaGameConfig getGameConfig(Luna luna) {
        LunaGameConfig gameConfig = new LunaGameConfig();
        gameConfig.setScreenManager(new SaltarScreenManager(luna.getTouchManager(), luna.getInputManager()));
        gameConfig.setDefaultScreen("test");
        return gameConfig;
    }
}
