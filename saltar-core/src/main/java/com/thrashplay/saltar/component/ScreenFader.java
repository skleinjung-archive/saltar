package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.Graphics;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class ScreenFader implements RenderableComponent, UpdateableComponent {

    private long delayTime = 0;
    private long delayStartTime = 0;
    private long fadeStartTime = 0;
    private float msPerAlphaDelta;

    private int alpha;

    public ScreenFader(int delayTime, int fadeTime) {
        this.delayTime = delayTime;
        msPerAlphaDelta = fadeTime / 256f;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (delayStartTime == 0) {
            delayStartTime = System.currentTimeMillis();
        }

        if (doneDelaying()) {
            if (fadeStartTime == 0) {
                fadeStartTime = System.currentTimeMillis();
            }

            long elapsed = System.currentTimeMillis() - fadeStartTime;
            alpha = Math.min(255, (int) (elapsed / msPerAlphaDelta));
        }
    }

    private boolean doneDelaying() {
        return (System.currentTimeMillis() - delayStartTime) >= delayTime;
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        int color = alpha << 24; // | 0 | 0 | 0
        graphics.fillRect(0, 0, graphics.getWidth(), graphics.getHeight(), color);
    }
}
