package com.thrashplay.saltar;

import com.thrashplay.luna.api.component.*;
import com.thrashplay.luna.api.engine.DefaultScreen;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
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
import com.thrashplay.luna.engine.LegacyGameObjectAdapter;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;
import com.thrashplay.saltar.component.AnimationStateBasedRenderer;
import com.thrashplay.saltar.component.KeyboardMovementController;
import com.thrashplay.saltar.component.Player;
import com.thrashplay.saltar.component.ViewportScrollController;

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

        AnimationConfig animationConfig = animationConfigManager.getAnimationConfig("animations/player_animation.json");
        SpriteSheet playerAnimationSpriteSheet = imageManager.createSpriteSheet(animationConfig.getSpriteSheet());

        ImageRenderer idleLeftImage = new ImageRenderer(playerAnimationSpriteSheet.getImage(1), true);
        idleLeftImage.setFlipHorizontally(true);
        ImageRenderer idleRightImage = new ImageRenderer(playerAnimationSpriteSheet.getImage(1), true);
        AnimationRenderer walkingLeftAnimation = new AnimationRenderer(animationConfig, playerAnimationSpriteSheet);
        walkingLeftAnimation.setFlipHorizontally(true);
        AnimationRenderer walkingRightAnimation = new AnimationRenderer(animationConfig, playerAnimationSpriteSheet);

        AnimationStateBasedRenderer playerRenderer = new AnimationStateBasedRenderer();
        playerRenderer.addRenderer(Player.AnimationState.Jumping, idleLeftImage);
        playerRenderer.addRenderer(Player.AnimationState.IdleFacingLeft, idleLeftImage);
        playerRenderer.addRenderer(Player.AnimationState.IdleFacingRight, idleRightImage);
        playerRenderer.addRenderer(Player.AnimationState.WalkingLeft, walkingLeftAnimation);
        playerRenderer.addRenderer(Player.AnimationState.WalkingRight, walkingRightAnimation);

//        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");
        GameObject player = new GameObject("player");
        player.addComponent(new Position(200, 50));
        player.addComponent(new Movement());
        player.addComponent(new Gravity(3));
        player.addComponent(new Player());
        player.addComponent(playerRenderer);
        player.addComponent(new Collider(1, true, new Rectangle(0, 0, 30, 55)));
        player.addComponent(CollisionHandler.class, new CollisionHandler() {
            @Override
            public void handleCollision(GameObject ourObject, GameObject otherObject, Rectangle ourBoundingBox, Rectangle otherBoundingBox) {
                Position position = ourObject.getComponent(Position.class);
                position.setY(otherBoundingBox.getY() - ourBoundingBox.getHeight());

                Movement movement = ourObject.getComponent(Movement.class);
                if (movement.getVelocityY() > 0) {
                    movement.setVelocityY(0);
                }
            }
        });
        player.addComponent(new KeyboardMovementController(inputManager));
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

        GameObject platform = new GameObject("platform");
        platform.addComponent(new Position(500, screenBounds.getBottom() - 64 - 32));
        platform.addComponent(new Collider(2, false, new Rectangle(0, 0, 48, 32)));
        platform.addComponent(new ImageRenderer(spriteSheet.getImage(14), true));
        gameObjectManager.register(platform);

        Button leftButton = new TextButton(multiTouchManager, "<", 32, screenBounds.getBottom() - 48, 32, 32);
        Button rightButton = new TextButton(multiTouchManager, ">", 96, screenBounds.getBottom() - 48, 32, 32);
        Button jumpButton = new TextButton(multiTouchManager, "^", screenBounds.getRight() - 64, screenBounds.getBottom() - 48, 32, 32);
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
        viewport.addComponent(new Position(48, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT));
        viewport.addComponent(new ViewportScrollController(player));
        gameObjectManager.register(viewport);

        LegacyGameObjectAdapter fpsDisplay = new LegacyGameObjectAdapter("fps display", new FpsDisplay(18));
        fpsDisplay.setRenderLayer(GameObject.RenderLayer.Overlay);
        gameObjectManager.register(fpsDisplay);
        gameObjectManager.register(new LegacyGameObjectAdapter("collision detector", new CollisionDetector(gameObjectManager)));

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

