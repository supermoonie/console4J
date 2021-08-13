package com.github.supermoonie.console4j.router;

import com.github.supermoonie.console4j.Console4J;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

/**
 * @author super_w
 * @since 2021/8/13
 */
@Slf4j
public class JvmRouter extends CefMessageRouterHandlerAdapter {

    private static final String GET_LOCAL_JVM = "GET_LOCAL_JVM";

    @Getter
    private final CefMessageRouter router;
    private static JvmRouter instance;

    private JvmRouter() {
        router = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig("jvmQuery", "cancelJvmQuery"));
        router.addHandler(this, true);
    }

    public static JvmRouter getInstance() {
        if (null == instance) {
            synchronized (JvmRouter.class) {
                if (null == instance) {
                    instance = new JvmRouter();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
        try {
            if (request.startsWith(GET_LOCAL_JVM)) {
                onGetLocalJvm(request, callback);
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

    private void onGetLocalJvm(String request, CefQueryCallback callback) {
        Console4J.getInstance().getExecutor().execute(() -> {

        });
    }
}
