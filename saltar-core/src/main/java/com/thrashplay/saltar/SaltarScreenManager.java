package com.thrashplay.saltar;

import com.thrashplay.luna.api.actor.ActorManager;
import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.input.TouchManager;
import com.thrashplay.luna.api.level.LevelManager;
import com.thrashplay.luna.engine.DefaultScreenManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarScreenManager extends DefaultScreenManager {
    public SaltarScreenManager(LevelManager levelManager, ActorManager actorManager, ImageManager imageManager, AnimationConfigManager animationConfigManager, MultiTouchManager multiTouchManager, TouchManager touchManager, InputManager inputManager) {
        registerScreen("test", new SaltarLevelScreen(levelManager, actorManager, imageManager, animationConfigManager, multiTouchManager, touchManager, inputManager));
    }
}
