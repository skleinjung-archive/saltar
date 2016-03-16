package com.thrashplay.saltar;

import com.thrashplay.luna.api.component.*;
import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.config.AnimationConfig;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.ui.Button;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;
import com.thrashplay.saltar.component.KeyboardMovementController;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TestScreen extends EntityManagerScreen {
    private ImageManager imageManager;
    private AnimationConfigManager animationConfigManager;
    private MultiTouchManager multiTouchManager;
    private InputManager inputManager;

    public TestScreen(final ImageManager imageManager, final AnimationConfigManager animationConfigManager, final MultiTouchManager multiTouchManager, final InputManager inputManager) {
        this.imageManager = imageManager;
        this.animationConfigManager = animationConfigManager;
        this.multiTouchManager = multiTouchManager;
        this.inputManager = inputManager;
    }

    @Override
    protected void doInitialize(Rectangle screenBounds) {
        entityManager.addEntity(new ClearScreen(0x7EC0EE));
        entityManager.addEntity(new FpsDisplay());

        Button leftButton = new TextButton(multiTouchManager, "<", 60, screenBounds.getBottom() - 160, 100, 100);
        Button rightButton = new TextButton(multiTouchManager, ">", 195, screenBounds.getBottom() - 160, 100, 100);
        Button jumpButton = new TextButton(multiTouchManager, "^", screenBounds.getRight() - 160, screenBounds.getBottom() - 160, 100, 100);
        VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
        virtualKeyboard.registerButtonForKey(leftButton, KeyCode.KEY_LEFT_ARROW);
        virtualKeyboard.registerButtonForKey(rightButton, KeyCode.KEY_RIGHT_ARROW);
        virtualKeyboard.registerButtonForKey(jumpButton, KeyCode.KEY_SPACE);
        entityManager.addEntity(virtualKeyboard);

        inputManager.addKeyboard(virtualKeyboard);

        AnimationConfig animationConfig = animationConfigManager.getAnimationConfig("animations/player_animation.json");
        AnimationRenderer animation = new AnimationRenderer(animationConfig, imageManager.createSpriteSheet(animationConfig.getSpriteSheet()));

        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");
        GameObject sprite = new GameObject("circle");
        sprite.addComponent(new Position(200, 200));
        sprite.addComponent(new Movement());
//        sprite.addComponent(new ImageRenderer(image, true));
        sprite.addComponent(animation);
        sprite.addComponent(new KeyboardMovementController(inputManager));
        entityManager.addEntity(sprite);

        /*
        entityManager.addEntity(new Renderable() {
            @Override
            public void render(Graphics graphics) {
//                graphics.drawImage(image, new Rectangle(15, 5, 44 - 15, 60 - 5), 50, 50);

                // entire walking frame
                graphics.drawImage(image, 50, 50);
                // just the head from the walking frame
                graphics.drawImage(image, new Rectangle(8, 0, 26, 26), 200, 50);

                // source bounds bigger than image
                graphics.drawImage(image, new Rectangle(0, 0, 200, 200), 400, 50);

                // source bounds extends below zero
                graphics.drawImage(image, new Rectangle(-100, -100, 125, 125), 600, 50);
            }
        });
        */
    }

    @Override
    public void shutdown() {
        entityManager.removeAll();
    }
}
