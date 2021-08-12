package com.github.supermoonie.console4j.ui;

import com.github.supermoonie.console4j.dialog.DevToolsDialog;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author supermoonie
 * @since 2021/3/1
 */
public class MenuBar extends JMenuBar {

    private final Frame owner;
    private final CefBrowser cefBrowser;

    public MenuBar(Frame owner,
                   CefBrowser cefBrowser) {
        this.owner = owner;
        this.cefBrowser = cefBrowser;
        JMenu toolsMenu = new JMenu("Tools");
        final JMenuItem showDevTools = new JMenuItem("Show DevTools");
        showDevTools.addActionListener(e -> {
            DevToolsDialog devToolsDlg =
                    new DevToolsDialog(MenuBar.this.owner, "DEV Tools", MenuBar.this.cefBrowser);
            devToolsDlg.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    showDevTools.setEnabled(true);
                }
            });
            devToolsDlg.setVisible(true);
            showDevTools.setEnabled(false);
        });
        toolsMenu.add(showDevTools);
        add(toolsMenu);

    }
}
