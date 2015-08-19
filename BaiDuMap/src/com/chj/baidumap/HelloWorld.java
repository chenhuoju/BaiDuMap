package com.chj.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * @����:com.chj.baidumap
 * @����:HelloWorld
 * @����:�»��
 * @ʱ��:2015-8-17 ����3:57:09
 * 
 * @����:����
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class HelloWorld extends Activity
{
	private static final String	TAG			= "HelloWorld";

	private MyBaiduSDKRecevier	baiduSDKRecevier;
	private BaiduMap			baiduMap;
	private MapView				mapView;
	// TODO:
	private double				latitude	= 40.050966;						// γ��
	private double				longitude	= 116.303128;						// ����
	private LatLng				Pos			= new LatLng(latitude, longitude);	// ����

	// ������: ����:118.073486,24.452261
	// ����ʡ �ܿ��� �ܿ��� �ܿ�ʦ��ѧԺ ���º� 114.690423,33.640877
	// ����ʡ ������ �谲�� ������ ��·�� 118.284773,24.6958
	// private double latitude = 24.452261;
	// private double longitude = 118.073486;
	// private LatLng Pos = new LatLng(latitude, longitude);

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
		// ���õ�ͼ����(V2.X 3-19 V1.X 3-18)
		// �� �޸����ļ�����ʽ���Ż���ռ䵽ʹ�ã����� 110M-->15M��
		// �� �����˼��� 3DЧ����18 19��

		mapView = (MapView) findViewById(R.id.map_view);

		baiduMap = mapView.getMap();
		// BaiduMap:������嵽ĳһ��MapView����ת���ƶ������š��¼�...

		// ������ͼ״̬��Ҫ�����ı仯,ʹ�ù�����MapStatusUpdateFactory����
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(19);// Ĭ�ϵ�����12��3-19��
		// ���õ�ͼ���ż���
		baiduMap.setMapStatus(mapStatusUpdate);

		// LatLng latLng = new LatLng(arg0, arg1);// ���� ��γ�� ����1 γ�� ����2 ����
		MapStatusUpdate mapStatusUpdatePoint = MapStatusUpdateFactory.newLatLng(Pos);
		// �������ĵ�,Ĭ�����찲��
		baiduMap.setMapStatus(mapStatusUpdatePoint);

		// mapView.showZoomControls(false);// Ĭ����true ��ʾ���Ű�ť
		// mapView.showScaleControl(false);// Ĭ����true ��ʾ���

		// baiduMap.getUiSettings().setCompassEnabled(false);// Ĭ����true ��ʾָ����
		// baiduMap.getUiSettings().setCompassPosition(new Point(x, y));// ָ��������
	}

	/**
	 * ��ʼ����ͼ
	 * 
	 */
	private void initManager()
	{
		SDKInitializer.initialize(getApplicationContext());// ���ܴ���Activity��������ȫ�ֵ�Content

		baiduSDKRecevier = new MyBaiduSDKRecevier();
		// ���㲥������ע��
		IntentFilter filter = new IntentFilter();
		filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);// ע���������
		filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);// ע��У��keyʧ��
		registerReceiver(baiduSDKRecevier, filter);

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

	/**
	 * ��������¼�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// ��ת���ƶ�������
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_1:// �Ŵ�
				// �Ŵ��ͼ���ż��� ÿ�ηŴ�һ������
				MapStatusUpdate zoomInStates = MapStatusUpdateFactory.zoomIn();
				baiduMap.setMapStatus(zoomInStates);
				break;
			case KeyEvent.KEYCODE_2:// ��С
				// ��С��ͼ���ż��� ÿ����Сһ������
				MapStatusUpdate zoomOutStates = MapStatusUpdateFactory.zoomOut();
				baiduMap.setMapStatus(zoomOutStates);
				break;
			case KeyEvent.KEYCODE_3:// ��ת
				// ��һ����Ϊ���� ��ת
				MapStatus mapStatus = baiduMap.getMapStatus();// ��ȡ��ͼ�ĵ�ǰ״̬
				float rotate = mapStatus.rotate;
				Log.d(TAG, "rotate:" + rotate);
				// ��ת ��Χ0~360
				MapStatus rotateStatus = new MapStatus.Builder().rotate(rotate + 30).build();
				// �����µ�MapStatus
				MapStatusUpdate rotateStatusUpdate = MapStatusUpdateFactory.newMapStatus(rotateStatus);
				baiduMap.setMapStatus(rotateStatusUpdate);
				break;
			case KeyEvent.KEYCODE_4:// ��ת
				// ��һ��ֱ�� Ϊ�� ��ת Overlooking ����
				MapStatus mapStatusOver = baiduMap.getMapStatus();
				float overlook = mapStatusOver.overlook;
				Log.d(TAG, "overlook:" + overlook);
				// 0~45
				MapStatus overStatus = new MapStatus.Builder().overlook(overlook - 5).build();
				MapStatusUpdate overStatusUpdate = MapStatusUpdateFactory.newMapStatus(overStatus);
				baiduMap.setMapStatus(overStatusUpdate);
				break;
			case KeyEvent.KEYCODE_5:// �ƶ�
				MapStatusUpdate movestatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(118.09772180000004, 24.4390262));
				// �������ĸ��µ�ͼ״̬ ��ʱ300����
				baiduMap.animateMapStatus(movestatusUpdate);
				break;

			// TODO:����ʾ����ʽչʾ��ͼ����
			case KeyEvent.KEYCODE_MENU:// �˵���
				AlertDialog.Builder builder = new AlertDialog.Builder(HelloWorld.this);
				builder.setTitle("��ͼ����");
				String[] items = new String[] { "�Ŵ�", "��С", "ˮƽ��ת", "��ֱ��ת", "ƽ��" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								// �Ŵ��ͼ���ż��� ÿ�ηŴ�һ������
								MapStatusUpdate zoomInStates = MapStatusUpdateFactory.zoomIn();
								baiduMap.setMapStatus(zoomInStates);
								break;
							case 1:
								// ��С��ͼ���ż��� ÿ����Сһ������
								MapStatusUpdate zoomOutStates = MapStatusUpdateFactory.zoomOut();
								baiduMap.setMapStatus(zoomOutStates);
								break;
							case 2:
								// ��һ����Ϊ���� ��ת
								MapStatus mapStatus = baiduMap.getMapStatus();// ��ȡ��ͼ�ĵ�ǰ״̬
								float rotate = mapStatus.rotate;
								// ��ת ��Χ0~360
								MapStatus rotateStatus = new MapStatus.Builder().rotate(rotate + 30).build();
								// �����µ�MapStatus
								MapStatusUpdate rotateStatusUpdate = MapStatusUpdateFactory.newMapStatus(rotateStatus);
								baiduMap.setMapStatus(rotateStatusUpdate);
								break;
							case 3:
								// ��һ��ֱ�� Ϊ�� ��ת Overlooking ����
								MapStatus mapStatusOver = baiduMap.getMapStatus();
								float overlook = mapStatusOver.overlook;
								// 0~45
								MapStatus overStatus = new MapStatus.Builder().overlook(overlook - 5).build();
								MapStatusUpdate overStatusUpdate = MapStatusUpdateFactory.newMapStatus(overStatus);
								baiduMap.setMapStatus(overStatusUpdate);
								break;
							case 4:
								MapStatusUpdate movestatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(118.09772180000004, 24.4390262));
								// �������ĸ��µ�ͼ״̬ ��ʱ300����
								baiduMap.animateMapStatus(movestatusUpdate);
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
	protected void onDestroy()
	{
		mapView.onDestroy();
		unregisterReceiver(baiduSDKRecevier);
		super.onDestroy();
	}

	/**
	 * �Զ���㲥������
	 */
	class MyBaiduSDKRecevier extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String result = intent.getAction();
			// �ж�����
			if (result.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR))
			{
				// �������
				Toast.makeText(getApplicationContext(), "������", 0).show();
			}
			// ��Ȩ��֤
			else if (result.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR))
			{
				// keyУ��ʧ��
				Toast.makeText(getApplicationContext(), "У��ʧ��", 0).show();
			}
		}
	}
}
