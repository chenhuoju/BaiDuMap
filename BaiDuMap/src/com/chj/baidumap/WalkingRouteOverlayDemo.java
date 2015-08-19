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
 * @����:com.chj.baidumap
 * @����:WalkingRouteOverlayDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����8:50:40
 * 
 * @����: ����·��
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class WalkingRouteOverlayDemo extends BaseActivity
{
	private RoutePlanSearch	routePlanSearch;	// ·�߶���

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		search();
	}

	/**
	 * ����
	 */
	private void search()
	{
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(new MyListener());

		WalkingRoutePlanOption walkingOption = new WalkingRoutePlanOption();
		PlanNode from = PlanNode.withLocation(new LatLng(33.640877, 114.690423));// ���:�ܿ�ʦ��ѧԺ
		PlanNode to = PlanNode.withLocation(new LatLng(33.580544, 114.665776));// �յ�:�ܿ�վ
		walkingOption.from(from);// ������,
		walkingOption.to(to);// �����յ�
		routePlanSearch.walkingSearch(walkingOption);
	}

	/**
	 * ����ݳ�·��
	 */
	class MyListener implements OnGetRoutePlanResultListener
	{

		/**
		 * �ݳ�·��
		 */
		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result)
		{

		}

		/**
		 * ����·��
		 */
		@Override
		public void onGetTransitRouteResult(TransitRouteResult result)
		{

		}

		/**
		 * ����·��
		 */
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "δ���������", 0).show();
				return;
			}

			baiduMap.clear();
			WalkingRouteOverlay overlay = new MyWalkingOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// ���¼����ݸ�overlay
			overlay.setData(result.getRouteLines().get(0));// ����·������,��һ��·��
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
		 * ��д�˷����Ըı�Ĭ�����ͼ��
		 */
		@Override
		public BitmapDescriptor getStartMarker()
		{
			// ���ͼ��
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		/**
		 * ��д�˷����Ըı�Ĭ���յ�ͼ��
		 */
		@Override
		public BitmapDescriptor getTerminalMarker()
		{
			// �յ�ͼ��
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

}
