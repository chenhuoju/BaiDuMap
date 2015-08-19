package com.chj.baidumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chj.baidumap.base.BaseActivity;

/**
 * @����:com.chj.baidumap
 * @����:PoiSearchInBoundsDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����3:37:33
 * 
 * @����:��Χ������
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class PoiSearchInBoundsDemo extends BaseActivity
{
	private static String	KEY_WORD	= "������";
	private PoiSearch		poiSearch;				// POI�����ӿڶ���

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(new MyListener());

		search();
	}

	/**
	 * ����
	 * 
	 * @������:24.452261,118.073486
	 * @����(0.001733, 0.000453��24.450528,118.073033
	 * @����(0.000321, -0.000567)24.45194,118.074053
	 */
	private void search()
	{
		PoiBoundSearchOption boundOption = new PoiBoundSearchOption();
		LatLngBounds latLngBounds = new LatLngBounds.Builder().
																include(new LatLng(24.450528, 118.073033))// �����ĵ�
																.include(new LatLng(24.45194, 118.074053))
																// ���ϵĵ�
																.build();// ��������Χ����
		boundOption.bound(latLngBounds);// ����poi������Χ
		boundOption.keyword(KEY_WORD);// ���ü����ؼ���
		poiSearch.searchInBound(boundOption);// ֻ�ǰѷ�Χ����������poi����Ϊ��ͼ�����ģ�ͬʱ����������Χ���poi
	}

	/**
	 * �Զ�����
	 */
	class MyListener implements OnGetPoiSearchResultListener
	{

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result)
		{

		}

		@Override
		public void onGetPoiResult(PoiResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "δ���������", 0).show();
				return;
			}
			PoiOverlay overlay = new MyPoiOverlay(baiduMap);// ����poi�ĸ�����
			baiduMap.setOnMarkerClickListener(overlay);// ���¼��ַ���overlay,overlay���ܴ������¼�
			overlay.setData(result);// ���ý��
			overlay.addToMap();// ����������Overlay ��ӵ���ͼ��
			overlay.zoomToSpan();// ���ŵ�ͼ��ʹ����Overlay���ں��ʵ���Ұ��
									// ע:�÷���ֻ��Marker���͵�overlay��Ч
		}

	}

	class MyPoiOverlay extends PoiOverlay
	{

		public MyPoiOverlay(BaiduMap arg0) {
			super(arg0);
		}

		@Override
		public boolean onPoiClick(int position)
		{
			PoiResult poiResult = getPoiResult();
			PoiInfo poiInfo = poiResult.getAllPoi().get(position);// �õ�������Ǹ�poi��Ϣ
			String text = poiInfo.name + "," + poiInfo.address;
			Toast.makeText(getApplicationContext(), text, 1).show();
			return super.onPoiClick(position);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(PoiSearchInBoundsDemo.this);
				builder.setTitle("����");
				View view = View.inflate(getApplicationContext(), R.layout.poi_search_items, null);
				final EditText et_key = (EditText) view.findViewById(R.id.poi_et_key_items);
				builder.setView(view);
				builder.setPositiveButton("ȷ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						baiduMap.clear();// ��յ�ͼ���е� Overlay �������Լ� InfoWindow
						KEY_WORD = et_key.getText().toString();
						search();
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("ȡ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
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
