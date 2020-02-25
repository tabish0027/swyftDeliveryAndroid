package io.devbeans.swyft.data_models;

import java.util.List;

public class scopeModel {
    private List<String> fields;
    private innerIncludeModel include;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public innerIncludeModel getInclude() {
        return include;
    }

    public void setInclude(innerIncludeModel include) {
        this.include = include;
    }
}
