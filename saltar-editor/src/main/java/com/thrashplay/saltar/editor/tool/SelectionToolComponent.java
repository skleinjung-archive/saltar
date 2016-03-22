package com.thrashplay.saltar.editor.tool;

import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.Project;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SelectionToolComponent extends AbstractTileSelectingTool {
    public SelectionToolComponent(Project project, MouseTouchManager mouseTouchManager, GameObjectManager gameObjectManager) {
        super(project, mouseTouchManager, gameObjectManager);
    }
}
