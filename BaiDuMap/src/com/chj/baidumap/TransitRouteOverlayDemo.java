package com.chj.baidumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.chj.baidumap.base.BaseActivity;

/**
 * 
 * @包名:com.chj.baidumap
 * @类名:TransitRouteOverlayDemo
 * @作者:陈火炬
 * @时间:2015-8-18 下午9:04:56
 * 
 * @描述:公交换乘路线
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class TransitRouteOverlayDemo extends BaseActivity
{
	private RoutePlanSearch	routePlanSearch;

	private static String	CITY			= "周口";
	// 公交换乘路线规划策略
	private TransitPolicy	NO_SUBWAY		= TransitRoutePlanOption.TransitPolicy.EBUS_NO_SUBWAY;		// 不含地铁
	private TransitPolicy	TIME_FIRST		= TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST;	// 时间优先
	private TransitPolicy	TRANSFER_FIRST	= TransitRoutePlanOption.TransitPolicy.EBUS_TRANSFER_FIRST; // 最少换乘
	private TransitPolicy	WALK_FIRST		= TransitRoutePlanOption.TransitPolicy.EBUS_WALK_FIRST;	// 最少步行距离
	private TransitPolicy	policy			= TRANSFER_FIRST;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		search();
	}

	/**
	 * 搜索
	 */
	private void search()
	{
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(new MyListener());
		TransitRoutePlanOption transitOption = new TransitRoutePlanOption();
		PlanNode from = PlanNode.withLocation(new LatLng(33.640877, 114.690423));// 起点:周口师范学院
		PlanNode to = PlanNode.withLocation(new LatLng(33.580544, 114.665776));// 终点:周口站
		transitOption.from(from);// 设置起,
		transitOption.to(to);// 设置终点
		transitOption.city(CITY);// 设置换乘策略
		transitOption.policy(policy);
		routePlanSearch.transitSearch(transitOption);
	}

	/**
	 * 处理驾车路线
	 */
	class MyListener implements OnGetRoutePlanResultListener
	{

		/**
		 * 驾车路线
		 */
		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result)
		{

		}

		/**
		 * 公交换乘路线
		 */
		@Override
		public void onGetTransitRouteResult(TransitRouteResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
				return;
			}

			baiduMap.clear();
			TransitRouteOverlay overlay = new MyTransitOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
			overlay.setData(result.getRouteLines().get(0));// 设置路线数据,第一条路线
			overlay.addToMap();
			overlay.zoomToSpan();
		}

		/**
		 * 步行路线
		 */
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result)
		{

		}
	}

	class MyTransitOverlay extends TransitRouteOverlay
	{

		public MyTransitOverlay(BaiduMap arg0)
		{
			super(arg0);
		}

		/**
		 * 覆写此方法以改变默认起点图标
		 */
		@Override
		public BitmapDescriptor getStartMarker()
		{
			// 起点图标
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		/**
		 * 覆写此方法以改变默认终点图标
		 */
		@Override
		public BitmapDescriptor getTerminalMarker()
		{
			// 终点图标
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_MENU:
				AlertDialog.Builder builder = new AlertDialog.Builder(TransitRouteOverlayDemo.this);
				builder.setTitle("公交换乘路线策略");
				String[] items = new String[] { "不含地铁", "时间优先", "最少换乘", "最少步行距离" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:// 不含地铁
								policy = NO_SUBWAY;
								search();
								dialog.dismiss();
								break;
							case 1:// 时间优先
								policy = TIME_FIRST;
								search();
								dialog.dismiss();
								break;
							case 2:// 最少换乘
								policy = TRANSFER_FIRST;
								search();
								dialog.dismiss();
								break;
							case 3:// 最少步行距离
								policy = WALK_FIRST;
								search();
								dialog.dismiss();
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
