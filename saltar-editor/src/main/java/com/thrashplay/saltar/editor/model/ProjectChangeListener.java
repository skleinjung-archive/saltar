package com.thrashplay.saltar.editor.model;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public interface ProjectChangeListener {
    /**
     * Called when the current project is changed (a new one is loaded, a new one is created, it is closed, etc).
     *
     * @param oldProject the previously active project, which may be null
     * @param newProject the newly active project, which may be null (such as when the project is closed, for example)
     */
    void onProjectChanged(Project oldProject, Project newProject);
}
