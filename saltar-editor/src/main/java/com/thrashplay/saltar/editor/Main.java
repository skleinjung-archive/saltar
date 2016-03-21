package com.thrashplay.saltar.editor;

import com.thrashplay.saltar.editor.model.SaltarEditorApp;
import com.thrashplay.saltar.editor.ui.NavigationUiManager;
import com.thrashplay.saltar.editor.swing.SaltarEditorWindow;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class Main {
    public static void main(String[] args) {
        SaltarEditorApp app = new SaltarEditorApp();
        app.initialize();

        SaltarEditorWindow window = new SaltarEditorWindow(app);
        window.setVisible(true);

        app.getLunaCanvas().initialize();
        app.getMainLoop().resume();

        // add grid
        NavigationUiManager navigationUiManager = new NavigationUiManager(app.getInputManager(), app.getMouseTouchManager(), app.getScreen().getGameObjectManager(), app.getIdGenerator());
        app.addProjectChangeListener(navigationUiManager);


//        GameObject grid = new GameObject("saltar-editor-grid");
//        grid.addComponent(new GridRenderer(new Level(1000, 1000)));
//        app.getScreen().register(grid);
    }
}
