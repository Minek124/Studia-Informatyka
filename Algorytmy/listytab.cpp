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


 class list{
public:
	
	vector<int> tab;

	void push_back(int x){
		tab.push_back(x);
	}	
	void wypisz_liste()
	{
		for(auto i=tab.begin();i!=tab.end();i++)
			cout<<*i<<endl;
	}
	bool empty(){	
		return tab.empty();
	}
	int front(){
		int s=tab[0];
		return s;
	}
	void pop_front(){
		for(auto i=tab.begin();i!=tab.end()-1;i++){
			*i=*(i+1);
		}
		tab.pop_back();
	}
	int size(){
		return tab.size();
	}
};

int main() {
    list L;
	int modulo=1000000;
	srand(time(NULL));
    int tc;

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

	