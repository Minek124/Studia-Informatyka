#include<cstdlib>
#include<ctime>
#include<cstdio>
#include<algorithm>
#include<iostream>
#include<time.h>
#include<random>
#include<vector>
#include <cstdio>
#include <math.h>
#include <string>

using namespace std;

/*class Slownik{
private:
vector<vector<string>> tab;

int* find(string s){
char x=s[0];
int cords[2];
int ii=0;
for(auto i=tab[x].begin();i!=tab[x].end();i++){
if(*i==s) {
cords[0]=x;
cords[1]=ii;
return cords;
}
ii++;
}
return nullptr;
}

public:
Slownik(){
tab.resize(255);
}

void add(string s){
char x=s[0];
tab[x].push_back(s);
}

void del(string s){
int* cords=find(s);
if(cords!=nullptr)
tab[cords[0]][cords[1]]=nullptr;
else
cout<< "nie ma takiego klucza"<<endl;
//cout<<tab[cords[0]][cords[1]];
}
bool czyJest(string s){
int* cords=find(s);
if(cords!=nullptr)
return true;
return false;
}

};*/
class Slownik {
private:
	int pozycjaUniwersum(string x){
		int i=0;
		for(auto it:uniwersum){
			if(it==x)
				return i;
			i++;
		}
	}
public:

	vector<string> uniwersum;
	unsigned long long tab;
	unsigned long long tab2;

	Slownik(vector<string> u): uniwersum(u),tab(0),tab2(0){}

	void wypiszUniwersum(){
		for(auto it:uniwersum){
			cout<< it<< " , ";
		}
	}
	void wypiszZbior(){
		long long a=1;
		int i=0;
		for(auto it = tab; it > 0 && i<uniwersum.size() && i<60; it >>= 1){
			if (a & it)
				cout<< uniwersum[i]<< " ";
			i++;
		}

		for(auto it = tab2; it > 0 && i<uniwersum.size(); it >>= 1){
			if (a & it)
				cout<< uniwersum[i]<< " ";
			i++;
		}
		endl(cout);
	}

	void add(string x){
		int poz=pozycjaUniwersum(x);
		if(poz<60){
			long long a=pow(2,poz);
			tab=tab | a;
		}else{
			poz-=60;
			long long a=pow(2,poz);
			tab2=tab2 | a;
		}

	}

	void del(string x){
		int poz=pozycjaUniwersum(x);
		if(poz<60){
		long long a=pow(2,pozycjaUniwersum(x));
		a=~a;
		tab=tab & a;
		}else{
			poz-=60;
			long long a=pow(2,pozycjaUniwersum(x));
			a=~a;
			tab2=tab2 & a;
		}
	}

	bool czyJest(string x){
		int poz=pozycjaUniwersum(x);
		if(poz<60){
		long long a=pow(2,pozycjaUniwersum(x));
		a=(a & tab);
		if(a>0) return true;
		}else{
			poz-=60;
			long long a=pow(2,pozycjaUniwersum(x));
			a=(a & tab2);
			if(a>0) return true;
		}
		return false;
	}

	void add(int x){
		if(x<60){
		long long a=pow(2,x);
		tab=tab | a;
		}else{
			x-=60;
			long long a=pow(2,x);
			tab2=tab2 | a;
		}
	}

	void del(int x){
		if(x<60){
			long long a=pow(2,x);
			a=~a;
			tab=tab & a;
		}else{
			x-=60;
			long long a=pow(2,x);
			a=~a;
			tab2=tab2 & a;
		}
	}

	bool czyJest(int x){
		if(x<60){
		long long a=pow(2,x);
		a=(a & tab);
		if(a>0) return true;
		}else{
			x-=60;
			long long a=pow(2,x);
			a=(a & tab2);
			if(a>0) return true;
		}
		return false;
	}

	Slownik operator|(Slownik s){  // suma
		Slownik tmp(uniwersum);
		tmp.tab=this->tab | s.tab;
		tmp.tab2=this->tab2 | s.tab2;
		return tmp;
	}

	Slownik operator&(Slownik s){  // czesc wspolna
		Slownik tmp(uniwersum);
		tmp.tab=this->tab & s.tab;
		tmp.tab2=this->tab2 & s.tab2;
		return tmp;
	}

	Slownik operator-(Slownik s){ //roznica symetryczna, nie normalna
		Slownik tmp(uniwersum);
		auto czesc_wspolna=this->tab & s.tab;
		auto czesc_wspolna2=this->tab2 & s.tab2;
		auto suma=this->tab | s.tab;
		auto suma2=this->tab2 | s.tab2;
		tmp.tab=suma & (~czesc_wspolna);
		tmp.tab2=suma2 & (~czesc_wspolna2);
		return tmp;
	}

	Slownik operator~(){
		Slownik tmp(uniwersum);
		tmp.tab=~(this->tab);
		tmp.tab2=~(this->tab2);
		return tmp;
	}
};


int main(){

	vector<string> u;
	int n,m,p;
	cout<< "podaj moc uniwersum:";
	cin>> n;
	cout<< "podaj slowa do slownika:"<<endl;
	for(int i=0;i<n;i++){
		string x;
		cin>> x;
		u.push_back(x);
	}


	cout<< "podaj ilosc zbiorow:";
	cin>> m;
	vector<Slownik> A;
	for(int i=0;i<m;i++){
		Slownik s(u);
		A.push_back(s);
	}
	for(int i=0;i<m;i++){
		int c;
		cout<< "podaj ilosc slow do zbioru:";
		cin>>c;
		for(int j=0;j<c;j++){
			string x;
			cin>> x;
			A[i].add(x);
		}
	}
	cout<< "podaj ilosc operacji:";
	cin>> p;
	for(int i=0;i<p;i++){
		char x;
		char nr,nr2,nr3; // nr - numer zbioru na ktorym operujemy
		cin>>x;
		switch(x){
		case 'e':   // 1 element
			cin>>x;
			switch(x){
			case 'd':
				cin >> nr;
				nr= nr -97;
				cin >> x;
				x= x - 49;
				A[nr].add(x);
				break;
			case 'u':
				cin >> nr;
				nr= nr -97;
				cin >> x;
				x= x - 49;
				A[nr].del(x);
				break;
			case 's':
				cin >> nr;
				nr= nr -97;
				cin >> x;
				x= x - 49;
				if(A[nr].czyJest(x)) 
					cout<< "TAK"<<endl;
				else 
					cout<< "NIE"<<endl;
				break;
			default:
				break;
			}
			break;
		case 'z': //1 element
			cin>>x;
			switch(x){
			case 's':
				cin >> nr;
				cin >> nr2;
				cin >> nr3;
				nr= nr -97;
				nr2= nr2 -97;
				nr3= nr3 -97;
				A[nr3]=A[nr]|A[nr2];
				break;
			case 'r':
				cin >> nr;
				cin >> nr2;
				cin >> nr3;
				nr= nr -97;
				nr2= nr2 -97;
				nr3= nr3 -97;
				A[nr3]=A[nr]-A[nr2];
				break;
			case 'p':
				cin >> nr;
				cin >> nr2;
				cin >> nr3;
				nr= nr -97;
				nr2= nr2 -97;
				nr3= nr3 -97;
				A[nr3]=A[nr]&A[nr2];
				break;
			case 'd':
				cin >> nr;
				cin >> nr2;
				nr= nr -97;
				nr2= nr2-97;
				A[nr2]=~A[nr];
				break;
			case 'w':
				cin >> nr;
				nr= nr -97;
				A[nr].wypiszZbior();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	system("pause");
	return 0;
}