package com.thrashplay.saltar.editor.swing;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.saltar.Saltar;
import com.thrashplay.saltar.editor.model.SaltarEditorApp;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.ui.MouseViewportController;
import com.thrashplay.saltar.editor.ui.SelectedTileTrackingViewportController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarEditorWindow extends JFrame {
    public SaltarEditorWindow(final SaltarEditorApp app) {
        super("Jump! Jump! Go! Go! Editor");

        JPanel lunaCanvasHolder = new JPanel();
        lunaCanvasHolder.setLayout(new GridLayout(1, 1));
        lunaCanvasHolder.add(app.getLunaCanvas());

        GameObject temp__for__testing = new GameObject(GameObjectIds.ID_VIEWPORT);
        temp__for__testing.addComponent(new Position(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT));
        temp__for__testing.addComponent(new MouseViewportController(app.getLeftMouseButtonTouchManager(), null));
        temp__for__testing.addComponent(new SelectedTileTrackingViewportController(null, null));

        GameObjectPropertiesPanel gameObjectPropertiesPanel = new GameObjectPropertiesPanel(temp__for__testing);
        JScrollPane gameObjectPropertiesScrollPane = new JScrollPane(gameObjectPropertiesPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        ProjectPropertiesPanel projectPropertiesPanel = new ProjectPropertiesPanel();
        app.addProjectChangeListener(projectPropertiesPanel);

        JTabbedPane propertiesPanel = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        propertiesPanel.setPreferredSize(new Dimension(325, 100));
        propertiesPanel.setMinimumSize(new Dimension(325, 100));
        propertiesPanel.add("Tile", gameObjectPropertiesScrollPane);
        propertiesPanel.add("Project", projectPropertiesPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lunaCanvasHolder, propertiesPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(760);


        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(new JScrollPane(new TileTemplateToolbar(app)), BorderLayout.LINE_START);

        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        Menu settings = new Menu("Settings");
        menubar.add(file);
//        menubar.add(settings);

        MenuItem newProject = new MenuItem("New Project");
        final JMenuItem projectSettings = new JMenuItem("Project...");
        MenuItem applicationSettings = new MenuItem("Application...");

        newProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.setProject(new Project(app));
//                projectSettings.setEnabled(true);
//                projectSettings.doClick();
            }
        });

        projectSettings.setEnabled(false); // disabled, because no project at app startup
        projectSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Project Settings activated.");
            }
        });

        file.add(newProject);
//        settings.add(projectSettings);
//        settings.add(applicationSettings);

        setMenuBar(menubar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}
