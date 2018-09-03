// Rect.h: interface for the CRect class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_RECT_H__2310142C_66E7_4525_A75F_B7B4964ED821__INCLUDED_)
#define AFX_RECT_H__2310142C_66E7_4525_A75F_B7B4964ED821__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CRectan : public CObject  
{
	DECLARE_SERIAL(CRectan)
public:
int m_x1,m_y1,m_x2,m_y2;
public:
	CRectan();
	CRectan(int x1,int y1,int x2,int y2);
	virtual void Serialize(CArchive &ar);
	virtual ~CRectan();

};

#endif // !defined(AFX_RECT_H__2310142C_66E7_4525_A75F_B7B4964ED821__INCLUDED_)
