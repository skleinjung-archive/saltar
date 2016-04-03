package com.thrashplay.saltar.debug;

import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.saltar.component.DebugStringRenderer;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MultiTouchManagerDebugStringProvider implements DebugStringRenderer.DebugStringProvider {
    private MultiTouchManager multiTouchManager;

    public MultiTouchManagerDebugStringProvider(MultiTouchManager multiTouchManager) {
        this.multiTouchManager = multiTouchManager;
    }

    @Override
    public String[] getDebugStrings(GameObjectManager gameObjectManager) {
        String string = multiTouchManager.getNumberOfActivePointers() + " pointers. IDs: ";
        for (int i = 0; i < multiTouchManager.getNumberOfActivePointers(); i++) {
            if (i != 0) {
                string += ", ";
            }
            string += multiTouchManager.getId(i);
        }
        return new String[] { string };
    }
}
