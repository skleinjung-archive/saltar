package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.LunaException;
import com.thrashplay.luna.api.level.config.*;
import com.thrashplay.saltar.editor.ui.ToolType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    private int startX = 0;
    private int startY = 0;

    // the currently selected tool from the toolbar
    private ToolType selectedTool;

    // the currently selected sprite template
    private int selectedTemplate = -1;

    private int selectedTileX = 0;
    private int selectedTileY = 0;

    private GameObjectConfig[][] gameObjectConfigs = new GameObjectConfig[0][];

    // todo: replace with me a settings dialog on projection creation / project settings from menu bar
    public Project(SaltarEditorApp app) {
        this.app = app;

        assetsRoot = "C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\saltar-app\\src\\main\\assets";
        spriteSheet = "spritesheets/level01_spritesheet.json";

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

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
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

    public GameObjectConfig createGameObjectConfig() {
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

        gameObjectConfigs[selectedTileY][selectedTileX] = config;

        return config;
    }

    public GameObjectConfig getGameObjectConfig(int tileX, int tileY) {
        return gameObjectConfigs[tileY][tileX];
    }

    public void removeTileConfig(int tileX, int tileY) {
        gameObjectConfigs[tileY][tileX] = null;
    }

    public LevelConfig getLevelConfig() {
        TileMapConfig tileMapConfig = new TileMapConfig();
        tileMapConfig.setId(1);
        tileMapConfig.setResource(spriteSheet);

        LevelConfig level = new LevelConfig();
        List<GameObjectConfig> gameObjectConfigList = new LinkedList<>();
        for (int y = 0; y < gameObjectConfigs.length; y++) {
            for (int x = 0; x < gameObjectConfigs[0].length; x++) {
                GameObjectConfig gameObjectConfig = gameObjectConfigs[y][x];
                if (gameObjectConfig != null) {
                    gameObjectConfigList.add(gameObjectConfig);
                }
            }
        }
        level.setObjects(gameObjectConfigList);
        level.setTileMaps(Arrays.asList(tileMapConfig));
        level.setStartX(startX);
        level.setStartY(startY);
        return level;
    }

    public void loadFrom(LevelConfig levelConfig) {
        if (levelConfig.getTileMaps().size() > 1) {
            throw new LunaException("Multiple tilemaps are not supported in this version of the editor.");
        }

        startX = levelConfig.getStartX();
        startY = levelConfig.getStartY();

        spriteSheet = levelConfig.getTileMaps().get(0).getResource();
        resizeGameObjectGrid();

        int tileWidth = level.getTileSize();
        int tileHeight = level.getTileSize();
        for (GameObjectConfig gameObjectConfig : levelConfig.getObjects()) {
            PositionConfig position = gameObjectConfig.getPosition();
            int tileX = position.getX() / tileWidth;
            int tileY = position.getY() / tileHeight;
            gameObjectConfigs[tileY][tileX] = gameObjectConfig;
        }
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

        GameObjectConfig[][] newGrid = new GameObjectConfig[tileCountY][];
        for (int y = 0; y < tileCountY; y++) {
            newGrid[y] = new GameObjectConfig[tileCountX];
        }

        for (int y = 0; y < gameObjectConfigs.length; y++) {
            for (int x = 0; x < gameObjectConfigs[0].length; x++) {
                if (y < tileCountY && x < tileCountX) {
                    newGrid[y][x] = gameObjectConfigs[y][x];
                }
            }
        }

        gameObjectConfigs = newGrid;
    }
}
