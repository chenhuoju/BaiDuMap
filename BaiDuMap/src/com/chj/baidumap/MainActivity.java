package com.chj.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baidu.mapapi.SDKInitializer;

public class MainActivity extends Activity implements OnItemClickListener
{
	private static String[]			objects	= new String[] {
											"hello world", "图层", "圆形覆盖物",
											"展示文字", "marker覆盖物", "范围内搜索",
											"周边搜索", "全城搜索", "驾车路线",
											"步行路线", "公交换乘", "我的位置" };
	private static Class[]			clazzs	= new Class[] {
											HelloWorld.class, LayerDemo.class, CircleOptionsDemo.class,
											TextOptionsDemo.class, MarketOptionsDemo.class, PoiSearchInBoundsDemo.class,
											PoiSearchNearByDemo.class, PoiSearchInCityDemo.class, DrivingRouteOverlayDemo.class,
											WalkingRouteOverlayDemo.class, TransitRouteOverlayDemo.class, LocationDemo.class };
	private ListView				list;
	private ArrayAdapter<String>	adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉title
		super.onCreate(savedInstanceState);

		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView()
	{
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list);
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.items, objects);

		list.setVerticalScrollBarEnabled(false);// 隐藏滚动条
		list.setDividerHeight(0);// 去掉分割线
		list.setAdapter(adapter);// 设置数据显示
		list.setOnItemClickListener(this);// 设置监听

		// 校验key
		SDKInitializer.initialize(getApplicationContext());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(getApplicationContext(), clazzs[position]);
		startActivity(intent);
	}

}
