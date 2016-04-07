package com.thrashplay.saltar.debug;

import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.saltar.component.DebugStringRenderer;
import com.thrashplay.saltar.component.Player;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PlayerAnimationStateDebugStringProvider implements DebugStringRenderer.DebugStringProvider {
    @Override
    public String[] getDebugStrings(GameObjectManager gameObjectManager) {
        GameObject playerObject = gameObjectManager.getGameObject("player");
        Player player = playerObject.getComponent(Player.class);

        return new String[] { "Player animation: " + player.getAnimationState().name() };
    }
}
