// paintView.cpp : implementation of the CPaintView class
//

#include "stdafx.h"
#include "paint.h"

#include "paintDoc.h"
#include "paintView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CPaintView

IMPLEMENT_DYNCREATE(CPaintView, CView)

BEGIN_MESSAGE_MAP(CPaintView, CView)
	//{{AFX_MSG_MAP(CPaintView)
	ON_COMMAND(ID_LINE, OnLine)
	ON_COMMAND(ID_RECT, OnRect)
	ON_COMMAND(ID_ELLIPSE, OnEllipse)
	ON_WM_LBUTTONUP()
	ON_WM_LBUTTONDOWN()
	ON_WM_MOUSEMOVE()
	//}}AFX_MSG_MAP
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, CView::OnFilePrintPreview)
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPaintView construction/destruction

CPaintView::CPaintView()
{
	// TODO: add construction code here
	m_Flags=0;
//	bitmap.CreateBitmap(20,20,NULL,NULL,IDB_BITMAP1);
}

CPaintView::~CPaintView()
{
}

BOOL CPaintView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CView::PreCreateWindow(cs);
}

/////////////////////////////////////////////////////////////////////////////
// CPaintView drawing

void CPaintView::OnDraw(CDC* pDC)
{
	CPaintDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);

	pDoc->DrawEllipse(pDC);
	pDoc->DrawLine(pDC);
	pDoc->DrawRect(pDC);
	if(m_Flags==3)
	{
		pDC->Ellipse(rect);
	}
	if(m_Flags==1)
	{
	pDC->MoveTo(rect.left,rect.top);
	pDC->LineTo(rect.right,rect.bottom);
	}
	if(m_Flags==2)
	{
		pDC->Rectangle(rect);
	}
	// TODO: add draw code for native data here
}

/////////////////////////////////////////////////////////////////////////////
// CPaintView printing

BOOL CPaintView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// default preparation
	return DoPreparePrinting(pInfo);
}

void CPaintView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add extra initialization before printing
}

void CPaintView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: add cleanup after printing
}

/////////////////////////////////////////////////////////////////////////////
// CPaintView diagnostics

#ifdef _DEBUG
void CPaintView::AssertValid() const
{
	CView::AssertValid();
}

void CPaintView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

CPaintDoc* CPaintView::GetDocument() // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CPaintDoc)));
	return (CPaintDoc*)m_pDocument;
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CPaintView message handlers

void CPaintView::OnLine() 
{
	// TODO: Add your command handler code here
	m_Flags=1;
}

void CPaintView::OnRect() 
{
	// TODO: Add your command handler code here
	m_Flags=2;
}


void CPaintView::OnEllipse() 
{
	// TODO: Add your command handler code here
	m_Flags=3;
}


void CPaintView::OnLButtonUp(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default
	CPaintDoc* pDoc = GetDocument();
	CRect m_Rect(pt.x,pt.y,point.x,point.y);
	if(m_Flags==3)
	{
		pDoc->NewEllipse(m_Rect.left,m_Rect.top,m_Rect.right,m_Rect.bottom);
		
	}
	if(m_Flags==1)
	{
	pDoc->NewLine(m_Rect.left,m_Rect.top,m_Rect.right,m_Rect.bottom);
	}
	if(m_Flags==2)
	{
		pDoc->NewRectan(m_Rect.left,m_Rect.top,m_Rect.right,m_Rect.bottom);
	}
	this->Invalidate(FALSE);
	CView::OnLButtonUp(nFlags, point);
}

void CPaintView::OnLButtonDown(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default
	CPaintDoc* pDoc = GetDocument();
	pt.x=point.x;
	pt.y=point.y;
	this->Invalidate();
	CView::OnLButtonDown(nFlags, point);
}

void CPaintView::OnMouseMove(UINT nFlags, CPoint point) 
{
	// TODO: Add your message handler code here and/or call default

	if(MK_LBUTTON & nFlags)
	{
	rect.left=pt.x;
	rect.top=pt.y;
	rect.right=point.x;
	rect.bottom=point.y;
	this->Invalidate();
	}

	CView::OnMouseMove(nFlags, point);
}

void CPaintView::OnUpdate(CView* pSender, LPARAM lHint, CObject* pHint) 
{
	// TODO: Add your specialized code here and/or call the base class
	
}
