// heapsort.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"


#include <iostream>
#include <list>
#include <fstream>
#include <vector>
#include <math.h>
using namespace std;
class Kopiec {
private:
	int* tab;
	int size;
	int find(int v, int a) {
		if (tab[a] == v)
			return a;
		if ((2 * a <= size) && (v <= tab[a])) {

			find(v, 2 * a);
		}
		if ((2 * a + 1 <= size) && (v <= tab[a])) {

			find(v, 2 * a + 1);
		}
		return -1; // nie ma 
	}
public:
	int insertSwapCount;
	int deleteSwapCount;
	Kopiec(int s) {
		size = 0;
		tab = new int[s + 2];
		insertSwapCount = 0;
		deleteSwapCount = 0;
	}
	void insert(int v) {
		int child = ++size;
		tab[child] = v;
		while (child > 1 && tab[child] < tab[div(child, 2).quot]) {
			swap(tab[child], tab[div(child, 2).quot]);
			child = div(child, 2).quot;
			insertSwapCount++;
		}
	}
	void napraw(int a) {
		int largest = a;
		if ((2 * a <= size) && (tab[2 * a] < tab[largest])) {
			largest = 2 * a;
		}
		if ((2 * a + 1 <= size) && (tab[2 * a + 1] < tab[largest])) {
			largest = 2 * a + 1;
		}
		if (largest != a) {
			swap(tab[a], tab[largest]);
			deleteSwapCount++;
			napraw(largest);
		}
	}
	int find(int v) {
		return find(v, 1);
	}
	void wypisz() {
		for (int i = 1; i <= size; i++) {
			cout << tab[i] << " ";
			
		}
		cout << " |" << endl;
	}
	int deleteMin() {
		int ret = tab[1];
		tab[1] = tab[size];
		--size;
		napraw(1);
		return ret;
	}
	bool empty() {
		if (size == 0)
			return true;
		return false;
	}
};


int main(int argc, char *args[]) {
	

	int input[] = {2,4,6,8,10,12,14,13,11,9,7,5,3,1 };
	int input2[100];
	int input3[100];
	for (int i = 0; i < 100; i++) {
		input2[i] = 100-i;
		if (i % 2 == 0) {
			input3[i] = 1;
		}
		else {
			input3[i] = 2;
		}
	}
	Kopiec k(9999);
	for (int i = 0; i < 14; i++) {
		k.insert(input[i]);
		//k.wypisz();
	}
	while (!k.empty()){
		cout << k.deleteMin() << " ";
		//k.wypisz();
	}
	endl(cout);
	cout << k.insertSwapCount << " " << k.deleteSwapCount << endl;
	endl(cout);


	Kopiec k2(9999);
	for (int i = 0; i < 100; i++) {
		k2.insert(input2[i]);
		//k.wypisz();
	}
	while (!k2.empty()) {
		cout << k2.deleteMin() << " ";
		//k.wypisz();
	}
	endl(cout);
	cout << k2.insertSwapCount << " " << k2.deleteSwapCount << endl;
	endl(cout);


	Kopiec k3(9999);
	for (int i = 0; i < 100; i++) {
		k3.insert(input3[i]);
		//k.wypisz();
	}
	while (!k3.empty()) {
		cout << k3.deleteMin() << " ";
		//k.wypisz();
	}
	endl(cout);
	cout << k3.insertSwapCount << " " << k3.deleteSwapCount << endl;
	endl(cout);


	system("pause");
	return 0;
}

