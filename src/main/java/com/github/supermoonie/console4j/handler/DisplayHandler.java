package com.github.supermoonie.console4j.handler;

import com.github.supermoonie.console4j.Console4J;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefDisplayHandlerAdapter;

/**
 * @author super_w
 * @since 2021/8/12
 */
public class DisplayHandler extends CefDisplayHandlerAdapter {

    @Override
    public void onTitleChange(CefBrowser browser, String title) {
        if (title.contains("devtools")) {
            return;
        }
        Console4J.getInstance().setTitle(title);
    }
}
