package com.cn.danceland.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shy on 2017/12/28 17:57
 * Email:644563767@qq.com
 *
 */


public class RequestOrderListBean implements Serializable {

    private boolean success;
    private String errorMsg;
    private Data data;

    @Override
    public String toString() {
        return "RequestOrderListBean{" +
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



    public class Data implements Serializable{

        private List<Content> content;
        private int number;
        private int size;
        private int totalElements;
        private int numberOfElements;
        private int totalPages;
        private boolean last;

        @Override
        public String toString() {
            return "Data{" +
                    "content=" + content +
                    ", number=" + number +
                    ", size=" + size +
                    ", totalElements=" + totalElements +
                    ", numberOfElements=" + numberOfElements +
                    ", totalPages=" + totalPages +
                    ", last=" + last +
                    '}';
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

        public List<Content> getContent() {
            return content;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
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

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public boolean getLast() {
            return last;
        }
        public class Content implements Serializable{

            private String id;
            private int bus_type;
            private int for_other;
            private String order_time;
            private String person_id;
            private String member_no;
            private String member_id;
            private String member_name;
            private String branch_id;
            private int status;
            private int pay_way;
            private float price;
            private String extends_info;
            private String order_no;
            private String phone_no;
            private String name;
            private String admin_emp_id;
            private String admin_emp_name;
            private String branch_name;
            private String belong_id;
            private String card_type_id;
            private String deposit_id;
            private String deposit_price;
            private String admin_emp_phone;
            private String chip_number;
            private String open_date;
            private String card_no;

            @Override
            public String toString() {
                return "Content{" +
                        "id='" + id + '\'' +
                        ", bus_type=" + bus_type +
                        ", for_other=" + for_other +
                        ", order_time='" + order_time + '\'' +
                        ", person_id='" + person_id + '\'' +
                        ", member_no='" + member_no + '\'' +
                        ", member_id='" + member_id + '\'' +
                        ", member_name='" + member_name + '\'' +
                        ", branch_id='" + branch_id + '\'' +
                        ", status=" + status +
                        ", pay_way=" + pay_way +
                        ", price=" + price +
                        ", extends_info='" + extends_info + '\'' +
                        ", order_no='" + order_no + '\'' +
                        ", phone_no='" + phone_no + '\'' +
                        ", name='" + name + '\'' +
                        ", admin_emp_id='" + admin_emp_id + '\'' +
                        ", admin_emp_name='" + admin_emp_name + '\'' +
                        ", branch_name='" + branch_name + '\'' +
                        ", belong_id='" + belong_id + '\'' +
                        ", card_type_id='" + card_type_id + '\'' +
                        ", deposit_id='" + deposit_id + '\'' +
                        ", deposit_price='" + deposit_price + '\'' +
                        ", admin_emp_phone='" + admin_emp_phone + '\'' +
                        ", chip_number='" + chip_number + '\'' +
                        ", open_date='" + open_date + '\'' +
                        ", card_no='" + card_no + '\'' +
                        '}';
            }

            public void setBus_type(int bus_type) {
                this.bus_type = bus_type;
            }

            public int getBus_type() {
                return bus_type;
            }

            public void setFor_other(int for_other) {
                this.for_other = for_other;
            }

            public int getFor_other() {
                return for_other;
            }

            public void setOrder_time(String order_time) {
                this.order_time = order_time;
            }

            public String getOrder_time() {
                return order_time;
            }



            public void setMember_no(String member_no) {
                this.member_no = member_no;
            }

            public String getMember_no() {
                return member_no;
            }



            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }

            public String getMember_name() {
                return member_name;
            }


            public void setStatus(int status) {
                this.status = status;
            }

            public int getStatus() {
                return status;
            }

            public void setPay_way(int pay_way) {
                this.pay_way = pay_way;
            }

            public int getPay_way() {
                return pay_way;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPerson_id() {
                return person_id;
            }

            public void setPerson_id(String person_id) {
                this.person_id = person_id;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getBranch_id() {
                return branch_id;
            }

            public void setBranch_id(String branch_id) {
                this.branch_id = branch_id;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public void setExtends_info(String extends_info) {
                this.extends_info = extends_info;
            }

            public String getExtends_info() {
                return extends_info;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setPhone_no(String phone_no) {
                this.phone_no = phone_no;
            }

            public String getPhone_no() {
                return phone_no;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setAdmin_emp_id(String admin_emp_id) {
                this.admin_emp_id = admin_emp_id;
            }

            public String getAdmin_emp_id() {
                return admin_emp_id;
            }

            public void setAdmin_emp_name(String admin_emp_name) {
                this.admin_emp_name = admin_emp_name;
            }

            public String getAdmin_emp_name() {
                return admin_emp_name;
            }

            public void setBranch_name(String branch_name) {
                this.branch_name = branch_name;
            }

            public String getBranch_name() {
                return branch_name;
            }

            public void setBelong_id(String belong_id) {
                this.belong_id = belong_id;
            }

            public String getBelong_id() {
                return belong_id;
            }

            public void setCard_type_id(String card_type_id) {
                this.card_type_id = card_type_id;
            }

            public String getCard_type_id() {
                return card_type_id;
            }

            public void setDeposit_id(String deposit_id) {
                this.deposit_id = deposit_id;
            }

            public String getDeposit_id() {
                return deposit_id;
            }

            public void setDeposit_price(String deposit_price) {
                this.deposit_price = deposit_price;
            }

            public String getDeposit_price() {
                return deposit_price;
            }

            public void setAdmin_emp_phone(String admin_emp_phone) {
                this.admin_emp_phone = admin_emp_phone;
            }

            public String getAdmin_emp_phone() {
                return admin_emp_phone;
            }

            public void setChip_number(String chip_number) {
                this.chip_number = chip_number;
            }

            public String getChip_number() {
                return chip_number;
            }

            public void setOpen_date(String open_date) {
                this.open_date = open_date;
            }

            public String getOpen_date() {
                return open_date;
            }

            public void setCard_no(String card_no) {
                this.card_no = card_no;
            }

            public String getCard_no() {
                return card_no;
            }

        }
    }
}
