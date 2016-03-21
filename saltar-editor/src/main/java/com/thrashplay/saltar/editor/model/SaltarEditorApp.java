package com.thrashplay.saltar.editor.model;

import com.thrashplay.luna.api.engine.ScreenManager;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.desktop.LunaCanvas;
import com.thrashplay.luna.desktop.graphics.DesktopImageManager;
import com.thrashplay.luna.desktop.graphics.DesktopSpriteSheetConfigManager;
import com.thrashplay.luna.desktop.input.DesktopKeyboard;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.luna.engine.DefaultScreenManager;
import com.thrashplay.luna.engine.LegacyGameObjectAdapter;
import com.thrashplay.luna.engine.loop.FixedFpsMainLoop;
import com.thrashplay.luna.input.DefaultInputManager;
import com.thrashplay.luna.renderable.ClearScreen;
import com.thrashplay.saltar.editor.screen.MutableScreen;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarEditorApp {

    private List<ProjectChangeListener> projectChangeListeners = new LinkedList<>();

    private Project project;
    private IdGenerator idGenerator;

    private LunaCanvas lunaCanvas;
    private FixedFpsMainLoop mainLoop;
    private MutableScreen screen;

    // managers
    private DefaultScreenManager screenManager;
    private DefaultInputManager inputManager;
    private MouseTouchManager mouseTouchManager;
    private DesktopImageManager imageManager;

    public void initialize() {
        lunaCanvas = new LunaCanvas(480, 320);
        lunaCanvas.setPreferredSize(new Dimension(720, 480));
        lunaCanvas.setMinimumSize(new Dimension(720, 480));

        screen = new MutableScreen();
        screen.register(new LegacyGameObjectAdapter(new ClearScreen(0xff000000)));

        screenManager = new DefaultScreenManager();
        screenManager.registerScreen("default", screen);
        screenManager.setCurrentScreen("default");

        mouseTouchManager = new MouseTouchManager(lunaCanvas, 480, 320);
        inputManager = new DefaultInputManager();
        inputManager.addKeyboard(new DesktopKeyboard(lunaCanvas));
        imageManager = new DesktopImageManager(new DesktopSpriteSheetConfigManager());

        mainLoop = new FixedFpsMainLoop(screenManager, lunaCanvas);

        // create model objects
        idGenerator = new IdGenerator();
    }

    public LunaCanvas getLunaCanvas() {
        return lunaCanvas;
    }

    public FixedFpsMainLoop getMainLoop() {
        return mainLoop;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public MouseTouchManager getMouseTouchManager() {
        return mouseTouchManager;
    }

    public DesktopImageManager getImageManager() {
        return imageManager;
    }

    public MutableScreen getScreen() {
        return screen;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        Project oldProject = this.project;
        this.project = project;
        fireProjectChangedEvent(oldProject, this.project);
    }

    public void addProjectChangeListener(ProjectChangeListener listener) {
        projectChangeListeners.add(listener);
    }

    public void removeProjectChangeListener(ProjectChangeListener listener) {
        projectChangeListeners.remove(listener);
    }

    /**
     * Notify registered {@link com.thrashplay.saltar.editor.model.ProjectChangeListener}s that the project has changed.
     * @param oldProject the previously active project
     * @param newProject the currently active project
     */
    private void fireProjectChangedEvent(Project oldProject, Project newProject) {
        for (ProjectChangeListener listener : projectChangeListeners) {
            listener.onProjectChanged(oldProject, newProject);
        }
    }
}
