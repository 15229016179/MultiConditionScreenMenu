package com.xiaoming.dropdownmenu.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xiaoming.dropdownmenu.R;

/**
 * 	Copyright	2016	CoderDream's Eclipse
 * 
 * 	All right reserved.
 * 	
 * 	Created on 2016年3月16日 下午2:56:34
 * 	
 * 	Update on 2016年3月16日 下午2:56:34
 * 
 * 	@author xiaoming
 *	
 * 	@mail wangfeng.wf@warmdoc.com
 * 
 * 	@tags An overview of this file: 菜单控件头部，封装了下拉列表菜单，头部按钮个数动态添加
 * 
 */
public class ExpandMenuView extends LinearLayout implements OnDismissListener {

	private ToggleButton selectedButton;
	private ArrayList<String> mTitles = new ArrayList<String>();
	private ArrayList<RelativeLayout> mSelectionViews = new ArrayList<RelativeLayout>();
	private ArrayList<ToggleButton> mToggleButton = new ArrayList<ToggleButton>();
	private Context mContext;
	private PopupWindow popupWindow;
	private int selectPosition;
	private int displayWidth;
	private int displayHeight;

	public ExpandMenuView(Context context) {
		super(context);
		initUtil(context);
	}

	public ExpandMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUtil(context);
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void initUtil(Context context) {
		mContext = context;
		displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
		displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
		setOrientation(LinearLayout.HORIZONTAL);
	}

	/**
	 * 设置筛选条件组合按钮初始值
	 * @param titles 筛选条件默认文本
	 * @param selectionViews 筛选列表视图
	 */
	public void setValue(ArrayList<String> titles, ArrayList<View> selectionViews) {
		if (mContext == null) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(mContext);

		mTitles = titles;
		for (int i = 0; i < selectionViews.size(); i++) {
			// 设置popupWindow的跟布局
			final RelativeLayout popRootLayout = new RelativeLayout(mContext);
			int maxHeight = (int) (displayHeight * 0.5);
			RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
			popRootLayout.addView(selectionViews.get(i), rl);
			mSelectionViews.add(popRootLayout);
			ToggleButton toggle = (ToggleButton) inflater.inflate(R.layout.view_toggle_button, this, false);
			addView(toggle);
			View line = new TextView(mContext);
			line.setBackgroundResource(R.color.them_brown);
			if (i < selectionViews.size() - 1) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1, 50);
				addView(line, lp);
			}
			mToggleButton.add(toggle);
			toggle.setTag(i);

			toggle.setText(mTitles.get(i));

			popRootLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					closeView();
				}
			});

			popRootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.transparency));
			toggle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					ToggleButton tButton = (ToggleButton) view;

					if (selectedButton != null && selectedButton != tButton) {
						selectedButton.setChecked(false);
					}
					selectedButton = tButton;
					selectPosition = (Integer) selectedButton.getTag();
					if (mOnButtonClickListener != null) {
						mOnButtonClickListener.onClick(selectPosition, selectedButton.isChecked());
					}
					startAnimation();
				}
			});
		}
	}

	/**
	 * 初始化popupWindow
	 */
	private void startAnimation() {

		if (popupWindow == null) {
			popupWindow = new PopupWindow(mSelectionViews.get(selectPosition), displayWidth, LayoutParams.WRAP_CONTENT);
			popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
			popupWindow.setFocusable(false);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparency)));
		}

		if (selectedButton.isChecked()) {
			if (!popupWindow.isShowing()) {
				showPopup(selectPosition);
			} else {
				popupWindow.setOnDismissListener(this);
				popupWindow.dismiss();
				hideView();
			}
		} else {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
				hideView();
			}
		}
	}

	/**
	 * 显示popupWindow
	 * @param position
	 */
	private void showPopup(int position) {
		View tView = mSelectionViews.get(selectPosition).getChildAt(0);
		if (tView instanceof ViewBaseAction) {
			ViewBaseAction f = (ViewBaseAction) tView;
			f.showMenu();
		}
		if (popupWindow.getContentView() != mSelectionViews.get(position)) {
			popupWindow.setContentView(mSelectionViews.get(position));
		}
		popupWindow.showAsDropDown(this, displayWidth, 1);
	}

	/**
	 * 关闭选择菜单视图
	 * @return
	 */
	public boolean closeView() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			hideView();
			if (selectedButton != null) {
				selectedButton.setChecked(false);
			}
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 隐藏根布局
	 */
	private void hideView() {
		View tView = mSelectionViews.get(selectPosition).getChildAt(0);
		if (tView instanceof ViewBaseAction) {
			ViewBaseAction f = (ViewBaseAction) tView;
			f.hideMenu();
		}
	}
	
	/**
	 * 将选中的条件显示在title上
	 * @param valueText
	 * @param position 在组合控件上的位置
	 */
	public void setTitle(String valueText, int position) {
		if (position < mToggleButton.size()) {
			mToggleButton.get(position).setText(valueText);
		}
	}

	/**
	 * 获取title当前显示的条件
	 */
	public String getTitle(int position) {
		if (position < mToggleButton.size() && mToggleButton.get(position).getText() != null) {
			return mToggleButton.get(position).getText().toString();
		}
		return "";
	}

	@Override
	public void onDismiss() {
		showPopup(selectPosition);
		closeView();
		popupWindow.setOnDismissListener(null);
	}

	private OnButtonClickListener mOnButtonClickListener;

	/**
	 * 设置tabItem的点击监听事件
	 */
	public void setOnButtonClickListener(OnButtonClickListener l) {
		mOnButtonClickListener = l;
	}

	/**
	 * 自定义tabitem点击回调接口
	 */
	public interface OnButtonClickListener {
		public void onClick(int selectPosition, boolean isChecked);
	}

	interface ViewBaseAction {

		/**
		 * 菜单隐藏操作
		 */
		public void hideMenu();

		/**
		 * 菜单显示操作
		 */
		public void showMenu();
	}

}
