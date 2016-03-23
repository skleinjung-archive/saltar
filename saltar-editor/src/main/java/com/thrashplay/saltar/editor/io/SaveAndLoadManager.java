package com.thrashplay.saltar.editor.io;

import com.google.gson.Gson;
import com.thrashplay.luna.api.LunaException;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.level.config.GameObjectConfig;
import com.thrashplay.luna.api.level.config.LevelConfig;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.SaltarEditorApp;

import javax.swing.*;
import java.io.*;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaveAndLoadManager {

    private SaltarEditorApp app;
    private Gson gson = new Gson();

    private JFileChooser levelFileChooser;
    private JFileChooser projectFileChooser;

    public SaveAndLoadManager(SaltarEditorApp app) {
        this.app = app;
    }

    public void exportLevel(Project project) {
        if (levelFileChooser == null) {
            levelFileChooser = new JFileChooser();
            levelFileChooser.setCurrentDirectory(new File("C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\saltar-app\\src\\main\\assets\\levels"));
        }

        doExport(levelFileChooser, project);
    }

    public void saveProject(Project project) {
        if (projectFileChooser == null) {
            projectFileChooser = new JFileChooser();
            projectFileChooser.setCurrentDirectory(new File("C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\assets\\levels"));
        }

        doExport(projectFileChooser, project);
    }

    public void loadProject() {
        if (projectFileChooser == null) {
            projectFileChooser = new JFileChooser();
            projectFileChooser.setCurrentDirectory(new File("C:\\sandbox\\thrashplay-android-apps\\modules\\saltar\\assets\\levels"));
        }

        int result = projectFileChooser.showSaveDialog(app.getWindow());
        if (result == JFileChooser.APPROVE_OPTION) {
            FileReader reader = null;
            try {
                reader = new FileReader(projectFileChooser.getSelectedFile().getAbsolutePath());
                LevelConfig config = gson.fromJson(reader, LevelConfig.class);

                Project newProject = new Project(app);
                newProject.loadFrom(config);

                app.setProject(newProject);

                for (GameObjectConfig gameObjectConfig : config.getObjects()) {
                    GameObject newGameObject = app.getGameObjectFactory().createGameObject(newProject, gameObjectConfig);
                    app.getScreen().getGameObjectManager().register(newGameObject);
                }
            } catch (IOException e1) {
                throw new LunaException("Failed to read from file '" + projectFileChooser.getSelectedFile() + "': " + e1.toString(), e1);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) { /* do nothing */ }
                }
            }
        }
    }

    private void doExport(JFileChooser fileChooser, Project project) {
        int result = fileChooser.showSaveDialog(app.getWindow());
        if (result == JFileChooser.APPROVE_OPTION) {
            FileWriter writer = null;
            try {
                writer = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath());
                gson.toJson(project.getLevelConfig(), writer);
            } catch (IOException e1) {
                throw new LunaException("Failed to write to file '" + fileChooser.getSelectedFile() + "': " + e1.toString(), e1);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e1) { /* do nothing */ }
                }
            }
        }
    }
}
