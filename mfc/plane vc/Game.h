// Game.h: interface for the CGame class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_GAME_H__50A0D243_D504_11D5_B2C4_5254AB2BD63E__INCLUDED_)
#define AFX_GAME_H__50A0D243_D504_11D5_B2C4_5254AB2BD63E__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class CGame  
{
public:
	CGame();
	virtual ~CGame();
	//��ʾ��Ϣ
	void DrawMessage(CDC*pDC,int width,int height);
	//����λͼ�Ƿ����
	void FireOutIf();
	//�ӵ��ƶ�
	void shotmove();
	//�л������ӵ�
	void Enemyshot();
	//�л�����
	void Enemyplaneout();
	//�ҷ����
	void Shot();
	//����͸��λͼ
	void TransparentBitmap(HDC hdc, HBITMAP hBitmap, 
					     short xStart, short yStart, short xadd,short yadd,
						 COLORREF cTransparentColor);
	//͸��ɫ
    COLORREF cTransparentColor;
	//�ɻ���ըλͼ
	CBitmap enemydead;
	//�л��ӵ�λͼ
	CBitmap bmenemyshot;
	//�л�λͼ
	CBitmap enemy;
	//�ҷ��ӵ�
	CBitmap bmshot;
	//�ҷ��ɻ�
	CBitmap plane;
	//�ҷ��ɻ�����
	short numplane;
	//��������
	int back[15][12];
	//�ɻ�����λ��
	int xStart,yStart;
	//����λͼ
	CBitmap bmfire;
	//����λ��
	CPoint pointfire;
	//�Ƿ����
	bool iffire;
	//����ǿ��
	int fire;

};

#endif // !defined(AFX_GAME_H__50A0D243_D504_11D5_B2C4_5254AB2BD63E__INCLUDED_)
