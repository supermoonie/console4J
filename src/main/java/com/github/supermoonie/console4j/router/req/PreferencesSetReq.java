package com.github.supermoonie.console4j.router.req;

import lombok.Data;

/**
 * @author super_w
 * @since 2021/7/17
 */
@Data
public class PreferencesSetReq<T> {

    private String key;

    private T value;
}
