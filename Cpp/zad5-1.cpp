#include <iostream>
#include <vector>
#include <limits>
#include <list>

template<typename Iter> 
typename std::iterator_traits<Iter>::value_type minimum(Iter beg, Iter end){
  typedef typename std::iterator_traits<Iter>::value_type T;
  if (beg == end)
    return std::numeric_limits<T>::lowest();
  T min = *beg;
  for(; beg != end; beg++){
    if(*beg < min)
      min = *beg;
  }
  return min;
}

int main() {
  std::list<long double> v;
  
  
  std::cout << "min:" << minimum(v.begin(), v.end()) << std::endl;
  return 0;
}
