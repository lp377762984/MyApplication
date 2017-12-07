package com.cn.danceland.myapplication.bean;

import java.util.List;

/**
 * Created by shy on 2017/12/7 16:27
 * Email:644563767@qq.com
 * x新闻内容
 */

public class RequestNewsDataBean {

    private boolean success;
    private String errorMsg;
    private Data data;

    @Override
    public String toString() {
        return "RequestNewsDataBean{" +
                "success=" + success +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }


    public class Data {

        private int total;
        private List<Items> items;

        @Override
        public String toString() {
            return "Data{" +
                    "total=" + total +
                    ", items=" + items +
                    '}';
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public List<Items> getItems() {
            return items;
        }


        public class Items {



            private String id;// 主键，自增
            private String img_url;// 缩略图
            private String title;// 新闻的title
            private String news_content;// 新闻内容
            private String url;// 浏览地址
            private String top;// 是否置顶
            private String delete_mark;// 删除标志
            private String end_time;// 显示结束时间
            private String start_time;// 显示开始时间
            private String publish_time;// 发布时间

            @Override
            public String toString() {
                return "Items{" +
                        "id='" + id + '\'' +
                        ", img_url='" + img_url + '\'' +
                        ", title='" + title + '\'' +
                        ", news_content='" + news_content + '\'' +
                        ", url='" + url + '\'' +
                        ", top='" + top + '\'' +
                        ", delete_mark='" + delete_mark + '\'' +
                        ", end_time='" + end_time + '\'' +
                        ", start_time='" + start_time + '\'' +
                        ", publish_time='" + publish_time + '\'' +
                        '}';
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setNews_content(String news_content) {
                this.news_content = news_content;
            }

            public String getNews_content() {
                return news_content;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUrl() {
                return url;
            }

            public void setTop(String top) {
                this.top = top;
            }

            public String getTop() {
                return top;
            }

            public void setDelete_mark(String delete_mark) {
                this.delete_mark = delete_mark;
            }

            public String getDelete_mark() {
                return delete_mark;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setPublish_time(String publish_time) {
                this.publish_time = publish_time;
            }

            public String getPublish_time() {
                return publish_time;
            }
        }
    }
}