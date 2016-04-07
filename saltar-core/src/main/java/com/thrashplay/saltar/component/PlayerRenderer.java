package com.thrashplay.saltar.component;

import com.thrashplay.luna.animation.AnimationStateBasedRenderer;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * Subclass of animation state based renderer that has logic for flashing while the player is invincible.
 *
 * @author Sean Kleinjung
 */
public class PlayerRenderer extends AnimationStateBasedRenderer {

    private long flashVisibleDuration;
    private long flashInvisibleDuration;
    private boolean invisible = false;
    private long lastTransitionTime;

    public PlayerRenderer(long flashVisibleDuration, long flashInvisibleDuration) {
        this.flashVisibleDuration = flashVisibleDuration;
        this.flashInvisibleDuration = flashInvisibleDuration;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        super.update(gameObject, delta);

        Player player = gameObject.getComponent(Player.class);
        if (player.isInvincible()) {
            if (invisible) {
                if (System.currentTimeMillis() - lastTransitionTime >= flashInvisibleDuration) {
                    lastTransitionTime = System.currentTimeMillis();
                    invisible = false;
                }
            } else {
                if (System.currentTimeMillis() - lastTransitionTime >= flashVisibleDuration) {
                    lastTransitionTime = System.currentTimeMillis();
                    invisible = true;
                }
            }
        } else {
            lastTransitionTime = 0;
            invisible = false;
        }
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        if (!invisible) {
            super.render(graphics, gameObject);
        }
        // do nothing if invisible
    }
}
