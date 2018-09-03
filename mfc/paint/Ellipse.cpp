// Ellipse.cpp: implementation of the CEllipse class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "paint.h"
#include "Ellipse.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////
IMPLEMENT_SERIAL(CEllipse,CObject,1)
CEllipse::CEllipse()
{}

CEllipse::~CEllipse()
{}

CEllipse::CEllipse(int x1,int y1,int x2,int y2)
{

	m_x1=x1;
	m_x2=x2;
	m_y1=y1;
	m_y2=y2;
}

void CEllipse::Serialize(CArchive &ar)
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
