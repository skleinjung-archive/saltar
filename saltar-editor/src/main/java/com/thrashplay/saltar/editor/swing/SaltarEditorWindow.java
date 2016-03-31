package com.thrashplay.saltar.editor.swing;

import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.ProjectChangeListener;
import com.thrashplay.saltar.editor.model.SaltarEditorApp;
import com.thrashplay.saltar.editor.ui.ToolType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Enumeration;

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

        GameObjectPropertiesPanel gameObjectPropertiesPanel = createGameObjectPropertiesPanel(app);
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
        getContentPane().add(new JScrollPane(new TemplateToolbar(app)), BorderLayout.LINE_START);
        getContentPane().add(createToolbar(app), BorderLayout.PAGE_START);

        MenuBar menubar = createMenu(app);
        setMenuBar(menubar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }

    private GameObjectPropertiesPanel createGameObjectPropertiesPanel(SaltarEditorApp app) {
        final GameObjectPropertiesPanel result = new GameObjectPropertiesPanel(null);

        final PropertyChangeListener selectionPropertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if ("selectedTileX".equals(event.getPropertyName()) || "selectedTileY".equals(event.getPropertyName())) {
                    Project project = (Project) event.getSource();
                    //result.setGameObject(project.getGameObject(project.getSelectedTileX(), project.getSelectedTileY()));
                }
            }
        };

        app.addProjectChangeListener(new ProjectChangeListener() {
            @Override
            public void onProjectChanged(Project oldProject, Project newProject) {
                if (oldProject != null) {
                    oldProject.removePropertyChangeListener(selectionPropertyChangeListener);
                }
                if (newProject != null) {
                    newProject.addPropertyChangeListener(selectionPropertyChangeListener);
                }
            }
        });

        return result;
    }

    private MenuBar createMenu(final SaltarEditorApp app) {
        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        Menu settings = new Menu("Settings");
        menubar.add(file);
//        menubar.add(settings);

        MenuItem newProject = new MenuItem("New Project");
        MenuItem openProject = new MenuItem("Open Project...");
        MenuItem saveProject = new MenuItem("Save Project...");
        MenuItem exportLevel = new MenuItem("Export Level...");
        final MenuItem projectSettings = new MenuItem("Project...");
        MenuItem applicationSettings = new MenuItem("Application...");

        newProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.setProject(new Project(app));
//                projectSettings.setEnabled(true);
//                projectSettings.doClick();
            }
        });

        exportLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.getSaveAndLoadManager().exportLevel(app.getProject());
            }
        });
        openProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.getSaveAndLoadManager().loadProject();
            }
        });
        saveProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.getSaveAndLoadManager().saveProject(app.getProject());
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
        file.add(openProject);
        file.addSeparator();
        file.add(saveProject);
        file.addSeparator();
        file.add(exportLevel);

//        settings.add(projectSettings);
//        settings.add(applicationSettings);

        return menubar;
    }

    private JToolBar createToolbar(SaltarEditorApp app) {
        JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);
        toolbar.setFloatable(false);

        final ButtonGroup buttonGroup = new ButtonGroup();
        final AbstractButton selectToolButton = createButton(app, buttonGroup, ToolType.Select, "/icons/select.png");
        toolbar.add(selectToolButton);
        toolbar.add(createButton(app, buttonGroup, ToolType.Paint, "/icons/paint.png"));
        toolbar.add(createButton(app, buttonGroup, ToolType.Erase, "/icons/delete.png"));
        toolbar.add(createButton(app, buttonGroup, ToolType.Monster, "/icons/monster.png"));
        toolbar.add(createButton(app, buttonGroup, ToolType.StartPosition, "/icons/player.png"));

        // when a new project is created or loaded, change back to the select tool
        app.addProjectChangeListener(new ProjectChangeListener() {
            @Override
            public void onProjectChanged(Project oldProject, Project newProject) {
                Enumeration<AbstractButton> buttons = buttonGroup.getElements();
                while (buttons.hasMoreElements()) {
                    AbstractButton button = buttons.nextElement();
                    if (button instanceof JToggleButton) {
                        button.setSelected(false);
                    }
                }

                selectToolButton.setSelected(true);
            }
        });

        return toolbar;
    }

    private AbstractButton createButton(final SaltarEditorApp app, final ButtonGroup buttonGroup, final ToolType toolType, final String iconPath) {
        URL imageUrl = SaltarEditorWindow.class.getResource(iconPath);

        JToggleButton button = new JToggleButton();
        button.setIcon(new ImageIcon(imageUrl));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (app.getProject() != null) {
                    app.getProject().setSelectedTool(toolType);
                }
            }
        });
        buttonGroup.add(button);

        return button;
    }


}
