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
	//��ʼ����λͼ��λ��
	goup=1000;
	//�Ƿ���ͣ
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
	//��Ļ���
	width=WindowRect.right-WindowRect.left;
	//��Ļ�߶�
	height=WindowRect.bottom-WindowRect.top;
	CDC Dc;
	 if(Dc.CreateCompatibleDC(pDC)==FALSE)
		  AfxMessageBox("Can't create DC");
	//�ڲ�ͬλ����ʾλͼ
  	Dc.SelectObject(backmap);
	//��ʾ����λͼʹ��������
	pDC->BitBlt((width-800)/2,0,800,height,&Dc,0,goup-2000,SRCCOPY);
	pDC->BitBlt((width-800)/2,0,800,height,&Dc,0,goup,SRCCOPY);

	CClientDC dc(this);

	//��ʾ
	//��鱳������
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
		{
			//��ʾ�ҷ��ӵ�
			if(game.back[i][j]==2)
				//����͸����ʾ����
				game.TransparentBitmap(dc.GetSafeHdc(), game.bmshot,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	
			//��ʾ�л�
			if(game.back[i][j]==3)
				game.TransparentBitmap(dc.GetSafeHdc(), game.enemy,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	
			//�ɻ�ը��
			if(game.back[i][j]==7||game.back[i][j]==8)
				game.TransparentBitmap(dc.GetSafeHdc(), game.enemydead,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	
			//�л��ӵ�
			if((game.back[i][j]==5)||(game.back[i][j]==4)||(game.back[i][j]==6))
				game.TransparentBitmap(dc.GetSafeHdc(), game.bmenemyshot,(width-800)/2+i*50+20,j*height/600*50, 0,0, game.cTransparentColor);	

		}
		//��ʾ����λͼ
	game.TransparentBitmap(dc.GetSafeHdc(), game.bmfire,(width-800)/2+game.pointfire.x*50+20,  game.pointfire.y*height/600*50, 0,0, game.cTransparentColor);	
	//��ʾ�ҷ��ɻ�
	game.TransparentBitmap(dc.GetSafeHdc(), game.plane,(width-800)/2+game.xStart*50+20,  game.yStart*height/600*50, 0,0, game.cTransparentColor);	
	//��ʾ��Ϣ
	game.DrawMessage(pDC,width,height);
	//��Ϣ�ķɻ�λͼ
	game.TransparentBitmap(dc.GetSafeHdc(), game.plane,(width-800)/2+20,  height-80, 0,0, game.cTransparentColor);	
	//��Ϣ�Ļ���λͼ
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
	//����λͼ����
	goup-=10;
	//λͼ���˱߽�
	if(goup<0)
	//λͼ�ڿ�ͷ
		goup=2000;
	//ÿ100�㣬������ִ��һ��
	if(goup%100==0)
	{
		//���ֵл�
		game.Enemyplaneout();
		//�л�����
		game.Enemyshot();
	}
	if(goup%1100==0)
		//����λͼ����
		game.FireOutIf();
	//�ػ�
	OnDraw(GetDC());
	//�������
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
			if(game.back[i][j]==2||game.back[i][j]==7||game.back[i][j]==8)
				game.back[i][j]=0;
	//�л��ӵ��ƶ�
	game.shotmove();
	CView::OnTimer(nIDEvent);

}


void CMy6_1View::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags) 
{
	// TODO: Add your message handler code here and/or call default
	switch(nChar)
	{
	case VK_F3:
		//��ͣ�Ƿ�
		bPause=!bPause;
		//�ǣ����ü�ʱ��
		if(bPause)
			SetTimer(1,200,NULL);
		//��ֹͣ��ʱ
		else
			KillTimer(1);
		break;
	//�ӵ���ť
	case VK_SPACE:
		//�ҷ������ӵ�
		game.Shot();
		break;
		//����
	case VK_LEFT:
		//λ�ü�1
		game.xStart--;
		//�߽�
		if(game.xStart<0)
			game.xStart=0;
		break;
		//����ͬ��
	case VK_RIGHT:
		game.xStart++;
		if(game.xStart>14)
			game.xStart=14;
		break;		
	//����
	case VK_UP:
		if(game.yStart>0)
			game.yStart--;
		break;
	//����
	case VK_DOWN:
		if(game.yStart<10)
			game.yStart++;
		break;
	}
	//�������λͼλ�ú��ҷ��ɻ�λ����ͬ
	if((game.pointfire.x==game.xStart)&&(game.pointfire.y==game.yStart))
		{
		//����λͼ��ʧ
			game.iffire=false;
           	if(game.fire>5)
			{
				game.fire++;
				game.numplane++;
			}
			else
				game.fire++;
			//����λͼ�ƶ����������ĵط�
			game.pointfire.y=-1;
		}

	OnDraw(GetDC());
	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}
