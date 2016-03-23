package com.thrashplay.saltar.editor.swing;

import com.thrashplay.luna.api.graphics.SpriteSheet;
import com.thrashplay.luna.desktop.graphics.DesktopImage;
import com.thrashplay.luna.desktop.graphics.DesktopImageManager;
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
public class TileTemplateToolbar extends JPanel implements ProjectChangeListener {
    private SaltarEditorApp app;

    public TileTemplateToolbar(SaltarEditorApp app) {
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
                        newProject.setSelectedTemplate(-1);
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
        if (project != null) {
            DesktopImageManager imageManager = app.getImageManager();
            SpriteSheet spriteSheet = imageManager.createSpriteSheet(project.getAssetsRoot(), project.getSpriteSheet());
            ButtonGroup buttonGroup = new ButtonGroup();
            for (int imageId : spriteSheet.getImageIds()) {
                DesktopImage image = (DesktopImage) spriteSheet.getImage(imageId);

                JToggleButton button = createButton(project, buttonGroup, imageId, image);
                add(button);
                add(Box.createVerticalStrut(3));

                // select the first templte by default instead of no template
                if (templateCount == 0) {
                    button.setSelected(true);
                    project.setSelectedTemplate(imageId);
                }

                templateCount++;
            }
        }

        // if we have any empty set of templates, create an invisible component to "reserve" our toolbar's width
        if (templateCount == 0) {
            add(Box.createHorizontalStrut(64));
        }

        revalidate();
    }

    private JToggleButton createButton(final Project project, final ButtonGroup buttonGroup, final int imageId, final DesktopImage image) {
        JToggleButton button = new JToggleButton();
        button.setMargin(new Insets(2, 5, 2, 5));
        button.setIcon(new ImageIcon(image.getBufferedImage()));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.setSelectedTemplate(imageId);
            }
        });
        buttonGroup.add(button);
        return button;
    }
}
