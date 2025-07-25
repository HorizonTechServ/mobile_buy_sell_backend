package com.one.mobilebuysellAPI.dto;

public class DashboardCountSummaryDto {
    private long totalBuyingCount;
    private long totalSellingCount;

    public DashboardCountSummaryDto(long totalBuyingCount, long totalSellingCount) {
        this.totalBuyingCount = totalBuyingCount;
        this.totalSellingCount = totalSellingCount;
    }

    public long getTotalBuyingCount() {
        return totalBuyingCount;
    }

    public void setTotalBuyingCount(long totalBuyingCount) {
        this.totalBuyingCount = totalBuyingCount;
    }

    public long getTotalSellingCount() {
        return totalSellingCount;
    }

    public void setTotalSellingCount(long totalSellingCount) {
        this.totalSellingCount = totalSellingCount;
    }
}