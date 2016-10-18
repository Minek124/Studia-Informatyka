#include <iostream>
#include <conio.h>
#include <ctime>
#include <cstdlib>

long it=0;
using namespace std;
struct Wezel{
	int wart;
	Wezel *lewo;
	Wezel *prawo;
	Wezel(int v){
		wart=v;
		lewo=nullptr;
		prawo=nullptr;
	}
};
class Drzewo{
private:
	Wezel *root;

	void add(int v,Wezel *w){
		if(w==nullptr){
			w= new Wezel(v);
			
		}
		if(v<w->wart){
			if(w->lewo==nullptr){ 
				w->lewo=new Wezel(v);
			}else{
				add(v,w->lewo);
			}
		}		
		if(v>w->wart){
			if(w->prawo==nullptr){
				w->prawo=new Wezel(v);
			}else{
				add(v,w->prawo);
			}
		}
		
	}
	Wezel* find(int v,Wezel *w){
		//it++;
		if(w->wart==v){
			return w;
		}
		if(v<w->wart){
			if(w->lewo!=nullptr){
				return find(v,w->lewo);
			}
		}
		if(v>w->wart){
			if(w->prawo!=nullptr){
				return find(v,w->prawo);
			}
		}
		return nullptr;
	}
	void print(Wezel *w){
		if(w->lewo!=nullptr){
			print(w->lewo);
		}

		cout<<w->wart<<endl;

		if(w->prawo!=nullptr){
			print(w->prawo);
		}
	}
public:
	Drzewo(): root(nullptr){}
	
	void add(int v){
		if(root==nullptr){
			root= new Wezel(v);
		}else{
			add(v,root);
		}
	}

	Wezel* find(int v){
		return find(v,root);
	}
	
	void print(){
		if(root==nullptr){
			cout<<"EMPTY";
		}else{
			print(root);
		}
	}
};

int main(){
	srand(time(NULL));
	Drzewo d;
	for(int i=0;i<40;i++){
		d.add(rand()%41);
	}
	d.print();
	cout<<"czy znajduje sie w tym drzewie liczba 10?adres:"<<d.find(10)<<"  00000000 to nullptr"<<endl; 
	
	Drzewo d2;
	int temp;
	time_t begin,end;
	begin = clock();
	for(int i=0;i<1000000;i++){
		temp=rand()*rand();
		temp/=1074;
		d2.add(temp);
	}
	end = clock();
	
	cout<<"czas 1M dodawan: "<<(end-begin)*1.0/CLOCKS_PER_SEC<<"s "<<endl;
	//d2.print();

	it=0;

	begin = clock();
	for(int i=0;i<1000000;i++){
		d2.find(i);
	}
	end = clock();

	cout<<"czas 1M wyszukiwan: "<<(end-begin)*1.0/CLOCKS_PER_SEC<<"s "<<endl;
	cout<<"liczba krokow???: "<<it<<endl;
	cout<<RAND_MAX;
	system("pause");
	return 0;
}
