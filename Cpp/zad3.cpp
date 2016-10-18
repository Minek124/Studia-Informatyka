#include <iostream>

template <typename T, int size = 16>
class Stos
{
    T data[size];
    int top_index;

public:
    Stos() : top_index(-1)
    { }

    bool empty() const
    {
        return (top_index == -1);
    }

    void push(const T & x)
    {
        data[++top_index] = x;
    }

    T & top()
    {
        return data[top_index];
    }

    const T & top() const
    {
        return data[top_index];
    }

    void pop()
    {
        top_index--;
    }
};


template<typename T, typename C> class DoublingStack {
private:
  C stack;
public:
  DoublingStack() {};
  void push(T val) {
    stack.push(val);
    stack.push(val);
  }
  void pop() {
    stack.pop();
  }
  bool empty() {
    return stack.empty();
  }
  T top(){
    return stack.top();
  }
};

int main()
{
    Stos<int> a;

    a.push(1); a.push(2); a.push(3);
    while (! a.empty()) {
        std::cout << a.top() << '\n';
        a.pop();
    }                   // powinno wypisać 3 2 1

    std::endl(std::cout);
    DoublingStack< int, Stos<int> > b;

    b.push(1); b.push(2); b.push(3);
    while (! b.empty()) {
        std::cout << b.top() << '\n';
        b.pop();
    }                   // powinno wypisać 3 3 2 2 1 1

    return 0;
}
