// Line.h: interface for the CLine class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_LINE_H__83073462_97C3_431B_ADE7_D8918492043E__INCLUDED_)
#define AFX_LINE_H__83073462_97C3_431B_ADE7_D8918492043E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CLine : public CObject  
{
		DECLARE_SERIAL(CLine)
public:
	int m_x1,m_y1,m_x2,m_y2;
public:
	CLine();
	virtual ~CLine();
	CLine(int x1,int y1,int x2,int y2);
	virtual void Serialize(CArchive &ar);
};

#endif // !defined(AFX_LINE_H__83073462_97C3_431B_ADE7_D8918492043E__INCLUDED_)
