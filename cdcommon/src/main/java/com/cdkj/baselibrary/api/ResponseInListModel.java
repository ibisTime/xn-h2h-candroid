package com.cdkj.baselibrary.api;

import java.util.List;

/**包含对象里的list
 * Created by cdkj on 2017/6/8.
 */

public class ResponseInListModel<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
