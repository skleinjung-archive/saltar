package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.component.Collider;
import com.thrashplay.luna.api.component.ImageRenderer;
import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.geom.Rectangle;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.api.graphics.SpriteSheet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

    // the currently selected sprite template
    private int selectedTemplate = -1;

    private int selectedTileX = 0;
    private int selectedTileY = 0;

    // todo: replace with me a settings dialog on projection creation / project settings from menu bar
    public Project(SaltarEditorApp app) {
        this.app = app;

        assetsRoot = "C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\saltar-app\\src\\main\\assets";
        spriteSheet = "spritesheets\\level1_spritesheet.json";
        level = new Level(2000, 500);
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
    }

    public int getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(int selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public GameObject createGameObjectFromSelectedTemplate() {
        SpriteSheet spriteSheet = app.getImageManager().createSpriteSheet(assetsRoot, this.spriteSheet);
        LunaImage image = spriteSheet.getImage(selectedTemplate);

        GameObject gameObject = new GameObject();
        gameObject.addComponent(new Position());        // position will be set by the tool that creates us
        gameObject.addComponent(new ImageRenderer(image, true));
        gameObject.addComponent(new Collider(2, false, new Rectangle(0, 0, level.getTileSize(), level.getTileSize())));

        return gameObject;
    }
}
