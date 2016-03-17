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

    public MainActivity() {
//        Config.renderImageBoundaries = true;
    }

    @Override
    protected LunaGameConfig getGameConfig(Luna luna) {
        LunaGameConfig gameConfig = new LunaGameConfig();
        gameConfig.setSceneDimensions(480, 320);
        gameConfig.setScreenManager(new SaltarScreenManager(luna.getImageManager(), luna.getAnimationConfigManager(), luna.getMultiTouchManager(), luna.getInputManager()));
        gameConfig.setDefaultScreen("test");
        return gameConfig;
    }

    @Override
    protected int getSceneWidth() {
        return 480;
    }

    @Override
    protected int getSceneHeight() {
        return 320;
    }
}
