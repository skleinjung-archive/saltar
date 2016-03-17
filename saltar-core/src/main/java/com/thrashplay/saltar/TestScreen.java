package com.thrashplay.saltar;

import com.thrashplay.luna.api.component.*;
import com.thrashplay.luna.api.engine.EntityManagerScreen;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.graphics.config.AnimationConfig;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.math.Random;
import com.thrashplay.luna.api.physics.CollisionDetector;
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
    protected void doInitialize() {
        entityManager.addEntity(new ClearScreen(0x7EC0EE));

        AnimationConfig animationConfig = animationConfigManager.getAnimationConfig("animations/player_animation.json");
        AnimationRenderer animation = new AnimationRenderer(animationConfig, imageManager.createSpriteSheet(animationConfig.getSpriteSheet()));

//        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");
        GameObject player = new GameObject("player");
        player.addComponent(new Position(200, 50));
        player.addComponent(new Movement());
        player.addComponent(new Gravity(3));
        player.addComponent(animation);
        player.addComponent(new Collider(1, true, new Rectangle(0, 0, 30, 55)));
        player.addComponent(CollisionHandler.class, new CollisionHandler() {
            @Override
            public void handleCollision(GameObject ourObject, GameObject otherObject, Rectangle ourBoundingBox, Rectangle otherBoundingBox) {
                Position position = ourObject.getComponent(Position.class);
                position.setY(otherBoundingBox.getY() - ourBoundingBox.getHeight());

                Movement movement = ourObject.getComponent(Movement.class);
                movement.setVelocityY(0);
            }
        });
        player.addComponent(new KeyboardMovementController(inputManager));
        entityManager.addEntity(player);

        SpriteSheet spriteSheet = imageManager.createSpriteSheet("spritesheets/terrain_spritesheet.json");
        final LunaImage image1 = spriteSheet.getImage(1);
        final LunaImage image2 = spriteSheet.getImage(2);
        final LunaImage image3 = spriteSheet.getImage(3);
        int blockCount = 0;
        //for (int z = 0; z < 100; z++) {
        Rectangle screenBounds = new Rectangle(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT);
            for (int y = screenBounds.getBottom() - 64; y <= screenBounds.getBottom() - 32; y += 32) {
//        for (int y = 0; y < screenBounds.getBottom() - 32; y += 32) {
                for (int x = 0; x < screenBounds.getRight(); x += 32) {
                    GameObject block = new GameObject("block-" + ++blockCount);
                    block.addComponent(new Position(x, y));

                    LunaImage image;
                    if (y == screenBounds.getBottom() - 64) {
                        image = image1;
                    } else if (Random.getInteger(2) == 0) {
                        image = image2;
                    } else {
                        image = image3;
                    }

                    block.addComponent(new ImageRenderer(image, true));

                    if (y == screenBounds.getBottom() - 64) {
                        block.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
                    }

                    System.out.println("Created " + blockCount + " blocks");
                    entityManager.addEntity(block);
                }
            }

        //}

        Button leftButton = new TextButton(multiTouchManager, "<", 32, screenBounds.getBottom() - 48, 32, 32);
        Button rightButton = new TextButton(multiTouchManager, ">", 96, screenBounds.getBottom() - 48, 32, 32);
        Button jumpButton = new TextButton(multiTouchManager, "^", screenBounds.getRight() - 64, screenBounds.getBottom() - 48, 32, 32);
        VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
        virtualKeyboard.registerButtonForKey(leftButton, KeyCode.KEY_LEFT_ARROW);
        virtualKeyboard.registerButtonForKey(rightButton, KeyCode.KEY_RIGHT_ARROW);
        virtualKeyboard.registerButtonForKey(jumpButton, KeyCode.KEY_SPACE);
        entityManager.addEntity(virtualKeyboard);

        inputManager.addKeyboard(virtualKeyboard);

        entityManager.addEntity(new FpsDisplay(18));

        entityManager.addEntity(new CollisionDetector(entityManager));

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

