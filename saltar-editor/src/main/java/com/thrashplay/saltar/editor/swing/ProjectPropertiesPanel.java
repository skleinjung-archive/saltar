package com.thrashplay.saltar.editor.swing;

import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.ProjectChangeListener;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class ProjectPropertiesPanel extends PropertiesPanel implements ProjectChangeListener {
    public ProjectPropertiesPanel() {
    }

    /**
     * A new project was loaded or created.
     *
     * @param oldProject the previously active project, which may be null
     * @param newProject the newly active project, which may be null (such as when the project is closed, for example)
     */
    @Override
    public void onProjectChanged(Project oldProject, Project newProject) {
        addSection("Project Properties");
        addProperty(newProject, "assetsRoot");
        addProperty(newProject, "spriteSheet");
//        addProperty(newProject, "selectedTemplate");
//        addProperty(newProject, "selectedTileX");
//        addProperty(newProject, "selectedTileY");
        addBottomBuffer();

        revalidate();
    }
}
