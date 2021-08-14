package com.github.supermoonie.console4j.router;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.github.supermoonie.console4j.Console4J;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author super_w
 * @since 2021/8/14
 */
@Slf4j
public class ThemeRouter extends CefMessageRouterHandlerAdapter {

    private static final String SET_THEME = "SET_THEME:";
    private static final String GET_THEME = "GET_THEME";

    private static final Map<String, String> THEME_MAP = new HashMap<String, String>() {{
        put("dark", FlatDarkLaf.class.getName());
        put("light", FlatLightLaf.class.getName());
    }};

    @Getter
    private final CefMessageRouter router;
    private static ThemeRouter instance;

    private ThemeRouter() {
        router = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig("themeQuery", "cancelThemeQuery"));
        router.addHandler(this, true);
    }

    public static ThemeRouter getInstance() {
        if (null == instance) {
            synchronized (ThemeRouter.class) {
                if (null == instance) {
                    instance = new ThemeRouter();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
        try {
            if (request.startsWith(SET_THEME)) {
                onSetTheme(request, callback);
            } else if (request.equals(GET_THEME)) {
                onGetTheme(callback);
            } else {
                callback.failure(404, "no cmd found");
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            callback.failure(500, e.getMessage());
            return true;
        }
    }

    private void onGetTheme(CefQueryCallback callback) {
        SwingUtilities.invokeLater(() -> {
            String themeName = Console4J.getPreferences().get("/theme", FlatDarkLaf.class.getName());
            if (FlatLightLaf.class.getName().equals(themeName)) {
                callback.success("light");
            } else {
                callback.success("dark");
            }
        });
    }

    private void onSetTheme(String request, CefQueryCallback callback) {
        SwingUtilities.invokeLater(() -> {
            String theme = request.substring(SET_THEME.length());
            try {
                String themeName = THEME_MAP.getOrDefault(theme, FlatDarkLaf.class.getName());
                UIManager.setLookAndFeel(themeName);
                FlatLaf.updateUI();
                Console4J.getPreferences().put("/theme", themeName);
                callback.success(theme);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.failure(500, e.getMessage());
            }
        });
    }
}
