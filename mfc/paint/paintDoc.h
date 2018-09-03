// paintDoc.h : interface of the CPaintDoc class
//
/////////////////////////////////////////////////////////////////////////////
#include<afxtempl.h>
#include"Line.h"
#include"Rect.h"
#include"Ellipse.h"
#if !defined(AFX_PAINTDOC_H__1713CC7C_7D13_4951_BC36_D6985D817037__INCLUDED_)
#define AFX_PAINTDOC_H__1713CC7C_7D13_4951_BC36_D6985D817037__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CPaintDoc : public CDocument
{
protected: // create from serialization only
	CPaintDoc();
	DECLARE_DYNCREATE(CPaintDoc)

// Attributes
public:
	CTypedPtrList<CObList,CLine*>m_LineList;
	CTypedPtrList<CObList,CEllipse*>m_EllipseList;
	CTypedPtrList<CObList,CRectan*>m_RectanList;

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPaintDoc)
	public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	//}}AFX_VIRTUAL

// Implementation
public:
	void DrawRect(CDC* pDC);
	void DrawEllipse(CDC* pDC);
	void DrawLine(CDC* pDC);
	void NewRectan(int x1,int y1,int x2,int y2);
	void NewEllipse(int x1,int y1,int x2,int y2);
	void NewLine(int x1,int y1,int x2,int y2);
	virtual ~CPaintDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CPaintDoc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PAINTDOC_H__1713CC7C_7D13_4951_BC36_D6985D817037__INCLUDED_)
