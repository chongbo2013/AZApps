package xu.ferris.azapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持2种UI模式切换ferris.xu
 */
public class SortGroupMemberAdapter extends BaseAdapter implements SectionIndexer {

	public boolean oldType=false;
	private List<GroupMemberBean> list = null;
	private Context mContext;

	public SortGroupMemberAdapter(Context mContext, List<GroupMemberBean> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 *
	 * @param list
	 */
	public void updateListView(List<GroupMemberBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		if(oldType){
			return list.size();
		}
		return getSectionsSize();
	}

	public GroupMemberBean getItem(int position) {
		if(oldType) {
			return list.get(position);
		}
		//根据分类首字母获取，第一个分类位置 item
		return list.get(getPositionForSection(integerList.get(position)));
	}

	public int getPositionForSectionNewType(int c) {
		for(int i=0;i<integerList.size();i++){
			char sortStr = integerList.get(i);
			if (sortStr == c) {
				return i;
			}
		}
		return -1;
	}
	public long getItemId(int position) {
		if(oldType) {
			return position;
		}
		//根据位置获取分类的第一个位置
		return getPositionForSection(integerList.get(position));
	}

	public View getView(final int position, View view, ViewGroup arg2) {

		final GroupMemberBean mContent =getItem(position);
		if(oldType) {
			ViewHolder viewHolder = null;
			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(R.layout.activity_apps_member_item, null);
				viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
				viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}

			viewHolder.tvTitle.setText(this.list.get(position).getName());
		}else{

			AppViewHolder viewHolder = null;
			if (view == null) {
				viewHolder = new AppViewHolder();
				view = LayoutInflater.from(mContext).inflate(R.layout.activity_apps_grid_item, null);
				viewHolder.appsGridView = (AppsGridView) view.findViewById(R.id.appgridView);
				viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
				view.setTag(viewHolder);
			} else {
				viewHolder = (AppViewHolder) view.getTag();
			}
			viewHolder.tvLetter.setText(mContent.getSortLetters());
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			//初始化grid
			viewHolder.appsGridView.clear();
			int firstClassPositon=getPositionForSection(integerList.get(position));
			int currentClass=mContent.getSortLetters().toUpperCase().charAt(0);
			for(;firstClassPositon<list.size();firstClassPositon++){
				GroupMemberBean findClassBean=list.get(firstClassPositon);
				String sortStr = findClassBean.getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if(firstChar==currentClass){
					viewHolder.appsGridView.add(findClassBean);
				}
			}
			viewHolder.appsGridView.notifyDataChaged();

		}
		return view;

	}



	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}
	final static class AppViewHolder {
		TextView tvLetter;
		AppsGridView appsGridView;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < list.size(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	public String getSelectTitle(int firstVisibleItem){

		return oldType?list.get(
				getPositionForSection(getSectionForPosition(firstVisibleItem))).getSortLetters():list.get(getPositionForSection(integerList.get(firstVisibleItem))).getSortLetters();

	}

	List<Character> integerList=new ArrayList<>();
	//获取分类标签的总数
	public int getSectionsSize(){
		if(list==null)
			return 0;
		integerList.clear();
		for(int i=0;i<list.size();i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if(!integerList.contains(firstChar))
			integerList.add(firstChar);
		}
		return integerList.size();
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}