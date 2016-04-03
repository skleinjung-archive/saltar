package com.thrashplay.saltar.debug;

import com.thrashplay.luna.api.collision.GridPartitioningBroadPhaseCollisionDetector;
import com.thrashplay.luna.api.collision.NarrowPhaseCollisionDetector;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.saltar.component.DebugStringRenderer;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class CollisionStatsDebugStringProvider implements DebugStringRenderer.DebugStringProvider {
    @Override
    public String[] getDebugStrings(GameObjectManager gameObjectManager) {
        String[] result = null;

        GameObject collisionDetection = gameObjectManager.getGameObject("collision detection");
        if (collisionDetection != null) {
            // get broad phase collision detection
            GridPartitioningBroadPhaseCollisionDetector broadPhase = collisionDetection.getComponent(GridPartitioningBroadPhaseCollisionDetector.class);
            if (broadPhase != null) {
                NarrowPhaseCollisionDetector narrowPhase = broadPhase.getNarrowPhaseCollisionDetector();
                int collisionChecks = narrowPhase.getAndResetCollisionChecks();
                result = new String[] { "Collision checks: " + collisionChecks };
            }
        }

        return result;
    }
}
