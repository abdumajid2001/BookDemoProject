package connection;

public class ConnectionProperties {

    private final String url;
    private final String user;
    private final String password;
    private final int inactiveConnectionTimeout;
    private final int maxConnectionReuse;
    private final int initialPoolSize;
    private final int minPoolSize;
    private final int maxPoolSize;

    public ConnectionProperties(String url,
                                String user,
                                String password,
                                String inactiveConnectionTimeout,
                                String maxConnectionReuse,
                                String initialPoolSize,
                                String minPoolSize,
                                String maxPoolSize) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.inactiveConnectionTimeout = inactiveConnectionTimeout.isEmpty() ? 60 : Integer.parseInt(inactiveConnectionTimeout);
        this.maxConnectionReuse = maxConnectionReuse.isEmpty() ? 1000 : Integer.parseInt(maxConnectionReuse);
        this.initialPoolSize = initialPoolSize.isEmpty() ? 0 : Integer.parseInt(initialPoolSize);
        this.minPoolSize = minPoolSize.isEmpty() ? 0 : Integer.parseInt(minPoolSize);
        this.maxPoolSize = maxPoolSize.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPoolSize);
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getInactiveConnectionTimeout() {
        return inactiveConnectionTimeout;
    }

    public int getMaxConnectionReuse() {
        return maxConnectionReuse;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

}
