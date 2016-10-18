#include <iostream>
#include <conio.h>
#include <ctime>
#include <random>
#include <string>
#include <list>
using namespace std;
struct Wezel{
	char wart;
	int p;
	Wezel *lewo;
	Wezel *prawo;
	Wezel(int v,int pr){
		wart=v;
		lewo=nullptr;
		prawo=nullptr;
		p=pr;
	}
};
class DrzewoH{
private:
	Wezel *root;
	vector<bool> tmp;

	void print(Wezel *w){
		cout<<w->wart<<endl;
		if(w->lewo!=nullptr){
			print(w->lewo);
		}
		if(w->prawo!=nullptr){
			print(w->prawo);
		}
	}

	void czytaj(vector<bool> tab,Wezel *w){
		for(auto i=tab.begin();i!=tab.end();i++){
			if(*i){
				if(w->prawo!=nullptr){
					w=w->prawo;
				}else{
					cout<<w->wart<<endl;
					w=root;
					i--;
				}
			}else{
				if(w->lewo!=nullptr){
					w=w->lewo;
				}else{
					cout<<w->wart<<endl;
					w=root;
					i--;
				}
			}
		}
		cout<<w->wart<<endl;
	}
	bool kod(char x,Wezel *w){
		if(w->wart==x)
			return true;
		if(w->lewo!=nullptr){
			tmp.push_back(false);
			if(kod(x,w->lewo))
				return true;
		}
		if(w->prawo!=nullptr){
			tmp.push_back(true);
			if(kod(x,w->prawo))
				return true;
		}
		tmp.pop_back();
	}
public:
	DrzewoH(){
		root=nullptr;
	}
	DrzewoH(char znak,int czestotliwosc){
		root=new Wezel(znak,czestotliwosc);
	}
	vector<bool> kod(char x){
		tmp.clear();
		if(root==nullptr){
			cout<<"EMPTY";
		}else{
			kod(x,root);
			return tmp;
		}
	}
	void print(){
		if(root==nullptr){
			cout<<"EMPTY";
		}else{
			print(root);
		}
	}
	
	void czytaj(vector<bool> tab){
		if(root==nullptr){
			cout<<"EMPTY";
		}else{
			czytaj(tab,root);
		}
	}
	int wart(){
		return root->p;
	}
	DrzewoH& operator+(DrzewoH d){ //sklej
		auto tmpRoot=root;
		int ilosc=0;
		if(tmpRoot!=nullptr)
			ilosc+=tmpRoot->p;
		if(d.root!=nullptr)
			ilosc+=d.root->p;
		root=new Wezel(1,ilosc);
		root->lewo=tmpRoot;
		root->prawo=d.root;  
		return *this;
	}
};

int main(){
	srand(time(NULL));
	//cout<<"podaj slowo:";
	string s="kataryna kataryniarki";
	//cin>>s;
	int znak[255];

	for(int i=0;i<255;i++){
		znak[i]=0;
	}
	for(auto i : s){//zliczanie prawdopodobienstw
		znak[i]++;
	}
	
	list<DrzewoH> tab; //tworzenie tablicy posortowanych jednowezlowych drzew, w sumie mi to sie nie przyda bo i tak szukam najmniejszych dwoch wezlow
	while(true){
		int min=9999;
		char poz=0;
		for(int i=0;i<255;i++){
			if(znak[i] && znak[i]<min){
				min=znak[i];
				poz=i;
			}
		}
		if(poz){
			tab.push_back(DrzewoH(poz,znak[poz]));
			znak[poz]=0;
		}else
			break;
	}

	//DrzewoH drzewoHuffmana=tab[0];
	while(tab.size()>=2){ //wyszukiwanie najmiejszczych dwoch wezlow (z najmmniejszym prawdopodobienstwem
		DrzewoH min1=*tab.begin();
		DrzewoH min2=*(++tab.begin());
		auto x=tab.begin();
		auto y=++tab.begin();
		for(auto it=++++(tab.begin());it!=tab.end();it++){ 
			int val=(*it).wart();
			if(min1.wart()>val && min2.wart()>val){
				if(min1.wart()> min2.wart()){
					min1=(*it);
					x=it;
					continue;
				}else{
					min2=(*it);
					y=it;
					continue;
				}
			}
			if(min1.wart()>val){
				min1=(*it);
				x=it;
				continue;
			}
			if(min2.wart()>val){
				min2=(*it);
				y=it;
				continue;
			}
		}
		auto tmp= (min1+min2);
		tab.erase(x);
		tab.erase(y);
		tab.push_front(tmp); //na koncu zostanie jednoelemenotowa tablica z z drzewem huffmana
	}
	DrzewoH drzewoHuffmana=tab.front();

	drzewoHuffmana.print();
	//auto kodSymbolu=drzewoHuffmana.kod('a');
	vector<bool> kod; // DrzewoH::kod(char znak) zwraca kod zerojedynkowy do tejze litery
	for(auto i : s){  
		auto tmp=drzewoHuffmana.kod(i);
		cout<< i << ": ";
		for(auto i : tmp)
			cout<<i;
		endl(cout);
		kod.reserve(tmp.size());
		copy(tmp.begin(),tmp.end(),back_inserter(kod)); // laczenie wektorow czyli tu powstaje caly kod huffmana 
	}
	for(auto i : kod)
		cout<<i;
	endl(cout);
	drzewoHuffmana.czytaj(kod); //sprawdzenie 
	system("pause");
	return 0;
}
