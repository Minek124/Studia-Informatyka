#include <iostream>
#include <conio.h>
#include <math.h>
using namespace std;

#define wzor ((x*x-1)*sinh(x)*sinh(x)*sinh(x))
#define epsilon 0.00000000001

double f(double x){
	return wzor;
}
double metodaSiecznych(double a,double b){
	double fx=f(b);
	double fxPrev=f(a);
	double xk=b-((fx*(b-a))/(fx-fxPrev));
	double fxk=f(xk);
	if(abs(fxk)<epsilon)
		return xk;
	return metodaSiecznych(b,xk);
}

int main(){
	double a=0;  
	double b=1;     
	double i=0;
	double skok=0.1;
	int znak=1;
	double suma=skok;
	while(i<5){
		a=i-skok;
		b=i;
		double x=metodaSiecznych(a,b);
		cout<<"dla przedzialu <"<<a<<" , "<<b<<"> miejsce zerowe f( "<<x<<" )= "<<f(x)<<endl;
		
		suma+=skok;
		i=i+znak*suma;

		if(znak==1){
			znak=-1;
		}else{
			znak=1;
		}
	}

	
	system("pause");
	return 0;
}