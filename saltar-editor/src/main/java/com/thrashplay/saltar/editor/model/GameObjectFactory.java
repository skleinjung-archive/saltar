package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.ImageRenderer;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.level.config.EnemyConfig;
import com.thrashplay.luna.api.level.config.TileConfig;
import com.thrashplay.luna.desktop.actor.DesktopActorManager;
import com.thrashplay.luna.desktop.graphics.DesktopAnimationConfigManager;
import com.thrashplay.luna.desktop.graphics.DesktopImage;
import com.thrashplay.luna.desktop.graphics.DesktopImageManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GameObjectFactory {
    private DesktopActorManager actorManager;
    private DesktopImageManager imageManager;
    private DesktopAnimationConfigManager animationConfigManager;

    public GameObjectFactory(DesktopActorManager actorManager, DesktopImageManager imageManager, DesktopAnimationConfigManager animationConfigManager) {
        this.actorManager = actorManager;
        this.imageManager = imageManager;
        this.animationConfigManager = animationConfigManager;
    }

    public GameObject createTile(Project project, TileConfig tileConfig) {
        SpriteSheet spriteSheet = imageManager.createSpriteSheet(project.getAssetsRoot(), project.getSpriteSheet());
        LunaImage image = spriteSheet.getImage(tileConfig.getRenderer().getImageId());

        GameObject gameObject = new GameObject();
        gameObject.addComponent(new Position(tileConfig.getPosition().getX(), tileConfig.getPosition().getY()));
        gameObject.addComponent(new ImageRenderer(image, true));
        gameObject.addComponent(new Collider(2, false));

        return gameObject;
    }

    public GameObject createEnemy(Project project, EnemyConfig enemy) {
        String enemyConfigFile = project.getEnemyConfigFileById(enemy.getFileId());
        DesktopImage image = EnemyUtils.getImageFromEnemyConfigFile(actorManager, animationConfigManager, imageManager, project, enemyConfigFile);

        GameObject gameObject = new GameObject();
        gameObject.addComponent(new Position(enemy.getPosition().getX(), enemy.getPosition().getY()));
        gameObject.addComponent(new ImageRenderer(image, true));
        gameObject.addComponent(new Collider(3, true));

        return gameObject;
    }
}
