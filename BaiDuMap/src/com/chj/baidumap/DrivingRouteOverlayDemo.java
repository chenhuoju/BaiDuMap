package com.chj.baidumap;

import java.util.ArrayList;
import java.util.List;

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
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.chj.baidumap.base.BaseActivity;

/**
 * @包名:com.chj.baidumap
 * @类名:DrivingRouteOverlayDemo
 * @作者:陈火炬
 * @时间:2015-8-18 下午7:16:15
 * 
 * @描述:驾车路线
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class DrivingRouteOverlayDemo extends BaseActivity
{
	private RoutePlanSearch	routePlanSearch;													// 路线对象
	// 驾车路线规划策略
	private DrivingPolicy	AVOID_JAM	= DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;	// 躲避拥堵
	private DrivingPolicy	DIS_FIRST	= DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST;	// 最短距离
	private DrivingPolicy	FEE_FIRST	= DrivingRoutePlanOption.DrivingPolicy.ECAR_FEE_FIRST;	// 较少费用
	private DrivingPolicy	TIME_FIRST	= DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST; // 时间优先
	private DrivingPolicy	policy		= DIS_FIRST;

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

		DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
		PlanNode from = PlanNode.withLocation(new LatLng(33.640877, 114.690423));// 起点:周口师范学院
		PlanNode to = PlanNode.withLocation(new LatLng(33.580544, 114.665776));// 终点:周口站
		drivingOption.from(from);// 设置起,
		drivingOption.to(to);// 设置终点
		List<PlanNode> nodes = new ArrayList<PlanNode>();
		nodes.add(PlanNode.withCityNameAndPlaceName("周口", "周口市政府"));
		drivingOption.passBy(nodes);
		drivingOption.policy(policy);// 设置驾车路线规划策略
		routePlanSearch.drivingSearch(drivingOption);
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
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
				return;
			}

			baiduMap.clear();
			DrivingRouteOverlay overlay = new MyDrivingOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
			overlay.setData(result.getRouteLines().get(0));// 设置路线数据,第一条路线
			overlay.addToMap();
			overlay.zoomToSpan();
		}

		/**
		 * 公交路线
		 */
		@Override
		public void onGetTransitRouteResult(TransitRouteResult result)
		{

		}

		/**
		 * 步行路线
		 */
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result)
		{

		}
	}

	class MyDrivingOverlay extends DrivingRouteOverlay
	{

		public MyDrivingOverlay(BaiduMap arg0) {
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
				AlertDialog.Builder builder = new AlertDialog.Builder(DrivingRouteOverlayDemo.this);
				builder.setTitle("驾车路线策略");
				String[] items = new String[] { "躲避拥堵", "最短距离", "较少费用", "时间优先" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:// 躲避拥堵
								policy = AVOID_JAM;
								search();
								break;
							case 1:// 最短距离
								policy = DIS_FIRST;
								search();
								break;
							case 2:// 较少费用
								policy = FEE_FIRST;
								search();
								break;
							case 3:// 时间优先
								policy = TIME_FIRST;
								search();
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
