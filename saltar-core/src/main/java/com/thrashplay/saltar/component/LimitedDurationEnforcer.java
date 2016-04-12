package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class LimitedDurationEnforcer implements UpdateableComponent {
    private long timeToLive;
    private long expirationTime;

    public LimitedDurationEnforcer(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (expirationTime == 0) {
            expirationTime = System.currentTimeMillis() + timeToLive;
        }

        if (System.currentTimeMillis() >= expirationTime) {
            gameObject.setDead(true);
        }
    }
}
