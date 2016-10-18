#ifndef matrixxx
#define matrixxx

#include <iostream>

template<typename typ> class Matrix{
private:
	typ *tab;

	typ skalar(typ v1[],typ v2[],int length){
		typ suma=0;
		for(int i=0;i<length;i++){
			suma=+(v1[i]*v2[i]);
		}
		return suma;
	}

	//zwraca wpolrzedna w tablicy
	int cord(int x,int y){
		return (x*columns+y);
	}
public:
	int rows;
	int columns;
	int size;
	
	//konstruktor przyjmujacy rozmiary macierzy bez inicializacji elementow
	Matrix(int w,int h):
	rows(w),
	columns(h),
	size(w*h),
	tab(new typ[w*h]){};
	
	// konstruktor przyjmujacy tablice oraz rozmiary macierzy
	Matrix(typ t[],int w,int h):
	rows(w),
	columns(h),
	size(w*h),
	tab(new typ[w*h]){
		copy(t,t+size,tab);
	}

	//zwraca tablice
	typ *getTab(){
		return tab;
	}

	//ustawia wszystkie elementy na dana wartosc
	void setAll(typ x){
		for(int i=0;i<size;i++)
			tab[i]=x;
	}

	//wypisuje macierz
	void print(){
		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				std::cout<< tab[cord(i,j)]<< " ";
			}
			std::endl(std::cout);
		}
		std::endl(std::cout);
	}

	//zwaraca macierz rozkladu choleskiego
	Matrix choleskyDecomposition(){
		Matrix LLt(rows,columns);
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++){
				if(i>j){
					double sum=0;
					for(int k=0;k<j;k++)
						sum+=(LLt[LLt.cord(i,k)]*LLt[LLt.cord(j,k)]);
					LLt[LLt.cord(i,j)]=(tab[cord(i,j)]-sum)/LLt[LLt.cord(j,j)];
					LLt[LLt.cord(j,i)]=(tab[cord(i,j)]-sum)/LLt[LLt.cord(j,j)];
				}
				if(i==j){
					double sum=0;
					for(int k=0;k<j;k++)
						sum+=(LLt[LLt.cord(j,k)]*LLt[LLt.cord(j,k)]);
					LLt[LLt.cord(i,j)]=sqrt(tab[cord(i,j)]-sum);
				}
			}
		return LLt;
	}

	//zwraca dlugosc wektora
	typ length(){
		typ length=0;
		for(int i=0;i<size;i++)
			length+=(tab[i]*tab[i]);
		length=sqrt(length);
		return length;
	}

	//normalizuje wektor i tylko wektor
	void normalize(){
		if(columns==1){
			typ skalar=length();
			for(int i=0;i<size;i++)
				tab[i]/=skalar;
		}
	}

	//transponuje macierz
	void transpose(){
		
		for(int i=0;i<rows;i++)
			for(int j=0;j<columns;j++){
				if(i>j){
					typ tmp=tab[cord(i,j)];
					tab[cord(i,j)]=tab[cord(j,i)];
					tab[cord(j,i)]=tmp;
				}
			}
		int tmp=rows;
		rows=columns;
		columns=tmp;
	}

	//zwraca macierz transponowana, nie modyfikuje glownej
	Matrix transposed(){
		Matrix<typ> tmp(tab,rows,columns);
		tmp.transpose();
		return tmp;
	}

	//wypisuje dwie najwieksze wartosci wlasne
	void metodaPotegowa(){
		auto A=*this;
		Matrix<typ> w(columns,1);
		w.setAll(0);
		w[0]=1;
		double epsilon =0.000001;
		Matrix<typ> wk(columns,1);
		while(true){
			wk=A*w;	
			wk.normalize();
			bool koniec=true;
			for(int i=0;i<rows;i++){
				if(abs(w[i]-wk[i])>epsilon)
					koniec=false;
			}
			if(koniec)
				break;
			w=wk;
		}
		cout<<"wektor wlasny v1:"<<endl;
		cout<<wk;
		cout<<"pierwsza maksymalna wartosc wlasna lambda1:"<<endl;
		cout<<(A*wk)[0]/wk[0]<<endl;
		
		typ skladowaProstopadla=0;
		for(int i=1;i<rows;i++)
			skladowaProstopadla-=w[i];
		skladowaProstopadla/=w[0];
		w.setAll(1);
		w[0]=skladowaProstopadla;
		w.normalize();
		Matrix<typ> v1(wk.getTab(),wk.rows,wk.columns);
		while(true){
			wk=A*w;
			wk=wk-(v1*(v1.transposed()*wk));
			wk.normalize();

			bool koniec=true;
			for(int i=0;i<rows;i++){
				if(abs(w[i]-wk[i])>epsilon)
					koniec=false;
			}
			if(koniec)
				break;
			w=wk;
		}
		cout<<"wektor wlasny v2:"<<endl;
		cout<<wk;
		cout<<"druga maksymalna wartosc wlasna lambda2:"<<endl;
		cout<<(A*wk)[5]/wk[5]<<endl;
	}

	//przeciazony operator strumienia
	friend std::ostream & operator <<(std::ostream & out, Matrix & k){
		for(int i=0;i<k.rows;i++){
			for(int j=0;j<k.columns;j++){
				out<< k.tab[k.cord(i,j)]<<" ";
			}
			out<<std::endl;
		}
		out<<std::endl;
		return out;
	}

	//dostep do elementu Ai,j poprzez  A(i,j)
	typ operator() (int x,int y){
		return tab[cord(x,y)];
	}

	//referencja do elementu tablicy, pozwala operowac na macierzach
	typ &operator[] (int i){
		return tab[i];
	}

	// operator przypisania
	Matrix operator=(Matrix& s){
		if(columns == s.columns && rows == s.rows){
			copy(s.tab,s.tab+(s.rows*s.columns),tab);
		}
		return *this;
	}

	//dodawanie macierzy
	Matrix operator+(Matrix& s){
		Matrix<typ> tmp(tab,rows,columns);
		for(int i=0;i<size;i++){
			tmp[i]+=s[i];
		}
		return tmp;
	}

	//odejmowanie macierzy
	Matrix operator-(Matrix& s){
		Matrix<typ> tmp(tab,rows,columns);
		for(int i=0;i<size;i++){
			tmp[i]-=s[i];
		}
		return tmp;
	}

	//mnozenie przez skalar
	Matrix operator*(double s){
		Matrix<typ> tmp(tab,rows,columns);
		for(int i=0;i<size;i++){
			tmp[i]*=s;
		}
		return tmp;
	}

	//dzielenie przez skalar
	Matrix operator/(double s){
		Matrix<typ> tmp(tab,rows,columns);
		for(int i=0;i<size;i++){
			tmp[i]/=s;
		}
		return tmp;
	}

	//mnorzenie macierzy
	Matrix operator*(Matrix& s){
		if(columns==s.rows){
			Matrix<typ> tmp(rows,s.columns);
			int vectorSize=columns;
			for(int i=0;i<rows;i++)
				for(int j=0;j<s.columns;j++){
					typ suma=0;
					for(int x=0;x<vectorSize;x++)
						suma+=(((*this)(i,x))*s(x,j));
					tmp[tmp.cord(i,j)]=suma;
				}
			return tmp;
		}
		if(s.rows==1 && s.columns==1){
			Matrix<typ> tmp(tab,rows,columns);
			tmp=tmp*s[0];
			return tmp;
		}
		return *this;
	}
};

#endif // Matrixxx
