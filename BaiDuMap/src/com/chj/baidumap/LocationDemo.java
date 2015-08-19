package com.chj.baidumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.chj.baidumap.base.BaseActivity;

/**
 * @����:com.chj.baidumap
 * @����:LocationDemo
 * @����:�»��
 * @ʱ��:2015-8-19 ����8:52:42
 * 
 * @����:��λ
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class LocationDemo extends BaseActivity
{
	public LocationClient										mLocationClient;													// ����LocationClient��
	public BDLocationListener									myListener		= new MyListener();								// ע���������
	private BitmapDescriptor									descriptor;

	// COMPASS ����̬����ʾ��λ����Ȧ�����ֶ�λͼ���ڵ�ͼ����
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	COMPASS			= MyLocationConfiguration.LocationMode.COMPASS;
	// FOLLOWING ����̬�����ֶ�λͼ���ڵ�ͼ����
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	FOLLOWING		= MyLocationConfiguration.LocationMode.FOLLOWING;
	// NORMAL ��̬ͨ�� ���¶�λ����ʱ���Ե�ͼ���κβ���
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	NORMAL			= MyLocationConfiguration.LocationMode.NORMAL;
	com.baidu.mapapi.map.MyLocationConfiguration.LocationMode	locationMode	= FOLLOWING;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		locate();
	}

	/**
	 * ��λ
	 */
	private void locate()
	{
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		option.setScanSpan(1000);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setNeedDeviceDirect(true);// ���ض�λ����������ֻ�ͷ�ķ���
		// option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		// option.setLocationNotify(true);//
		// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		// option.setIgnoreKillProcess(false);//
		// ��ѡ��Ĭ��false����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ��ɱ��
		// option.SetIgnoreCacheException(false);//
		// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		mLocationClient.setLocOption(option);

		setConfigeration();
	}

	/**
	 * @����1: MyLocationConfiguration.LocationMode locationMode ��λͼ����ʾ��ʽ
	 * @COMPASS ����̬����ʾ��λ����Ȧ�����ֶ�λͼ���ڵ�ͼ����;
	 * @FOLLOWING ����̬�����ֶ�λͼ���ڵ�ͼ����;
	 * @NORMAL ��̬ͨ�� ���¶�λ����ʱ���Ե�ͼ���κβ���
	 * 
	 * @����2:boolean enableDirection �Ƿ�������ʾ������Ϣ;true��false
	 * 
	 * @����3:BitmapDescriptor customMarker �û��Զ��嶨λͼ��
	 */
	private void setConfigeration()
	{
		baiduMap.clear();
		descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		MyLocationConfiguration configeration = new MyLocationConfiguration(locationMode, true, descriptor);
		baiduMap.setMyLocationConfigeration(configeration);// ���ö�λ��ʾ��ģʽ
		baiduMap.setMyLocationEnabled(true);// �򿪶�λͼ��
	}

	@Override
	protected void onStart()
	{
		mLocationClient.start();
		super.onStart();
	}

	@Override
	protected void onPause()
	{
		mLocationClient.stop();
		super.onPause();
	}

	/**
	 * �Զ��������
	 */
	class MyListener implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation result)
		{
			if (result != null)
			{
				MyLocationData data = new MyLocationData.Builder()
																	.latitude(result.getLatitude())// γ������
																	.longitude(result.getLongitude())
																	// ��������
																	.build();
				baiduMap.setMyLocationData(data);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_MENU:
				AlertDialog.Builder builder = new AlertDialog.Builder(LocationDemo.this);
				builder.setTitle("��λͼ����ʾ��ʽ");
				String[] items = new String[] { "����̬", "����̬", "��̬ͨ" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:// ����̬
								locationMode = COMPASS;
								setConfigeration();
								break;
							case 1:// ����̬
								locationMode = FOLLOWING;
								setConfigeration();
								break;
							case 2:// ��̬ͨ
								locationMode = NORMAL;
								setConfigeration();
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
}
