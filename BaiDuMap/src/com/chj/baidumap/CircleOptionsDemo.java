package com.chj.baidumap;

import android.os.Bundle;

import com.baidu.mapapi.map.CircleOptions;
import com.chj.baidumap.base.BaseActivity;

/**
 * @����:com.chj.baidumap
 * @����:CircleOptionsDemo
 * @����:�»��
 * @ʱ��:2015-8-18 ����9:34:45
 * 
 * @����:Բ�θ�����
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
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
	 * ����Բ�θ�����
	 */
	private void drawCircle()
	{
		// ����һ��Բ
		// Բ��+�뾶
		// ��ɫ+�Ƿ����+Բ���߿�

		// Բ�θ����ﵽ����
		// 1.��������
		CircleOptions circleOptions = new CircleOptions();
		// 2.��������������
		circleOptions.center(Pos) // Բ��
						.radius(1000)
						// �뾶 ��λ��m
						.fillColor(0x60FF0000);// ͸���� �� �� ��
		// .stroke(new Stroke(10, 0x600FF000));//�߿� ����1:��· ����2:��ɫ
		// 3.�Ѷ�����ӵ���ͼ��
		baiduMap.addOverlay(circleOptions);
	}
}
