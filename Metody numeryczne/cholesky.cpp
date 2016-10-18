#include <iostream>
#include <conio.h>
#include <math.h>
#include "matrix.h"

using namespace std;
int main(){
	
	long double tab[]={
  18.000, 22.000, 54.000, 42.000,
  22.000, 70.000, 86.000, 62.000,
  54.000, 86.000, 174.000, 134.000,
  42.000, 62.000, 134.000, 106.000};
	long double tab2[]={
	 1,    -1,    -1,    -1,    -1,
    -1,     2 ,    0  ,   0   ,  0,
    -1  ,   0    , 3    , 1    , 1,
    -1   ,  0 ,    1     ,4   ,  2,
	-1 ,    0   ,  1 ,    2  ,   5};
	long double tab3[]={19,13,10,10,13,-17,13,13,10,10,-11,13,10,10,10,-2,10,10,10,10,-2,10,10,10,13,-11,10,10,13,13,-17,13,10,10,13,19};

	Matrix<long double> x(tab,4,4);
	Matrix<long double> y(tab2,5,5);
	Matrix<long double> z(tab3,6,6);	
	
	//cholesky decomposition
	cout<< "rozklad choleskiego macierzy:"<<endl;
	cout<<x;
	cout<< x.choleskyDecomposition();
	cout<< "rozklad choleskiego macierzy:"<<endl;
	cout<<y;
	cout<< y.choleskyDecomposition();
	system("pause");
	return 0;
}