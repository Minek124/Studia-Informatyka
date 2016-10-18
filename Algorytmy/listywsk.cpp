#include<cstdlib>
#include<ctime>
#include<cstdio>
#include<algorithm>
#include<iostream>
#include<time.h>
#include<random>
#include<vector>
 // wszysty na stale gen.cpp
using namespace std;

	struct wezel{
	int wart;
	wezel* nast;
	} ;
 class list{
public:
	
	wezel* root;
	wezel* w;
	int size1;
	list(){
	root=nullptr;
	size1=0;
	};
	void push_back(int x){
		size1++;
		if (root==nullptr){
			w=new wezel();
			root=w;
			root->wart=x;
			root->nast=nullptr;
		}
		else{
			wezel* wsk=root;
			wezel* nowy;
			while((wsk->nast)!=nullptr){
				wsk=wsk->nast;
			}
			nowy=new wezel();
			nowy->wart=x;
			nowy->nast=nullptr;
			wsk->nast=nowy;

		};
	}	
	void wypisz_liste()
	{
		wezel *wsk=root;         
		while( wsk != NULL )        
		{
		printf ("%i\n", wsk->wart); 
		wsk = wsk->nast;            
		}                 
	}
	bool empty(){
		if (root == nullptr) return true;
		return false;
	}
	int front(){
		int s;
		s=root->wart;
		return s;
	}
	void pop_front(){
		wezel* wsk=root;
		wezel* nowy;
		nowy=wsk->nast;
		root=nowy;
		delete wsk;
		size1--;
	}
	int size(){
		return size1;
	}
};

int main() {
    list L;
	int modulo=1000000;
	srand(time(NULL));
    int tc;
  //  scanf("%d", &tc);
	cout<< "podaj tc:"<<endl;
	tc=rand()%modulo;
	int i;
    for(int t=0; t<tc; t++) {
        char c = ' ';
        while(c!='A' && c!='D' && c!='S'){
            i=rand() %3;
			switch(i){
				case 0:
					c='A';
					break;
				case 1:
					c='D';
					break;
				case 2:
					c='S';
					break;
				default:
					cout<<"cos nie tak";
					break;
			}

		}
			//c = getchar(); // wczytuj znak po znaku dopoki nie napotkasz znaku dot. polecenia
        if(c=='A') {
            int num=rand()%modulo;
           // scanf("%d", &num);
            L.push_back(num);
        }
        else if(c=='D') {
            if(L.empty()) // lista pusta
                printf("EMPTY\n");
            else {
                int num = L.front();
                L.pop_front();
                printf("front: %d\n", num);
            }
        }
        else {
            printf("size: %d\n", L.size());
        }
    }
	system("pause");
    return 0;
}

	