package com.easy.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RabbitMQ 启动属性注入类
 * {@link RabbitProperties properties config} for rabbitmq.
 * @author uzdz
 * @since 0.1.0
 * @date: 2018/11/30 15:42
 */
@ConfigurationProperties(prefix = "rabbitmq.config", ignoreUnknownFields = true)
public class RabbitProperties {
    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;

    public static final String DEFAULT_VIRTUAL_HOST = "/";

    private String address;

    private Integer port;

    private String username;

    private String password;

    private String virtualHost = DEFAULT_VIRTUAL_HOST;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String toString() {
        return "RabbitProperties{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", virtualHost='" + virtualHost + '\'' +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
}
