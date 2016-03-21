package com.thrashplay.saltar.editor.ui;

import com.thrashplay.saltar.editor.model.SaltarEditorApp;
import com.thrashplay.saltar.editor.model.Project;

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

        GameObjectEditorPanel propertiesEditor = new GameObjectEditorPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lunaCanvasHolder, propertiesEditor);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(760);

        getContentPane().add(splitPane);

        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu settings = new JMenu("Settings");
        menubar.add(file);
//        menubar.add(settings);

        JMenuItem newProject = new JMenuItem("New Project");
        final JMenuItem projectSettings = new JMenuItem("Project...");
        JMenuItem applicationSettings = new JMenuItem("Application...");

        newProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.setProject(new Project());
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

        setJMenuBar(menubar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}
