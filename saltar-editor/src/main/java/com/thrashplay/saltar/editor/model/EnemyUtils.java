package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.actor.config.ActorAnimationConfig;
import com.thrashplay.luna.api.actor.config.ActorConfig;
import com.thrashplay.luna.api.animation.AnimationConfig;
import com.thrashplay.luna.api.animation.AnimationFrameConfig;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.desktop.actor.DesktopActorManager;
import com.thrashplay.luna.desktop.graphics.DesktopAnimationConfigManager;
import com.thrashplay.luna.desktop.graphics.DesktopImage;
import com.thrashplay.luna.desktop.graphics.DesktopImageManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class EnemyUtils {
    public static DesktopImage getImageFromEnemyConfigFile(DesktopActorManager actorManager, DesktopAnimationConfigManager animationConfigManager, DesktopImageManager imageManager, Project project, String enemyConfigFile) {
        ActorConfig enemyConfig = actorManager.createActorConfig(project.getAssetsRoot(), enemyConfigFile);
        ActorAnimationConfig defaultAnimationPointer = findDefaultAnimationConfig(enemyConfig);
        AnimationConfig defaultAnimationConfig = animationConfigManager.getAnimationConfig(project.getAssetsRoot(), defaultAnimationPointer.getFile());

        AnimationFrameConfig firstFrame = defaultAnimationConfig.getFrames().get(0);
        SpriteSheet spriteSheet = imageManager.createSpriteSheet(project.getAssetsRoot(), defaultAnimationConfig.getSpriteSheet());
        return (DesktopImage) spriteSheet.getImage(firstFrame.getImageId());
    }

    private static ActorAnimationConfig findDefaultAnimationConfig(ActorConfig enemyConfig) {
        ActorAnimationConfig result = null;
        for (ActorAnimationConfig animationConfig : enemyConfig.getAnimations().values()) {
            if (animationConfig.isDefault() || result == null) {
                result = animationConfig;
            }
        }
        return result;
    }
}
