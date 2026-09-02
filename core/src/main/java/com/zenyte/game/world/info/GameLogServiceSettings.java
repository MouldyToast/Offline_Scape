package com.zenyte.game.world.info;

import java.util.Objects;

public class GameLogServiceSettings {

    private boolean enabled;
    private String databaseUrl;
    private int databasePort;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;
    private int servicePort;

    public GameLogServiceSettings(boolean enabled, String databaseUrl, int databasePort, String databaseName, String databaseUser, String databasePassword, int servicePort) {
        this.enabled = enabled;
        this.databaseUrl = databaseUrl;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.servicePort = servicePort;
    }

    @SuppressWarnings("unused")
    public GameLogServiceSettings() {
        this(false, "localhost", -1, null, null, null, -1);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameLogServiceSettings that = (GameLogServiceSettings) o;
        return isEnabled() == that.isEnabled() && getDatabasePort() == that.getDatabasePort() && getServicePort() == that.getServicePort() && Objects.equals(getDatabaseUrl(), that.getDatabaseUrl()) && Objects.equals(getDatabaseName(), that.getDatabaseName()) && Objects.equals(getDatabaseUser(), that.getDatabaseUser()) && Objects.equals(getDatabasePassword(), that.getDatabasePassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isEnabled(), getDatabaseUrl(), getDatabasePort(), getDatabaseName(), getDatabaseUser(), getDatabasePassword(), getServicePort());
    }
}
