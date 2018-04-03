package com.cn.danceland.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feng on 2018/4/2.
 */

public class CourseMemberBean implements Serializable {

    private List<Content> content;
    private boolean last;
    private int number;
    private int numberOfElements;
    private int size;
    private int totalElements;
    private int totalPages;
    public void setContent(List<Content> content) {
        this.content = content;
    }
    public List<Content> getContent() {
        return content;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
    public boolean getLast() {
        return last;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getTotalPages() {
        return totalPages;
    }

    public class Content {

        private int id;
        private String self_avatar_path;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setSelf_avatar_path(String self_avatar_path) {
            this.self_avatar_path = self_avatar_path;
        }
        public String getSelf_avatar_path() {
            return self_avatar_path;
        }

    }
}
