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
 * @包名:com.chj.baidumap
 * @类名:MarketOptionsDemo
 * @作者:陈火炬
 * @时间:2015-8-18 上午10:20:17
 * 
 * @描述:market覆盖物
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class MarketOptionsDemo extends BaseActivity
{
	private View		mBubble;	// 泡泡对象
	private TextView	title;		// 标题

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		drawMarket();

		// 思路：点击某一个market，在脑袋上弹出泡泡
		// 1.加载泡泡，添加到mapView，设置为隐藏
		// 2.当点击时，更新泡泡到位置，设置为显示
		initBubble();
	}

	/**
	 * 初始化泡泡
	 */
	private void initBubble()
	{
		// 加载泡泡，添加到mapView，设置为隐藏
		mBubble = View.inflate(getApplicationContext(), R.layout.bubble, null);
		LayoutParams params = new MapViewLayoutParams.Builder()
																.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
																// 安装经纬度设置位置
																.position(Pos)
																// 不能传null，设置为mapMode时，必须设置position
																.width(MapViewLayoutParams.WRAP_CONTENT)
																.height(MapViewLayoutParams.WRAP_CONTENT)
																.build();
		mapView.addView(mBubble, params);
		mBubble.setVisibility(View.INVISIBLE);
		title = (TextView) mBubble.findViewById(R.id.title);
	}

	/**
	 * 绘制market覆盖物
	 */
	private void drawMarket()
	{
		// 根据资源 Id 创建 bitmap 描述信息
		BitmapDescriptor bitmapDes = BitmapDescriptorFactory.fromResource(R.drawable.eat_icon);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(Pos)// 设置位置
						.icon(bitmapDes)
						// 设置图标
						.draggable(true)
						// 设置是否可用拖拽，默认false
						.title("鼓浪屿");// 设置标题
		baiduMap.addOverlay(markerOptions);
		markerOptions = new MarkerOptions();
		// baiduMap.setOnMapLongClickListener(null);//拖拽监听

		markerOptions = new MarkerOptions().title("向北")
											.position(new LatLng(latitude + 0.001, longitude))
											.icon(bitmapDes);
		baiduMap.addOverlay(markerOptions);

		markerOptions = new MarkerOptions().title("向东")
											.position(new LatLng(latitude, longitude + 0.001))
											.icon(bitmapDes);
		baiduMap.addOverlay(markerOptions);

		ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
		bitmaps.add(bitmapDes);
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_geo));
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_st));
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_en));
		markerOptions = new MarkerOptions().title("向西南")
											.position(new LatLng(latitude - 0.001, longitude - 0.001))
											.icons(bitmaps)// 显示多个图片来回切换，类似于帧动画
											.period(25);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快
		baiduMap.addOverlay(markerOptions);

		baiduMap.setOnMarkerClickListener(new MyListener());
	}

	class MyListener implements OnMarkerClickListener
	{

		@Override
		public boolean onMarkerClick(Marker result)
		{
			// 当点击时，更新泡泡到位置，设置为显示
			LayoutParams params = new MapViewLayoutParams.Builder()
																	.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)
																	// 安装经纬度设置位置
																	.position(result.getPosition())
																	// 不能传null
																	.width(MapViewLayoutParams.WRAP_CONTENT)
																	.height(MapViewLayoutParams.WRAP_CONTENT)
																	.yOffset(-5)
																	// 距离positiond的像素，向下是正值，向上是负值
																	.build();
			mapView.updateViewLayout(mBubble, params);
			mBubble.setVisibility(View.VISIBLE);
			title.setText(result.getTitle());

			return false;
		}

	}
}
