#include <iostream>

template <typename T>
class Register {
private:
  T sum;
public:
  Register(): sum(0) {};
  T get(){
    return sum;
  }
  void put(T e){
    sum += e;
  }
  void reset(){
    sum = 0;
  }
};

int main()
{
    Register<double> k;

    k.put(2.00);
    k.put(3.95);
    k.put(1.50);
    std::cout << k.get() << '\n';   // powinno wypisać 7.45

    k.put(2.00);
    std::cout << k.get() << '\n';   // powinno wypisać 9.45

    k.reset();
    std::cout << k.get() << '\n';   // powinno wypisać 0.0

    return 0;
}

