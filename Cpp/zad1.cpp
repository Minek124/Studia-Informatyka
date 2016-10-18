#include <iostream>

using namespace std;

template <typename T>
T sum_of_array_elements(T * begin, T * end){
  T sum = 0;
  for(T* i = begin;i < end; ++i){
    sum+=*i;
  }
  return sum;
}

int main(){

int a[] = {1,2,3,4};

double b[] = {1.5,2.5,3.5,4};

cout << sum_of_array_elements(a, a + 4) << endl;
cout << sum_of_array_elements(b, b + 4) << endl;

return 0;
}
