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
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chj.baidumap.base.BaseActivity;

/**
 * 
 * @包名:com.chj.baidumap
 * @类名:PoiSearchNearByDemo
 * @作者:陈火炬
 * @时间:2015-8-18 下午5:09:39
 * 
 * @描述:周边搜索,即圆形区域搜索
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class PoiSearchNearByDemo extends BaseActivity
{
	private static String	KEY_WORD	= "揽月湖";
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
	 */
	private void search()
	{
		// 圆形覆盖区域
		CircleOptions circleOptions = new CircleOptions();
		circleOptions.center(new LatLng(33.640877, 114.690423)).radius(1000).fillColor(0x60FF0000);
		baiduMap.addOverlay(circleOptions);

		// 周边搜索
		PoiNearbySearchOption nearbyOption = new PoiNearbySearchOption();
		nearbyOption.location(new LatLng(33.640877, 114.690423));// 设置中心点
		nearbyOption.radius(1000);// 设置半径，单位是米
		nearbyOption.keyword(KEY_WORD);// 设置关键字
		poiSearch.searchNearby(nearbyOption);
	}

	/**
	 * 自定义类
	 */
	class MyListener implements OnGetPoiSearchResultListener
	{

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result)
		{
			if (result == null || SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error)
			{
				Toast.makeText(getApplicationContext(), "未搜索到结果", 0).show();
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

			PoiDetailSearchOption detailOption = new PoiDetailSearchOption();
			detailOption.poiUid(poiInfo.uid);// 设置poi的uid,欲检索的poi的uid
			poiSearch.searchPoiDetail(detailOption);// POI 详情检索
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
				AlertDialog.Builder builder = new AlertDialog.Builder(PoiSearchNearByDemo.this);
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
