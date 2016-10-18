#include <iostream>
#include <list>

using namespace std;

int main(){

list<int> a;
a.push_back(5);
a.push_back(15);
a.push_back(25);
a.push_back(35);

for(auto it = a.begin();it != a.end();++it){
	cout<< *it << endl;
}
cout << "hello world" << a.front() << endl;
return 0;
}
