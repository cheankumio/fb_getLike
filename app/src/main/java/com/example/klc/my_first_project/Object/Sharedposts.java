package com.example.klc.my_first_project.Object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klc on 2017/4/23.
 */

public class Sharedposts {
    public List<Detial> data = new ArrayList<>();
    public Pages paging;

    public List<Detial> getData() {
        return data;
    }

    public void setData(List<Detial> data) {
        this.data = data;
    }

    public Pages getPaging() {
        return paging;
    }

    public void setPaging(Pages paging) {
        this.paging = paging;
    }
}
