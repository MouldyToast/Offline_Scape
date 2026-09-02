package com.zenyte.game.world.info;

public class ApiSettings {
    private boolean enabled;
    private String scheme;
    private String host;
    private int port;
    private String token;

    public ApiSettings(boolean enabled, String scheme, String host, int port, String token) {
        this.enabled = enabled;
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.token = token;
    }

    public ApiSettings() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ApiSettings(enabled=" + this.isEnabled() + ", scheme=" + this.getScheme() + ", host=" + this.getHost() + ", port=" + this.getPort() + ", token=" + this.getToken() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ApiSettings)) return false;
        final ApiSettings other = (ApiSettings) o;
        if (!other.canEqual(this)) return false;
        if (this.isEnabled() != other.isEnabled()) return false;
        if (this.getPort() != other.getPort()) return false;
        final Object this$scheme = this.getScheme();
        final Object other$scheme = other.getScheme();
        if (this$scheme == null ? other$scheme != null : !this$scheme.equals(other$scheme)) return false;
        final Object this$host = this.getHost();
        final Object other$host = other.getHost();
        if (this$host == null ? other$host != null : !this$host.equals(other$host)) return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        return this$token == null ? other$token == null : this$token.equals(other$token);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ApiSettings;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isEnabled() ? 79 : 97);
        result = result * PRIME + this.getPort();
        final Object $scheme = this.getScheme();
        result = result * PRIME + ($scheme == null ? 43 : $scheme.hashCode());
        final Object $host = this.getHost();
        result = result * PRIME + ($host == null ? 43 : $host.hashCode());
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 43 : $token.hashCode());
        return result;
    }
}
