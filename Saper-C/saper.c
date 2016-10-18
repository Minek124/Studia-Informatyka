#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <time.h>
#include <string.h>

int bomby[100][100];
int ilosc[100][100];
int odkryte[100][100];
int flagi[100][100];
char widok[100][100];
int bok=10;
int iloscb=10;
int koniec=0;
int przegrana;
int y;
int	x;
int oldx;
int oldy;
int fc=1;
void odkryj();
void rozstaw();
void przelicz();
void rysujb();
void rysuji();
void rysujw();
void domyslnie();
void aktualizuj();
void sprawdz();


	void rozstaw()
	{
		int i;
		for (i=1 ; i <= iloscb ;i++)
		{
			int a,b;
			a=(rand()%bok)+1;
			b=(rand()%bok)+1;
			if (bomby[a][b]==1 || (a==y && b==x)) i--;
			if (bomby[a][b]==0 && (a!=y || b!=x)) bomby[a][b]=1;
		}
	}
	void przelicz()
	{
		int suma;
		int i,j;
		for (i=1;i<=bok;i++)
			{	
				for (j=1;j<=bok;j++)
				{	
					suma=0;
					if(bomby[i-1][j-1]==1) suma++;
					if(bomby[i-1][j]==1) suma++;
					if(bomby[i-1][j+1]==1) suma++;
					if(bomby[i][j-1]==1) suma++;
					if(bomby[i][j+1]==1) suma++;
					if(bomby[i+1][j-1]==1) suma++;
					if(bomby[i+1][j]==1) suma++;
					if(bomby[i+1][j+1]==1) suma++;
					if(bomby[i][j]==1) suma=9;
					ilosc[i][j]=suma;	
				}
		}

	}

	void rysujb()
	{
		
		int i,j;
		for (i=1;i<=bok;i++)
			{
				printf("\n");
				printf("|");
				for (j=1;j<=bok;j++)
				{
					printf("%i|",bomby[i][j]);
				}
		}
	printf("\n");
	}	
		
	void rysuji()
	{
		int i,j;
		for (i=1;i<=bok;i++)
			{
				printf("\n");
				printf("|");
				for (j=1;j<=bok;j++)
				{
					printf("%i|",ilosc[i][j]);
				}			
		}
			
		printf("\n");
	}	
	void rysujw()
	{
		system("cls");
		int i,j;
		for (i=1;i<=bok;i++)
			{
				printf("\n");
				printf("|");
				for (j=1;j<=bok;j++)
				{
					if(j==x && i==y) printf("%c|",widok[i][j]);
					else
					{
					if(flagi[i][j]==1) printf("*|");
					else
					{
						if(odkryte[i][j]==0) printf("%c|",widok[i][j]);
						if(odkryte[i][j]==1) printf("%i|",ilosc[i][j]);
					}
					}
				}			
		}

		printf("\n");
	}	

	void domyslnie()
	{
		
		int j;
		int i;
		for(i=0;i<=99;i++)
			for(j=0;j<=99;j++)
				widok[i][j]='#';
		for(i=0;i<=99;i++)
			for(j=0;j<=99;j++)
				odkryte[i][j]=0;
		for(i=0;i<=99;i++)
			for(j=0;j<=99;j++)
				flagi[i][j]=0;
		for(i=0;i<=99;i++)
			for(j=0;j<=99;j++)
				bomby[i][j]=0;
	}
	void aktualizuj()
	{
		if(odkryte[oldy][oldx]==0 && widok[y][x]!='*') widok[oldy][oldx]='#';
		if(widok[y][x]!='*') widok[y][x]='@';
		rysujw();
	}

	void sprawdz()
	{
		int i,j;
		int suma =0;
		if (fc==1)
		{
			rozstaw();
			przelicz();
		}
		if(flagi[y][x]!=1)
		{

			if(ilosc[y][x]==9) 
			{
				koniec=1;
				przegrana=1;
				aktualizuj();
			}
			if(ilosc[y][x]!=9) 
			{
				odkryte[y][x]=1;
				odkryj();
				aktualizuj();
				for(i=1;i<=bok;i++)
					for(j=1;j<=bok;j++)
						if(odkryte[i][j]==1) suma++;
				if (suma>=(bok*bok)-iloscb) 
				{
					koniec = 1;
					przegrana = 0;
				}
			}
		}
	}
	void odkryj()
	{
		int wszystko=0;
		int j;
		int i;
		while(wszystko==0)
		{
			wszystko=1;
			for(i=1;i<=bok;i++)
				for(j=1;j<=bok;j++)
					if (odkryte[i][j]==1 && ilosc[i][j]==0)
					{
						
						if(odkryte[i-1][j-1]==0) {odkryte[i-1][j-1]=1;wszystko=0;}
						if(odkryte[i-1][j]==0) {odkryte[i-1][j]=1;wszystko=0;}
						if(odkryte[i-1][j+1]==0) {odkryte[i-1][j+1]=1;wszystko=0;}
						if(odkryte[i][j+1]==0) {odkryte[i][j+1]=1;wszystko=0;}
						if(odkryte[i][j-1]==0) {odkryte[i][j-1]=1;wszystko=0;}
						if(odkryte[i+1][j-1]==0) {odkryte[i+1][j-1]=1;wszystko=0;}
						if(odkryte[i+1][j]==0) {odkryte[i+1][j]=1;wszystko=0;}
						if(odkryte[i+1][j+1]==0) {odkryte[i+1][j+1]=1;wszystko=0;}

					}
		}
		rysujw();
	}
int main()
{
	srand((unsigned int)time(NULL));
	char input;
	
printf("Wtiam w grze Saper\n");
	do
	{
	printf("podaj dlugosc boku: ");
	scanf("%i",&bok);
	printf("ilosc bomb: ");
	scanf("%i",&iloscb);
	}
	while (iloscb>=(bok*bok));
	x=bok/2;
	y=bok/2;
	
	domyslnie();
	rysujw();
	do
	{
		input = getch();
		oldy=y;
		oldx=x;

		switch(input)
		{
			case 'w':
				if(y>1) y--;
			break;
			
			case 's':
				if(y<bok) y++;
			break;
			
			case 'a':
				if(x>1) x--;
			break;
			
			case 'd':
				if(x<bok) x++;
			break;
			
			case 'e':
				sprawdz();
				fc=0;
			break;

			case 'q':
				if(flagi[y][x]==0) flagi[y][x]=1;
				else flagi[y][x]=0;
			break;
		} 
		aktualizuj();
		
	}
	while(koniec==0);
	if(przegrana==1) 
	{
		printf("Przegrales!\n");
		rysujb();
		rysuji();
	}
	if(przegrana==0) printf("Wygrales!\n");
	getch();
	return 0;
}