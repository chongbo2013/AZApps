package xu.ferris.azapps.glide;

import java.io.Serializable;

/**
 * Created by xff on 2017/8/9.
 */

public class VideoFid  implements Serializable {
    public String file;
    public long time=0;
    public int type=0;//0获取缩略图，1根据时间获取图片 2
    public int width=0;
    public int height=0;
    public String key="";
    public long videoTime=0;
    public VideoFid(String fid) {
      this(fid,0);
    }
    public VideoFid(String file, long time) {
        this(file,time,0,0);
    }
    public VideoFid(String file, long time, int width, int height) {
        this.file = file;
        this.time=time;
        this.width=width;
        this.height=height;
        key=file+"?"+ String.valueOf(time);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VideoFid imageFid = (VideoFid) o;
        return key.equals(imageFid.key);

    }
    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
