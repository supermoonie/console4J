package com.github.supermoonie.console4j.handler;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefFocusHandlerAdapter;

import java.awt.*;

/**
 * @author super_w
 * @since 2021/8/12
 */
public class FocusHandler extends CefFocusHandlerAdapter {

    private boolean browserFocus = true;

    @Override
    public void onGotFocus(CefBrowser browser) {
        if (browserFocus) {
            return;
        }
        browserFocus = true;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        browser.setFocus(true);
    }

    @Override
    public void onTakeFocus(CefBrowser browser, boolean next) {
        browserFocus = false;
    }
}
