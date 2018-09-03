// Line.cpp: implementation of the CLine class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "paint.h"
#include "Line.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////
IMPLEMENT_SERIAL(CLine,CObject,1)
CLine::CLine()
{

}

CLine::~CLine()
{

}

CLine::CLine(int x1,int y1,int x2,int y2)
{
	m_x1=x1;
	m_x2=x2;
	m_y1=y1;
	m_y2=y2;
}

void CLine::Serialize(CArchive &ar)
{
if (ar.IsStoring())
	{
	ar<<m_x1<<m_x2<<m_y1<<m_y2;
	}
	else
	{
		ar>>m_x1>>m_x2>>m_y1>>m_y2;
	}

}
