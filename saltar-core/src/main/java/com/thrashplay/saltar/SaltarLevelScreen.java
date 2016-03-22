package com.thrashplay.saltar;

import com.thrashplay.luna.api.component.*;
import com.thrashplay.luna.api.engine.DefaultScreen;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.AnimationConfigManager;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.graphics.config.AnimationConfig;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.luna.api.input.MultiTouchManager;
import com.thrashplay.luna.api.level.LevelManager;
import com.thrashplay.luna.api.physics.CrossCollisionDetector;
import com.thrashplay.luna.api.ui.Button;
import com.thrashplay.luna.engine.LegacyGameObjectAdapter;
import com.thrashplay.luna.input.VirtualKeyboard;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.luna.renderable.FpsDisplay;
import com.thrashplay.luna.ui.TextButton;
import com.thrashplay.saltar.component.*;

import java.util.List;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarLevelScreen extends DefaultScreen {
    private LevelManager levelManager;
    private ImageManager imageManager;
    private AnimationConfigManager animationConfigManager;
    private MultiTouchManager multiTouchManager;
    private InputManager inputManager;

    public SaltarLevelScreen(LevelManager levelManager, ImageManager imageManager, AnimationConfigManager animationConfigManager, MultiTouchManager multiTouchManager, InputManager inputManager) {
        this.levelManager = levelManager;
        this.imageManager = imageManager;
        this.animationConfigManager = animationConfigManager;
        this.multiTouchManager = multiTouchManager;
        this.inputManager = inputManager;
    }

    @Override
    protected void doInitialize() {
        super.doInitialize();

        gameObjectManager.register(new LegacyGameObjectAdapter("clear screen", new ClearScreen(0x7EC0EE)));

        List<GameObject> levelObjects = levelManager.createLevelObjects("levels/level01.json");
        for (GameObject object : levelObjects) {
            gameObjectManager.register(object);
        }

        GameObject player = createPlayer();

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
    }

    private GameObject createPlayer() {
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
        playerRenderer.addRenderer(Player.AnimationState.IdleFacingLeft, idleLeftImage);
        playerRenderer.addRenderer(Player.AnimationState.IdleFacingRight, idleRightImage);
        playerRenderer.addRenderer(Player.AnimationState.WalkingLeft, walkingLeftAnimation);
        playerRenderer.addRenderer(Player.AnimationState.WalkingRight, walkingRightAnimation);
        playerRenderer.addRenderer(Player.AnimationState.JumpingRight, jumpingAnimation);

//        final LunaImage image = imageManager.createSpriteSheet("spritesheets/player_spritesheet.json").getImage(1); // createImage("graphics/daxbotsheet.png");

        int maxPlayerVelocity = 8;
        GameObject player = new GameObject("player");
        player.addComponent(new Position(32, 0));
        player.addComponent(new Movement());
        player.addComponent(new Gravity(3));
        player.addComponent(new Player());
        player.addComponent(new Collider(1, true, new Rectangle(maxPlayerVelocity + 1, 0, 30 - ((maxPlayerVelocity + 1) * 2), 55),  new Rectangle(0, maxPlayerVelocity + 1, 30, 55 - ((maxPlayerVelocity + 1) * 2))));
        player.addComponent(CollisionHandler.class, new PlayerCollisionHandler());
        player.addComponent(new KeyboardMovementController(inputManager));
        player.addComponent(playerRenderer);
        gameObjectManager.register(player);

        return player;
    }
}