package com.wind.common.entity;

/**
 * @author: HuangYongJie
 * @version: v1.0
 * @since: 2020/1/9 13:51
 **/
public class Pages extends Operator {
    public static final int PAGESIZE_DEFAULT = 10;
    public static final int DEFAULT_ORDER_NO = 1;
    public static final int DEFAULT_ORDER_YES = 2;
    public static final String ORDER_DIRECTION_ASC = "ASC";
    public static final String ORDER_DIRECTION_DESC = "DESC";
    private String targetType;
    private Integer pageIndex = 1;
    private Integer pageSize = 50;
    private Integer totalCount = 0;
    private Integer pagesNumber;
    private String orderField;
    private String orderDirection;
    private Integer defaultOrder = 2;
    private Integer startIndex = 0;
    private Integer unfold = 1;

    public Pages() {
    }

    public String getTargetType() {
        return this.targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Integer getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        this.pageSize = this.pageSize > 0 ? this.pageSize : 10;
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPagesNumber() {
        return this.pagesNumber;
    }

    public void setPagesNumber(Integer pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public String getOrderField() {
        return this.orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return this.orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public Integer getDefaultOrder() {
        return this.defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public int getStartIndex() {
        int tmpPageIndex = this.getPageIndex() > 0 ? this.getPageIndex() - 1 : 0;
        this.startIndex = tmpPageIndex * this.getPageSize();
        return this.startIndex;
    }

    public Integer getUnfold() {
        return this.unfold;
    }

    public void setUnfold(Integer unfold) {
        this.unfold = unfold;
    }
}
