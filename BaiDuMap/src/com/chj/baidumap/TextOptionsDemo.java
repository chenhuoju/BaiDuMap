package com.chj.baidumap;

import android.graphics.Typeface;
import android.os.Bundle;

import com.baidu.mapapi.map.TextOptions;
import com.chj.baidumap.base.BaseActivity;

/**
 * @包名:com.chj.baidumap
 * @类名:TextOptionsDemo
 * @作者:陈火炬
 * @时间:2015-8-18 上午10:07:16
 * 
 * @描述:文字覆盖物,展示文字
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class TextOptionsDemo extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		drawText();
	}

	/**
	 * 绘制文字覆盖物
	 */
	private void drawText()
	{
		TextOptions textOptions = new TextOptions();
		textOptions.fontColor(0x60FF0000)//字体颜色
					.text("鼓浪屿定位")//文字内容
					.position(Pos)//位置
					.fontSize(32)//字体大小
					.typeface(Typeface.SERIF)// 字体
					.rotate(24);// 旋转
		baiduMap.addOverlay(textOptions);
	}
}
