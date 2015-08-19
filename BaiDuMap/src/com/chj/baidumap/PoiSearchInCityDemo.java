package com.chj.baidumap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chj.baidumap.base.BaseActivity;

/**
 * 
 * @����:com.chj.baidumap
 * @����:PoiSearchNearByDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����5:09:39
 * 
 * @����:����������
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class PoiSearchInCityDemo extends BaseActivity
{
	private static String	CITY				= "�ܿ�";
	private static String	KEY_WORD			= "ѧԺ";
	private PoiSearch		poiSearch;					// POI�����ӿڶ���
	private int				currentPageIndex	= 0;

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
	 */
	private void search()
	{
		PoiCitySearchOption cityOption = new PoiCitySearchOption();
		cityOption.city(CITY);
		cityOption.keyword(KEY_WORD);
		cityOption.pageNum(currentPageIndex);
		poiSearch.searchInCity(cityOption);
	}

	/**
	 * �Զ�����
	 */
	class MyListener implements OnGetPoiSearchResultListener
	{

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "δ���������", 0).show();
				return;
			}

			// String text = result.getAddress() + "::" + result.getCommentNum()
			// + "::" + result.getEnvironmentRating();
			// Toast.makeText(getApplicationContext(), text, 0).show();
		}

		@Override
		public void onGetPoiResult(PoiResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "δ���������", 0).show();
				return;
			}
			String text = "��" + result.getTotalPageNum() + "ҳ����"
							+ result.getTotalPoiNum() + "������ǰ��"
							+ result.getCurrentPageNum() + "ҳ����ǰҳ"
							+ result.getCurrentPageCapacity() + "��";
			Toast.makeText(getApplicationContext(), text, 1).show();

			baiduMap.clear();// ��յ�ͼ���е� Overlay �������Լ� InfoWindow
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

			PoiDetailSearchOption detailOption = new PoiDetailSearchOption();
			detailOption.poiUid(poiInfo.uid);// ����poi��uid,��������poi��uid
			poiSearch.searchPoiDetail(detailOption);// POI �������
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
			case KeyEvent.KEYCODE_VOLUME_UP:// �������Ӽ�
				currentPageIndex++;
				search();
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:// ������С��
				break;
			case KeyEvent.KEYCODE_MENU:
				AlertDialog.Builder builder = new AlertDialog.Builder(PoiSearchInCityDemo.this);
				builder.setTitle("����");
				View view = View.inflate(getApplicationContext(), R.layout.poi_search_items, null);
				final RelativeLayout rl_city = (RelativeLayout) view.findViewById(R.id.rl_city);
				final EditText et_city = (EditText) view.findViewById(R.id.poi_et_city_items);
				final EditText et_key = (EditText) view.findViewById(R.id.poi_et_key_items);
				rl_city.setVisibility(View.VISIBLE);
				builder.setView(view);
				builder.setPositiveButton("ȷ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						baiduMap.clear();
						CITY = et_city.getText().toString();
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
