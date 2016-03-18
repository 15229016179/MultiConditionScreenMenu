package com.xiaoming.dropdownmenu.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xiaoming.dropdownmenu.R;
import com.xiaoming.dropdownmenu.view.ExpandMenuView.ViewBaseAction;

/**
 * Copyright 2016 CoderDream's Eclipse
 * 
 * All right reserved.
 * 
 * Created on 2016年3月16日 下午6:15:18
 * 
 * Update on 2016年3月16日 下午6:15:18
 * 
 * @author xiaoming
 * 
 * @mail wangfeng.wf@warmdoc.com
 * 
 * @tags An overview of this file: 筛选列表（单列表）
 * 
 */
public class SingleListFilterView extends RelativeLayout implements ViewBaseAction {

	/**
	 * 数据列表
	 */
	private ListView mListView;
	/**
	 * 数据集合，构造时传入
	 */
	private List<String> data;// 显示字段
	/**
	 * 监听实现接口
	 */
	private OnSelectListener mOnSelectListener;
	/**
	 * 显示（选择）的数据
	 */
	private String showText = "";

	/**
	 * 构造一个一个列表的条件选择
	 * 
	 * @param context
	 * @param data
	 *            数据集合
	 * @param show
	 *            默认显示的文本
	 */
	public SingleListFilterView(Context context, List<String> data, String show) {
		super(context);
		this.showText = show;
		this.data = data;
		init(context);
	}

	public SingleListFilterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SingleListFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 控件初始化，构造时调用
	 * 
	 * @param context
	 */
	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_filter_list_single, this, true);
		mListView = (ListView) findViewById(R.id.listView_list);

		// 设置数据
		TextAdapter listViewAdapter = new TextAdapter(context, data, true);
		listViewAdapter.setSelectedPositionNoNotify(0);
		mListView.setAdapter(listViewAdapter);
		listViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			public void onItemClick(View view, int position) {
				if (position < data.size()) {
					showText = data.get(position);
					mOnSelectListener.getValue(showText, position);
				}

			}
		});
	}

	/**
	 * 获得显示（选择）的数据
	 * 
	 * @return 字符串数据
	 */
	public String getShowText() {
		return showText;
	}

	/**
	 * 设置列表点击（选择）监听
	 * 
	 * @param onSelectListener
	 *            实现本类的OnSelectListener监听接口
	 */
	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	/**
	 * @author warmdoc_ANDROID_001 点击（选择）监听接口
	 */
	public interface OnSelectListener {
		/**
		 * 监听实现此方法
		 * 
		 * @param showText
		 *            选择到的数据
		 * @param position
		 *            选择到的位置
		 */
		public void getValue(String showText, int position);
	}

	@Override
	public void hideMenu() {
	}

	@Override
	public void showMenu() {
	}

}
