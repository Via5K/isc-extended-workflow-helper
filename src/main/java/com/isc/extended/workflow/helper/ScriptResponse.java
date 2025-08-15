package com.isc.extended.workflow.helper;

import java.time.Instant;

public class ScriptResponse {
    private Instant timestamp;
    private Object data;
    private int status;
    private String error;
    private String language;

    public ScriptResponse() {}

    public ScriptResponse(Instant timestamp, Object data, int status, String error, String language) {
        this.timestamp = timestamp;
        this.data = data;
        this.status = status;
        this.error = error;
        this.language = language;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
