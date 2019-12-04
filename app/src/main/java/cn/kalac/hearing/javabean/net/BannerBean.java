package cn.kalac.hearing.javabean.net;

import java.util.List;

public class BannerBean {

    /**
     * banners : [{"imageUrl":"http://p1.music.126.net/VMFTMzwWLTUCQZfEousTIw==/109951163889895561.jpg","targetId":34613413,"adid":null,"targetType":10,"titleColor":"red","typeTitle":"VIP专享","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"34613413","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/i1mBxzhE2RiqTCpGvFFk3w==/109951163889780725.jpg","targetId":2289763322,"adid":null,"targetType":1000,"titleColor":"red","typeTitle":"独家","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"2289763322","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/v0WGNZ8_Z-we6U8yT8fRuQ==/109951163888621304.jpg","targetId":75696264,"adid":null,"targetType":10,"titleColor":"red","typeTitle":"独家","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"75696264","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/LuhaBXyhTYWq4xz_LWnTfQ==/109951163888651190.jpg","targetId":1347687051,"adid":null,"targetType":1,"titleColor":"red","typeTitle":"独家","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"1347687051","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/yLHxReJqFzvOAI-_Up6cnQ==/109951163888744059.jpg","targetId":10851726,"adid":null,"targetType":1004,"titleColor":"red","typeTitle":"独家自制","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"10851726","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/4ryK5WfBUxjrs-sqWbuTYQ==/109951163889896190.jpg","targetId":75522629,"adid":null,"targetType":10,"titleColor":"red","typeTitle":"VIP专享","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"75522629","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/UPPQ1ZXyiTCLZxMuLK4irw==/109951163888697075.jpg","targetId":1348413562,"adid":null,"targetType":1,"titleColor":"red","typeTitle":"独家首发","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"1348413562","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/EXFhJalu-3pMoBMFq_ROjg==/109951163888720130.jpg","targetId":0,"adid":null,"targetType":3000,"titleColor":"blue","typeTitle":"商城","url":"https://music.163.com/m/at/hryxfxyl","exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"0","program":null,"event":null,"video":null,"song":null},{"imageUrl":"http://p1.music.126.net/Ome0TrBKgTw6DTGqgv7T3Q==/109951163888702175.jpg","targetId":2059716995,"adid":null,"targetType":1001,"titleColor":"red","typeTitle":"独家自制","url":null,"exclusive":false,"monitorImpress":null,"monitorClick":null,"monitorType":null,"monitorImpressList":null,"monitorClickList":null,"monitorBlackList":null,"extMonitor":null,"extMonitorInfo":null,"adSource":null,"adLocation":null,"encodeId":"2059716995","program":null,"event":null,"video":null,"song":null}]
     * code : 200
     */

    private int code;
    private List<BannersBean> banners;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public static class BannersBean {
        /**
         * imageUrl : http://p1.music.126.net/VMFTMzwWLTUCQZfEousTIw==/109951163889895561.jpg
         * targetId : 34613413
         * adid : null
         * targetType : 10
         * titleColor : red
         * typeTitle : VIP专享
         * url : null
         * exclusive : false
         * monitorImpress : null
         * monitorClick : null
         * monitorType : null
         * monitorImpressList : null
         * monitorClickList : null
         * monitorBlackList : null
         * extMonitor : null
         * extMonitorInfo : null
         * adSource : null
         * adLocation : null
         * encodeId : 34613413
         * program : null
         * event : null
         * video : null
         * song : null
         */

        private String imageUrl;
        private long targetId;
        private Object adid;
        private int targetType;
        private String titleColor;
        private String typeTitle;
        private Object url;
        private boolean exclusive;
        private Object monitorImpress;
        private Object monitorClick;
        private Object monitorType;
        private Object monitorImpressList;
        private Object monitorClickList;
        private Object monitorBlackList;
        private Object extMonitor;
        private Object extMonitorInfo;
        private Object adSource;
        private Object adLocation;
        private String encodeId;
        private Object program;
        private Object event;
        private Object video;
        private Object song;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public long getTargetId() {
            return targetId;
        }

        public void setTargetId(long targetId) {
            this.targetId = targetId;
        }

        public Object getAdid() {
            return adid;
        }

        public void setAdid(Object adid) {
            this.adid = adid;
        }

        public int getTargetType() {
            return targetType;
        }

        public void setTargetType(int targetType) {
            this.targetType = targetType;
        }

        public String getTitleColor() {
            return titleColor;
        }

        public void setTitleColor(String titleColor) {
            this.titleColor = titleColor;
        }

        public String getTypeTitle() {
            return typeTitle;
        }

        public void setTypeTitle(String typeTitle) {
            this.typeTitle = typeTitle;
        }

        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public boolean isExclusive() {
            return exclusive;
        }

        public void setExclusive(boolean exclusive) {
            this.exclusive = exclusive;
        }

        public Object getMonitorImpress() {
            return monitorImpress;
        }

        public void setMonitorImpress(Object monitorImpress) {
            this.monitorImpress = monitorImpress;
        }

        public Object getMonitorClick() {
            return monitorClick;
        }

        public void setMonitorClick(Object monitorClick) {
            this.monitorClick = monitorClick;
        }

        public Object getMonitorType() {
            return monitorType;
        }

        public void setMonitorType(Object monitorType) {
            this.monitorType = monitorType;
        }

        public Object getMonitorImpressList() {
            return monitorImpressList;
        }

        public void setMonitorImpressList(Object monitorImpressList) {
            this.monitorImpressList = monitorImpressList;
        }

        public Object getMonitorClickList() {
            return monitorClickList;
        }

        public void setMonitorClickList(Object monitorClickList) {
            this.monitorClickList = monitorClickList;
        }

        public Object getMonitorBlackList() {
            return monitorBlackList;
        }

        public void setMonitorBlackList(Object monitorBlackList) {
            this.monitorBlackList = monitorBlackList;
        }

        public Object getExtMonitor() {
            return extMonitor;
        }

        public void setExtMonitor(Object extMonitor) {
            this.extMonitor = extMonitor;
        }

        public Object getExtMonitorInfo() {
            return extMonitorInfo;
        }

        public void setExtMonitorInfo(Object extMonitorInfo) {
            this.extMonitorInfo = extMonitorInfo;
        }

        public Object getAdSource() {
            return adSource;
        }

        public void setAdSource(Object adSource) {
            this.adSource = adSource;
        }

        public Object getAdLocation() {
            return adLocation;
        }

        public void setAdLocation(Object adLocation) {
            this.adLocation = adLocation;
        }

        public String getEncodeId() {
            return encodeId;
        }

        public void setEncodeId(String encodeId) {
            this.encodeId = encodeId;
        }

        public Object getProgram() {
            return program;
        }

        public void setProgram(Object program) {
            this.program = program;
        }

        public Object getEvent() {
            return event;
        }

        public void setEvent(Object event) {
            this.event = event;
        }

        public Object getVideo() {
            return video;
        }

        public void setVideo(Object video) {
            this.video = video;
        }

        public Object getSong() {
            return song;
        }

        public void setSong(Object song) {
            this.song = song;
        }
    }
}
