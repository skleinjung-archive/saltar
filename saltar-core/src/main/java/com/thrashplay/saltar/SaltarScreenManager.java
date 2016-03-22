package com.thrashplay.saltar;

import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.level.LevelManager;
import com.thrashplay.luna.engine.DefaultScreenManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarScreenManager extends DefaultScreenManager {
    public SaltarScreenManager(LevelManager levelManager, ImageManager imageManager, AnimationConfigManager animationConfigManager, MultiTouchManager multiTouchManager, InputManager inputManager) {
        registerScreen("test", new SaltarLevelScreen(levelManager, imageManager, animationConfigManager, multiTouchManager, inputManager));
    }
}
