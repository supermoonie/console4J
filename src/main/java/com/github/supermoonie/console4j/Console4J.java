package com.github.supermoonie.console4j;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGUtils;
import com.github.supermoonie.console4j.handler.AppHandler;
import com.github.supermoonie.console4j.handler.DisplayHandler;
import com.github.supermoonie.console4j.handler.FocusHandler;
import com.github.supermoonie.console4j.router.JvmRouter;
import com.github.supermoonie.console4j.router.ThemeRouter;
import com.github.supermoonie.console4j.ui.MenuBar;
import com.github.supermoonie.console4j.utils.Folders;
import com.github.supermoonie.console4j.utils.PropertiesUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.JCefLoader;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * @author super_w
 * @since 2021/8/12
 */
@Slf4j
public class Console4J extends JFrame {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36";

    @Getter
    private static Console4J instance;
    @Getter
    private final CefApp cefApp;
    @Getter
    private final CefClient client;
    @Getter
    private final CefBrowser cefBrowser;
    @Getter
    private final ScheduledExecutorService executor;
    @Getter
    private static Preferences preferences;

    private Console4J(String[] args, Color bgColor) throws Exception {
        // init executor
        executor = new ScheduledThreadPoolExecutor(
                10,
                new BasicThreadFactory.Builder()
                        .namingPattern("schedule-exec-%d")
                        .daemon(false)
                        .uncaughtExceptionHandler((thread, throwable) -> {
                            String error = String.format("thread: %s, error: %s", thread.toString(), throwable.getMessage());
                            log.error(error, throwable);
                        }).build(), (r, executor) -> log.warn("Thread: {} reject by {}", r.toString(), executor.toString()));
        List<Image> icons = FlatSVGUtils.createWindowIconImages("/Tools.svg");
        setIconImages(icons);
        File cefPath = Folders.createTempFolder(".console4J", ".cef");
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = false;
        settings.cache_path = cefPath.getAbsolutePath();
        String debugLogPath = cefPath.getAbsolutePath() + File.separator + "debug.log";
        settings.log_file = debugLogPath;
        new File(debugLogPath).deleteOnExit();
        settings.persist_session_cookies = true;
        settings.user_agent = USER_AGENT;
        settings.background_color = settings.new ColorType(bgColor.getAlpha(), bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        CefApp.addAppHandler(new AppHandler(args));
        cefApp = JCefLoader.installAndLoadCef(settings);
        client = cefApp.createClient();
        String host = PropertiesUtil.getHost();
        cefBrowser = client.createBrowser(host + "/index.html", false, false);
        client.addFocusHandler(new FocusHandler());
        client.addDisplayHandler(new DisplayHandler());
        Component uiComponent = cefBrowser.getUIComponent();
        getContentPane().add(uiComponent, BorderLayout.CENTER);
        if (!PropertiesUtil.isRelease()) {
            MenuBar menuBar = new MenuBar(this, cefBrowser);
            setJMenuBar(menuBar);
        }
        addRouter();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                client.dispose();
                Console4J.this.dispose();
                executor.shutdown();
                try {
                    executor.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage(), ex);
                }
                System.exit(0);
            }
        });
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(800, 600));
        setLocation(screenSize.width / 2 - 800 / 2, screenSize.height / 2 - 600 / 2);
        setResizable(true);
        setFocusable(true);
        setAutoRequestFocus(true);
        setVisible(true);
    }

    private void addRouter() {
        client.addMessageRouter(ThemeRouter.getInstance().getRouter());
        client.addMessageRouter(JvmRouter.getInstance().getRouter());
    }

    public static void main(String[] args) {
        try {
            java.awt.Toolkit.getDefaultToolkit();
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            if (SystemUtils.IS_OS_MAC) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.UIElement", "true");
            }
            preferences = Preferences.userRoot().node("/console4J");
            String themeName = preferences.get("/theme", FlatDarkLaf.class.getName());
            log.info("current theme: {}", themeName);
            FlatLightLaf.setup();
            FlatDarkLaf.setup();
            UIManager.setLookAndFeel(themeName);
            Color bgColor = themeName.equals(FlatDarkLaf.class.getName()) ? new Color(100,48, 48, 48) : new Color(100, 250, 250, 250);
            instance = new Console4J(args, bgColor);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(0);
        }

    }
}
