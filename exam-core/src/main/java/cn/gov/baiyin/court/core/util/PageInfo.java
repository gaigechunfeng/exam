package cn.gov.baiyin.court.core.util;

import java.util.List;

/**
 * Created by WK on 2017/3/25.
 */
public class PageInfo {

    private List<?> list;
    private int currPage = 1;
    private int pageSize = 10;
    private int allPage;

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getAllPage() {
        return allPage;
    }

    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }
}
