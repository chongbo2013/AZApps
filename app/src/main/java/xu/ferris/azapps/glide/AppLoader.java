package xu.ferris.azapps.glide;

import android.content.ComponentName;
import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by xff on 2017/8/9.
 */

public class AppLoader implements ModelLoader<ComponentName,InputStream> {
    public AppLoader() {

    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(ComponentName imageFid, int width, int height) {
        return new AppFidFetcher(imageFid);
    }

    // ModelLoader工厂，在向Glide注册自定义ModelLoader时使用到
    public static class Factory implements ModelLoaderFactory<ComponentName, InputStream> {


        @Override
        public ModelLoader<ComponentName, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new AppLoader();
        }

        @Override
        public void teardown() {

        }
    }

}
