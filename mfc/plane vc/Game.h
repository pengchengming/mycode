// Game.h: interface for the CGame class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_GAME_H__50A0D243_D504_11D5_B2C4_5254AB2BD63E__INCLUDED_)
#define AFX_GAME_H__50A0D243_D504_11D5_B2C4_5254AB2BD63E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CGame  
{
public:
	CGame();
	virtual ~CGame();
	//显示信息
	void DrawMessage(CDC*pDC,int width,int height);
	//火力位图是否出现
	void FireOutIf();
	//子弹移动
	void shotmove();
	//敌机发射子弹
	void Enemyshot();
	//敌机出现
	void Enemyplaneout();
	//我方射击
	void Shot();
	//设置透明位图
	void TransparentBitmap(HDC hdc, HBITMAP hBitmap, 
					     short xStart, short yStart, short xadd,short yadd,
						 COLORREF cTransparentColor);
	//透明色
    COLORREF cTransparentColor;
	//飞机爆炸位图
	CBitmap enemydead;
	//敌机子弹位图
	CBitmap bmenemyshot;
	//敌机位图
	CBitmap enemy;
	//我方子弹
	CBitmap bmshot;
	//我方飞机
	CBitmap plane;
	//我方飞机数量
	short numplane;
	//背景数组
	int back[15][12];
	//飞机出现位置
	int xStart,yStart;
	//火力位图
	CBitmap bmfire;
	//火力位置
	CPoint pointfire;
	//是否出现
	bool iffire;
	//火力强度
	int fire;

};

#endif // !defined(AFX_GAME_H__50A0D243_D504_11D5_B2C4_5254AB2BD63E__INCLUDED_)
