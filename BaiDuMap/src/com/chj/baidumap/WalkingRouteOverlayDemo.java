package com.chj.baidumap;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.chj.baidumap.base.BaseActivity;

/**
 * 
 * @包名:com.chj.baidumap
 * @类名:WalkingRouteOverlayDemo
 * @作者:陈火炬
 * @时间:2015-8-18 下午8:50:40
 * 
 * @描述: 步行路线
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class WalkingRouteOverlayDemo extends BaseActivity
{
	private RoutePlanSearch	routePlanSearch;	// 路线对象

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

		WalkingRoutePlanOption walkingOption = new WalkingRoutePlanOption();
		PlanNode from = PlanNode.withLocation(new LatLng(33.640877, 114.690423));// 起点:周口师范学院
		PlanNode to = PlanNode.withLocation(new LatLng(33.580544, 114.665776));// 终点:周口站
		walkingOption.from(from);// 设置起,
		walkingOption.to(to);// 设置终点
		routePlanSearch.walkingSearch(walkingOption);
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
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
				return;
			}

			baiduMap.clear();
			WalkingRouteOverlay overlay = new MyWalkingOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
			overlay.setData(result.getRouteLines().get(0));// 设置路线数据,第一条路线
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	class MyWalkingOverlay extends WalkingRouteOverlay
	{

		public MyWalkingOverlay(BaiduMap arg0)
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

}
