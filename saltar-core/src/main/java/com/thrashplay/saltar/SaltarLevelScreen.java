package com.thrashplay.saltar;

import com.thrashplay.luna.animation.AnimationStateBasedRenderer;
import com.thrashplay.luna.api.actor.ActorManager;
import com.thrashplay.luna.api.actor.config.ActorConfig;
import com.thrashplay.luna.api.animation.AnimationConfig;
import com.thrashplay.luna.api.animation.AnimationRenderer;
import com.thrashplay.luna.api.collision.CrossCollisionDetector;
import com.thrashplay.luna.api.component.*;
import com.thrashplay.luna.api.engine.DefaultScreen;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.level.LevelManager;
import com.thrashplay.luna.api.level.config.LevelConfig;
import com.thrashplay.luna.api.ui.Button;
import com.thrashplay.luna.collision.BoundingBoxesDebugRenderer;
import com.thrashplay.luna.collision.CrossBoundingBoxes;
import com.thrashplay.luna.collision.RendererBasedBoundingBoxes;
import com.thrashplay.luna.engine.LegacyGameObjectAdapter;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;
import com.thrashplay.saltar.component.KeyboardMovementController;
import com.thrashplay.saltar.component.Player;
import com.thrashplay.saltar.component.PlayerCollisionHandler;
import com.thrashplay.saltar.component.ViewportScrollController;

import java.util.List;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarLevelScreen extends DefaultScreen {
    private LevelManager levelManager;
    private ActorManager actorManager;
    private ImageManager imageManager;
    private AnimationConfigManager animationConfigManager;
    private MultiTouchManager multiTouchManager;
    private InputManager inputManager;

    public SaltarLevelScreen(LevelManager levelManager, ActorManager actorManager, ImageManager imageManager, AnimationConfigManager animationConfigManager, MultiTouchManager multiTouchManager, InputManager inputManager) {
        this.levelManager = levelManager;
        this.actorManager = actorManager;
        this.imageManager = imageManager;
        this.animationConfigManager = animationConfigManager;
        this.multiTouchManager = multiTouchManager;
        this.inputManager = inputManager;
    }

    @Override
    protected void doInitialize() {
        super.doInitialize();

        gameObjectManager.register(new LegacyGameObjectAdapter("clear screen", new ClearScreen(0x7EC0EE)));

        LevelConfig levelConfig = levelManager.createLevelConfig("levels/level01.json");
        List<GameObject> levelObjects = levelManager.createLevelObjects(levelConfig);
        for (GameObject object : levelObjects) {
            gameObjectManager.register(object);
        }

        GameObject player = createPlayer(levelConfig.getStartX(), levelConfig.getStartY());
        gameObjectManager.register(player);

        Position playerPosition = player.getComponent(Position.class);
        AnimationConfig walkAnimation = animationConfigManager.getAnimationConfig("animations/enemies/blob_walk.json");
        SpriteSheet blobAnimationSpriteSheet = imageManager.createSpriteSheet(walkAnimation.getSpriteSheet());
        AnimationRenderer walkingLeftAnimation = new AnimationRenderer(walkAnimation, blobAnimationSpriteSheet);

        ActorConfig blobConfig = actorManager.createActorConfig("enemies/blob.json");

        GameObject blob1 = actorManager.createActorObject(blobConfig);
        blob1.setId("blob");
        blob1.getComponent(Position.class).setRect(playerPosition.getX() + 600, playerPosition.getY(), 0, 0);
        gameObjectManager.register(blob1);

//        GameObject blob2 = actorManager.createActorObject(blobConfig);
//        blob2.getComponent(Position.class).setRect(playerPosition.getX() + 250, playerPosition.getY(), 0, 0);
//        gameObjectManager.register(blob2);
//
//        GameObject blob3 = actorManager.createActorObject(blobConfig);
//        blob3.getComponent(Position.class).setRect(playerPosition.getX() + 350, playerPosition.getY(), 0, 0);
//        gameObjectManager.register(blob3);

//        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");

        Rectangle screenBounds = new Rectangle(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT);
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

        GameObject debug = new GameObject("debug");
        debug.setRenderLayer(GameObject.RenderLayer.Foreground);
        debug.addComponent(new BoundingBoxesDebugRenderer(gameObjectManager));
        gameObjectManager.register(debug);
    }

    private GameObject createPlayer(int startX, int startY) {
        AnimationConfig walkAnimation = animationConfigManager.getAnimationConfig("animations/player/sara_walk.json");
        AnimationConfig jumpAnimationConfig = animationConfigManager.getAnimationConfig("animations/player/jump.json");
        AnimationConfig deathAnimation = animationConfigManager.getAnimationConfig("animations/player/sara_death.json");
        SpriteSheet playerAnimationSpriteSheet = imageManager.createSpriteSheet(walkAnimation.getSpriteSheet());

        ImageRenderer idleLeftImage = new SpriteSheetImageRenderer(playerAnimationSpriteSheet, 1, true);
        idleLeftImage.setFlipHorizontally(true);
        ImageRenderer idleRightImage = new SpriteSheetImageRenderer(playerAnimationSpriteSheet, 1, true);
        AnimationRenderer walkingLeftAnimation = new AnimationRenderer(walkAnimation, playerAnimationSpriteSheet);
        walkingLeftAnimation.setFlipHorizontally(true);
        AnimationRenderer walkingRightAnimation = new AnimationRenderer(walkAnimation, playerAnimationSpriteSheet);
        AnimationRenderer jumpingAnimation = new AnimationRenderer(jumpAnimationConfig, playerAnimationSpriteSheet);
        AnimationRenderer dyingRightAnimatino = new AnimationRenderer(deathAnimation, playerAnimationSpriteSheet);
        AnimationRenderer dyingLeftAnimation = new AnimationRenderer(deathAnimation, playerAnimationSpriteSheet);
        dyingLeftAnimation.setFlipHorizontally(true);

        AnimationStateBasedRenderer playerRenderer = new AnimationStateBasedRenderer();
        playerRenderer.addRenderer("IdleFacingLeft", idleLeftImage);
        playerRenderer.addRenderer("IdleFacingRight", idleRightImage);
        playerRenderer.addRenderer("WalkingLeft", walkingLeftAnimation);
        playerRenderer.addRenderer("WalkingRight", walkingRightAnimation);
        playerRenderer.addRenderer("JumpingRight", jumpingAnimation);
        playerRenderer.addRenderer("DyingRight", dyingRightAnimatino);
        playerRenderer.addRenderer("DyingLeft", dyingLeftAnimation);
        playerRenderer.setCurrentState("IdleFacingRight");

//        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");

        int maxPlayerVelocity = 8;
        GameObject player = new GameObject("player");
        player.addComponent(new Position(startX, startY));
        player.addComponent(new Movement());
        player.addComponent(new Gravity(3));
        player.addComponent(new Player());
        player.addComponent(new Collider(1, true));
        player.addComponent(new CrossBoundingBoxes(new RendererBasedBoundingBoxes(), maxPlayerVelocity + 1, maxPlayerVelocity + 1));
        player.addComponent(new PlayerCollisionHandler());
        player.addComponent(new KeyboardMovementController(inputManager));
        player.addComponent(playerRenderer);

        return player;
    }
}
