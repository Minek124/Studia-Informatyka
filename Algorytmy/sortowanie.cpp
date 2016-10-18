using namespace std;

#include<cstdlib>
#include<ctime>
#include<cstdio>
#include<algorithm>
#include<iostream>
const int maxZ = 100; // maksymalna ilosc testow
const int maxSize = 10; // maksymalny rozmiar pojedynczego testu
const int maxValue = 1000; // maksymalna wartosc liczby z ciagu liczb

int tab[maxSize];
int size;
int tab2[maxSize];
int sumaujemna[maxValue+1];
int sumadodatnia[maxValue+1];
int z;

void wyswietl(){
	for(int j=0; j<size; j++){
		
		printf("%d ", tab[j]);
		printf("\n");
	};
}
void wyswietl2(){
	for(int j=0; j<size; j++){
		
		printf("%d ", tab2[j]);
		printf("\n");
	};
}

int main() {
	cout<< "podaj ilosc testow:";
	scanf("%d", &z);
	for(int g=0; g<z; g++) {
	cout<< "podaj ilosc liczb:";
	scanf("%d", &size);
	printf("\n");
	cout<< "podaj liczby, kazda akceptujac enterem:\n";
	for(int j=0; j<size; j++) scanf("%d", &tab[j]);	
	int typ;
	bool k=true;	
	bool czybylaujemna=false;
	int a,b;
	printf("\n");
	cout<< "podales liczby:\n";
	wyswietl();
	cout<< "podaj typ sortowania \n 1-bubblesort \n 2-przez wybieranie \n 3-przez wstawianie \n 4- przez zliczanie\n";
	cin>> typ;
	switch(typ){
		
		case 1:
			// BUBBLESORT _________________________________________________
			while(k){
				k=false;
					for(int j=0; j<size-1; j++){
					if(tab[j]>tab[j+1]) {
						a=tab[j];
						tab[j]=tab[j+1];
						tab[j+1]=a;
						k=true;
					};
				};
			};
			printf("\n");
			wyswietl();
			break;
		
		case 2:
			// PRZEZ WYBIERANIE _____________________________________________
			for(int i=0; i<size; i++){ 
				a=tab[i];
				b=i;
				for(int j=0; j<size; j++) 
					if (tab[j]<a) {
						a=tab[j];
						b=j;
						};
				tab2[i]=a;
				tab[b]=maxValue;
				};
			printf("\n");
			wyswietl2();
			break;
		
		case 3:
			// PRZEZ WSTAWIANIE
			tab2[0]=tab[0];
			for(int i=1; i<size; i++){
				for(int j=0; j<=i-1; j++) {
					if(tab[i]<tab2[j]){
						for(int k=i-1;k>=j;k--) tab2[k+1]=tab2[k];
						tab2[j]=tab[i];
						break;
					}
					if(j==(i-1)) {
						if(tab[i]<tab2[j]) {
							tab2[j+1]=tab2[j];
							tab2[j]=tab[i];
							break;
						}
						tab2[j+1]=tab[i];
						break;
					}
				}
			}
			printf("\n");
			wyswietl2();
		case 4:	
			//PRZEZ ZLICZANIE
			for(int i=0;i<=maxValue;i++){
				sumadodatnia[i]=0;
				sumaujemna[i]=0;
			}

			int j=0;
			czybylaujemna=false;
			for(int i=0;i<size;i++){
				if(tab[i]>=0) ++sumadodatnia[tab[i]];
				else {
					++sumaujemna[-tab[i]];
					czybylaujemna=true;
				}
			}
			if(czybylaujemna){
				for(int i=-maxValue;i<0;i++)
					while(sumaujemna[-i]){
						tab2[j]=i;
						j++;
						--sumaujemna[-i];
					}
				for(int i=0;i<=maxValue;i++)
					while(sumadodatnia[i]){
						tab2[j]=i;
						j++;
						--sumadodatnia[i];
					}
			}
			else {
				for(int i=0;i<=maxValue;i++)
					while(sumadodatnia[i]){
						tab2[j]=i;
						j++;
						--sumadodatnia[i];
					}
			}
			printf("\n");
			wyswietl2();
			break;

	} //Switch
	}
	system("pause");
	return 0;
}
