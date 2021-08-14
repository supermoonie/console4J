package com.github.supermoonie.console4j.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.supermoonie.console4j.Console4J;
import com.github.supermoonie.console4j.router.req.PreferencesSetReq;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;

/**
 * @author super_w
 * @since 2021/7/17
 */
@Slf4j
public class PreferencesRouter extends CefMessageRouterHandlerAdapter {

    private static final String PREFERENCES_GET_STRING = "preferences:get:string:";
    private static final String PREFERENCES_SET_STRING = "preferences:set:string:";

    @Getter
    private final CefMessageRouter preferencesRouter;
    private static PreferencesRouter instance;

    private PreferencesRouter() {
        preferencesRouter = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig("preferencesQuery", "cancelPreferencesQuery"));
        preferencesRouter.addHandler(this, true);
    }

    public static PreferencesRouter getInstance() {
        if (null == instance) {
            synchronized (PreferencesRouter.class) {
                if (null == instance) {
                    instance = new PreferencesRouter();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
        try {
            if (request.startsWith(PREFERENCES_GET_STRING)) {
                onGetString(request, callback);
                return true;
            } else if (request.startsWith(PREFERENCES_SET_STRING)) {
                onSetString(request, callback);
                return true;
            }
            callback.failure(404, "no cmd found");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            callback.failure(500, e.getMessage());
            return true;
        }
    }

    private void onSetString(String request, CefQueryCallback callback) {
        Console4J.getInstance().getExecutor().execute(() -> {
            try {
                String req = request.replace(PREFERENCES_SET_STRING, "");
                if (StringUtils.isEmpty(req)) {
                    callback.failure(405, "cmd: " + PREFERENCES_SET_STRING + " args is empty!");
                    return;
                }
                PreferencesSetReq<String> setReq = JSON.parseObject(req, new TypeReference<PreferencesSetReq<String>>() {
                });
                Console4J.getPreferences().put(setReq.getKey(), setReq.getValue());
                callback.success("");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.failure(-500, e.getMessage());
            }

        });
    }

    private void onGetString(String request, CefQueryCallback callback) {
        Console4J.getInstance().getExecutor().execute(() -> {
            try {
                String req = request.replace(PREFERENCES_GET_STRING, "");
                if (StringUtils.isEmpty(req)) {
                    callback.failure(405, "cmd: " + PREFERENCES_GET_STRING + " args is empty!");
                    return;
                }
                String value = Console4J.getPreferences().get(req, "");
                callback.success(value);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                callback.failure(-500, e.getMessage());
            }

        });
    }
}
