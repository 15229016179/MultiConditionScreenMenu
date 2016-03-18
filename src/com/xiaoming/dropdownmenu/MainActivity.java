package com.xiaoming.dropdownmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xiaoming.dropdownmenu.entity.City;
import com.xiaoming.dropdownmenu.entity.Grad;
import com.xiaoming.dropdownmenu.entity.Item;
import com.xiaoming.dropdownmenu.entity.Province;
import com.xiaoming.dropdownmenu.util.SortAlgorithm;
import com.xiaoming.dropdownmenu.view.DoubleListFilterView;
import com.xiaoming.dropdownmenu.view.ExpandMenuView;
import com.xiaoming.dropdownmenu.view.ExpandMenuView.OnButtonClickListener;
import com.xiaoming.dropdownmenu.view.SingleListFilterView;

/**
 * 	Copyright	2016	CoderDream's Eclipse
 * 
 * 	All right reserved.
 * 	
 * 	Created on 2016年3月17日 下午4:59:44
 * 	
 * 	Update on 2016年3月17日 下午4:59:44
 * 
 * 	@author xiaoming
 *	
 * 	@mail wangfeng.wf@warmdoc.com
 * 
 * 	@tags An overview of this file: 可扩展的条件筛选菜单Demo主页
 * 
 */
public class MainActivity extends Activity {

	/**
	 * 列表原数据
	 */
	private List<Item> allItems;
	/**
	 * 可扩展的条件筛选菜单组合控件
	 */
	private ExpandMenuView expandTabView;
	/**
	 * 筛选条件视图集合
	 */
	private ArrayList<View> mViewArray;
	private BaseAdapter adapter;
	/**
	 * 城市筛选条件数据
	 */
	private List<Province> allCitys;
	/**
	 * 等级筛选条件数据
	 */
	private List<Grad> grads;
	
	/**
	 * 筛选后的数据
	 */
	private List<Item> items;
	
	private ArrayList<String> superItemDatas;
	
	// 筛选条件
	private String cityName = null;
	private String gradId = null;
	private int sort = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		String JSONData = getCityDataJSON();
		allItems = new ArrayList<Item>();
		allItems.addAll(JSONObject.parseArray(JSONData, Item.class));
		items.addAll(allItems);
		adapter.notifyDataSetChanged();

		allCitys = new ArrayList<Province>();
		String JSONTerm = getCityTermJSON();
		allCitys.addAll(JSONObject.parseArray(JSONTerm, Province.class));

		superItemDatas = new ArrayList<String>();
		SparseArray<List<String>> children = new SparseArray<List<String>>();

		// 提取并设置数据
		for (int i = 0; i < allCitys.size(); i++) {
			superItemDatas.add(allCitys.get(i).getName());
			List<City> citys = allCitys.get(i).getCitys();
			List<String> items = new ArrayList<String>();
			if (citys == null || citys.size() == 0) {
				children.put(i, null);
				continue;
			}
			for (int j = 0; j < citys.size(); j++) {
				items.add(citys.get(j).getCityName());
			}
			children.put(i, items);
		}

		final DoubleListFilterView cityFilterView = new DoubleListFilterView(this, "城市筛选", superItemDatas, children, 0, 0);
		cityFilterView.setOnSelectListener(new DoubleListFilterView.OnSelectListener() {

			@Override
			public void getValue(String showText, int superPosition, int position) {
				refreshScreen(cityFilterView, showText, superPosition, position);
			}
		});

		// 等级筛选
		grads = new ArrayList<Grad>();
		grads.addAll(JSONObject.parseArray(getGradTermJSON(), Grad.class));
		List<String> gradItems = new ArrayList<String>();
		for (int i = 0; i < grads.size(); i++) {
			gradItems.add(grads.get(i).getGradName());
		}
		final SingleListFilterView gradFilterView = new SingleListFilterView(this, gradItems, "等级筛选");
		gradFilterView.setOnSelectListener(new SingleListFilterView.OnSelectListener() {

			@Override
			public void getValue(String showText, int position) {
				refreshScreen(gradFilterView, showText, -1, position);
			}
		});

		// 排序
		List<String> sortItems = new ArrayList<String>();
		sortItems.add("智能排序");
		sortItems.add("资产升序");
		sortItems.add("资产降序");
		final SingleListFilterView sortFilterView = new SingleListFilterView(this, sortItems, "排序筛选");
		sortFilterView.setOnSelectListener(new SingleListFilterView.OnSelectListener() {

			@Override
			public void getValue(String showText, int position) {
				refreshScreen(sortFilterView, showText, -1, position);
			}
		});
		
		//添加条件筛选控件到数据集合中
		mViewArray = new ArrayList<View>();
		mViewArray.add(cityFilterView);
		mViewArray.add(gradFilterView);
		mViewArray.add(sortFilterView);
		
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add("城市筛选");
		mTextArray.add("等级筛选");
		mTextArray.add("排序筛选");
		
		//给组合控件设置数据
		expandTabView.setValue(mTextArray, mViewArray);
		
		//处理组合控件按钮点击事件
		expandTabView.setOnButtonClickListener(new OnButtonClickListener() {
			
			@Override
			public void onClick(int selectPosition, boolean isChecked) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void initView() {
		expandTabView = (ExpandMenuView) findViewById(R.id.expandTabView);
		ListView listView = (ListView) findViewById(R.id.listView);
		items = new ArrayList<Item>();
		adapter = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Item item = items.get(position);
				String content = item.getName() + "   $ " + item.getNumber() + "  " + item.getGrade() + "级      " + item.getProv() + "-"
						+ item.getCityName();

				TextView textView = new TextView(MainActivity.this);
				textView.setLayoutParams(new AbsListView.LayoutParams(-1, 100));
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
				textView.setTextColor(getResources().getColor(R.color.them_brown));
				textView.setText(content);
				return textView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return items.get(position);
			}

			@Override
			public int getCount() {
				return items.size();
			}
		};
		listView.setAdapter(adapter);
	}
	
	/**
	 * 更新筛选条件
	 * @param view
	 * @param showText
	 * @param superPosition
	 * @param pos 选中的位置
	 */
	private void refreshScreen(View view, String showText, int superPosition, int pos){
		expandTabView.closeView();
		int position = getPositon(view);
		if (position >= 0)
			expandTabView.setTitle(showText, position);
		
		items.clear();
		switch (position) {
		case 0:// 城市筛选
			if(superPosition == 0){
				cityName = null;
				break;
			}
			Province province = allCitys.get(superPosition);
			List<City> citys = province.getCitys();
			if(citys == null || citys.size() == 0 || pos == 0){
				cityName = province.getName();
				break;
			}
			City city = citys.get(pos);
			cityName = city.getCityName();
			break;
		case 1:// 等级筛选
			if(pos == 0){
				gradId = null;
				break;
			}
			Grad grad = grads.get(pos);
			gradId = grad.getId();
			break;
		case 2:// 排序
			sort = pos;
			break;
		}
		for (int i = 0; i < allItems.size(); i++) {
			boolean isCity = false;
			boolean isGrad = false;
			Item item = allItems.get(i);
			// 筛选城市
			if(cityName != null){
				String prov = item.getProv();
				String cName = item.getCityName();
				if(cityName.equals(prov) || cityName.equals(cName)){
					isCity = true;
				}
				if("其他".equals(cityName)){
					if(!superItemDatas.contains(prov)){
						isCity = true;
					}
				}
			}else {
				isCity = true;
			}
			// 筛选等级
			if(gradId != null){
				String grade = item.getGrade();
				if(gradId.equals(grade)){
					isGrad = true;
				}
			}else{
				isGrad = true;
			}
			if(isCity && isGrad){
				items.add(item);
			}
		}
		// 排序
		if(sort != 0){
			long[] condition = new long[items.size()];
			for (int i = 0; i < items.size(); i++) {
				int number = items.get(i).getNumber();
				condition[i] = number;
			}
			SortAlgorithm.selectSort(items, condition, sort == 1 ? "asc" : "desc");
		}
		
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 获取当前点击的位置
	 * 
	 * @param tView
	 * @return
	 */
	private int getPositon(View tView) {
		for (int i = 0; i < mViewArray.size(); i++) {
			if (mViewArray.get(i) == tView)
				return i;
		}

		return -1;
	}

	private String getCityTermJSON() {
		return "[" + "{\"name\":\"全部城市\",\"citys\":[]}," + "{\"name\":\"北京\",\"citys\":[{\"cityName\":\"北京市\"}]},"
				+ "{\"name\":\"上海\",\"citys\":[{\"cityName\":\"上海市\"}]}," + "{\"name\":\"天津\",\"citys\":[{\"cityName\":\"天津市\"}]},"
				+ "{\"name\":\"广东省\",\"citys\":[" + "{\"cityName\":\"广东省全部\"}," + "{\"cityName\":\"广州市\"}," + "{\"cityName\":\"深圳市\"},"
				+ "{\"cityName\":\"东莞市\"}," + "{\"cityName\":\"中山市\"}," + "{\"cityName\":\"潮州市\"}," + "{\"cityName\":\"揭阳市\"},"
				+ "{\"cityName\":\"云浮市\"}," + "{\"cityName\":\"珠海市\"}," + "{\"cityName\":\"汕头市\"}," + "{\"cityName\":\"韶关市\"},"
				+ "{\"cityName\":\"佛山市\"}," + "{\"cityName\":\"江门市\"}," + "{\"cityName\":\"湛江市\"}," + "{\"cityName\":\"茂名市\"}" + "]},"
				+ "{\"name\":\"江苏省\",\"citys\":[" + "{\"cityName\":\"江苏省全部\"}," + "{\"cityName\":\"苏州市\"}," + "{\"cityName\":\"南京市\"},"
				+ "{\"cityName\":\"无锡市\"}," + "{\"cityName\":\"常州市\"}," + "{\"cityName\":\"连云港市\"}," + "{\"cityName\":\"徐州市\"}" + "]},"
				+ "{\"name\":\"浙江省\",\"citys\":[" + "{\"cityName\":\"浙江省全部\"}," + "{\"cityName\":\"杭州市\"}," + "{\"cityName\":\"宁波市\"},"
				+ "{\"cityName\":\"温州市\"}," + "{\"cityName\":\"嘉兴市\"}," + "{\"cityName\":\"湖州市\"}," + "{\"cityName\":\"金华市\"},"
				+ "{\"cityName\":\"台州市\"}," + "{\"cityName\":\"绍兴市\"}" + "]}," + "{\"name\":\"陕西省\",\"citys\":[" + "{\"cityName\":\"陕西省全部\"},"
				+ "{\"cityName\":\"西安市\"}," + "{\"cityName\":\"宝鸡市\"}," + "{\"cityName\":\"咸阳市\"}," + "{\"cityName\":\"渭南市\"},"
				+ "{\"cityName\":\"汉中市\"}," + "{\"cityName\":\"延安市\"}," + "{\"cityName\":\"商洛市\"}," + "{\"cityName\":\"安康市\"},"
				+ "{\"cityName\":\"铜川市\"}," + "{\"cityName\":\"阎良市\"}" + "]}," + "{\"name\":\"其他\",\"citys\":[{\"cityName\":\"其他全部\"}]}" + "]";
	}

	private String getCityDataJSON() {
		return "[{\"prov\":\"北京\",\"name\":\"旅游公司2\",\"cityName\":\"北京市\",\"number\":\"15200000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x5\",\"cityName\":\"云浮市\",\"number\":\"3100000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"空气公司4\",\"cityName\":\"苏州市\",\"number\":\"8240000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"楼房公司3\",\"cityName\":\"深圳市\",\"number\":\"12140000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"石油公司1\",\"cityName\":\"西安市\",\"number\":\"7600000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"上海\",\"name\":\"软件公司5\",\"cityName\":\"上海市\",\"number\":\"15124000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"空气公司2\",\"cityName\":\"苏州市\",\"number\":\"8220000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"北京\",\"name\":\"旅游公司5\",\"cityName\":\"北京市\",\"number\":\"15022000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"太阳公司4\",\"cityName\":\"连云港市\",\"number\":\"4200000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司1\",\"cityName\":\"宁波市\",\"number\":\"4900000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"上海\",\"name\":\"软件公司1\",\"cityName\":\"上海市\",\"number\":\"15120000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x3\",\"cityName\":\"潮州市\",\"number\":\"4000000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"西湖公司1\",\"cityName\":\"杭州市\",\"number\":\"7500000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司1\",\"cityName\":\"广州市\",\"number\":\"15210000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"上海\",\"name\":\"软件公司2\",\"cityName\":\"上海市\",\"number\":\"15121000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"楼房公司2\",\"cityName\":\"深圳市\",\"number\":\"12130000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"北京\",\"name\":\"旅游公司3\",\"cityName\":\"北京市\",\"number\":\"15300000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"上海\",\"name\":\"软件公司6\",\"cityName\":\"上海市\",\"number\":\"15125000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司6\",\"cityName\":\"商洛市\",\"number\":\"2820000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司4\",\"cityName\":\"汉中市\",\"number\":\"2580000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x10\",\"cityName\":\"湛江市\",\"number\":\"3030000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"北京\",\"name\":\"旅游公司1\",\"cityName\":\"北京市\",\"number\":\"15100000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"空气公司1\",\"cityName\":\"苏州市\",\"number\":\"8210000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司2\",\"cityName\":\"广州市\",\"number\":\"15220000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"太阳公司3\",\"cityName\":\"常州市\",\"number\":\"3500000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"甘肃省\",\"name\":\"拉面公司1\",\"cityName\":\"兰州市\",\"number\":\"3932000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司3\",\"cityName\":\"广州市\",\"number\":\"15230000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"楼房公司4\",\"cityName\":\"深圳市\",\"number\":\"12150000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"太阳公司2\",\"cityName\":\"无锡市\",\"number\":\"5200000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司1\",\"cityName\":\"宝鸡市\",\"number\":\"4860000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x2\",\"cityName\":\"中山市\",\"number\":\"5000000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司7\",\"cityName\":\"绍兴市\",\"number\":\"3580000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"北京\",\"name\":\"旅游公司4\",\"cityName\":\"北京市\",\"number\":\"15020000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x6\",\"cityName\":\"珠海市\",\"number\":\"3200000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"石油公司2\",\"cityName\":\"西安市\",\"number\":\"7612000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x9\",\"cityName\":\"佛山市\",\"number\":\"3010000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司3\",\"cityName\":\"嘉兴市\",\"number\":\"3900000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司8\",\"cityName\":\"铜川市\",\"number\":\"2990000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x0\",\"cityName\":\"江门市\",\"number\":\"3020000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司3\",\"cityName\":\"渭南市\",\"number\":\"3690000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x11\",\"cityName\":\"茂名市\",\"number\":\"3040000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"山西省\",\"name\":\"能源公司1\",\"cityName\":\"太原市\",\"number\":\"3652000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"上海\",\"name\":\"软件公司3\",\"cityName\":\"上海市\",\"number\":\"15122000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"太阳公司1\",\"cityName\":\"南京市\",\"number\":\"5000000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"楼房公司1\",\"cityName\":\"深圳市\",\"number\":\"12120000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"太阳公司5\",\"cityName\":\"徐州市\",\"number\":\"4400000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"石油公司3\",\"cityName\":\"西安市\",\"number\":\"7613000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x8\",\"cityName\":\"韶关市\",\"number\":\"3220000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"西湖公司2\",\"cityName\":\"杭州市\",\"number\":\"7512000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x1\",\"cityName\":\"东莞市\",\"number\":\"11000000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司5\",\"cityName\":\"广州市\",\"number\":\"15250000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司2\",\"cityName\":\"温州市\",\"number\":\"4800000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司4\",\"cityName\":\"广州市\",\"number\":\"15240000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司6\",\"cityName\":\"台州市\",\"number\":\"3550000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x7\",\"cityName\":\"汕头市\",\"number\":\"3210000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司2\",\"cityName\":\"咸阳市\",\"number\":\"3560000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"上海\",\"name\":\"软件公司4\",\"cityName\":\"上海市\",\"number\":\"15123000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司5\",\"cityName\":\"延安市\",\"number\":\"2560000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"江苏省\",\"name\":\"空气公司3\",\"cityName\":\"苏州市\",\"number\":\"8230000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司7\",\"cityName\":\"安康市\",\"number\":\"2810000\",\"grade\":\"D\"},"
				+ "{\"prov\":\"广东省\",\"name\":\"鲨鱼公司x4\",\"cityName\":\"揭阳市\",\"number\":\"3000000\",\"grade\":\"B\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司5\",\"cityName\":\"金华市\",\"number\":\"3510000\",\"grade\":\"A\"},"
				+ "{\"prov\":\"浙江省\",\"name\":\"皮鞋公司4\",\"cityName\":\"湖州市\",\"number\":\"3600000\",\"grade\":\"C\"},"
				+ "{\"prov\":\"陕西省\",\"name\":\"煤炭公司9\",\"cityName\":\"阎良市\",\"number\":\"4340000\",\"grade\":\"B\"}]";
	}

	private String getGradTermJSON() {
		return "[{\"id\":\"E\",\"gradName\":\"全部等级\"},{\"id\":\"A\",\"gradName\":\"A级\"},{\"id\":\"B\",\"gradName\":\"B级\"},{\"id\":\"C\",\"gradName\":\"C级\"},{\"id\":\"D\",\"gradName\":\"D级\"}]";

	}

}
