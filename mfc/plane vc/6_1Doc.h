// 6_1Doc.h : interface of the CMy6_1Doc class
//
/////////////////////////////////////////////////////////////////////////////

#if !defined(AFX_6_1DOC_H__AF9C10AB_D5E7_11D5_B2C4_5254AB2BD63E__INCLUDED_)
#define AFX_6_1DOC_H__AF9C10AB_D5E7_11D5_B2C4_5254AB2BD63E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000


class CMy6_1Doc : public CDocument
{
protected: // create from serialization only
	CMy6_1Doc();
	DECLARE_DYNCREATE(CMy6_1Doc)

// Attributes
public:

// Operations
public:

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CMy6_1Doc)
	public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);
	//}}AFX_VIRTUAL

// Implementation
public:
	virtual ~CMy6_1Doc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	//{{AFX_MSG(CMy6_1Doc)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_6_1DOC_H__AF9C10AB_D5E7_11D5_B2C4_5254AB2BD63E__INCLUDED_)
