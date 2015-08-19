package com.chj.baidumap.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.chj.baidumap.R;

/**
 * @����:com.chj.baidumap.base
 * @����:BaseActivity
 * @����:�»��
 * @ʱ��:2015-8-18 ����9:36:02
 * 
 * @����:��ͼ����
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class BaseActivity extends Activity
{
	protected BaiduMap	baiduMap;
	protected MapView	mapView;

	// ����40.050966,116.303128
	// �ܿ�ʦ��ѧԺ ���º� 33.640877,114.690423
	// �ϼ� 24.6958,118.284773
	// ������ 24.452261,118.073486
	protected double	latitude	= 24.452261;
	protected double	longitude	= 118.073486;
	protected LatLng	Pos			= new LatLng(latitude, longitude);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��title
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common);

		initManager();
		init();
	}

	/**
	 * ���õ�ͼ����
	 */
	private void init()
	{
		mapView = (MapView) findViewById(R.id.map_view);
		baiduMap = mapView.getMap();

		// ���õ�ͼ���ż���
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);// Ĭ�ϵ�����12��3-19��
		baiduMap.setMapStatus(mapStatusUpdate);

		// �������ĵ�,Ĭ�����찲��
		MapStatusUpdate mapStatusUpdatePoint = MapStatusUpdateFactory.newLatLng(Pos);
		baiduMap.setMapStatus(mapStatusUpdatePoint);
	}

	/**
	 * ��ʼ����ͼ
	 * 
	 */
	private void initManager()
	{
		// SDKInitializer.initialize(getApplicationContext());//
		// ���ܴ���Activity��������ȫ�ֵ�Content
	}

	@Override
	protected void onResume()
	{
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mapView.onDestroy();
		super.onDestroy();
	}
}
