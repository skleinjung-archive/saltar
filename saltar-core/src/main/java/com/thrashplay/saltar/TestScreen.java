package com.thrashplay.saltar;

import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TestScreen extends EntityManagerScreen {
    public TestScreen() {
        entityManager.addEntity(new ClearScreen());
        entityManager.addEntity(new FpsDisplay());
    }
}
