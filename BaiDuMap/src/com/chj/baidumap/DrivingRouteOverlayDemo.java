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
 * @����:com.chj.baidumap
 * @����:DrivingRouteOverlayDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����7:16:15
 * 
 * @����:�ݳ�·��
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class DrivingRouteOverlayDemo extends BaseActivity
{
	private RoutePlanSearch	routePlanSearch;													// ·�߶���
	// �ݳ�·�߹滮����
	private DrivingPolicy	AVOID_JAM	= DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;	// ���ӵ��
	private DrivingPolicy	DIS_FIRST	= DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST;	// ��̾���
	private DrivingPolicy	FEE_FIRST	= DrivingRoutePlanOption.DrivingPolicy.ECAR_FEE_FIRST;	// ���ٷ���
	private DrivingPolicy	TIME_FIRST	= DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST; // ʱ������
	private DrivingPolicy	policy		= DIS_FIRST;

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

		DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
		PlanNode from = PlanNode.withLocation(new LatLng(33.640877, 114.690423));// ���:�ܿ�ʦ��ѧԺ
		PlanNode to = PlanNode.withLocation(new LatLng(33.580544, 114.665776));// �յ�:�ܿ�վ
		drivingOption.from(from);// ������,
		drivingOption.to(to);// �����յ�
		List<PlanNode> nodes = new ArrayList<PlanNode>();
		nodes.add(PlanNode.withCityNameAndPlaceName("�ܿ�", "�ܿ�������"));
		drivingOption.passBy(nodes);
		drivingOption.policy(policy);// ���üݳ�·�߹滮����
		routePlanSearch.drivingSearch(drivingOption);
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
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "δ���������", 0).show();
				return;
			}

			baiduMap.clear();
			DrivingRouteOverlay overlay = new MyDrivingOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// ���¼����ݸ�overlay
			overlay.setData(result.getRouteLines().get(0));// ����·������,��һ��·��
			overlay.addToMap();
			overlay.zoomToSpan();
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

		}
	}

	class MyDrivingOverlay extends DrivingRouteOverlay
	{

		public MyDrivingOverlay(BaiduMap arg0) {
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

	/**
	 * ��ť����¼�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_MENU:
				AlertDialog.Builder builder = new AlertDialog.Builder(DrivingRouteOverlayDemo.this);
				builder.setTitle("�ݳ�·�߲���");
				String[] items = new String[] { "���ӵ��", "��̾���", "���ٷ���", "ʱ������" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:// ���ӵ��
								policy = AVOID_JAM;
								search();
								break;
							case 1:// ��̾���
								policy = DIS_FIRST;
								search();
								break;
							case 2:// ���ٷ���
								policy = FEE_FIRST;
								search();
								break;
							case 3:// ʱ������
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
