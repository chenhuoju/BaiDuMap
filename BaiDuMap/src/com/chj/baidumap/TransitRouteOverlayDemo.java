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
 * @����:com.chj.baidumap
 * @����:TransitRouteOverlayDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����9:04:56
 * 
 * @����:��������·��
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class TransitRouteOverlayDemo extends BaseActivity
{
	private RoutePlanSearch	routePlanSearch;

	private static String	CITY			= "�ܿ�";
	// ��������·�߹滮����
	private TransitPolicy	NO_SUBWAY		= TransitRoutePlanOption.TransitPolicy.EBUS_NO_SUBWAY;		// ��������
	private TransitPolicy	TIME_FIRST		= TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST;	// ʱ������
	private TransitPolicy	TRANSFER_FIRST	= TransitRoutePlanOption.TransitPolicy.EBUS_TRANSFER_FIRST; // ���ٻ���
	private TransitPolicy	WALK_FIRST		= TransitRoutePlanOption.TransitPolicy.EBUS_WALK_FIRST;	// ���ٲ��о���
	private TransitPolicy	policy			= TRANSFER_FIRST;

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
		TransitRoutePlanOption transitOption = new TransitRoutePlanOption();
		PlanNode from = PlanNode.withLocation(new LatLng(33.640877, 114.690423));// ���:�ܿ�ʦ��ѧԺ
		PlanNode to = PlanNode.withLocation(new LatLng(33.580544, 114.665776));// �յ�:�ܿ�վ
		transitOption.from(from);// ������,
		transitOption.to(to);// �����յ�
		transitOption.city(CITY);// ���û��˲���
		transitOption.policy(policy);
		routePlanSearch.transitSearch(transitOption);
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
		 * ��������·��
		 */
		@Override
		public void onGetTransitRouteResult(TransitRouteResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "δ���������", 0).show();
				return;
			}

			baiduMap.clear();
			TransitRouteOverlay overlay = new MyTransitOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// ���¼����ݸ�overlay
			overlay.setData(result.getRouteLines().get(0));// ����·������,��һ��·��
			overlay.addToMap();
			overlay.zoomToSpan();
		}

		/**
		 * ����·��
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
				AlertDialog.Builder builder = new AlertDialog.Builder(TransitRouteOverlayDemo.this);
				builder.setTitle("��������·�߲���");
				String[] items = new String[] { "��������", "ʱ������", "���ٻ���", "���ٲ��о���" };
				builder.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:// ��������
								policy = NO_SUBWAY;
								search();
								dialog.dismiss();
								break;
							case 1:// ʱ������
								policy = TIME_FIRST;
								search();
								dialog.dismiss();
								break;
							case 2:// ���ٻ���
								policy = TRANSFER_FIRST;
								search();
								dialog.dismiss();
								break;
							case 3:// ���ٲ��о���
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
