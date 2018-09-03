// 6_1View.h : interface of the CMy6_1View class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_6_1VIEW_H__9FBE772D_D48B_11D5_B2C4_5254AB2BD63E__INCLUDED_)
#define AFX_6_1VIEW_H__9FBE772D_D48B_11D5_B2C4_5254AB2BD63E__INCLUDED_

#include "Game.h"	// Added by ClassView
#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CMy6_1View : public CView
{
protected: // create from serialization only
	CMy6_1View();
	DECLARE_DYNCREATE(CMy6_1View)

// Attributes
public:
	CMy6_1Doc* GetDocument();

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMy6_1View)
	public:
	virtual void OnDraw(CDC* pDC);  // overridden to draw this view
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	protected:
	virtual BOOL OnPreparePrinting(CPrintInfo* pInfo);
	virtual void OnBeginPrinting(CDC* pDC, CPrintInfo* pInfo);
	virtual void OnEndPrinting(CDC* pDC, CPrintInfo* pInfo);
	//}}AFX_VIRTUAL

// Implementation
public:
	//暂停
	bool bPause;
	//屏幕宽度
	int width;
	//屏幕高度
	int height;
	//类CGame
	CGame game;
	//背景位图移动大小：10
	int goup;
	//背景位图
	CBitmap backmap;
	virtual ~CMy6_1View();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CMy6_1View)
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnTimer(UINT nIDEvent);
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in 6_1View.cpp
inline CMy6_1Doc* CMy6_1View::GetDocument()
   { return (CMy6_1Doc*)m_pDocument; }
#endif

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_6_1VIEW_H__9FBE772D_D48B_11D5_B2C4_5254AB2BD63E__INCLUDED_)
