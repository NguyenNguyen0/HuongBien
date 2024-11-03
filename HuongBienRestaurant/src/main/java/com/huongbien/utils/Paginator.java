package com.huongbien.utils;

import java.util.List;
import java.util.function.BiFunction;

public class Paginator<T> {
    private final boolean isRollBack;

    private final int itemsPerPage;
    private int totalPages;
    private int totalItems;

    private List<T> data = null;

    private int currentPageIndex;
    private int startIndex;
    private int endIndex;

    private BiFunction<Integer, Integer, List<T>> getPage = null;

    public Paginator(List<T> data, int itemsPerPage, boolean isRollBack) {
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

    public Paginator(BiFunction<Integer, Integer, List<T>> getPage, int totalItems, int itemsPerPage, boolean isRollBack) {
        this.isRollBack = isRollBack;

        this.getPage = getPage;

        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
        this.totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        this.currentPageIndex = 1;
        this.startIndex = 0;
        this.endIndex = Math.min(startIndex + itemsPerPage, totalItems);
    }

    public List<T> getCurrentPage() {
        if (getPage != null) {
            return getPage.apply(startIndex, itemsPerPage);
        }

        return data.subList(startIndex, endIndex);
    }

    public void goToFirstPage() {
        setCurrentPageIndex(1);
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + itemsPerPage, totalItems);
    }

    public void goToPreviousPage() {
        setCurrentPageIndex(getCurrentPageIndex() - 1);
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + itemsPerPage, totalItems);
    }

    public void goToNextPage() {
        setCurrentPageIndex(getCurrentPageIndex() + 1);
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + itemsPerPage, totalItems);
    }

    public void goToLastPage() {
        setCurrentPageIndex(getTotalPages());
        startIndex = (getCurrentPageIndex() - 1) * itemsPerPage;
        endIndex = Math.min(startIndex + getCurrentPageIndex(), totalItems);
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
