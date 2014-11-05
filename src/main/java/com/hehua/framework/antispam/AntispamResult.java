package com.hehua.framework.antispam;

import java.util.ArrayList;
import java.util.List;

public class AntispamResult {

    private List<AntispamException> exceptions;

    private String originText;

    private String safeText;

    private String highlightText;

    /**
     * @param originText
     */
    public AntispamResult(String originText) {
        super();
        this.originText = originText;
        this.exceptions = new ArrayList<>();
    }

    public List<AntispamException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<AntispamException> exceptions) {
        this.exceptions = exceptions;
    }

    public void addException(AntispamException exception) {
        this.exceptions.add(exception);
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public String getSafeText() {
        return safeText;
    }

    public void setSafeText(String safeText) {
        this.safeText = safeText;
    }

    public boolean isSafe() {
        return exceptions == null || exceptions.isEmpty();
    }

    public String getHighlightText() {
        return highlightText;
    }

    public void setHighlightText(String highlightText) {
        this.highlightText = highlightText;
    }

    @Override
    public String toString() {
        return "AntispamResult [exceptions=" + exceptions + ", originText=" + originText
                + ", safeText=" + safeText + ", highlightText=" + highlightText + "]";
    }

}
