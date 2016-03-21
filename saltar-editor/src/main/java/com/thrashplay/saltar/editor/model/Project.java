package com.thrashplay.saltar.editor.model;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Project {
    /// root folder for assets, should be the game project's assets folder
    private String assetsRoot;

    // the level associated with this project, in the future we would like to support multiple levels per project
    private Level level;

    // todo: replace with me a settings dialog on projection creation / project settings from menu bar
    public Project() {
        level = new Level(2000, 500);
        assetsRoot = "C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\saltar-app\\src\\main\\assets";
    }

    public String getAssetsRoot() {
        return assetsRoot;
    }

    public void setAssetsRoot(String assetsRoot) {
        this.assetsRoot = assetsRoot;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
