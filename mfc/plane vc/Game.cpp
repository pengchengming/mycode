// Game.cpp: implementation of the CGame class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "6_1.h"
#include "Game.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CGame::CGame()
{
	int i,j;
	plane.LoadBitmap(IDB_BITMAP2);
	bmshot.LoadBitmap(IDB_BITMAP4);
	enemy.LoadBitmap(IDB_BITMAP3);
	enemydead.LoadBitmap(IDB_BITMAP5);
	bmenemyshot.LoadBitmap(IDB_BITMAP6);
	bmfire.LoadBitmap(IDB_BITMAP7);
	//设置透明色
	cTransparentColor=RGB(192,192,192);
	//我发飞机数量5
	numplane=5;
	//飞机开始出现位置
	xStart=5;
	yStart=10;
	//火力没有出现
	iffire=false;
	//火力强度
	fire=0;
	//数组为0
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
			back[i][j]=0;
}


CGame::~CGame()
{

}

 
//本函数把一种指定的颜色变成透明色，并可改变大小
//   hdc 显示句柄
//   hBitmap要显示的位图
//   xStart，xStart显示的位置
//   xadd,yadd显示的位图的加大加小
//   cTransparentColor变成透明的那种颜色
void CGame::TransparentBitmap(HDC hdc, HBITMAP hBitmap, 
					     short xStart, short yStart, short xadd,short yadd,
						 COLORREF cTransparentColor)
{
   BITMAP     m_bm;
   COLORREF   cColor;  
   
   // 创建临时DC
   HDC     hMem, hBack, hObject, hTemp, hSave;
   hBack   = CreateCompatibleDC(hdc);
   hObject = CreateCompatibleDC(hdc);
   hMem    = CreateCompatibleDC(hdc);
   hSave   = CreateCompatibleDC(hdc);   
   hTemp   = CreateCompatibleDC(hdc);
   // 选入位图
   SelectObject(hTemp, hBitmap);   
   GetObject(hBitmap, sizeof(BITMAP), (LPSTR)&m_bm);
   //显示位图宽高
   POINT      ptSize;
   // 取得位图的宽度
   ptSize.x = m_bm.bmWidth;     
    // 取得位图的该度
   ptSize.y = m_bm.bmHeight;           
   // 转换为逻辑点值
   DPtoLP(hTemp, &ptSize, 1);   
   
   // 创建临时位图
   HBITMAP    bmBack, bmObject, bmMem, bmSave;
   // 单色位图
   bmBack   = CreateBitmap(ptSize.x, ptSize.y, 1, 1, NULL);    
   bmObject = CreateBitmap(ptSize.x, ptSize.y, 1, 1, NULL);
   // 与设备兼容位图
   bmMem    = CreateCompatibleBitmap(hdc, ptSize.x, ptSize.y);
   bmSave      = CreateCompatibleBitmap(hdc, ptSize.x, ptSize.y);

   // 将创建的临时位图选入临时DC中
   HBITMAP    OldbmBack, OldbmObject, OldbmMem, OldbmSave;
   OldbmBack   = (HBITMAP)SelectObject(hBack, bmBack);
   OldbmObject = (HBITMAP)SelectObject(hObject, bmObject);
   OldbmMem    = (HBITMAP)SelectObject(hMem, bmMem);
   OldbmSave   = (HBITMAP)SelectObject(hSave, bmSave);

   // 设置映射模式
   SetMapMode(hTemp, GetMapMode(hdc));
   // 先保留原始位图
   BitBlt(hSave, 0, 0, ptSize.x, ptSize.y, hTemp, 0, 0, SRCCOPY);
   // 将背景颜色设置为需透明的颜色
   cColor = SetBkColor(hTemp, cTransparentColor);
   // 创建目标屏蔽码
   BitBlt(hObject, 0, 0, ptSize.x, ptSize.y, hTemp, 0, 0, SRCCOPY);
   // 恢复源DC的原始背景色
   SetBkColor(hTemp, cColor);

   // 创建反转的目标屏蔽码
   BitBlt(hBack, 0, 0, ptSize.x, ptSize.y, hObject, 0, 0, NOTSRCCOPY);
   // 拷贝主DC的背景到目标DC
   BitBlt(hMem, 0, 0, ptSize.x, ptSize.y, hdc, xStart, yStart, SRCCOPY);
   // 屏蔽位图的显示区
   BitBlt(hMem, 0, 0, ptSize.x, ptSize.y, hObject, 0, 0, SRCAND);
   // 屏蔽位图中的透明色
   BitBlt(hTemp, 0, 0, ptSize.x, ptSize.y, hBack, 0, 0, SRCAND);
   // 将位图与目标DC的背景左异或操作
   BitBlt(hMem, 0, 0, ptSize.x, ptSize.y, hTemp, 0, 0, SRCPAINT);
   // 拷贝目标到屏幕上 
   StretchBlt(hdc, xStart, yStart, ptSize.x+xadd, ptSize.y+yadd, hMem, 0, 0, ptSize.x, ptSize.y,SRCCOPY);
   // 恢复原始位图
   BitBlt(hTemp, 0, 0, ptSize.x, ptSize.y, hSave, 0, 0, SRCCOPY);

   // 删除临时内存位图
   DeleteObject(SelectObject(hBack, OldbmBack));
   DeleteObject(SelectObject(hObject, OldbmObject));
   DeleteObject(SelectObject(hMem, OldbmMem));
   DeleteObject(SelectObject(hSave, OldbmSave));

   // 删除临时内存DC
   DeleteDC(hMem);
   DeleteDC(hBack);
   DeleteDC(hObject);
   DeleteDC(hSave);
   DeleteDC(hTemp);
}
//以2表示子弹
void CGame::Shot()
{
	int i;
	//火力0  一线火力
	if(fire==0)
		//从飞机前方到尽头
		for(i=0;i<yStart;i++)
		{
			//如果有敌机
			if(back[xStart][i]==3)
				//敌机被炸位图
				back[xStart][i]=7;
			else
				//我方子弹位图
				back[xStart][i]=2;
		}
		//火力1  两线火力
	if(fire==1)
	{
		for(i=1;i<=yStart;i++)
		{
			//如果不出界
			if((xStart-i)>=0)
			{
				//左上角
				if(back[xStart-i][yStart-i]==3)
					back[xStart-i][yStart-i]=7;
				else
					back[xStart-i][yStart-i]=2;
			}
			//如果不出界
			if((xStart+i)<15)
			{
				//右上角
				if(back[xStart+i][yStart-i]==3)
					back[xStart+i][yStart-i]=7;
				else
					back[xStart+i][yStart-i]=2;
			}
		}
	}
	//其他  三线火力
	if(fire>1)
	{
		for(i=1;i<=yStart;i++)
		{
			if((xStart-i)>=0)
			{
				if(back[xStart-i][yStart-i]==3)
					back[xStart-i][yStart-i]=7;
				else
					back[xStart-i][yStart-i]=2;
			}
			//正前方
			if(back[xStart][i]==3)
				//敌机被炸位图
				back[xStart][yStart-i]=7;
			else
			    back[xStart][yStart-i]=2;

			if((xStart+i)<15)
			{
				if(back[xStart+i][yStart-i]==3)
					back[xStart+i][yStart-i]=7;
				else
					back[xStart+i][yStart-i]=2;
			}
			
		}
		
	}
}

void CGame::Enemyplaneout()
{
	int x,y;
	//初始化随即数种子
	srand(GetTickCount());	
	//循环到飞机出现为止
    do
	{
		x=rand()%15;
		y=rand()%3;
		//如果位置空
		if(back[x][y]==0)
			//显示敌机
			back[x][y]=3;
	}while(back[x][y]==0);

}

void CGame::Enemyshot()
{
	int i,j;
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
			if(back[i][j]==3)
			{
				//如果左下方空
				if(back[i-1][j+1]==0)
					//数组赋值4，表示子弹向左下方移动
					back[i-1][j+1]=4;
				//如果下方空
				if(back[i][j+1]==0)
					//数组赋值5，表示子弹向下方移动
					back[i][j+1]=5;
				//如果右下方空
				if(back[i+1][j+1]==0)
					//数组赋值5，表示子弹向右下方移动
					back[i+1][j+1]=6;
			}
}

void CGame::shotmove()
{
	int i,j;
	for(j=11;j>=0;j--)
		for(i=14;i>=0;i--)
		{
			//数组赋值4，表示子弹向左下方移动
			if(back[i][j]==4)
			{
				//清除数组
				back[i][j]=0;
				//如果不出界
				if(i>0)
					//如果左下方空
					if(back[i-1][j+1]==0)
						//数组赋值4，表示子弹向左下方移动
						back[i-1][j+1]=4;
			}
			if(back[i][j]==5)
			{
				back[i][j]=0;
				if(back[i][j+1]==0)
					back[i][j+1]=5;
			}
			if(back[i][j]==6)
			{
				back[i][j]=0;
				if(i<14)
					if(back[i+1][j+1]==0)
						back[i+1][j+1]=6;
			}
			//如果有敌机或子弹
			if((back[xStart][yStart]==3)||(back[xStart][yStart]==4)||(back[xStart][yStart]==5)||(back[xStart][yStart]==6))
			{
				//数组赋值8，表示我方被炸
				back[xStart][yStart]=8;
				//飞机数量减1
				numplane--;
			}
			//如果敌方子弹到了下方边界
			if((back[i][11]==4)||(back[i][11]==5)||(back[i][11]==6))
				//赋值0
				back[i][11]=0;
		}

}
//火力位图是否出现
void CGame::FireOutIf()
{
	//没有出现
	if(!iffire)
	{
		//随机位置
		pointfire.x=rand()%15;
		pointfire.y=rand()%8;
		//出现
		iffire=true;
	}
	//出现
	else
	{
		//如果和我方飞机位置相同
		if((pointfire.x==xStart)&&(pointfire.y==yStart))
		{
			//不出现
			iffire=false;
			//如果火力大于5
			if(fire>5)
			{
				//火力加强
				fire++;
				//飞机数量加强
				numplane++;
			}
			//否则
			else
				//火力加强
				fire++;
			//消失
			pointfire.y=-1;
		}
		else
		{
			//位置下移
			pointfire.y++;
			//出界
			if(pointfire.y>11)
				//不出现
				iffire=false;
		}

				
	}
		
}

void CGame::DrawMessage(CDC *pDC,int width,int height)
{
	int nOldDC=pDC->SaveDC();	
	//设置字体
	CFont font;    
	if(0==font.CreatePointFont(250,"Comic Sans MS"))
					{
						AfxMessageBox("Can't Create Font");
					}
	pDC->SelectObject(&font);
    //设置字体颜色及其背景颜色
	CString str;
	pDC->SetTextColor(RGB(0,10,244));
	pDC->SetBkColor(RGB(0,255,0));
    //输出数字
	str.Format("%d",numplane);	
	pDC->TextOut((width-800)/2+70,height-80,str);
	str.Format("%d",fire);	
	pDC->TextOut((width-800)/2+800-70,height-80,str);

	pDC->TextOut((width-800)/2+200,height-40,"暂停:F3   退出:Esc");

	pDC->RestoreDC(nOldDC);

}
