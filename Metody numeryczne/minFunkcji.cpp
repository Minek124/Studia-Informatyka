#include <iostream>
#include <math.h>
#include <string>
#include <conio.h>
#include <time.h>
using namespace std;

#define wzor (1-x)*(1-x)+100*(y-(x*x))*(y-(x*x));

int main(){
	srand(time(NULL));
	const int iloscLosowan=999999;
	const int iloscBisekcji=20;
	double zasieg=100;
	double x=0;
	double y=0;
	double min=wzor;
	double cordX=0;
	double cordY=0;
	double i;
	double j;
	double fx;
	int it=0;
	while(it++<iloscBisekcji){
		int it2=0;
		while(it2++<iloscLosowan){
			x=(((double)rand()/RAND_MAX)*2*zasieg)-zasieg+cordX;
			y=(((double)rand()/RAND_MAX)*2*zasieg)-zasieg+cordY;
			fx=wzor;
			if(fx<min){
				min=fx;
				i=x;
				j=y;
			}
		}
		cout<<"dla:"<<it<<" iteracji minimum funkcji f("<<i<<","<<j<<")="<<min<<endl;
		cordX=i;
		cordY=j;
		zasieg/=10;
	}
	

	system("pause");
	return 0;
}