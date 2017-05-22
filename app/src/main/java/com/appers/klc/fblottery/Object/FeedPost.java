package com.appers.klc.fblottery.Object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klc on 2017/4/22.
 */

public class FeedPost {
    public List<Detial> data = new ArrayList<>();
    public Pages paging;

    public List<Detial> getData() {
        return data;
    }

    public void setData(List<Detial> data) {
        this.data = data;
    }

    public Pages getPages() {
        return paging;
    }

    public void setPages(Pages pages) {
        this.paging = pages;
    }
}
