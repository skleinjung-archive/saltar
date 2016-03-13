package com.thrashplay.saltar;

import com.thrashplay.luna.android.engine.LunaGame;
import com.thrashplay.luna.android.input.AndroidTouchManager;
import com.thrashplay.luna.engine.LunaGameConfig;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MainActivity extends LunaGame {

    @Override
    protected LunaGameConfig getGameConfig() {
        LunaGameConfig gameConfig = new LunaGameConfig();
        gameConfig.setScreenManager(new SaltarScreenManager(new AndroidTouchManager(getSurfaceView())));
        gameConfig.setDefaultScreen("test");
        return gameConfig;
    }
}
