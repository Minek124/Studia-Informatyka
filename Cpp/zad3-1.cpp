#include <iostream>
#include <vector>
#include <cstdlib>
#include <algorithm>
struct F {
  bool operator()(int y) { 
    return ((y % 2) == 0); 
  }
};

using namespace std;
int main() {
  
  F fun;
  vector<int> v;
  for (int i=0; i<10; i++){
    int x = rand();
    cout << x << endl;
    v.push_back( x ); 
  }
  int c = count_if (v.begin(), v.end(), fun);
  cout << c << endl;

  return 0;
}
