package com.example.newmusic.model;

/**
 * Created by Administrator on 2016/11/28.
 */
public class RadioModel {

    private NewData data;

    public NewData getData() {
        return data;
    }

    public void setData(NewData data) {
        this.data = data;
    }

    public class NewData{

        private Song song;

        public Song getSong() {
            return song;
        }

        public void setSong(Song song) {
            this.song = song;
        }

        public class Song{

            private java.util.List<List>list;

            public java.util.List<List> getList() {
                return list;
            }

            public void setList(java.util.List<List> list) {
                this.list = list;
            }

            public class List{
                private String f;

                public String getF() {
                    return f;
                }

                public void setF(String f) {
                    this.f = f;
                }
            }

        }
    }
}
