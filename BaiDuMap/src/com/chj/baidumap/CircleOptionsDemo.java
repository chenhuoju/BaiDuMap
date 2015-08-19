package com.chj.baidumap;

import android.os.Bundle;

import com.baidu.mapapi.map.CircleOptions;
import com.chj.baidumap.base.BaseActivity;

/**
 * @包名:com.chj.baidumap
 * @类名:CircleOptionsDemo
 * @作者:陈火炬
 * @时间:2015-8-18 上午9:34:45
 * 
 * @描述:圆形覆盖物
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class CircleOptionsDemo extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		drawCircle();
	}

	/**
	 * 绘制圆形覆盖物
	 */
	private void drawCircle()
	{
		// 定义一个圆
		// 圆心+半径
		// 颜色+是否填充+圆到线宽

		// 圆形覆盖物到操作
		// 1.创建对象
		CircleOptions circleOptions = new CircleOptions();
		// 2.给对象设置数据
		circleOptions.center(Pos) // 圆心
						.radius(1000)
						// 半径 单位是m
						.fillColor(0x60FF0000);// 透明度 红 绿 蓝
		// .stroke(new Stroke(10, 0x600FF000));//边框 参数1:线路 参数2:颜色
		// 3.把对象添加到地图中
		baiduMap.addOverlay(circleOptions);
	}
}
