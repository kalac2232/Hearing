package cn.kalac.hearing.javabean.net;

import java.util.List;

public class SearchHotWordBean {


    /**
     * code : 200
     * result : {"hots":[{"first":"吴青峰新歌","second":1,"third":null,"iconType":1},{"first":"夏目友人帐","second":1,"third":null,"iconType":0},{"first":"触及真心OST","second":1,"third":null,"iconType":0},{"first":"东方风云榜投票","second":1,"third":null,"iconType":0},{"first":"起风了","second":1,"third":null,"iconType":0},{"first":"阿丽塔","second":1,"third":null,"iconType":0},{"first":"A妹新专辑","second":1,"third":null,"iconType":0},{"first":"流浪地球","second":1,"third":null,"iconType":0},{"first":"远东韵律Vava合作新歌","second":1,"third":null,"iconType":0},{"first":"ONE OK ROCK新歌","second":1,"third":null,"iconType":0}]}
     */

    private ResultBean result;


    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<HotsBean> hots;

        public List<HotsBean> getHots() {
            return hots;
        }

        public void setHots(List<HotsBean> hots) {
            this.hots = hots;
        }

        public static class HotsBean {
            /**
             * first : 吴青峰新歌
             * second : 1
             * third : null
             * iconType : 1
             */

            private String first;
            private int second;
            private Object third;
            private int iconType;

            public String getFirst() {
                return first;
            }

            public void setFirst(String first) {
                this.first = first;
            }

            public int getSecond() {
                return second;
            }

            public void setSecond(int second) {
                this.second = second;
            }

            public Object getThird() {
                return third;
            }

            public void setThird(Object third) {
                this.third = third;
            }

            public int getIconType() {
                return iconType;
            }

            public void setIconType(int iconType) {
                this.iconType = iconType;
            }
        }
    }
}
