package com.yjtse.lamp.read;


import com.yjtse.lamp.domain.BaseModel;

/**
 * Created by _SOLID
 * GitHub:https://github.com/burgessjp
 * Date:2017/3/18
 * Time:15:35
 * Desc:
 */

public class XianDuCategory extends BaseModel {
    private String title;
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
