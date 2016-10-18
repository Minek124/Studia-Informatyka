#ifndef _ZMPWCPP_LISTA_H
#define _ZMPWCPP_LISTA_H

#include <iterator>
#include <cstdlib>

// CzÄĹciowa implementacja listy jednokierunkowej z iteratorem zgodnej
// z konwencjami STL.

template <typename T>
class Lista
{
    class Iterator;

    class Element
    {
        T data;
        Element * next;

        Element(const T & val, Element * nxt = 0) : data(val), next(nxt)
        { }

        friend class Lista;
        friend class Iterator;
    };

    class Iterator
    {
        Element * current;

        Iterator(Element * position) : current(position)
        { }

        friend class Lista;

    public:
        // standardowe typedef-y dostarczane przez wszystkie iteratory STL
        typedef std::forward_iterator_tag iterator_category;
        typedef T value_type;
        typedef std::ptrdiff_t difference_type;
        typedef T * pointer;
        typedef T & reference;
        // moĹźna by teĹź po prostu dziedziczyÄ po std::forward_iterator<T>,
        // ona dostarcza te piÄÄ typedef-Ăłw
        // ptrdiff_t to typ wyniku otrzymywanego gdy odejmiemy dwa wskaĹşniki,
        // zaleĹźny od architektury sprzÄtowej naszego systemu

        Iterator() : current(0)
        { }

        // kompilator sam wygeneruje konstruktor Iterator(const Iterator &)
        // oraz operator=(const Iterator &) o domyĹlnych treĹciach

        T & operator*()
        {
            return current->data;
        }

        T * operator->()
        {
            return &(current->data);
        }

        Iterator & operator++()     // preinkrementacja
        {
            current = current->next;
            return *this;
        }

        Iterator operator++(int)    // postinkrementacja
        {
            Iterator tmp = *this;
            current = current->next;
            return tmp;
        }

        bool operator==(const Iterator rhs)
        {
            return current == rhs.current;
        }

        bool operator!=(const Iterator rhs)
        {
            return current != rhs.current;
        }
    };

    Element * head;

public:
    // dwa ze standardowych typedef-Ăłw dostarczanych przez wszystkie
    // pojemniki STL; jeĹli chcesz, dopisz tutaj pozostaĹe
    typedef T value_type;
    typedef Iterator iterator;

    Lista() : head(0)
    { }

    ~Lista();

    void push_front(const T &);

    iterator begin()
    {
        return iterator(head);
    }

    iterator end()
    {
        return iterator(0);
    }
};

template <typename T>
Lista<T>::~Lista()
{
    Element * p;
    while (head) {
        p = head;
        head = head->next;
        delete p;
    }
}

template <typename T>
void Lista<T>::push_front(const T & val)
{
    Element * p = new Element(val, head);
    head = p;
}

#endif
