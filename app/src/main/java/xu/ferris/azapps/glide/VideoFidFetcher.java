package xu.ferris.azapps.glide;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xff on 2015/9/19.
 */
public class VideoFidFetcher implements DataFetcher<InputStream> {
    // 检查是否取消任务的标识
    private volatile boolean mIsCanceled;
    private final VideoFid mImageFid;
    private InputStream mInputStream;


    public VideoFidFetcher(VideoFid imageFid, int width, int height) {
        this.mImageFid = imageFid;

    }

    /**
     * 在后台线程中调用，用于获取图片的数据流，给Glide处理
     *
     * @param priority
     * @return
     * @throws Exception
     */
    @Override
    public InputStream loadData(Priority priority) throws Exception {
        if(mImageFid==null)
            return null;
        if (mImageFid.file == null) {
            if (mIsCanceled) {
                return null;
            }
        }
        if (mIsCanceled) {
            return null;
        }
        // 从视频文件获取缩略图
        return fetchStream(mImageFid.type,mImageFid.file,mImageFid.time,mImageFid.width,mImageFid.height);
    }


    private InputStream fetchStream(int type, String url, long time, int width, int height) {
        // 缓存请求，用来及时取消连接

            Bitmap bitmap = null;
            try {
                Thread thread = Thread.currentThread();
                String name = thread.getName();
                Log.d("thread_test", "thread:" + name);
                File videoFile = new File(url);
                if (mIsCanceled) {
                    return null;
                }
                if (videoFile.exists() && videoFile.isFile()) {
//                    synchronized (object) {
                        if (type == 0) {//获取缩略图
                            bitmap = createVideoThumbnail(mImageFid,videoFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        } else if (type == 1) {//根据时间获取缩略图
                            bitmap = createVideoThumbnailByTime(mImageFid,videoFile.getAbsolutePath(), time, MediaStore.Video.Thumbnails.MICRO_KIND);
                        } else {//根据时间获取缩略图，并且裁剪宽高
                            bitmap = createVideoThumbnailByTimeWithSize(mImageFid,videoFile.getAbsolutePath(), time, width, height);
                        }
//                    }
                    if (bitmap != null && !bitmap.isRecycled()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        mInputStream = new ByteArrayInputStream(baos.toByteArray());

                        return mInputStream;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }

        return null;
    }

    public static Object object=new Object();

    public static Bitmap createVideoThumbnail(VideoFid mImageFid, String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);

            //获取时间

            String totalTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(mImageFid!=null&&!TextUtils.isEmpty(totalTime)){
                mImageFid.videoTime= Long.parseLong(totalTime);
            }

        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    120,
                    120,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
    public   static Bitmap createVideoThumbnailByTime(VideoFid mImageFid, String filePath, long time, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            String totalTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(mImageFid!=null&&!TextUtils.isEmpty(totalTime)){
                mImageFid.videoTime= Long.parseLong(totalTime);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (bitmap == null) return null;

        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    120,
                    120,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public  static Bitmap createVideoThumbnailByTimeWithSize(VideoFid mImageFid, String filePath, long time, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            String totalTime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if(mImageFid!=null&&!TextUtils.isEmpty(totalTime)){
                mImageFid.videoTime= Long.parseLong(totalTime);
            }
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (bitmap == null) return null;
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    width,
                    height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }

    @Override
    public void cleanup() {
        System.out.print("cleanup");
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                //e.printStackTrace();
            } finally {
                mInputStream = null;
            }
        }
    }


    @Override
    public String getId() {
        return mImageFid==null?"":mImageFid.key;
    }

    /**
     * 在UI线程中调用，取消加载任务
     */
    @Override
    public void cancel() {
        mIsCanceled = true;
        //取消视频获取图片任务
    }
}