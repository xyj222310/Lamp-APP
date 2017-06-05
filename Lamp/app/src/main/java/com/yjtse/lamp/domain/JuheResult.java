package com.yjtse.lamp.domain;


import java.util.List;

/**
 * Created by yjtse on 2017/4/5.
 */

public class JuheResult {
    /**
     * reason : 成功的返回
     * result : {"stat":"1","data":[{"uniquekey":"221ccb7d046000134160a1fb47d6b5f9","title":"甜馨偷用小璐化妆品，难道是知道麻麻的\u201cbaby肌\u201d里有文章？","date":"2017-06-05 00:00","category":null,"author_name":"环球网","url":"http://mini.eastday.com/mobile/170605000001578.html","thumbnail_pic_s":"http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_26_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_22_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_25_mwpm_03200403.jpeg"},{"uniquekey":"409ad8247e639ca1841ca43ede66b459","title":"彩妆｜原来直男最不喜欢的眼妆是这三类！附少女风妆容教学~","date":"2017-06-04 23:52","category":null,"author_name":"toutiao.com","url":"http://mini.eastday.com/mobile/170604235212714.html","thumbnail_pic_s":"http://09.imgmini.eastday.com/mobile/20170604/20170604235212_486bd7ce287f51c3b1a0eae06f0b1dfe_10_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://09.imgmini.eastday.com/mobile/20170604/20170604235212_486bd7ce287f51c3b1a0eae06f0b1dfe_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://09.imgmini.eastday.com/mobile/20170604/20170604235212_486bd7ce287f51c3b1a0eae06f0b1dfe_4_mwpm_03200403.jpeg"}]}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * stat : 1
         * data : [{"uniquekey":"221ccb7d046000134160a1fb47d6b5f9","title":"甜馨偷用小璐化妆品，难道是知道麻麻的\u201cbaby肌\u201d里有文章？","date":"2017-06-05 00:00","category":null,"author_name":"环球网","url":"http://mini.eastday.com/mobile/170605000001578.html","thumbnail_pic_s":"http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_26_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_22_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_25_mwpm_03200403.jpeg"},{"uniquekey":"409ad8247e639ca1841ca43ede66b459","title":"彩妆｜原来直男最不喜欢的眼妆是这三类！附少女风妆容教学~","date":"2017-06-04 23:52","category":null,"author_name":"toutiao.com","url":"http://mini.eastday.com/mobile/170604235212714.html","thumbnail_pic_s":"http://09.imgmini.eastday.com/mobile/20170604/20170604235212_486bd7ce287f51c3b1a0eae06f0b1dfe_10_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://09.imgmini.eastday.com/mobile/20170604/20170604235212_486bd7ce287f51c3b1a0eae06f0b1dfe_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://09.imgmini.eastday.com/mobile/20170604/20170604235212_486bd7ce287f51c3b1a0eae06f0b1dfe_4_mwpm_03200403.jpeg"}]
         */

        private String stat;
        private List<DataBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * uniquekey : 221ccb7d046000134160a1fb47d6b5f9
             * title : 甜馨偷用小璐化妆品，难道是知道麻麻的“baby肌”里有文章？
             * date : 2017-06-05 00:00
             * category : null
             * author_name : 环球网
             * url : http://mini.eastday.com/mobile/170605000001578.html
             * thumbnail_pic_s : http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_26_mwpm_03200403.jpeg
             * thumbnail_pic_s02 : http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_22_mwpm_03200403.jpeg
             * thumbnail_pic_s03 : http://09.imgmini.eastday.com/mobile/20170605/20170605000001_7e3cab902ce7cfe8aedc725d48903786_25_mwpm_03200403.jpeg
             */

            private String uniquekey;
            private String title;
            private String date;
            private Object category;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Object getCategory() {
                return category;
            }

            public void setCategory(Object category) {
                this.category = category;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }
        }
    }
}
