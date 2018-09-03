// Game.cpp: implementation of the CGame class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "6_1.h"
#include "Game.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CGame::CGame()
{
	int i,j;
	plane.LoadBitmap(IDB_BITMAP2);
	bmshot.LoadBitmap(IDB_BITMAP4);
	enemy.LoadBitmap(IDB_BITMAP3);
	enemydead.LoadBitmap(IDB_BITMAP5);
	bmenemyshot.LoadBitmap(IDB_BITMAP6);
	bmfire.LoadBitmap(IDB_BITMAP7);
	//����͸��ɫ
	cTransparentColor=RGB(192,192,192);
	//�ҷ��ɻ�����5
	numplane=5;
	//�ɻ���ʼ����λ��
	xStart=5;
	yStart=10;
	//����û�г���
	iffire=false;
	//����ǿ��
	fire=0;
	//����Ϊ0
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
			back[i][j]=0;
}


CGame::~CGame()
{

}

 
//��������һ��ָ������ɫ���͸��ɫ�����ɸı��С
//   hdc ��ʾ���
//   hBitmapҪ��ʾ��λͼ
//   xStart��xStart��ʾ��λ��
//   xadd,yadd��ʾ��λͼ�ļӴ��С
//   cTransparentColor���͸����������ɫ
void CGame::TransparentBitmap(HDC hdc, HBITMAP hBitmap, 
					     short xStart, short yStart, short xadd,short yadd,
						 COLORREF cTransparentColor)
{
   BITMAP     m_bm;
   COLORREF   cColor;  
   
   // ������ʱDC
   HDC     hMem, hBack, hObject, hTemp, hSave;
   hBack   = CreateCompatibleDC(hdc);
   hObject = CreateCompatibleDC(hdc);
   hMem    = CreateCompatibleDC(hdc);
   hSave   = CreateCompatibleDC(hdc);   
   hTemp   = CreateCompatibleDC(hdc);
   // ѡ��λͼ
   SelectObject(hTemp, hBitmap);   
   GetObject(hBitmap, sizeof(BITMAP), (LPSTR)&m_bm);
   //��ʾλͼ���
   POINT      ptSize;
   // ȡ��λͼ�Ŀ��
   ptSize.x = m_bm.bmWidth;     
    // ȡ��λͼ�ĸö�
   ptSize.y = m_bm.bmHeight;           
   // ת��Ϊ�߼���ֵ
   DPtoLP(hTemp, &ptSize, 1);   
   
   // ������ʱλͼ
   HBITMAP    bmBack, bmObject, bmMem, bmSave;
   // ��ɫλͼ
   bmBack   = CreateBitmap(ptSize.x, ptSize.y, 1, 1, NULL);    
   bmObject = CreateBitmap(ptSize.x, ptSize.y, 1, 1, NULL);
   // ���豸����λͼ
   bmMem    = CreateCompatibleBitmap(hdc, ptSize.x, ptSize.y);
   bmSave      = CreateCompatibleBitmap(hdc, ptSize.x, ptSize.y);

   // ����������ʱλͼѡ����ʱDC��
   HBITMAP    OldbmBack, OldbmObject, OldbmMem, OldbmSave;
   OldbmBack   = (HBITMAP)SelectObject(hBack, bmBack);
   OldbmObject = (HBITMAP)SelectObject(hObject, bmObject);
   OldbmMem    = (HBITMAP)SelectObject(hMem, bmMem);
   OldbmSave   = (HBITMAP)SelectObject(hSave, bmSave);

   // ����ӳ��ģʽ
   SetMapMode(hTemp, GetMapMode(hdc));
   // �ȱ���ԭʼλͼ
   BitBlt(hSave, 0, 0, ptSize.x, ptSize.y, hTemp, 0, 0, SRCCOPY);
   // ��������ɫ����Ϊ��͸������ɫ
   cColor = SetBkColor(hTemp, cTransparentColor);
   // ����Ŀ��������
   BitBlt(hObject, 0, 0, ptSize.x, ptSize.y, hTemp, 0, 0, SRCCOPY);
   // �ָ�ԴDC��ԭʼ����ɫ
   SetBkColor(hTemp, cColor);

   // ������ת��Ŀ��������
   BitBlt(hBack, 0, 0, ptSize.x, ptSize.y, hObject, 0, 0, NOTSRCCOPY);
   // ������DC�ı�����Ŀ��DC
   BitBlt(hMem, 0, 0, ptSize.x, ptSize.y, hdc, xStart, yStart, SRCCOPY);
   // ����λͼ����ʾ��
   BitBlt(hMem, 0, 0, ptSize.x, ptSize.y, hObject, 0, 0, SRCAND);
   // ����λͼ�е�͸��ɫ
   BitBlt(hTemp, 0, 0, ptSize.x, ptSize.y, hBack, 0, 0, SRCAND);
   // ��λͼ��Ŀ��DC�ı�����������
   BitBlt(hMem, 0, 0, ptSize.x, ptSize.y, hTemp, 0, 0, SRCPAINT);
   // ����Ŀ�굽��Ļ�� 
   StretchBlt(hdc, xStart, yStart, ptSize.x+xadd, ptSize.y+yadd, hMem, 0, 0, ptSize.x, ptSize.y,SRCCOPY);
   // �ָ�ԭʼλͼ
   BitBlt(hTemp, 0, 0, ptSize.x, ptSize.y, hSave, 0, 0, SRCCOPY);

   // ɾ����ʱ�ڴ�λͼ
   DeleteObject(SelectObject(hBack, OldbmBack));
   DeleteObject(SelectObject(hObject, OldbmObject));
   DeleteObject(SelectObject(hMem, OldbmMem));
   DeleteObject(SelectObject(hSave, OldbmSave));

   // ɾ����ʱ�ڴ�DC
   DeleteDC(hMem);
   DeleteDC(hBack);
   DeleteDC(hObject);
   DeleteDC(hSave);
   DeleteDC(hTemp);
}
//��2��ʾ�ӵ�
void CGame::Shot()
{
	int i;
	//����0  һ�߻���
	if(fire==0)
		//�ӷɻ�ǰ������ͷ
		for(i=0;i<yStart;i++)
		{
			//����ел�
			if(back[xStart][i]==3)
				//�л���ըλͼ
				back[xStart][i]=7;
			else
				//�ҷ��ӵ�λͼ
				back[xStart][i]=2;
		}
		//����1  ���߻���
	if(fire==1)
	{
		for(i=1;i<=yStart;i++)
		{
			//���������
			if((xStart-i)>=0)
			{
				//���Ͻ�
				if(back[xStart-i][yStart-i]==3)
					back[xStart-i][yStart-i]=7;
				else
					back[xStart-i][yStart-i]=2;
			}
			//���������
			if((xStart+i)<15)
			{
				//���Ͻ�
				if(back[xStart+i][yStart-i]==3)
					back[xStart+i][yStart-i]=7;
				else
					back[xStart+i][yStart-i]=2;
			}
		}
	}
	//����  ���߻���
	if(fire>1)
	{
		for(i=1;i<=yStart;i++)
		{
			if((xStart-i)>=0)
			{
				if(back[xStart-i][yStart-i]==3)
					back[xStart-i][yStart-i]=7;
				else
					back[xStart-i][yStart-i]=2;
			}
			//��ǰ��
			if(back[xStart][i]==3)
				//�л���ըλͼ
				back[xStart][yStart-i]=7;
			else
			    back[xStart][yStart-i]=2;

			if((xStart+i)<15)
			{
				if(back[xStart+i][yStart-i]==3)
					back[xStart+i][yStart-i]=7;
				else
					back[xStart+i][yStart-i]=2;
			}
			
		}
		
	}
}

void CGame::Enemyplaneout()
{
	int x,y;
	//��ʼ���漴������
	srand(GetTickCount());	
	//ѭ�����ɻ�����Ϊֹ
    do
	{
		x=rand()%15;
		y=rand()%3;
		//���λ�ÿ�
		if(back[x][y]==0)
			//��ʾ�л�
			back[x][y]=3;
	}while(back[x][y]==0);

}

void CGame::Enemyshot()
{
	int i,j;
	for(i=0;i<15;i++)
		for(j=0;j<12;j++)
			if(back[i][j]==3)
			{
				//������·���
				if(back[i-1][j+1]==0)
					//���鸳ֵ4����ʾ�ӵ������·��ƶ�
					back[i-1][j+1]=4;
				//����·���
				if(back[i][j+1]==0)
					//���鸳ֵ5����ʾ�ӵ����·��ƶ�
					back[i][j+1]=5;
				//������·���
				if(back[i+1][j+1]==0)
					//���鸳ֵ5����ʾ�ӵ������·��ƶ�
					back[i+1][j+1]=6;
			}
}

void CGame::shotmove()
{
	int i,j;
	for(j=11;j>=0;j--)
		for(i=14;i>=0;i--)
		{
			//���鸳ֵ4����ʾ�ӵ������·��ƶ�
			if(back[i][j]==4)
			{
				//�������
				back[i][j]=0;
				//���������
				if(i>0)
					//������·���
					if(back[i-1][j+1]==0)
						//���鸳ֵ4����ʾ�ӵ������·��ƶ�
						back[i-1][j+1]=4;
			}
			if(back[i][j]==5)
			{
				back[i][j]=0;
				if(back[i][j+1]==0)
					back[i][j+1]=5;
			}
			if(back[i][j]==6)
			{
				back[i][j]=0;
				if(i<14)
					if(back[i+1][j+1]==0)
						back[i+1][j+1]=6;
			}
			//����ел����ӵ�
			if((back[xStart][yStart]==3)||(back[xStart][yStart]==4)||(back[xStart][yStart]==5)||(back[xStart][yStart]==6))
			{
				//���鸳ֵ8����ʾ�ҷ���ը
				back[xStart][yStart]=8;
				//�ɻ�������1
				numplane--;
			}
			//����з��ӵ������·��߽�
			if((back[i][11]==4)||(back[i][11]==5)||(back[i][11]==6))
				//��ֵ0
				back[i][11]=0;
		}

}
//����λͼ�Ƿ����
void CGame::FireOutIf()
{
	//û�г���
	if(!iffire)
	{
		//���λ��
		pointfire.x=rand()%15;
		pointfire.y=rand()%8;
		//����
		iffire=true;
	}
	//����
	else
	{
		//������ҷ��ɻ�λ����ͬ
		if((pointfire.x==xStart)&&(pointfire.y==yStart))
		{
			//������
			iffire=false;
			//�����������5
			if(fire>5)
			{
				//������ǿ
				fire++;
				//�ɻ�������ǿ
				numplane++;
			}
			//����
			else
				//������ǿ
				fire++;
			//��ʧ
			pointfire.y=-1;
		}
		else
		{
			//λ������
			pointfire.y++;
			//����
			if(pointfire.y>11)
				//������
				iffire=false;
		}

				
	}
		
}

void CGame::DrawMessage(CDC *pDC,int width,int height)
{
	int nOldDC=pDC->SaveDC();	
	//��������
	CFont font;    
	if(0==font.CreatePointFont(250,"Comic Sans MS"))
					{
						AfxMessageBox("Can't Create Font");
					}
	pDC->SelectObject(&font);
    //����������ɫ���䱳����ɫ
	CString str;
	pDC->SetTextColor(RGB(0,10,244));
	pDC->SetBkColor(RGB(0,255,0));
    //�������
	str.Format("%d",numplane);	
	pDC->TextOut((width-800)/2+70,height-80,str);
	str.Format("%d",fire);	
	pDC->TextOut((width-800)/2+800-70,height-80,str);

	pDC->TextOut((width-800)/2+200,height-40,"��ͣ:F3   �˳�:Esc");

	pDC->RestoreDC(nOldDC);

}
