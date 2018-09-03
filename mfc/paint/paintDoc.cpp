// paintDoc.cpp : implementation of the CPaintDoc class
//

#include "stdafx.h"
#include "paint.h"

#include "paintDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CPaintDoc

IMPLEMENT_DYNCREATE(CPaintDoc, CDocument)

BEGIN_MESSAGE_MAP(CPaintDoc, CDocument)
	//{{AFX_MSG_MAP(CPaintDoc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CPaintDoc construction/destruction

CPaintDoc::CPaintDoc()
{
	// TODO: add one-time construction code here

}

CPaintDoc::~CPaintDoc()
{
}

BOOL CPaintDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}



/////////////////////////////////////////////////////////////////////////////
// CPaintDoc serialization

void CPaintDoc::Serialize(CArchive& ar)
{
m_LineList.Serialize(ar);
m_EllipseList.Serialize(ar);
m_RectanList.Serialize(ar);
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}

/////////////////////////////////////////////////////////////////////////////
// CPaintDoc diagnostics

#ifdef _DEBUG
void CPaintDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CPaintDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CPaintDoc commands

void CPaintDoc::NewLine(int x1,int y1,int x2,int y2)
{
 CLine* pLineItem=new CLine(x1,y1,x2,y2);
 m_LineList.AddTail(pLineItem);
 this->SetModifiedFlag();
}

void CPaintDoc::NewEllipse(int x1,int y1,int x2,int y2)
{
	CEllipse* pEllipseItem=new CEllipse(x1,y1,x2,y2);
 m_EllipseList.AddTail(pEllipseItem);
 this->SetModifiedFlag();
}

void CPaintDoc::NewRectan(int x1,int y1,int x2,int y2)
{
	CRectan* pRectanItem=new CRectan(x1,y1,x2,y2);
	m_RectanList.AddTail(pRectanItem);
 this->SetModifiedFlag();
}

void CPaintDoc::DrawLine(CDC* pDC)
{
	POSITION pos=m_LineList.GetHeadPosition();
	while(pos!=NULL)
	{
		CLine* Line=m_LineList.GetNext(pos);
		pDC->MoveTo(Line->m_x1,Line->m_y1);
			pDC->LineTo(Line->m_x2,Line->m_y2);
	}
}

void CPaintDoc::DrawEllipse(CDC* pDC)
{
	POSITION pos=m_EllipseList.GetHeadPosition();
	while(pos!=NULL)
	{
		CEllipse* Ellipse=m_EllipseList.GetNext(pos);
		pDC->Ellipse(Ellipse->m_x1,Ellipse->m_y1,Ellipse->m_x2,Ellipse->m_y2);
	}

}

void CPaintDoc::DrawRect(CDC* pDC)
{
	POSITION pos=m_RectanList.GetHeadPosition();
	while(pos!=NULL)
	{
		CRectan* Rectan=m_RectanList.GetNext(pos);
		pDC->Rectangle(Rectan->m_x1,Rectan->m_y1,Rectan->m_x2,Rectan->m_y2);
	}

}
