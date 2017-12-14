package xu.ferris.azapps.glide;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by xff on 2017/8/9.
 */

public class VideoLoader implements ModelLoader<VideoFid,InputStream> {
    public VideoLoader() {

    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(VideoFid imageFid, int width, int height) {
        return new VideoFidFetcher(imageFid,width,height);
    }

    // ModelLoader工厂，在向Glide注册自定义ModelLoader时使用到
    public static class Factory implements ModelLoaderFactory<VideoFid, InputStream> {


        @Override
        public ModelLoader<VideoFid, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new VideoLoader();
        }

        @Override
        public void teardown() {

        }
    }

}
