package com.chj.baidumap;

import android.graphics.Typeface;
import android.os.Bundle;

import com.baidu.mapapi.map.TextOptions;
import com.chj.baidumap.base.BaseActivity;

/**
 * @����:com.chj.baidumap
 * @����:TextOptionsDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����10:07:16
 * 
 * @����:���ָ�����,չʾ����
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
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
	 * �������ָ�����
	 */
	private void drawText()
	{
		TextOptions textOptions = new TextOptions();
		textOptions.fontColor(0x60FF0000)//������ɫ
					.text("�����춨λ")//��������
					.position(Pos)//λ��
					.fontSize(32)//�����С
					.typeface(Typeface.SERIF)// ����
					.rotate(24);// ��ת
		baiduMap.addOverlay(textOptions);
	}
}
