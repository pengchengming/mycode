// 6_1.h : main header file for the 6_1 application
//

#if !defined(AFX_6_1_H__AF9C10A5_D5E7_11D5_B2C4_5254AB2BD63E__INCLUDED_)
#define AFX_6_1_H__AF9C10A5_D5E7_11D5_B2C4_5254AB2BD63E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"       // main symbols

/////////////////////////////////////////////////////////////////////////////
// CMy6_1App:
// See 6_1.cpp for the implementation of this class
//

class CMy6_1App : public CWinApp
{
public:
	CMy6_1App();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMy6_1App)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation
	//{{AFX_MSG(CMy6_1App)
	afx_msg void OnAppAbout();
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_6_1_H__AF9C10A5_D5E7_11D5_B2C4_5254AB2BD63E__INCLUDED_)
