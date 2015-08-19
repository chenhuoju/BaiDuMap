package com.chj.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

/**
 * @����:com.chj.baidumap
 * @����:LayerDemo
 * @����:�»��
 * @ʱ��:2015-8-17 ����9:07:30
 * 
 * @����:ͼ��
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class LayerDemo extends Activity
{
	private MapView		mapView;

	private BaiduMap	baiduMap;

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

		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initManager()
	{
		SDKInitializer.initialize(getApplicationContext());

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// ��ͼ ��ͨͼ ����ͼ
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_1:// ��ͼ
				// ���õ�ͼ���� MAP_TYPE_NORMAL ��ͨͼ�� MAP_TYPE_SATELLITE ����ͼ
				baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
				baiduMap.setTrafficEnabled(false);
				break;
			case KeyEvent.KEYCODE_2: // ����ͼ
				baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				baiduMap.setTrafficEnabled(false);
				break;
			case KeyEvent.KEYCODE_3:// ��ͨͼ
				// �Ƿ�򿪽�ͨͼ��
				baiduMap.setTrafficEnabled(true);
				break;
			case KeyEvent.KEYCODE_MENU:// �˵���
				AlertDialog.Builder builder = new AlertDialog.Builder(LayerDemo.this);
				builder.setTitle("��ͼ����");
				String[] items = new String[] { "��ͼ", "����ͼ", "��ͨͼ" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								// ���õ�ͼ���� MAP_TYPE_NORMAL ��ͨͼ��
								// MAP_TYPE_SATELLITE ����ͼ
								baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
								baiduMap.setTrafficEnabled(false);
								break;
							case 1:
								baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
								baiduMap.setTrafficEnabled(false);
								break;
							case 2:
								// �Ƿ�򿪽�ͨͼ��
								baiduMap.setTrafficEnabled(true);
								break;
							default:
								dialog.dismiss();
								break;
						}
					}
				});
				builder.show();
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
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
