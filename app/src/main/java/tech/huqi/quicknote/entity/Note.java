package tech.huqi.quicknote.entity;


import android.os.Parcel;
import android.os.Parcelable;

import tech.huqi.quicknote.config.Constants;
import tech.huqi.quicknote.util.TimeUtil;

/**
 * Created by hzhuqi on 2019/4/7
 */
public class Note implements Parcelable, Comparable<Note> {
    private int id;
    private String title;
    private String content;
    private String date;
    private String address;
    private long timestamp;
    private long lastModify;
    private int isWasted;

    public Note() {
        date = TimeUtil.getDataTime(Constants.TIME_FORMAT_Y_M_D_H_M);
        timestamp = System.currentTimeMillis();
        isWasted = 0;
    }

    public Note(String title, String content, String date, String address, long lastModify,
                int isWasted) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.address = address;
        this.lastModify = lastModify;
        this.isWasted = isWasted;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        date = in.readString();
        address = in.readString();
        timestamp = in.readLong();
        lastModify = in.readLong();
        isWasted = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    public int getIsWasted() {
        return isWasted;
    }

    public void setWasted(int wasted) {
        isWasted = wasted;
    }

    public boolean isWasted() {
        return isWasted == 1;
    }

    /**
     * 将字符串文本解析为Note格式对象
     *
     * @return
     */
    public static Note paraseString2Note() {
        Note note = null;
        return note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(address);
        dest.writeLong(lastModify);
        dest.writeLong(timestamp);
        dest.writeInt(isWasted);
    }

    @Override
    public int compareTo(Note o) {
        return (int) (this.timestamp - o.timestamp);
    }
}
