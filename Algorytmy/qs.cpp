// qs.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include<iostream>
#include<random>
#include<time.h>
using namespace std;


void show(int *t,int size) {
	cout << endl;
	for (auto i = 0; i<size; i++)
		cout << t[i] << endl;
}

int pivot(int *t, int l, int r) {
	int srodek = t[l];
	int i = l;
	int j = r;
	while (true)
	{
		while (t[j] > srodek) j--;
		while (t[i] < srodek) i++;
		if (i<j) {
			swap(t[i], t[j]);
			i++;
			j--;
		}
		else
			return j;
	}

}

void quicksort(int *t, int l, int r) {
	int a;
	if (l<r) {
		a = pivot(t, l, r);
		quicksort(t, l, a);
		quicksort(t, a + 1, r);
	}
}

int main() {
	srand(time(NULL));
	int* tab;
	int size = 400000000;
	clock_t start, end;	
	//cout << "N = ";
	//scanf("%d", &size);
	tab = new int[size];
	for (int j = 0; j<size; j++) {
		tab[j] = rand();
	}
	//show(tab,size);
	//quicksort(tab, 0, size - 1);
	start = clock();
	pivot(tab, 0, size - 1);
	end = clock();
	//show(tab,size);
	//cout.precision(20);
	cout << "time: "<< (double)(end - start) / CLOCKS_PER_SEC;
	system("pause");
	return 0;
}
