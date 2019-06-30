package tech.huqi.quicknote.entity;

import android.net.Uri;

/**
 * Created by hzhuqi on 2019/4/14
 */
public class Attachment {
    /**
     * 附件的Uri描述
     */
    private Uri uri;
    /**
     * 附件的路径
     */
    private String path;
    /**
     * 附件类型
     */
    private int type;

    public Attachment() {
    }

    public Attachment(Uri uri, String path, int type) {
        this.uri = uri;
        this.path = path;
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
