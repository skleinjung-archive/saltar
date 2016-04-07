package com.thrashplay.saltar.screen;

import com.thrashplay.luna.api.engine.DefaultScreen;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.engine.LegacyGameObjectAdapter;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.saltar.component.IntroScreenMessage;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class LevelIntroScreen extends DefaultScreen {

    private String message;
    private int timeToDisplay;
    private String nextScreen;

    private long startTime = 0;

    public LevelIntroScreen(String message, int timeToDisplay, String nextScreen) {
        this.message = message;
        this.timeToDisplay = timeToDisplay;
        this.nextScreen = nextScreen;
    }

    @Override
    protected void doInitialize() {
        super.doInitialize();

        gameObjectManager.register(new LegacyGameObjectAdapter("clear screen", new ClearScreen(0x000000)));

        GameObject messageObject = new GameObject("message");
        messageObject.setRenderLayer(GameObject.RenderLayer.Overlay);
        messageObject.addComponent(new IntroScreenMessage(message));
        gameObjectManager.register(messageObject);
    }

    @Override
    public String getNextScreen() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }

        if ((System.currentTimeMillis() - startTime) >= timeToDisplay) {
            return nextScreen;
        } else {
            return super.getNextScreen();
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        gameObjectManager.unregisterAll();
        startTime = 0;
    }
}
