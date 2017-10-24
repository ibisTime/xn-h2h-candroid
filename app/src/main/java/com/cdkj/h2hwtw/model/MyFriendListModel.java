package com.cdkj.h2hwtw.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 李先俊 on 2017/7/20.
 */

public class MyFriendListModel implements Parcelable {


    /**
     * pageNO : 1
     * start : 0
     * pageSize : 1
     * totalCount : 154
     * totalPage : 154
     * list : [{"userId":"U2017062912572008286","loginName":"CD13858208223","nickname":"暖心","loginPwdStrength":"1","kind":"f1","level":"0","userReferee":"SYS_USER_YAOCHENG","mobile":"13858208223","status":"0","createDatetime":"Jun 29, 2017 12:57:20 PM","updater":"U2017062912572008286","updateDatetime":"Jun 29, 2017 12:57:20 PM","companyCode":"CD-CYC000009","openId":"ojdtdv-rkkjB-w3KKpBapHDapBnc","systemCode":"CD-CYC000009","userRefereeName":"yaocheng","userExt":{"userId":"U2017062912572008286","gender":"2","photo":"http://wx.qlogo.cn/mmopen/QT0Ms","systemCode":"CD-CYC000009","loginName":"CD13858208223","mobile":"13858208223","userReferee":"SYS_USER_YAOCHENG"}}]
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * userId : U2017062912572008286
         * loginName : CD13858208223
         * nickname : 暖心
         * loginPwdStrength : 1
         * kind : f1
         * level : 0
         * userReferee : SYS_USER_YAOCHENG
         * mobile : 13858208223
         * status : 0
         * createDatetime : Jun 29, 2017 12:57:20 PM
         * updater : U2017062912572008286
         * updateDatetime : Jun 29, 2017 12:57:20 PM
         * companyCode : CD-CYC000009
         * openId : ojdtdv-rkkjB-w3KKpBapHDapBnc
         * systemCode : CD-CYC000009
         * userRefereeName : yaocheng
         * userExt : {"userId":"U2017062912572008286","gender":"2","photo":"http://wx.qlogo.cn/mmopen/QT0Ms","systemCode":"CD-CYC000009","loginName":"CD13858208223","mobile":"13858208223","userReferee":"SYS_USER_YAOCHENG"}
         */

        private String userId;
        private String loginName;
        private String nickname;
        private String loginPwdStrength;
        private String kind;
        private String level;
        private String userReferee;
        private String mobile;
        private String status;
        private String createDatetime;
        private String updater;
        private String updateDatetime;
        private String companyCode;
        private String openId;
        private String systemCode;
        private String userRefereeName;
        private UserExtBean userExt;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLoginPwdStrength() {
            return loginPwdStrength;
        }

        public void setLoginPwdStrength(String loginPwdStrength) {
            this.loginPwdStrength = loginPwdStrength;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }

        public String getUpdateDatetime() {
            return updateDatetime;
        }

        public void setUpdateDatetime(String updateDatetime) {
            this.updateDatetime = updateDatetime;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getUserRefereeName() {
            return userRefereeName;
        }

        public void setUserRefereeName(String userRefereeName) {
            this.userRefereeName = userRefereeName;
        }

        public UserExtBean getUserExt() {
            return userExt;
        }

        public void setUserExt(UserExtBean userExt) {
            this.userExt = userExt;
        }

        public static class UserExtBean implements Parcelable {
            /**
             * userId : U2017062912572008286
             * gender : 2
             * photo : http://wx.qlogo.cn/mmopen/QT0Ms
             * systemCode : CD-CYC000009
             * loginName : CD13858208223
             * mobile : 13858208223
             * userReferee : SYS_USER_YAOCHENG
             */

            private String userId;
            private String gender;
            private String photo;
            private String systemCode;
            private String loginName;
            private String mobile;
            private String userReferee;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getSystemCode() {
                return systemCode;
            }

            public void setSystemCode(String systemCode) {
                this.systemCode = systemCode;
            }

            public String getLoginName() {
                return loginName;
            }

            public void setLoginName(String loginName) {
                this.loginName = loginName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getUserReferee() {
                return userReferee;
            }

            public void setUserReferee(String userReferee) {
                this.userReferee = userReferee;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.userId);
                dest.writeString(this.gender);
                dest.writeString(this.photo);
                dest.writeString(this.systemCode);
                dest.writeString(this.loginName);
                dest.writeString(this.mobile);
                dest.writeString(this.userReferee);
            }

            public UserExtBean() {
            }

            protected UserExtBean(Parcel in) {
                this.userId = in.readString();
                this.gender = in.readString();
                this.photo = in.readString();
                this.systemCode = in.readString();
                this.loginName = in.readString();
                this.mobile = in.readString();
                this.userReferee = in.readString();
            }

            public static final Creator<UserExtBean> CREATOR = new Creator<UserExtBean>() {
                @Override
                public UserExtBean createFromParcel(Parcel source) {
                    return new UserExtBean(source);
                }

                @Override
                public UserExtBean[] newArray(int size) {
                    return new UserExtBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.userId);
            dest.writeString(this.loginName);
            dest.writeString(this.nickname);
            dest.writeString(this.loginPwdStrength);
            dest.writeString(this.kind);
            dest.writeString(this.level);
            dest.writeString(this.userReferee);
            dest.writeString(this.mobile);
            dest.writeString(this.status);
            dest.writeString(this.createDatetime);
            dest.writeString(this.updater);
            dest.writeString(this.updateDatetime);
            dest.writeString(this.companyCode);
            dest.writeString(this.openId);
            dest.writeString(this.systemCode);
            dest.writeString(this.userRefereeName);
            dest.writeParcelable(this.userExt, flags);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.userId = in.readString();
            this.loginName = in.readString();
            this.nickname = in.readString();
            this.loginPwdStrength = in.readString();
            this.kind = in.readString();
            this.level = in.readString();
            this.userReferee = in.readString();
            this.mobile = in.readString();
            this.status = in.readString();
            this.createDatetime = in.readString();
            this.updater = in.readString();
            this.updateDatetime = in.readString();
            this.companyCode = in.readString();
            this.openId = in.readString();
            this.systemCode = in.readString();
            this.userRefereeName = in.readString();
            this.userExt = in.readParcelable(UserExtBean.class.getClassLoader());
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public MyFriendListModel() {
    }

    protected MyFriendListModel(Parcel in) {
        this.list = in.createTypedArrayList(ListBean.CREATOR);
    }

    public static final Creator<MyFriendListModel> CREATOR = new Creator<MyFriendListModel>() {
        @Override
        public MyFriendListModel createFromParcel(Parcel source) {
            return new MyFriendListModel(source);
        }

        @Override
        public MyFriendListModel[] newArray(int size) {
            return new MyFriendListModel[size];
        }
    };
}
