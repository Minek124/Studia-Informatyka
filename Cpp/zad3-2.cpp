#include <iostream>
#include <vector>
#include <cstdlib>
#include <algorithm>

template <typename T, int N = 16> class Stack;

template<int N> class Stack<char, N> {
  char _rep[N];
  unsigned int _top;
public:
  Stack():_top(0){};
  void push(char e) {_rep[_top++]=e;}
  void push(const char * s){
    int i = 0;
    while(s[i] != 0){
      push(s[i++]);
    }
  }
  char pop() {return _rep[--_top];}
  bool empty() {
    return (_top == 0);
  }
};


using namespace std;
int main() {
  
  Stack<char, 55> a;
  a.push('a'); a.push('b'); a.push('c');
  while (! a.empty()) {
        std::cout << a.pop() << '\n';
  }
  char * n = "sda";
  a.push(n);
  while (! a.empty()) {
        std::cout << a.pop() << '\n';
  }
  return 0;
}
