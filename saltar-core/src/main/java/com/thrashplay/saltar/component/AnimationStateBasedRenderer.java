package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.Graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class AnimationStateBasedRenderer implements UpdateableComponent, RenderableComponent {
    private Map<Player.AnimationState, RenderableComponent> stateToRendererMap = new HashMap<>();
    private RenderableComponent currentRenderer = null;

    public void addRenderer(Player.AnimationState state, RenderableComponent component) {
        stateToRendererMap.put(state, component);
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        Player player = gameObject.getComponent(Player.class);
        currentRenderer = stateToRendererMap.get(player.getAnimationState());

        if (currentRenderer instanceof UpdateableComponent) {
            ((UpdateableComponent) currentRenderer).update(gameObject, delta);
        }
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        if (currentRenderer != null) {
            currentRenderer.render(graphics, gameObject);
        }
    }
}
