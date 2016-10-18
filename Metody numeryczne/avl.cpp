#include <iostream>
#include <ctime>

using namespace std;
struct Wezel{
	int wart;
	int bal;
	Wezel *lewo;
	Wezel *prawo;
	Wezel* lewaGora;
	Wezel* prawaGora;
	Wezel *gora(){
		if(lewaGora!=nullptr)
			return lewaGora;
		if(prawaGora!=nullptr)
			return prawaGora;
		return nullptr;
	}
	Wezel(int v){
		wart=v;
		bal=0;
		lewaGora=nullptr;
		prawaGora=nullptr;
		lewo=nullptr;
		prawo=nullptr;
	}
};
class Drzewo{
private:
	Wezel *root;
	Wezel* poprzedni; // hard coded

	void add(int v,Wezel *w){
		if(v<w->wart){
			if(w->lewo==nullptr){ 
				w->lewo=new Wezel(v);
				w->lewo->lewaGora=w;
				(w->bal)--;
				if(w->bal==-1){ //wzroslo drzewo
					balRefresh2(w);
					balance(w);
				}
			}else{
				add(v,w->lewo);
			}
		}		
		if(v>w->wart){
			if(w->prawo==nullptr){
				w->prawo=new Wezel(v);
				w->prawo->prawaGora=w;
				(w->bal)++;
				if(w->bal==1){  //wzroslo drzewo
					balRefresh2(w);
					balance(w);
				}
			}else{
				add(v,w->prawo);
			}
		}

	}
	Wezel* find(int v,Wezel *w){
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
		cout<<"  ";
		if(w->lewo!=nullptr){
			print(w->lewo);
		}

		cout<<w->wart<<endl;

		if(w->prawo!=nullptr){
			print(w->prawo);
		}
	}
	void balRefresh(Wezel *w){ // przy usuwaniu
		if(w->lewaGora!=nullptr){
			int up=abs((w->gora()->bal));
			(w->gora()->bal)++;
			up=abs((w->gora()->bal))-up;
			if(up==-1)
				balRefresh(w->gora());
		}
		if(w->prawaGora!=nullptr){
			int up=abs((w->gora()->bal));
			(w->gora()->bal)--;
			up=abs((w->gora()->bal))-up;
			if(up==-1)
				balRefresh(w->gora());
		}
	}
	void balRefresh2(Wezel *w){  //przy dodawaniu
		if(w->lewaGora!=nullptr){
			int up=abs((w->gora()->bal));
			(w->gora()->bal)--;
			up=abs((w->gora()->bal))-up;
			if(up==1)
				balRefresh2(w->gora());
		}
		if(w->prawaGora!=nullptr){
			int up=abs((w->gora()->bal));
			(w->gora()->bal)++;
			up=abs((w->gora()->bal))-up;
			if(up==1)
				balRefresh2(w->gora());
		}
	}
	
	void rot(Wezel *p,Wezel *q){  // p- syn , q-ojciec
		if(q->lewo==p){
			int a=0;
			int b=0;
			int c=0;
			if(p->bal>0)
				b=p->bal;
			if(p->bal<0)
				a=abs(p->bal);
			c=1+q->bal+abs(p->bal);
			q->lewo=p->prawo;
			p->prawo=q;
			q->bal=c-b;
			p->bal=max(b,c)+1-a;
			p->lewaGora=q->lewaGora;
			p->prawaGora=q->prawaGora;
			if((p->lewaGora)!=nullptr)
				p->lewaGora->lewo=p;
			if((p->prawaGora)!=nullptr)
				p->prawaGora->prawo=p;
			if((p->gora())==nullptr)
				root=p;
			q->lewaGora=nullptr;
			q->prawaGora=p;
			if(p->bal==-1 && q->bal==-2){
				q->bal=0;
				p->bal=0;
			}
			return ;
		}
		if(q->prawo==p){
			int a=0;
			int b=0;
			int c=0;
			if(p->bal>0)
				c=(p->bal);
			if(p->bal<0)
				b=abs(p->bal);
			a=1-q->bal+abs(p->bal);
			q->bal=b-a;
			p->bal=c-max(a,b)-1;
			
			q->prawo=p->lewo;
			p->lewo=q;
			p->lewaGora=q->lewaGora;
			p->prawaGora=q->prawaGora;
			if((p->lewaGora)!=nullptr)
				p->lewaGora->lewo=p;
			if((p->prawaGora)!=nullptr)
				p->prawaGora->prawo=p;
			if((p->gora())==nullptr)
				root=p;
			q->lewaGora=p;
			q->prawaGora=nullptr;
			if(p->bal==1 && q->bal==2){
				q->bal=0;
				p->bal=0;
			}
			return ;
		}
		
		
	}
	void rot(Wezel *p,Wezel *q,Wezel *r){ //p-syn q-tata r-dziadek
		rot(p,q);
		rot(p,r);
	}
	void balance(Wezel *w){
		if((w->bal)>=2){
			if(w->prawo->bal==1){
				rot(w->prawo,w);
				return;
			}
			if(w->prawo->bal==-1){
				rot(w->prawo->lewo,w->prawo,w);
				return;
			}
		}
		if((w->bal)<=-2){
			if(w->lewo->bal==-1)
				rot(w->lewo,w);
			if(w->lewo->bal==1)
				rot(w->lewo->prawo,w->lewo,w);
		}
		if(w->gora()!=nullptr)
			balance(w->gora());
	}
	void balance2(Wezel *w){
		if((w->bal)>=2){
			if(w->prawo->bal==1 || w->prawo->bal==0)
				rot(w->prawo,w);
			if(w->prawo->bal==-1)
				rot(w->prawo->lewo,w->prawo,w);
		}
		if((w->bal)<=-2){
			if(w->lewo->bal==-1 || w->lewo->bal==0)
				rot(w->lewo,w);
			if(w->lewo->bal==1)
				rot(w->lewo->prawo,w->lewo,w);
		}
		if(w->gora()!=nullptr)
			balance2(w->gora());
	}
	void del(Wezel *w){
		if(w->lewo==nullptr && w->prawo==nullptr){
			if(w->lewaGora!=nullptr){
				balRefresh(w);
				balance2(w); //uwaga
				w->gora()->lewo=nullptr;
				
			}
			if(w->prawaGora!=nullptr){
				balRefresh(w);
				balance2(w); //uwaga
				w->gora()->prawo=nullptr;
				
			}
			delete w;
			return;
		}
		if(w->lewo==nullptr && w->prawo!=nullptr){
			auto tmp = w->prawo;
			while(tmp->lewo!=nullptr)
				tmp=tmp->lewo;
			w->wart=tmp->wart;
			del(tmp);
			return;
		}
		if(w->lewo!=nullptr && w->prawo==nullptr){
			auto tmp = w->lewo;
			while(tmp->prawo!=nullptr)
				tmp=tmp->prawo;
			w->wart=tmp->wart;
			del(tmp);
			return;
		}
		if(w->lewo!=nullptr && w->prawo!=nullptr){
			auto tmp = w->prawo;
			while(tmp->lewo!=nullptr)
				tmp=tmp->lewo;
			w->wart=tmp->wart;
			del(tmp);
			return;
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
	int getBalance(){
		return root->bal;
	}
	void del(int v){
		del(find(v,root));
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
	d.add(10);
	d.add(5);
	d.add(15);
	d.add(12);
	d.add(16);
	d.add(11);
	d.add(18);
	cout<<"balans: "<<d.getBalance()<<endl;
	d.print();
	d.del(12);
	//d.del(16);
	d.print();
	cout<<"balans: "<<d.getBalance()<<endl;
	system("pause");
	return 0;
}