package es.app2u.beat.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import es.app2u.beat.logging.Logger;

public class Parameters {

    private String method;
    private String url;
    private ConnectionHandler.Authorization authorization;
    private boolean notifyProgress = false;
    private Object data;
    private int connectionTimeout = ConnectionConfiguration.getInstance().getTimeoutConnection();
    private int writeTimeout = ConnectionConfiguration.getInstance().getTimeoutWrite();
    private int readTimeout = ConnectionConfiguration.getInstance().getTimeoutRead();
    private boolean trustAllCertificates = ConnectionConfiguration.getInstance().getTrustAllCertificates();
    private final Set<Logger> loggers = new HashSet<>();
    private Map<String, String> headerValues = createDefaultHeaderValues();
    private Map<String, String> queryParams = new HashMap<>();

    public Parameters() {
        // Empty constructor
    }

    public Parameters(Parameters parameters) {
        // Constructor used to create a copy of the parameters
        this.authorization = parameters.authorization;
        this.url = parameters.url;
        this.connectionTimeout = parameters.connectionTimeout;
        this.data = parameters.data;
        this.headerValues = parameters.headerValues;
        this.method = parameters.method;
        this.notifyProgress = parameters.notifyProgress;
        this.readTimeout = parameters.readTimeout;
        this.trustAllCertificates = parameters.trustAllCertificates;
        this.writeTimeout = parameters.writeTimeout;
        this.queryParams = parameters.queryParams;
    }

    protected static Map<String, String> createDefaultHeaderValues() {
        Map<String, String> defaultHeader= new HashMap<>();
        defaultHeader.put("Accept-Language", Locale.getDefault().getLanguage());
        defaultHeader.put("Connection", "keep-alive");
        return defaultHeader;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ConnectionHandler.Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(ConnectionHandler.Authorization authorization) {
        this.authorization = authorization;
    }

    public String getUrl() {
        return url;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public void setNotifyProgress(boolean notifyProgress) {
        this.notifyProgress = notifyProgress;
    }

    public boolean notifyProgress() {
        return this.notifyProgress;
    }

    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setWriteTimeout(int timeout) {
        this.writeTimeout = timeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }

    public boolean getTrustAllCertificates() {
        return this.trustAllCertificates;
    }

    public Map<String, String> getHeaderValues() {
        return headerValues;
    }

    public void setHeaderValues(Map<String, String> otherHeaderValues) {
        this.headerValues = otherHeaderValues;
    }

    public void addHeaderValue(String key, String value) {
        this.headerValues.put(key, value);
    }

    public Set<Logger> getLoggers() {
        return loggers;
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void addQueryParams(String key, String value) {
        this.queryParams.put(key, value);
    }

}