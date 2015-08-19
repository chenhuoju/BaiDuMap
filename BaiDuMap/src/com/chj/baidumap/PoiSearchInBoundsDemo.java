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
 * @包名:com.chj.baidumap
 * @类名:PoiSearchInBoundsDemo
 * @作者:陈火炬
 * @时间:2015-8-18 下午3:37:33
 * 
 * @描述:范围内搜索
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class PoiSearchInBoundsDemo extends BaseActivity
{
	private static String	KEY_WORD	= "鼓浪屿";
	private PoiSearch		poiSearch;				// POI检索接口对象

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(new MyListener());

		search();
	}

	/**
	 * 搜索
	 * 
	 * @鼓浪屿:24.452261,118.073486
	 * @左下(0.001733, 0.000453）24.450528,118.073033
	 * @右上(0.000321, -0.000567)24.45194,118.074053
	 */
	private void search()
	{
		PoiBoundSearchOption boundOption = new PoiBoundSearchOption();
		LatLngBounds latLngBounds = new LatLngBounds.Builder().
																include(new LatLng(24.450528, 118.073033))// 东北的点
																.include(new LatLng(24.45194, 118.074053))
																// 西南的点
																.build();// 创建地理范围对象
		boundOption.bound(latLngBounds);// 设置poi检索范围
		boundOption.keyword(KEY_WORD);// 设置检索关键字
		poiSearch.searchInBound(boundOption);// 只是把范围能搜索到的poi设置为地图的中心，同时会搜索到范围外的poi
	}

	/**
	 * 自定义类
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
				Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
				return;
			}
			PoiOverlay overlay = new MyPoiOverlay(baiduMap);// 处理poi的覆盖物
			baiduMap.setOnMarkerClickListener(overlay);// 把事件分发给overlay,overlay才能处理点击事件
			overlay.setData(result);// 设置结果
			overlay.addToMap();// 将搜索所有Overlay 添加到地图上
			overlay.zoomToSpan();// 缩放地图，使所有Overlay都在合适的视野内
									// 注:该方法只对Marker类型的overlay有效
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
			PoiInfo poiInfo = poiResult.getAllPoi().get(position);// 得到点击的那个poi信息
			String text = poiInfo.name + "," + poiInfo.address;
			Toast.makeText(getApplicationContext(), text, 1).show();
			return super.onPoiClick(position);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(PoiSearchInBoundsDemo.this);
				builder.setTitle("搜索");
				View view = View.inflate(getApplicationContext(), R.layout.poi_search_items, null);
				final EditText et_key = (EditText) view.findViewById(R.id.poi_et_key_items);
				builder.setView(view);
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						baiduMap.clear();// 清空地图所有的 Overlay 覆盖物以及 InfoWindow
						KEY_WORD = et_key.getText().toString();
						search();
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

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
