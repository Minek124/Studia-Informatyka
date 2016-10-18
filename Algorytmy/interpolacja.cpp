#include <iostream>
#include <time.h>
#include <fstream>

using namespace std;

const int ILOSC_WEZLOW=10;
double fx[ILOSC_WEZLOW];

// metoda lagrange
double L(double x,int b){
	double x1=b-3;
	double x2=b-2;
	double x3=b-1;
	double x4=b;
	double y1=fx[b-3];
	double y2=fx[b-2];
	double y3=fx[b-1];
	double y4=fx[b];
	return (y1*((x-x2)/(x1-x2))*((x-x3)/(x1-x3))*((x-x4)/(x1-x4)) + y2*((x-x1)/(x2-x1))*((x-x3)/(x2-x3))*((x-x4)/(x2-x4)) + y3*((x-x1)/(x3-x1))*((x-x2)/(x3-x2))*((x-x4)/(x3-x4)) + y4*((x-x1)/(x4-x1))*((x-x2)/(x4-x2))*((x-x3)/(x4-x3)));
}



int main(){
	srand(time(NULL));
	fstream file;
	file.open("wykres.txt",ios::out);
	double skok=0.01;
	for(int i=0;i<ILOSC_WEZLOW;i++){
		fx[i]=(rand()/(double)RAND_MAX)*4-2;
	}
	int b=3;
	int c=0;
	for(double i=0;i<ILOSC_WEZLOW-skok;i=i+skok){
		if((i+2)>=b && b<=9)
			b++;	
		if((c%10)==0)
			cout<<"f("<<i<<")= "<<L(i,b)<<endl;
		file<<i<<" "<<L(i,b)<<endl;
		file.flush();
		c++;
	}
	file.close();


	system("pause");
	return 0;
}