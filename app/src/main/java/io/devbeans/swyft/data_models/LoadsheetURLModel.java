package io.devbeans.swyft.data_models;

public class LoadsheetURLModel {
    private RiderIDModel where;
    private String order;
    private includeModel include;

    public RiderIDModel getWhere() {
        return where;
    }

    public void setWhere(RiderIDModel where) {
        this.where = where;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public includeModel getInclude() {
        return include;
    }

    public void setInclude(includeModel include) {
        this.include = include;
    }
}
