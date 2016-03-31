package com.thrashplay.saltar.editor.swing;

import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.desktop.actor.DesktopActorManager;
import com.thrashplay.luna.desktop.graphics.DesktopAnimationConfigManager;
import com.thrashplay.luna.desktop.graphics.DesktopImage;
import com.thrashplay.luna.desktop.graphics.DesktopImageManager;
import com.thrashplay.saltar.editor.model.EnemyUtils;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.ProjectChangeListener;
import com.thrashplay.saltar.editor.model.SaltarEditorApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class TemplateToolbar extends JPanel implements ProjectChangeListener {
    private SaltarEditorApp app;

    public TemplateToolbar(SaltarEditorApp app) {
        this.app = app;
        app.addProjectChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        updateTemplates(app.getProject());
    }

    @Override
    public void onProjectChanged(final Project oldProject, final Project newProject) {
        if (newProject != null) {
            newProject.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    if ("spriteSheet".equals(event.getPropertyName())) {
                        newProject.setSelectedTileTemplate(-1);
                        updateTemplates(newProject);
                    } else if ("selectedTool".equals(event.getPropertyName())) {
                        updateTemplates(newProject);
                    }
                }
            });
        }

        updateTemplates(newProject);
    }

    private void updateTemplates(Project project) {
        removeAll();

        int templateCount = 0;
        if (project != null && project.getSelectedTool() != null) {
            switch (project.getSelectedTool()) {
                case Paint:
                    templateCount = addTileTemplates(project);
                    break;

                case Monster:
                    templateCount = addMonsterTemplates(project);

                default:
                    // add nothing by default
            }
        }

        // if we have any empty set of templates, create an invisible component to "reserve" our toolbar's width
        if (templateCount == 0) {
            add(Box.createHorizontalStrut(64));
        }

        revalidate();
    }

    private int addTileTemplates(Project project) {
        int templateCount = 0;

        DesktopImageManager imageManager = app.getImageManager();
        SpriteSheet spriteSheet = imageManager.createSpriteSheet(project.getAssetsRoot(), project.getSpriteSheet());
        ButtonGroup buttonGroup = new ButtonGroup();
        for (int imageId : spriteSheet.getImageIds()) {
            DesktopImage image = (DesktopImage) spriteSheet.getImage(imageId);

            JToggleButton button = createTileButton(project, buttonGroup, imageId, image);
            add(button);
            add(Box.createVerticalStrut(3));

            // select the first template by default instead of no template
            if (templateCount == 0) {
                button.setSelected(true);
                project.setSelectedTileTemplate(imageId);
            }

            templateCount++;
        }
        return templateCount;
    }

    private int addMonsterTemplates(Project project) {
        int templateCount = 0;

        DesktopImageManager imageManager = app.getImageManager();
        DesktopAnimationConfigManager animationConfigManager = app.getAnimationConfigManager();
        DesktopActorManager actorManager = app.getActorManager();

        ButtonGroup buttonGroup = new ButtonGroup();
        for (String enemyConfigFile : project.getEnemyConfigFiles()) {
            DesktopImage image = EnemyUtils.getImageFromEnemyConfigFile(actorManager, animationConfigManager, imageManager, project, enemyConfigFile);

            JToggleButton button = createMonsterButton(project, buttonGroup, enemyConfigFile, image);
            add(button);
            add(Box.createVerticalStrut(3));

            // select the first template by default instead of no template
            if (templateCount == 0) {
                button.setSelected(true);
                project.setSelectedMonsterTemplate(enemyConfigFile);
            }

            templateCount++;
        }

        return templateCount;
    }

    private JToggleButton createTileButton(final Project project, final ButtonGroup buttonGroup, final int imageId, final DesktopImage image) {
        JToggleButton button = new JToggleButton();
        button.setMargin(new Insets(2, 5, 2, 5));
        button.setIcon(new ImageIcon(image.getBufferedImage()));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.setSelectedTileTemplate(imageId);
            }
        });
        buttonGroup.add(button);
        return button;
    }

    private JToggleButton createMonsterButton(final Project project, final ButtonGroup buttonGroup, final String enemyConfigFile, final DesktopImage image) {
        JToggleButton button = new JToggleButton();
        button.setMargin(new Insets(2, 5, 2, 5));
        button.setIcon(new ImageIcon(image.getBufferedImage()));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.setSelectedMonsterTemplate(enemyConfigFile);
            }
        });
        buttonGroup.add(button);
        return button;
    }


}
