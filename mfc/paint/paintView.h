// paintView.h : interface of the CPaintView class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_PAINTVIEW_H__A1859826_764F_497F_A353_9355617CC5A8__INCLUDED_)
#define AFX_PAINTVIEW_H__A1859826_764F_497F_A353_9355617CC5A8__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CPaintView : public CView
{
protected: // create from serialization only
	CPaintView();
	DECLARE_DYNCREATE(CPaintView)

// Attributes
public:
	CPaintDoc* GetDocument();
	int m_Flags;
	CPoint pt;
	CRect rect;
//	CBitmap bitmap;
// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CPaintView)
	public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnUpdate(CView* pSender, LPARAM lHint, CObject* pHint);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CPaintView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CPaintView)
	afx_msg void OnLine();
	afx_msg void OnRect();
	afx_msg void OnEllipse();
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in paintView.cpp
inline CPaintDoc* CPaintView::GetDocument()
   { return (CPaintDoc*)m_pDocument; }
#endif

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_PAINTVIEW_H__A1859826_764F_497F_A353_9355617CC5A8__INCLUDED_)
