// 6_1Doc.cpp : implementation of the CMy6_1Doc class
//

#include "stdafx.h"
#include "6_1.h"

#include "6_1Doc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CMy6_1Doc

IMPLEMENT_DYNCREATE(CMy6_1Doc, CDocument)

BEGIN_MESSAGE_MAP(CMy6_1Doc, CDocument)
	//{{AFX_MSG_MAP(CMy6_1Doc)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CMy6_1Doc construction/destruction

CMy6_1Doc::CMy6_1Doc()
{
	// TODO: add one-time construction code here

}

CMy6_1Doc::~CMy6_1Doc()
{
}

BOOL CMy6_1Doc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}



/////////////////////////////////////////////////////////////////////////////
// CMy6_1Doc serialization

void CMy6_1Doc::Serialize(CArchive& ar)
{
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
// CMy6_1Doc diagnostics

#ifdef _DEBUG
void CMy6_1Doc::AssertValid() const
{
	CDocument::AssertValid();
}

void CMy6_1Doc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG

/////////////////////////////////////////////////////////////////////////////
// CMy6_1Doc commands
