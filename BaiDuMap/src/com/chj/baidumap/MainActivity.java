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
											"hello world", "ͼ��", "Բ�θ�����",
											"չʾ����", "marker������", "��Χ������",
											"�ܱ�����", "ȫ������", "�ݳ�·��",
											"����·��", "��������", "�ҵ�λ��" };
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��title
		super.onCreate(savedInstanceState);

		initView();
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView()
	{
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list);
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.items, objects);

		list.setVerticalScrollBarEnabled(false);// ���ع�����
		list.setDividerHeight(0);// ȥ���ָ���
		list.setAdapter(adapter);// ����������ʾ
		list.setOnItemClickListener(this);// ���ü���

		// У��key
		SDKInitializer.initialize(getApplicationContext());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(getApplicationContext(), clazzs[position]);
		startActivity(intent);
	}

}
