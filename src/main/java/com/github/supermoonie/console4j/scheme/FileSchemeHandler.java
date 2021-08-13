// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package com.github.supermoonie.console4j.scheme;

import com.github.supermoonie.console4j.mime.MimeMappings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * The example for the second scheme with domain handling is a more
 * complex example and is taken from the parent project CEF. Please
 * see CEF: "cefclient/scheme_test.cpp" for futher details
 *
 * @author wangc
 */
@Slf4j
public class FileSchemeHandler extends CefResourceHandlerAdapter {
    public static final String WIN_SCHEME = "file";

    private byte[] data;
    private int offset = 0;
    private String mimeType;

    public FileSchemeHandler() {
        super();
    }

    @Override
    public synchronized boolean processRequest(CefRequest request, CefCallback callback) {
        boolean handled = false;
        try {
            int randomIndex = request.getURL().lastIndexOf("?t=");
            String url = request.getURL();
            if (-1 != randomIndex) {
                url = url.substring(0, randomIndex);
            }
            int index = url.lastIndexOf(".");
            if (-1 != index) {
                String extension = request.getURL().substring(index + 1);
                mimeType = MimeMappings.DEFAULT.get(extension);
            }
            String path = URLDecoder.decode(url.replace("file://", ""), StandardCharsets.UTF_8.toString());
            log.info("url: {}, path: {}", request.getURL(), path);
            File file = new File(path);
            data = FileUtils.readFileToByteArray(file);
            handled = true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (handled) {
            // Indicate the headers are available.
            callback.Continue();
            return true;
        }
        return false;
    }

    @Override
    public void getResponseHeaders(CefResponse response, IntRef responseLength, StringRef redirectUrl) {
        response.setMimeType(mimeType);
        response.setStatus(200);
        responseLength.set(data.length);
    }

    @Override
    public synchronized boolean readResponse(
            byte[] dataOut, int bytesToRead, IntRef bytesRead, CefCallback callback) {
        boolean hasData = false;

        if (offset < data.length) {
            // Copy the next block of data into the buffer.
            int transferSize = Math.min(bytesToRead, (data.length - offset));
            System.arraycopy(data, offset, dataOut, 0, transferSize);
            offset += transferSize;
            bytesRead.set(transferSize);
            hasData = true;
        } else {
            offset = 0;
            bytesRead.set(0);
        }
        return hasData;
    }
}
