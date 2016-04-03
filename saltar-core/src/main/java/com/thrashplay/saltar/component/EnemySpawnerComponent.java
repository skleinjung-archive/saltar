package com.thrashplay.saltar.component;

import com.thrashplay.luna.api.actor.ActorManager;
import com.thrashplay.luna.api.actor.config.ActorConfig;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class EnemySpawnerComponent implements UpdateableComponent {

    private GameObjectManager gameObjectManager;
    private ActorManager actorManager;
    private String enemyFileName;
    private long respawnTime;

    private long lastEnemySpawned;

    public EnemySpawnerComponent(GameObjectManager gameObjectManager, ActorManager actorManager, String enemyFileName, long respawnTime) {
        this.gameObjectManager = gameObjectManager;
        this.actorManager = actorManager;
        this.enemyFileName = enemyFileName;
        this.respawnTime = respawnTime;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if ((System.currentTimeMillis() - lastEnemySpawned) > respawnTime) {
            lastEnemySpawned = System.currentTimeMillis();

            Position spawnerPosition = gameObject.getComponent(Position.class);

            ActorConfig config = actorManager.createActorConfig(enemyFileName);
            GameObject enemyObject = actorManager.createActorObject(config);
            Position enemyPosition = enemyObject.getComponent(Position.class);
            enemyPosition.setX(spawnerPosition.getX());
            enemyPosition.setY(spawnerPosition.getY());

            gameObjectManager.register(enemyObject);
        }
    }
}
