#include <iostream>
#include <list>
#include <fstream>
#include <vector>
#include <math.h>
using namespace std;
#define NIESKONCZONOSC 2000000000
class Kopiec {
private:
	int** tab;
	int size;
	int find(int v,int a){
		if(tab[a][0] == v)
			return a;
		if((2*a <= size)  && (v <= tab[a][0])){
            
            find(v,2*a);
		}
        if((2*a + 1 <= size) && (v <= tab[a][1])){
			
            find(v,2*a+1);
		}
		return -1; // nie ma 
	}
public:
	Kopiec(int s){
		size = 0;
		tab = new int*[s+2];
		for(int i = 0; i < s+2;i++)
			tab[i] = new int [2];
		
	}	
	void erase(int a){
		if (a != -1){
			int ret = tab[1][0];
			tab[a][0]=tab[size][0];
			tab[a][1]=tab[size][1];
			--size;
			napraw(a);
		}

	}
	void insert (int v,int w){
		int child = ++size;
		tab[child][0] = v;
		tab[child][1] = w;
		while (child > 1 && tab[child][1] < tab[div(child,2).quot][1]){
			swap(tab[child],tab[div(child,2).quot]);
			child = div(child,2).quot;
		}
	}
	void napraw2(int a){
		if((2*a <= size)  && (tab[2*a][1] < tab[a][1])){
            swap (tab[a] , tab[a*2]);
            napraw(2*a);
		}
        if((2*a + 1 <= size) && (tab[2*a + 1][1] < tab[a][1])){
			swap (tab[a] , tab[2*a+1]);
            napraw(2*a+1);
		}
	}
	void napraw(int x){ // nierekurencyjnie
		int a = x;
		while(true){
			if((2*a <= size)  && (tab[2*a][1] < tab[a][1])){
				swap (tab[a] , tab[a*2]);
				a = 2 * a;
			}
			if((2*a + 1 <= size) && (tab[2*a + 1][1] < tab[a][1])){
				swap (tab[a] , tab[2*a+1]);
				a = (2 * a) +1;
			}
			else
				break;
		}

	}
	int find(int v){
		return find(v,1);
	}
	void wypisz(){
		for(int i = 1; i<=size;i++)
			cout<<tab[i][1]<<endl;
		cout<<"koniec kopca"<<endl;
		for(int i = size+1; i<=10;i++)
			cout<<tab[i][1]<<endl;
		endl(cout);
	}
	int deleteMin(){
		int ret = tab[1][0];
		tab[1][0]=tab[size][0];
		tab[1][1]=tab[size][1];
		--size;
		napraw(1);
		return ret;
	}
	bool empty(){
		if (size == 0)
			return true;
		return false;
	}
};

class Node {
public:
	int data;
	int *weight;
	list<Node*> lista_sasiedztwa; // lista wskaznikow sasiadow
	Node(): 
		data(-1)
		{};
	Node(int v,int size): 
		data(v),
		weight(new int[size])
	{};
};

class Graph{
private:
	Node **lista;
	int *kolor;
	int *rodzic;
	int size;
	int czas;
	int *poczatek;
	int *koniec;
	vector<int> lider;
	vector<int> ranga;

	void makeSet (int x){	
		lider[x] = x;
		ranga[x] = 0;
	} 
	void Union (int x, int y){

		int fx= find (x);
		int fy= find (y);

		if (ranga[fx] > ranga[fy]){
			lider[fy] = fx;
			ranga[fx] += ranga[fy];
		}else{
			lider[fx] = fy;
			ranga[fy] += ranga[fx];
		}
	}
	int find (int x){
		if (x != lider[x])
			lider[x] = find(lider[x]);
 
		return lider[x];
	}
	void dfs(int v){
		kolor[v]=1; // szary 
		Node *tmp =lista[v];
		czas = czas + 1;
		poczatek[v] = czas;
		for(auto it=tmp->lista_sasiedztwa.begin();it!=tmp->lista_sasiedztwa.end();it++){
			if(kolor[(*it)->data]==0){
				rodzic[v] = (*it)->data;
				cout<<"krawedz drzewowa ("<<v<<","<<(*it)->data<<")"<<endl;
				dfs((*it)->data);
			}else if(kolor[(*it)->data]==1){
				cout<<"krawedz wsteczna ("<<v<<","<<(*it)->data<<")"<<endl;
			}else if(kolor[(*it)->data]==2){
				if(poczatek[v]<poczatek[(*it)->data])
					cout<<"krawedz wyprzedzajaca ("<<v<<","<<(*it)->data<<")"<<endl;
				else
					cout<<"krawedz poprzeczna ("<<v<<","<<(*it)->data<<")"<<endl;
			}
		}
		kolor[v]=2;
		czas = czas + 1;
		koniec[v] = czas;
	}
	list< vector<int> > getEdges(){
		list< vector<int> > e; // krawedzie
		for(int v = 0;v< size;v++){
			Node* p = lista[v];
			for(auto it = p->lista_sasiedztwa.begin();it != p->lista_sasiedztwa.end();it++){
				int waga = p->weight[(*it)->data];
				int sasiad = (*it)->data;
				bool znaleziono = false;
				for(auto it2 = e.begin();it2 != e.end();it2++){
					if(waga < (*it2)[2]){
						int t[3] = {v, sasiad, waga};
						vector<int> tmp(t,t+3);
						e.insert(it2,tmp);
						znaleziono = true;
						if(it2==e.begin())
							break;
						else
							it2--;
						while((*it2)[2] == waga){ // usuwanie zdublowanych krawedzi, poniewaz chce miec graf nieskierowany
							if( ((*it2)[0] == sasiad) && ((*it2)[1] == v) ) {
								e.erase(it2);
								break;
							}
							if(it2==e.begin())
								break;
							it2--;
						}
						break;
					}
				}
				if(!znaleziono){
					int t[3] = {v, sasiad, waga};
					vector<int> tmp(t,t+3);
					e.push_back(tmp);
					auto it2 = --e.end();
					while((*it2)[2] == waga){ // usuwanie zdublowanych krawedzi, poniewaz chce miec graf nieskierowany
						if( ((*it2)[0] == sasiad) && ((*it2)[1] == v) ) {
							e.erase(it2);
							break;
						}
						if(it2==e.begin())
							break;
						it2--;
					}
				}
			}
		}
		/*for(auto i : e){
			cout<< i[0]<<" "<<i[1]<<" "<<i[2]<<endl;
		}*/
		return e;
	}
public:
	Graph(Node **t,int size): 
		lista(t),
		size(size){
	}
	int first(int v){
		if(v<0 || v>=size)
			return -2;  // poza lista wiezcholkow
		Node *tmp = lista[v];
		if(tmp!=nullptr){
			if(tmp->lista_sasiedztwa.size()!=0){
				int index=tmp->lista_sasiedztwa.front()->data;
				return index;
			}
			else 
				return -1; //brak sasiadow
		}
		return -2; // poza lista wiezcholkow
	}
	int next(int v,int i){
		if(v<0 || v>=size)
			return -2;  // poza lista wiezcholkow
		Node *tmp=lista[v];
		if(tmp->lista_sasiedztwa.size()<i+1){
			return -1;
		}
		int j=0;
		for(auto it=tmp->lista_sasiedztwa.begin();it!=tmp->lista_sasiedztwa.end();it++){
			if(i==j)
				return (*it)->data;
			j++;
		}
	}
	Node* vertex(int v,int i){
		if(v<0 || v>=size)
			return nullptr;  // poza lista wiezcholkow
		Node *tmp=lista[v];
		for(auto it=tmp->lista_sasiedztwa.begin();it!=tmp->lista_sasiedztwa.end();it++){
			if(i==(*it)->data){  //  tu byl blad sprawdzam index, nie pozycja w liscie
				return (*it);
			}
		}
	}
	void rozp(int w){
		//inicjalizacja
		kolor=new int[size];
		rodzic=new int[size];
		poczatek=new int[size];
		koniec=new int[size];
		czas=0;
		for(int i=0;i<size;i++){
			kolor[i]=0;
			poczatek[i] = 0;
			koniec[i] = 0;
			rodzic[i] = -1;
		}
		dfs(w);
		for(int i=0;i<size;i++){
			if(kolor[i]==0){
				dfs(i);
			}
		}
	}
	void dijkstra (int od){
		int *distance = new int[size];
		for (int i=0;i<size;i++){
			distance[i] = NIESKONCZONOSC; 
		}
		distance[od] = 0;
		Kopiec k(size);
		for(int i = 0; i<size; ++i) {
			if(i!=od)
				k.insert(i, NIESKONCZONOSC);
			else
				k.insert(i, 0);
		}
		
		while(!k.empty()){
			
			int v = k.deleteMin();
			Node* p = lista[v];
			for(auto it = p->lista_sasiedztwa.begin();it != p->lista_sasiedztwa.end();it++){
				int waga = p->weight[(*it)->data];
				int sasiad = (*it)->data;
				if(distance [sasiad] > distance[v]+waga){
					k.erase(k.find(sasiad));
					distance[sasiad] = distance[v]+waga;
					k.insert(sasiad,distance[sasiad]);
					
				}
			}
		}
		for(int i=0;i<size;i++)
			cout<<"dystans dla v= "<<i<<" wynosi "<<distance[i]<<endl;
	}

	void kruskal(){

		/*
		wyszukuje w grafie wszystkie krawedzie
		zwraca liste krawedzi nieskierowanych w grafie w porzadku niemalejacym
		wiec kazda krawedz musi byc w obie strony
		*/
		list< vector<int> > edges = getEdges(); 
		lider.resize(size+1);
		ranga.resize(size+1);
		for(int i =0;i<size;i++){
			makeSet(i);
		}
		while(!edges.empty()){
			vector<int> e = edges.front();
			edges.pop_front();
			if(find(e[0]) != find(e[1])){
				cout<<"krawedz z "<< e[0] <<" do " << e[1]<<endl;
				Union(e[0],e[1]);
			}
		}
	}
};

int main(int argc,char *args[]){
	//inicjalizacja
/*
	wczytywanie z pliku 
6	---- pierwsza liczba
0 1 7
1 0 7
0 2 9
2 0 9
0 5 14
5 0 14
1 2 10
2 1 10
1 3 15
3 1 15
2 5 2
5 2 2
5 4 9
4 5 9
2 3 11
3 2 11
4 3 6
3 4 6
*/
	Node **l;
	int size;
	std::fstream plik( "D:/input.txt", std::ios::in );
	if( plik.good() == true ){
		char dane[ 255 ];
		plik.getline( dane, 255 );
		size=atoi(&dane[0]);
		l=new Node*[size];
		while(!plik.eof()){
			plik.getline( dane, 255 );
			int a=atoi(&dane[0]);
			int b=atoi(&dane[2]);
			int c=atoi(&dane[4]);
			if(l[a]==(void *)0xCDCDCDCD) // uninitialized heap memory
				l[a]=new Node(a,size);
			if(l[b]==(void *)0xCDCDCDCD) // uninitialized heap memory
				l[b]=new Node(b,size);
			
			l[a]->lista_sasiedztwa.push_back(l[b]);
			l[a]->weight[b]=c;//dodana waga
			cout<<"krawedz: ("<<a<<","<<b<<") z waga: "<<c<<endl;
		}
		
	}else
		cout<<"blad odczytu"<<endl;
	
	Graph G(l,size);
	cout<<"drzewo rozpinajace algorytmem kruskala:"<<endl;
	G.kruskal();
	system("pause");
	return 0;
}

