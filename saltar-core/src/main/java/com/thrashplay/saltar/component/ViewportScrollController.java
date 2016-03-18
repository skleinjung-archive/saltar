package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.saltar.Saltar;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class ViewportScrollController implements UpdateableComponent {
    private GameObject player;

    public ViewportScrollController(GameObject player) {
        this.player = player;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        Position playerPosition = player.getComponent(Position.class);
        Position viewportPosition = gameObject.getComponent(Position.class);

        int leftScrollBoundary = (int) (Saltar.SCENE_WIDTH / 3f);
        if (playerPosition.getX() - viewportPosition.getX() < leftScrollBoundary) {
            viewportPosition.setX(playerPosition.getX() - leftScrollBoundary);
        }

        int rightScrollBoundary = (int) ((Saltar.SCENE_WIDTH / 3f) * 2);
        if (playerPosition.getX() - viewportPosition.getX() > rightScrollBoundary) {
            viewportPosition.setX(playerPosition.getX() - rightScrollBoundary);
        }

        // clamp to edge of game world
        if (viewportPosition.getX() < 0) {
            viewportPosition.setX(0);
        }
    }
}
