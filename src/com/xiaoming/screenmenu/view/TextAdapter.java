package com.xiaoming.screenmenu.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiaoming.screenmenu.R;

/**
 * 	Copyright	2016	CoderDream's Eclipse
 * 
 * 	All right reserved.
 * 	
 * 	Created on 2016年3月16日 下午5:42:57
 * 	
 * 	Update on 2016年3月16日 下午5:42:57
 * 
 * 	@author xiaoming
 *	
 * 	@mail wangfeng.wf@warmdoc.com
 * 
 * 	@tags An overview of this file: 列表条件筛选数据适配器
 * 
 */
public class TextAdapter extends ArrayAdapter<String> {

	private Context mContext;
	/**
	 * 列表数据
	 */
	private List<String> mListData;
	/**
	 * 选中的位置
	 */
	private int selectedPos = -1;
	/**
	 * 是否显示选中指示
	 */
	private boolean mIsSelectedShow;
	/**
	 * 点击监听
	 */
	private OnClickListener onClickListener;
	/**
	 * item的点击监听
	 */
	private OnItemClickListener mOnItemClickListener;

	/**
	 * 构造一个医生列表条件筛选列表数据适配器
	 * 
	 * @param context
	 * @param listData
	 *            传入列表数据
	 * @param isSelectedShow 是否显示选中指示
	 */
	public TextAdapter(Context context, List<String> listData, boolean isSelectedShow) {
		super(context, 0, listData);
		mContext = context;
		mListData = listData;
		mIsSelectedShow = isSelectedShow;
		init();
	}

	/**
	 * 初始化 构造时调用
	 */
	private void init() {
		onClickListener = new OnClickListener() {

			public void onClick(View view) {
				if (selectedPos != -1) {
					selectedPos = (Integer) view.getTag();
					setSelectedPosition(selectedPos);
				}

				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
				}
			}
		};
	}

	/**
	 * 设置选中的position,并通知列表刷新
	 */
	public void setSelectedPosition(int pos) {
		if (mListData != null && pos < mListData.size()) {
			selectedPos = pos;
			notifyDataSetChanged();
		}
	}

	/**
	 * 设置选中的position,但不通知刷新
	 */
	public void setSelectedPositionNoNotify(int pos) {
		selectedPos = pos;
	}

	/**
	 * 获取选中的position
	 */
	public int getSelectedPosition() {
		if (mListData != null && selectedPos < mListData.size()) {
			return selectedPos;
		}

		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_content, parent, false);
		}
		textView = (TextView) convertView.findViewById(R.id.item_textView_popItem);

		convertView.setTag(position);
		String mString = "";
		if (mListData != null) {
			if (position < mListData.size()) {
				mString = mListData.get(position);

			}
		}
		textView.setText(mString);
		convertView.setOnClickListener(onClickListener);

		if (mIsSelectedShow) {// 设置点击选择后改变背景效果
			convertView.setBackgroundColor(Color.WHITE);
			if (position == selectedPos) {
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.selection_gray));
			}
		}

		return convertView;
	}

	/**
	 * 设置item点击监听
	 * 
	 * @param l
	 *            实现本类的OnItemClickListener接口
	 */
	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	/**
	 * 重新定义菜单选项单击接口
	 */
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

}
