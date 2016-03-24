package com.thrashplay.saltar;

import com.thrashplay.luna.api.animation.AnimationRenderer;
import com.thrashplay.luna.api.collision.CollisionHandler;
import com.thrashplay.luna.api.component.*;
import com.thrashplay.luna.api.engine.DefaultScreen;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.animation.AnimationConfig;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.math.Random;
import com.thrashplay.luna.api.collision.CrossCollisionDetector;
import com.thrashplay.luna.api.ui.Button;
import com.thrashplay.luna.engine.LegacyGameObjectAdapter;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.animation.AnimationStateBasedRenderer;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;
import com.thrashplay.saltar.component.*;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TestScreen extends DefaultScreen {
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
        gameObjectManager.register(new LegacyGameObjectAdapter("clear screen", new ClearScreen(0x7EC0EE)));

        AnimationConfig walkAnimation = animationConfigManager.getAnimationConfig("animations/player/walk.json");
        AnimationConfig jumpAnimationConfig = animationConfigManager.getAnimationConfig("animations/player/jump.json");
        SpriteSheet playerAnimationSpriteSheet = imageManager.createSpriteSheet(walkAnimation.getSpriteSheet());

        ImageRenderer idleLeftImage = new ImageRenderer(playerAnimationSpriteSheet.getImage(1), true);
        idleLeftImage.setFlipHorizontally(true);
        ImageRenderer idleRightImage = new ImageRenderer(playerAnimationSpriteSheet.getImage(1), true);
        AnimationRenderer walkingLeftAnimation = new AnimationRenderer(walkAnimation, playerAnimationSpriteSheet);
        walkingLeftAnimation.setFlipHorizontally(true);
        AnimationRenderer walkingRightAnimation = new AnimationRenderer(walkAnimation, playerAnimationSpriteSheet);
        AnimationRenderer jumpingAnimation = new AnimationRenderer(jumpAnimationConfig, playerAnimationSpriteSheet);

        AnimationStateBasedRenderer playerRenderer = new AnimationStateBasedRenderer();
        playerRenderer.addRenderer("IdleFacingLeft", idleLeftImage);
        playerRenderer.addRenderer("IdleFacingRight", idleRightImage);
        playerRenderer.addRenderer("WalkingLeft", walkingLeftAnimation);
        playerRenderer.addRenderer("WalkingRight", walkingRightAnimation);
        playerRenderer.addRenderer("JumpingRight", jumpingAnimation);
        playerRenderer.setCurrentState("IdleFacingRight");

//        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");

        int maxPlayerVelocity = 8;
        GameObject player = new GameObject("player");
        player.addComponent(new Position(200, 50));
        player.addComponent(new Movement());
        player.addComponent(new Gravity(3));
        player.addComponent(new Player());
        player.addComponent(new Collider(1, true, new Rectangle(maxPlayerVelocity + 1, 0, 30 - ((maxPlayerVelocity + 1) * 2), 55),  new Rectangle(0, maxPlayerVelocity + 1, 30, 55 - ((maxPlayerVelocity + 1) * 2))));
        player.addComponent(CollisionHandler.class, new PlayerCollisionHandler());
        player.addComponent(new KeyboardMovementController(inputManager));
        player.addComponent(playerRenderer);
        gameObjectManager.register(player);

        SpriteSheet spriteSheet = imageManager.createSpriteSheet("spritesheets/level1_spritesheet.json");
        final LunaImage image1 = spriteSheet.getImage(1);
        final LunaImage image2 = spriteSheet.getImage(2);
        final LunaImage image3 = spriteSheet.getImage(3);
        int blockCount = 0;
        //for (int z = 0; z < 100; z++) {
        Rectangle screenBounds = new Rectangle(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT);
            for (int y = screenBounds.getBottom() - 64; y <= screenBounds.getBottom() - 32; y += 32) {
//        for (int y = 0; y < screenBounds.getBottom() - 32; y += 32) {
                for (int x = 0; x < screenBounds.getRight() * 3; x += 32) {
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
                    gameObjectManager.register(block);
                }
            }

        for (int tileIndex = 4; tileIndex <= 13; tileIndex++) {
            int y = screenBounds.getBottom() - 64;
            int tileRow = (tileIndex - 4) / 2;
            int tileColumn = tileIndex % 2;
            y -= (5 - tileRow) * 32;

            GameObject block = new GameObject("fancyblock-" + (tileIndex - 3));
            block.addComponent(new Position(32 * tileColumn, y));
            block.addComponent(new ImageRenderer(spriteSheet.getImage(tileIndex), true));

            if (tileColumn == 1) {
                block.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
            }

            gameObjectManager.register(block);
        }

        GameObject platform = new GameObject("ground-platform");
        platform.addComponent(new Position(1300, screenBounds.getBottom() - 64 - 32));
        platform.addComponent(new Collider(2, false, new Rectangle(0, 0, 48, 32)));
        platform.addComponent(new ImageRenderer(spriteSheet.getImage(14), true));
        gameObjectManager.register(platform);

        GameObject platformLeft = new GameObject("air-platform-left");
        platformLeft.addComponent(new Position(400, screenBounds.getBottom() - 64 - 128));
        platformLeft.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
        platformLeft.addComponent(new ImageRenderer(image1, true));
        gameObjectManager.register(platformLeft);
        GameObject platformCenter = new GameObject("air-platform-center");
        platformCenter.addComponent(new Position(432, screenBounds.getBottom() - 64 - 128));
        platformCenter.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
        platformCenter.addComponent(new ImageRenderer(image1, true));
        gameObjectManager.register(platformCenter);
        GameObject platformRight = new GameObject("air-platform-right");
        platformRight.addComponent(new Position(464, screenBounds.getBottom() - 64 - 128));
        platformRight.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
        platformRight.addComponent(new ImageRenderer(image1, true));
        gameObjectManager.register(platformRight);

        GameObject platformLeft2 = new GameObject("air-platform-left2");
        platformLeft2.addComponent(new Position(600, screenBounds.getBottom() - 64 - 96));
        platformLeft2.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
        platformLeft2.addComponent(new ImageRenderer(image1, true));
        gameObjectManager.register(platformLeft2);
        GameObject platformCenter2 = new GameObject("air-platform-center2");
        platformCenter2.addComponent(new Position(632, screenBounds.getBottom() - 64 - 96));
        platformCenter2.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
        platformCenter2.addComponent(new ImageRenderer(image1, true));
        gameObjectManager.register(platformCenter2);
        GameObject platformRight2 = new GameObject("air-platform-right2");
        platformRight2.addComponent(new Position(664, screenBounds.getBottom() - 64 - 96));
        platformRight2.addComponent(new Collider(2, false, new Rectangle(0, 0, 32, 32)));
        platformRight2.addComponent(new ImageRenderer(image1, true));
        gameObjectManager.register(platformRight2);


        Button leftButton = new TextButton(multiTouchManager, "<", 16, screenBounds.getBottom() - 56, 48, 48);
        Button rightButton = new TextButton(multiTouchManager, ">", 80, screenBounds.getBottom() - 56, 48, 48);
        Button jumpButton = new TextButton(multiTouchManager, "^", screenBounds.getRight() - 64, screenBounds.getBottom() - 56, 48, 48);
        VirtualKeyboard virtualKeyboard = new VirtualKeyboard();
        virtualKeyboard.registerButtonForKey(leftButton, KeyCode.KEY_LEFT_ARROW);
        virtualKeyboard.registerButtonForKey(rightButton, KeyCode.KEY_RIGHT_ARROW);
        virtualKeyboard.registerButtonForKey(jumpButton, KeyCode.KEY_SPACE);
        LegacyGameObjectAdapter virtualKeyboardGameObject = new LegacyGameObjectAdapter("virtual keyboard", virtualKeyboard);
        virtualKeyboardGameObject.setRenderLayer(GameObject.RenderLayer.Overlay);
        gameObjectManager.register(virtualKeyboardGameObject);

        inputManager.addKeyboard(virtualKeyboard);

        // create the viewport
        GameObject viewport = new GameObject(GameObjectIds.ID_VIEWPORT);
        viewport.addComponent(new Position(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT));
        viewport.addComponent(new ViewportScrollController(player));
        gameObjectManager.register(viewport);

        LegacyGameObjectAdapter fpsDisplay = new LegacyGameObjectAdapter("fps display", new FpsDisplay(18));
        fpsDisplay.setRenderLayer(GameObject.RenderLayer.Overlay);
        gameObjectManager.register(fpsDisplay);
        gameObjectManager.register(new LegacyGameObjectAdapter("collision detector", new CrossCollisionDetector(gameObjectManager)));

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
        gameObjectManager.unregisterAll();
    }
}

