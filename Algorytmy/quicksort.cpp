using namespace std;

#include<cstdlib>
#include<ctime>
#include<cstdio>
#include<algorithm>
#include<iostream>
#include<vector>
#include<time.h>
#include<random>
const int maxZ = 100; // maksymalna ilosc testow
const int maxSize = 10; // maksymalny rozmiar pojedynczego testu
const int maxValue = 1000; // maksymalna wartosc liczby z ciagu liczb


bool czyposortowana(vector<double> &t)
{
	for(auto i=t.begin();i<t.end()-1;i++)
		if((*i)>(*(i+1))) return false;
	return true;
}

void wyswietl(vector<double> &t){
	cout<< endl;
	for(auto i=t.begin();i<t.end();i++)
		cout<< *i<<endl;
}


int dziel(vector<double> &t,int l,int r){
	int srodek=t[l];
	int i=l;
	int j=r;
	int x;
	while(true)
	{
		while(t[j] > srodek) j--;
		while(t[i] < srodek) i++;
		if(i<j)
		{
			x=t[i];
			t[i]=t[j];
			t[j]=x;
			i++;
			j--;
		}
		else
			return j;
	}

}


void quicksort(vector<double> &t,int l,int r){
	int a;
	if(l<r){
		a=dziel(t,l,r);
		quicksort(t,l,a);
		quicksort(t,a+1,r);
	}
}

/*int quicksort2(vector<double> &t,int pierwszy,int ostatni,int p){
	int x;
				
				int pivot=p;
				x=t[pivot];
				t[pivot]=t[ostatni];
				t[ostatni]=x;
				int j=pierwszy;
				int swap;
				for(int i=pierwszy+1;i<ostatni;i++){
					if(t[i]<x){
						swap=t[j];
						t[j]=t[i];
						t[i]=swap;
						j++;
				}
				}
				if(j!=(ostatni-1)){
				t[ostatni]=t[j];
				t[j]=x;
				return j;
				}
				return ostatni;
}*/

int main() {
	vector<double> tab;
	vector<int> temp;
	vector<vector<int>> Todo; 
	int z;
	int size;
	srand(time(NULL));
	cout<< "podaj ilosc testow:";
	cin >>z;
	for(int g=0; g<z; g++) {
		tab.clear();
		cout<< "podaj ilosc liczb:";
		scanf("%d", &size);
		cout<< "\npodaj liczby, kazda akceptujac enterem:\n";
		double x;
		for(int j=0; j<size; j++) {
			//cin >>x;
			tab.push_back(rand() & maxValue);	
		}
		int typ;
		cout<< "\n podales liczby:\n";
		wyswietl(tab);
		cout<< "podaj typ quicksort \n 1-z rekurencja \n 2-bez rekurencji ";
		cin>> typ;
		switch(typ){
		
			case 1:
				// Z rekurencja _________________________________________________
				quicksort(tab,0,tab.size()-1);
				wyswietl(tab);
				break;
		
			case 2:
				// Bez rekurencji ____________________________________________
				int ostatni=tab.size()-1;
				int pivot=(int)(tab.size() / 2);
				int pivot2;
				int pierwszy;
				temp.clear();
				temp.push_back(1);
				temp.push_back(0);
				temp.push_back(tab.size()-1);
				Todo.clear();
				Todo.push_back(temp);
				int i=0;
				auto n=1;
				while(i<n){
					if(Todo[i][1]<Todo[i][2]){
					pivot=dziel(tab,Todo[i][1],Todo[i][2]);
															//void quicksort(vector<double> &t,int l,int r){
						temp.clear();						//		int a;
						temp.push_back(1);					//		if(l<r){
						temp.push_back(Todo[i][1]);			//			a=dziel(t,l,r);
						temp.push_back(pivot);				//			quicksort(t,l,a);
						Todo.push_back(temp);				//			quicksort(t,a+1,r);
						n++;

						temp.clear();
						temp.push_back(1); // nie potrzebne juz
						temp.push_back(pivot+1);
						temp.push_back(Todo[i][2]);
						Todo.push_back(temp);
						n++;
					}
					i++;
				}
				wyswietl(tab);
			
				break;
	
	
		}; //Switch
	};
	system("pause");
	return 0;
}
