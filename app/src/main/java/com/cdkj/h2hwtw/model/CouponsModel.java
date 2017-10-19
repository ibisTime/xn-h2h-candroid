package com.cdkj.h2hwtw.model;

import java.math.BigDecimal;

/**
 * Created by 李先俊 on 2017/10/19.
 */

public class CouponsModel {
    /**
     * code : CP2017102841022241154
     * companyCode : CD-WTW000016
     * endDatetime : Sep 11, 2017 10:11:11 AM
     * parValue : 200
     * releaseDatetime : Oct 11, 2017 10:22:24 AM
     * releaser : admin
     * startDatetime : Sep 10, 2017 10:11:11 AM
     * status : 2
     * systemCode : CD-WTW000016
     * toUser : SYS_USER_WTW
     * updateDatetime : Oct 11, 2017 10:24:59 AM
     * updater : admin
     * useRange : 1
     * user : {"identityFlag":"0","kind":"P","level":"0","loginName":"admin","userId":"SYS_USER_WTW"}
     */

    private String code;
    private String companyCode;
    private String endDatetime;
    private BigDecimal parValue;
    private String releaseDatetime;
    private String releaser;
    private String startDatetime;
    private String status;
    private String systemCode;
    private String toUser;
    private String updateDatetime;
    private String updater;
    private String useRange;
    private UserBean user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

    public String getReleaseDatetime() {
        return releaseDatetime;
    }

    public void setReleaseDatetime(String releaseDatetime) {
        this.releaseDatetime = releaseDatetime;
    }

    public String getReleaser() {
        return releaser;
    }

    public void setReleaser(String releaser) {
        this.releaser = releaser;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * identityFlag : 0
         * kind : P
         * level : 0
         * loginName : admin
         * userId : SYS_USER_WTW
         */

        private String identityFlag;
        private String kind;
        private String level;
        private String loginName;
        private String userId;

        public String getIdentityFlag() {
            return identityFlag;
        }

        public void setIdentityFlag(String identityFlag) {
            this.identityFlag = identityFlag;
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

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
