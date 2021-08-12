package com.github.supermoonie.console4j.dialog;

import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author supermoonie
 * @since 2021/2/26
 */
public class DevToolsDialog extends JDialog {
    private final CefBrowser devTools;

    public DevToolsDialog(Frame owner, String title, CefBrowser browser) {
        this(owner, title, browser, null);
    }

    public DevToolsDialog(Frame owner, String title, CefBrowser browser,
                          Point inspectAt) {
        super(owner, title, false);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocation(owner.getLocation().x + 20, owner.getLocation().y + 20);

        devTools = browser.getDevTools(inspectAt);
        Component uiComponent = devTools.getUIComponent();
        uiComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                boolean flag = (e.isControlDown() || e.isMetaDown()) && (e.getKeyCode() == KeyEvent.VK_C);
                if (flag) {
                    devTools.getFocusedFrame().copy();
                }
            }
        });
        add(uiComponent);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                dispose();
            }
        });
    }

    public CefBrowser getDevTools() {
        return devTools;
    }

    @Override
    public void dispose() {
        devTools.close(true);
        super.dispose();
    }
}
