// 6_1View.cpp : implementation of the CMy6_1View class
//

#include "stdafx.h"
#include "6_1.h"

#include "6_1Doc.h"
#include "6_1View.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CMy6_1View

IMPLEMENT_DYNCREATE(CMy6_1View, CView)

BEGIN_MESSAGE_MAP(CMy6_1View, CView)
	//{{AFX_MSG_MAP(CMy6_1View)
	ON_WM_CREATE()
	ON_WM_TIMER()
	ON_WM_KEYDOWN()
	//}}AFX_MSG_MAP
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, CView::OnFilePrintPreview)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CMy6_1View construction/destruction

CMy6_1View::CMy6_1View()
{
	// TODO: add construction code here
	backmap.LoadBitmap(IDB_BITMAP1);
	//开始背景位图的位置
	goup=1000;
	//是否暂停
	bPause=true;
}

CMy6_1View::~CMy6_1View()
{
}

BOOL CMy6_1View::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CView::PreCreateWindow(cs);
}

/////////////////////////////////////////////////////////////////////////////
// CMy6_1View drawing

void CMy6_1View::OnDraw(CDC* pDC)
{
	int i,j;
	CMy6_1Doc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	// TODO: add draw code for native data here
	CRect WindowRect;
	GetWindowRect(&WindowRect);
	//屏幕宽度
	width=WindowRect.right-WindowRect.left;
	//屏幕高度
	height=WindowRect.bottom-WindowRect.top;
	CDC Dc;
	 if(Dc.CreateCompatibleDC(pDC)==FALSE)
		  AfxMessageBox("Can't create DC");
	//在不同位置显示位图
  	Dc.SelectObject(backmap);
	//显示两张位图使它们连接
	pDC->BitBlt((width-800)/2,0,800,height,&Dc,0,goup-2000,SRCCOPY);
	pDC->BitBlt((width-800)/2,0,800,height,&Dc,0,goup,SRCCOPY);

	CClientDC dc(this);

	//显示
	//检查背景数组
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
		{
			//显示我方子弹
			if(game.back[i][j]==2)
				//利用透明显示函数
				game.TransparentBitmap(dc.GetSafeHdc(), game.bmshot,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	
			//显示敌机
			if(game.back[i][j]==3)
				game.TransparentBitmap(dc.GetSafeHdc(), game.enemy,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	
			//飞机炸毁
			if(game.back[i][j]==7||game.back[i][j]==8)
				game.TransparentBitmap(dc.GetSafeHdc(), game.enemydead,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	
			//敌机子弹
			if((game.back[i][j]==5)||(game.back[i][j]==4)||(game.back[i][j]==6))
				game.TransparentBitmap(dc.GetSafeHdc(), game.bmenemyshot,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	

		}
		//显示火力位图
	game.TransparentBitmap(dc.GetSafeHdc(), game.bmfire,(width-800)/2+game.pointfire.x*50+20,  game.pointfire.y*height/600*50, 0,0, game.cTransparentColor);	
	//显示我方飞机
	game.TransparentBitmap(dc.GetSafeHdc(), game.plane,(width-800)/2+game.xStart*50+20,  game.yStart*height/600*50, 0,0, game.cTransparentColor);	
	//显示信息
	game.DrawMessage(pDC,width,height);
	//信息的飞机位图
	game.TransparentBitmap(dc.GetSafeHdc(), game.plane,(width-800)/2+20,  height-80, 0,0, game.cTransparentColor);	
	//信息的火力位图
	game.TransparentBitmap(dc.GetSafeHdc(), game.bmfire,(width-800)/2+800-120, height-80, 0,0, game.cTransparentColor);	


}

/////////////////////////////////////////////////////////////////////////////
// CMy6_1View printing

BOOL CMy6_1View::OnPreparePrinting(CPrintInfo* pInfo)
{
	// default preparation
	return DoPreparePrinting(pInfo);
}

void CMy6_1View::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add extra initialization before printing
}

void CMy6_1View::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add cleanup after printing
}

/////////////////////////////////////////////////////////////////////////////
// CMy6_1View diagnostics

#ifdef _DEBUG
void CMy6_1View::AssertValid() const
{
	CView::AssertValid();
}

void CMy6_1View::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CMy6_1Doc* CMy6_1View::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CMy6_1Doc)));
	return (CMy6_1Doc*)m_pDocument;
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CMy6_1View message handlers

int CMy6_1View::OnCreate(LPCREATESTRUCT lpCreateStruct) 
{
	if (CView::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	// TODO: Add your specialized creation code here
	SetTimer(1,400,NULL);
	return 0;
}

void CMy6_1View::OnTimer(UINT nIDEvent) 
{
	// TODO: Add your message handler code here and/or call default
	int i,j;
	//背景位图下移
	goup-=10;
	//位图到了边界
	if(goup<0)
	//位图在开头
		goup=2000;
	//每100点，即两秒执行一次
	if(goup%100==0)
	{
		//出现敌机
		game.Enemyplaneout();
		//敌机发射
		game.Enemyshot();
	}
	if(goup%1100==0)
		//火力位图操作
		game.FireOutIf();
	//重画
	OnDraw(GetDC());
	//数组清空
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
			if(game.back[i][j]==2||game.back[i][j]==7||game.back[i][j]==8)
				game.back[i][j]=0;
	//敌机子弹移动
	game.shotmove();
	CView::OnTimer(nIDEvent);

}


void CMy6_1View::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags) 
{
	// TODO: Add your message handler code here and/or call default
	switch(nChar)
	{
	case VK_F3:
		//暂停是否
		bPause=!bPause;
		//是，设置计时器
		if(bPause)
			SetTimer(1,200,NULL);
		//否，停止计时
		else
			KillTimer(1);
		break;
	//子弹按钮
	case VK_SPACE:
		//我方发射子弹
		game.Shot();
		break;
		//左移
	case VK_LEFT:
		//位置减1
		game.xStart--;
		//边界
		if(game.xStart<0)
			game.xStart=0;
		break;
		//道理同上
	case VK_RIGHT:
		game.xStart++;
		if(game.xStart>14)
			game.xStart=14;
		break;		
	//上跳
	case VK_UP:
		if(game.yStart>0)
			game.yStart--;
		break;
	//下跳
	case VK_DOWN:
		if(game.yStart<10)
			game.yStart++;
		break;
	}
	//如果火力位图位置和我方飞机位置相同
	if((game.pointfire.x==game.xStart)&&(game.pointfire.y==game.yStart))
		{
		//火力位图消失
			game.iffire=false;
           	if(game.fire>5)
			{
				game.fire++;
				game.numplane++;
			}
			else
				game.fire++;
			//火力位图移动到见不到的地方
			game.pointfire.y=-1;
		}

	OnDraw(GetDC());
	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}
