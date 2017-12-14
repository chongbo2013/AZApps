package xu.ferris.azapps.glide;

import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import xu.ferris.azapps.AppsActivity;

/**
 * Created by xff on 2015/9/19.
 */
public class AppFidFetcher implements DataFetcher<InputStream> {
    // 检查是否取消任务的标识
    private volatile boolean mIsCanceled;
    private final ComponentName packageName;
    private InputStream mInputStream;


    public AppFidFetcher(ComponentName packageName) {
        this.packageName = packageName;

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
        if (packageName==null)
            return null;

        if (mIsCanceled) {
            return null;
        }
        return fetchStream();
    }


    private InputStream fetchStream() {
        Bitmap bitmap = null;
        try {
            if (mIsCanceled) {
                return null;
            }
            if (packageName!=null) {
                PackageManager pkm = AppsActivity.appsActivity.getPackageManager();
                Drawable mAppicon = pkm.getActivityInfo(packageName,
                        ActivityInfo.FLAG_STATE_NOT_NEEDED).loadIcon(pkm);

                if(mAppicon!=null&&mAppicon instanceof BitmapDrawable){
                    bitmap=((BitmapDrawable)mAppicon).getBitmap();
                }
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
        return packageName.toString();
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