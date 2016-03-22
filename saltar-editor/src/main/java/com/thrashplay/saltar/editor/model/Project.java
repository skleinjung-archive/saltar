package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.ImageRenderer;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.api.level.config.*;
import com.thrashplay.saltar.editor.ui.ToolType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Project {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private SaltarEditorApp app;

    /// root folder for assets, should be the game project's assets folder
    private String assetsRoot;

    // the spritesheet associated with this project, in the future we would like to support multiple sprite sheets per project
    // this should be a relative URL, with assetsRoot as its base
    private String spriteSheet;

    // the level associated with this project, in the future we would like to support multiple levels per project
    private Level level;

    // the currently selected tool from the toolbar
    private ToolType selectedTool;

    // the currently selected sprite template
    private int selectedTemplate = -1;

    private int selectedTileX = 0;
    private int selectedTileY = 0;

    private GameObject[][] gameObjects;
    private Map<GameObject, GameObjectConfig> gameObjectConfigs = new HashMap<>();

    // todo: replace with me a settings dialog on projection creation / project settings from menu bar
    public Project(SaltarEditorApp app) {
        this.app = app;

        gameObjects = new GameObject[0][];

        assetsRoot = "C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\saltar-app\\src\\main\\assets";
        spriteSheet = "spritesheets\\level1_spritesheet.json";

        //level = new Level(240, 26);  <- mario maker dimensions
        level = new Level(80, 26);

        resizeGameObjectGrid();
    }

    public String getAssetsRoot() {
        return assetsRoot;
    }

    public void setAssetsRoot(String assetsRoot) {
        String oldValue = this.assetsRoot;
        this.assetsRoot = assetsRoot;
        pcs.firePropertyChange("assetsRoot", oldValue, assetsRoot);
    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(String spriteSheet) {
        String oldValue = this.spriteSheet;
        this.spriteSheet = spriteSheet;
        pcs.firePropertyChange("spriteSheet", oldValue, spriteSheet);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        resizeGameObjectGrid();
    }

    public int getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(int selectedTemplate) {
        int oldValue = this.selectedTemplate;
        this.selectedTemplate = selectedTemplate;
        pcs.firePropertyChange("selectedTemplate", oldValue, selectedTemplate);
    }

    public ToolType getSelectedTool() {
        return selectedTool;
    }

    public void setSelectedTool(ToolType selectedTool) {
        ToolType oldValue = this.selectedTool;
        this.selectedTool = selectedTool;
        pcs.firePropertyChange("selectedTool", oldValue, selectedTool);
    }

    public int getSelectedTileX() {
        return selectedTileX;
    }

    public void setSelectedTileX(int selectedTileX) {
        int oldValue = this.selectedTileX;
        this.selectedTileX = selectedTileX;
        pcs.firePropertyChange("selectedTileX", oldValue, selectedTileX);
    }

    public int getSelectedTileY() {
        return selectedTileY;
    }

    public void setSelectedTileY(int selectedTileY) {
        int oldValue = this.selectedTileY;
        this.selectedTileY = selectedTileY;
        pcs.firePropertyChange("selectedTileY", oldValue, selectedTileY);
    }

    /**
     * Retrieves the game object at the specified tile grid coordinates.
     * @param tileX the x coordinate of the tile
     * @param tileY the y coordinate of the tile
     * @return the matching game object, or null if an empty tile is referenced
     */
    public GameObject getGameObject(int tileX, int tileY) {
        return gameObjects[tileY][tileX];
    }

    public GameObject createGameObject() {
        if (selectedTemplate == -1) {
            return null;
        }

        PositionConfig position = new PositionConfig();
        position.setX(selectedTileX * level.getTileSize());
        position.setY(selectedTileY * level.getTileSize());

        RendererConfig renderer = new RendererConfig();
        renderer.setTileMapId(1);
        renderer.setImageId(selectedTemplate);

        GameObjectConfig config = new GameObjectConfig();
        config.setPosition(position);
        config.setRenderer(renderer);

        SpriteSheet spriteSheet = app.getImageManager().createSpriteSheet(assetsRoot, this.spriteSheet);
        LunaImage image = spriteSheet.getImage(renderer.getImageId());

        GameObject gameObject = new GameObject();
        gameObject.addComponent(new Position(position.getX(), position.getY()));
        gameObject.addComponent(new ImageRenderer(image, true));
        gameObject.addComponent(new Collider(2, false, new Rectangle(0, 0, level.getTileSize(), level.getTileSize())));

        gameObjectConfigs.put(gameObject, config);
        gameObjects[selectedTileY][selectedTileX] = gameObject;
        return gameObject;
    }

    public void eraseGameObject(int tileX, int tileY) {
        gameObjectConfigs.remove(gameObjects[tileY][tileX]);
        gameObjects[tileY][tileX] = null;
    }

    public LevelConfig getLevelConfig() {
        TileMapConfig tileMapConfig = new TileMapConfig();
        tileMapConfig.setId(1);
        tileMapConfig.setResource(spriteSheet);

        LevelConfig level = new LevelConfig();
        level.setObjects(new LinkedList<GameObjectConfig>(gameObjectConfigs.values()));
        level.setTileMaps(Arrays.asList(tileMapConfig));
        return level;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    private void resizeGameObjectGrid() {
        int tileCountX = level.getGridSizeX();
        int tileCountY = level.getGridSizeY();

        GameObject[][] newGrid = new GameObject[tileCountY][];
        for (int y = 0; y < tileCountY; y++) {
            newGrid[y] = new GameObject[tileCountX];
        }

        for (int y = 0; y < gameObjects.length; y++) {
            for (int x = 0; x < gameObjects[0].length; x++) {
                if (y < tileCountY && x < tileCountX) {
                    newGrid[y][x] = gameObjects[y][x];
                }
            }
        }

        gameObjects = newGrid;
    }
}
