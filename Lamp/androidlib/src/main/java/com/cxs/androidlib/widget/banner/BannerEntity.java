package com.cxs.androidlib.widget.banner;

/**
 * Created by _CXS on 16/8/24 21:37
 * 100332338@qq.com
 * <p>
 * Banner模型类
 */
public class BannerEntity
{
    public BannerEntity(){

    }

    public BannerEntity(String link, String title, String img)
    {

        this.link = link;
        this.title = title;
        this.img = img;
    }

    public String title;


    public String img;


    public String link;

    @Override
    public String toString() {
        return "BannerEntity{" +
                "title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
