package com.isc.extended.workflow.helper;

public class ScriptRequest {
    private String language;
    private String script;

    public ScriptRequest() {}

    public ScriptRequest(String language, String script) {
        this.language = language;
        this.script = script;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
