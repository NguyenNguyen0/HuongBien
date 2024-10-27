package com.huongbien.utils;

import java.util.List;

public class Paginator<T> {
    private final boolean isRollBack;
    private final int itemsPerPage;
    private int totalPages;
    private int currentPageIndex;
    private List<T> data;

    private int startIndex;
    private int endIndex;

    public Paginator(int itemsPerPage, List<T> data, boolean isRollBack) {
        this.isRollBack = isRollBack;
        this.data = data;
        this.itemsPerPage = itemsPerPage;
        this.currentPageIndex = 1;
        setTotalPage(data.size(), itemsPerPage);

        this.startIndex = currentPageIndex - 1;
        this.endIndex = Math.min(startIndex + itemsPerPage, getData().size());
    }

    public Paginator(List<T> data, boolean isRollBack) {
        this.isRollBack = isRollBack;
        this.data = data;
        this.itemsPerPage = 10;
        this.currentPageIndex = 1;
        setTotalPage(data.size(), itemsPerPage);

        this.startIndex = currentPageIndex - 1;
        this.endIndex = Math.min(startIndex + itemsPerPage, getData().size());
    }

    public List<T> getCurrentPage() {
        return data.subList(startIndex, endIndex);
    }

    public void goToFirstPage() {
        setCurrentPageIndex(1);
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + itemsPerPage, getData().size());
    }

    public void goToPreviousPage() {
        setCurrentPageIndex(getCurrentPageIndex() - 1);
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + itemsPerPage, getData().size());
    }

    public void goToNextPage() {
        setCurrentPageIndex(getCurrentPageIndex() + 1);
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + itemsPerPage, getData().size());
    }

    public void goToLastPage() {
        setCurrentPageIndex(getTotalPages());
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + getCurrentPageIndex(), getData().size());
    }

    private void setTotalPage(int dataSize, int itemsPerPage) {
        this.totalPages = (int) Math.ceil((double) dataSize / itemsPerPage);
    }

    private void setCurrentPageIndex(int nextPageIndex) {
        if (nextPageIndex > totalPages && isRollBack) {
            nextPageIndex = 1;
        } else if (nextPageIndex <= 0 && isRollBack) {
            nextPageIndex = getTotalPages();
        } else if (nextPageIndex <= 0 || nextPageIndex > totalPages) {
            return;
        }

        this.currentPageIndex = nextPageIndex;
    }

    public void setData(List<T> data) {
        this.data = data;
        setTotalPage(data.size(), getItemsPerPage());
        setCurrentPageIndex(1);
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public List<T> getData() {
        return data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
}
