package xu.ferris.azapps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xff on 2017/12/14.
 */

public class AppsGridView extends GridView {
    AppAdapter appAdapter;
    List<GroupMemberBean> groupMemberBeans=new ArrayList<>();
    public AppsGridView(Context context) {
        super(context);
        init();
    }

    public AppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    void init(){
        appAdapter=new AppAdapter();
        setAdapter(appAdapter);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        getLayoutParams().height = getMeasuredHeight();
    }

    public void clear() {
        groupMemberBeans.clear();
    }
    public void add(GroupMemberBean findClassBean) {
        groupMemberBeans.add(findClassBean);
    }

    public void notifyDataChaged() {
        appAdapter.notifyDataSetChanged();
    }

    public class AppAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return groupMemberBeans.size();
        }

        @Override
        public GroupMemberBean getItem(int position) {
            return groupMemberBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            AppViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new AppViewHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.activity_apps_icon_item, null);
                viewHolder.appsGridView = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            } else {
                viewHolder = (AppViewHolder) view.getTag();
            }


            return view;
        }

        final  class AppViewHolder {
            TextView tvLetter;
            ImageView appsGridView;
        }
    }


}
