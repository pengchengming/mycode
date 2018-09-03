// Ellipse.h: interface for the CEllipse class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_ELLIPSE_H__6EF0DA16_32AB_4D7F_B73A_FE05BAA44F16__INCLUDED_)
#define AFX_ELLIPSE_H__6EF0DA16_32AB_4D7F_B73A_FE05BAA44F16__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CEllipse : public CObject  
{
		DECLARE_SERIAL(CEllipse)
public:
	int m_x1,m_y1,m_x2,m_y2;
public:
	CEllipse();
	virtual ~CEllipse();
	CEllipse(int x1,int y1,int x2,int y2);
	virtual void Serialize(CArchive &ar);

};

#endif // !defined(AFX_ELLIPSE_H__6EF0DA16_32AB_4D7F_B73A_FE05BAA44F16__INCLUDED_)
