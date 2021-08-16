package com.github.supermoonie.console4j.client;

import javax.management.MBeanServerConnection;

/**
 * @author supermoonie
 * @since 2021/8/16
 */
public interface ConsoleContext {

    /**
     * The {@link ConsoleContext.ConnectionState ConnectionState} bound property name.
     */
    String CONNECTION_STATE_PROPERTY = "connectionState";

    /**
     * Values for the {@linkplain #CONNECTION_STATE_PROPERTY
     * <i>ConnectionState</i>} bound property.
     */
    enum ConnectionState {
        /**
         * The connection has been successfully established.
         */
        CONNECTED,
        /**
         * No connection present.
         */
        DISCONNECTED,
        /**
         * The connection is being attempted.
         */
        CONNECTING
    }

    /**
     * Returns the {@link MBeanServerConnection MBeanServerConnection} for the
     * connection to an application.  The returned
     * {@code MBeanServerConnection} object becomes invalid when
     * the connection state is changed to the
     * {@link ConsoleContext.ConnectionState#DISCONNECTED DISCONNECTED} state.
     *
     * @return the {@code MBeanServerConnection} for the
     * connection to an application.
     */
    MBeanServerConnection getMBeanServerConnection();

    /**
     * Returns the current connection state.
     *
     * @return the current connection state.
     */
    ConsoleContext.ConnectionState getConnectionState();
}
