package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.ImageRenderer;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.level.config.GameObjectConfig;
import com.thrashplay.luna.desktop.graphics.DesktopImageManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GameObjectFactory {
    private DesktopImageManager imageManager;
    private int tileSize;

    public GameObjectFactory(DesktopImageManager imageManager, int tileSize) {
        this.imageManager = imageManager;
        this.tileSize = tileSize;
    }

    public GameObject createGameObject(Project project, GameObjectConfig gameObjectConfig) {
        SpriteSheet spriteSheet = imageManager.createSpriteSheet(project.getAssetsRoot(), project.getSpriteSheet());
        LunaImage image = spriteSheet.getImage(gameObjectConfig.getRenderer().getImageId());

        GameObject gameObject = new GameObject();
        gameObject.addComponent(new Position(gameObjectConfig.getPosition().getX(), gameObjectConfig.getPosition().getY()));
        gameObject.addComponent(new ImageRenderer(image, true));
        gameObject.addComponent(new Collider(2, false, new Rectangle(0, 0, tileSize, tileSize)));

        return gameObject;
    }
}
