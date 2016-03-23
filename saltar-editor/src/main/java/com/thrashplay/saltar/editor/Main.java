package com.thrashplay.saltar.editor;

import com.thrashplay.saltar.editor.model.SaltarEditorApp;
import com.thrashplay.saltar.editor.ui.ToolGameObjectManager;
import com.thrashplay.saltar.editor.ui.UiGameObjectManager;
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

        app.setWindow(window);
        app.getLunaCanvas().initialize();
        app.getMainLoop().resume();

        // add grid
        UiGameObjectManager navigationUiManager = new UiGameObjectManager(
                app.getInputManager(),
                app.getLeftMouseButtonTouchManager(),
                app.getMiddleMouseButtonTouchManager(),
                app.getScreen().getGameObjectManager(),
                app.getIdGenerator());
        app.addProjectChangeListener(navigationUiManager);

        ToolGameObjectManager toolGameObjectManager = new ToolGameObjectManager(app.getGameObjectFactory(), app.getScreen().getGameObjectManager(), app.getGameObjectGridSelectionManager(), app.getLeftMouseButtonTouchManager());
        app.addProjectChangeListener(toolGameObjectManager);


//        GameObject grid = new GameObject("saltar-editor-grid");
//        grid.addComponent(new GridRenderer(new Level(1000, 1000)));
//        app.getScreen().register(grid);
    }
}
