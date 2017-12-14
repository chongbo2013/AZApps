package xu.ferris.azapps.glide;

import android.content.ComponentName;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by x002 on 2016/12/3.
 */

public class VideoGlideModule implements GlideModule {
//    public static final int yourSizeInBytes_disk=1024*1024*50;//默认50M本地缓存
    public static final int yourSizeInBytes_memory=1024*1024*20;//默认15M内存
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

//        builder.setDiskCache(new DiskLruCacheFactory(new DiskLruCacheFactory.CacheDirectoryGetter() {
//            @Override public File getCacheDirectory() {
//                return new File(IMAGE_CACHE);
//            }
//        }, yourSizeInBytes_disk));
        //图片缓存
        builder.setMemoryCache(new LruResourceCache(yourSizeInBytes_memory));
    }



    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(VideoFid.class, InputStream.class, new VideoLoader.Factory());
        glide.register(ComponentName.class, InputStream.class, new AppLoader.Factory());
    }
}