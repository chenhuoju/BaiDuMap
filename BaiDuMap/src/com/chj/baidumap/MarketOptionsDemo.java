package com.chj.baidumap;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.chj.baidumap.base.BaseActivity;

/**
 * @����:com.chj.baidumap
 * @����:MarketOptionsDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����10:20:17
 * 
 * @����:market������
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class MarketOptionsDemo extends BaseActivity
{
	private View		mBubble;	// ���ݶ���
	private TextView	title;		// ����

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		drawMarket();

		// ˼·�����ĳһ��market�����Դ��ϵ�������
		// 1.�������ݣ���ӵ�mapView������Ϊ����
		// 2.�����ʱ���������ݵ�λ�ã�����Ϊ��ʾ
		initBubble();
	}

	/**
	 * ��ʼ������
	 */
	private void initBubble()
	{
		// �������ݣ���ӵ�mapView������Ϊ����
		mBubble = View.inflate(getApplicationContext(), R.layout.bubble, null);
		LayoutParams params = new MapViewLayoutParams.Builder()
																.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
																// ��װ��γ������λ��
																.position(Pos)
																// ���ܴ�null������ΪmapModeʱ����������position
																.width(MapViewLayoutParams.WRAP_CONTENT)
																.height(MapViewLayoutParams.WRAP_CONTENT)
																.build();
		mapView.addView(mBubble, params);
		mBubble.setVisibility(View.INVISIBLE);
		title = (TextView) mBubble.findViewById(R.id.title);
	}

	/**
	 * ����market������
	 */
	private void drawMarket()
	{
		// ������Դ Id ���� bitmap ������Ϣ
		BitmapDescriptor bitmapDes = BitmapDescriptorFactory.fromResource(R.drawable.eat_icon);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(Pos)// ����λ��
						.icon(bitmapDes)
						// ����ͼ��
						.draggable(true)
						// �����Ƿ������ק��Ĭ��false
						.title("������");// ���ñ���
		baiduMap.addOverlay(markerOptions);
		markerOptions = new MarkerOptions();
		// baiduMap.setOnMapLongClickListener(null);//��ק����

		markerOptions = new MarkerOptions().title("��")
											.position(new LatLng(latitude + 0.001, longitude))
											.icon(bitmapDes);
		baiduMap.addOverlay(markerOptions);

		markerOptions = new MarkerOptions().title("��")
											.position(new LatLng(latitude, longitude + 0.001))
											.icon(bitmapDes);
		baiduMap.addOverlay(markerOptions);

		ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
		bitmaps.add(bitmapDes);
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_geo));
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_st));
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_en));
		markerOptions = new MarkerOptions().title("������")
											.position(new LatLng(latitude - 0.001, longitude - 0.001))
											.icons(bitmaps)// ��ʾ���ͼƬ�����л���������֡����
											.period(25);// ���ö���֡ˢ��һ��ͼƬ��Դ��Marker�����ļ��ʱ�䣬ֵԽС����Խ��
		baiduMap.addOverlay(markerOptions);

		baiduMap.setOnMarkerClickListener(new MyListener());
	}

	class MyListener implements OnMarkerClickListener
	{

		@Override
		public boolean onMarkerClick(Marker result)
		{
			// �����ʱ���������ݵ�λ�ã�����Ϊ��ʾ
			LayoutParams params = new MapViewLayoutParams.Builder()
																	.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
																	// ��װ��γ������λ��
																	.position(result.getPosition())
																	// ���ܴ�null
																	.width(MapViewLayoutParams.WRAP_CONTENT)
																	.height(MapViewLayoutParams.WRAP_CONTENT)
																	.yOffset(-5)
																	// ����positiond�����أ���������ֵ�������Ǹ�ֵ
																	.build();
			mapView.updateViewLayout(mBubble, params);
			mBubble.setVisibility(View.VISIBLE);
			title.setText(result.getTitle());

			return false;
		}

	}
}
