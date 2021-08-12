package com.github.supermoonie.console4j.handler;

import com.github.supermoonie.console4j.scheme.ClientSchemeHandler;
import com.github.supermoonie.console4j.scheme.FileSchemeHandler;
import org.cef.CefApp;
import org.cef.callback.CefCommandLine;
import org.cef.handler.CefAppHandlerAdapter;

/**
 * @author super_w
 * @since 2021/8/12
 */
public class AppHandler extends CefAppHandlerAdapter {

    public AppHandler(String[] args) {
        super(args);
    }

    @Override
    public void stateHasChanged(CefApp.CefAppState state) {
        if (state == CefApp.CefAppState.TERMINATED) {
            System.exit(0);
        }
    }

    @Override
    public void onBeforeCommandLineProcessing(String processType, CefCommandLine commandLine) {
        commandLine.appendSwitch("disable-web-security");
        commandLine.appendSwitch("–allow-file-access-from-files");
        commandLine.appendSwitch("--enable-local-file-accesses");
        super.onBeforeCommandLineProcessing(processType, commandLine);
    }

    @Override
    public void onContextInitialized() {
        CefApp.getInstance().registerSchemeHandlerFactory("https", "console4j",
                (browser, frame, schemeName, request) -> new ClientSchemeHandler());
        CefApp.getInstance().registerSchemeHandlerFactory(FileSchemeHandler.WIN_SCHEME, "",
                (browser, frame, schemeName, request) -> new FileSchemeHandler());
    }
}
