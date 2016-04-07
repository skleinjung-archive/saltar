package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Hud implements RenderableComponent {

    private GameObjectManager gameObjectManager;

    private LunaImage heartImage;
    private LunaImage weaponImage;
    private LunaImage emptyPowerImage;
    private LunaImage lifePowerImage;
    private LunaImage weaponPowerImage;
    private LunaImage saraImage;

    public Hud(GameObjectManager gameObjectManager, ImageManager imageManager) {
        this.gameObjectManager = gameObjectManager;

        SpriteSheet spriteSheet = imageManager.createSpriteSheet("spritesheets/ui_spritesheet.json");
        heartImage = spriteSheet.getImage(1);
        weaponImage = spriteSheet.getImage(2);
        lifePowerImage = spriteSheet.getImage(3);
        weaponPowerImage = spriteSheet.getImage(5);
        emptyPowerImage = spriteSheet.getImage(4);
        saraImage = spriteSheet.getImage(6);
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {
        graphics.fillRect(0, 0, graphics.getWidth(), 16, 0xff000016);
        graphics.drawImage(heartImage, 8, 0);
        graphics.drawImage(weaponImage, 8, 8);


        GameObject playerObject = gameObjectManager.getGameObject("player");
        Player player = playerObject.getComponent(Player.class);

        int currentLife = player.getCurrentHealth();
        int maxLife = player.getMaximumHealth();

        for (int i = 0; i < maxLife; i++) {
            LunaImage image = i < currentLife ? lifePowerImage : emptyPowerImage;
            graphics.drawImage(image, 16 + (i * 4), 0);
        }
    }
}
