package xu.ferris.azapps.glide;

import android.content.ComponentName;
import android.content.Context;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;

/**
 * Created by xff on 2017/8/9.
 */

public class GlideUtils {
    DrawableRequestBuilder<ComponentName> mGlideBuilder=null;
    static  GlideUtils glideUtils=null;
    Context context;
    public GlideUtils(Context context){
        this.context=context;
        mGlideBuilder = Glide.with(context)
                .from(ComponentName.class).centerCrop().crossFade();
    }
    public static GlideUtils get(Context context) {
        if (glideUtils == null) {
            synchronized (GlideUtils.class) {
                if (glideUtils == null) {
                    glideUtils = new GlideUtils(context);
                }
            }
        }
        return glideUtils;
    }

    public GenericRequestBuilder getGlide() {
        if(context!=null&&mGlideBuilder==null){
            mGlideBuilder = Glide.with(context)
                    .from(ComponentName.class) // 设置数据源类型为我们的ImageFid
                    .fitCenter().crossFade();
        }
        return mGlideBuilder;
    }
}
