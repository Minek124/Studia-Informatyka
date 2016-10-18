#include<iostream>

/**
 * @file uj_list.hpp
 * Zawiera implementacje listy jednokierunkowej
 * @author Michał Mogiła
 *
 */

 /*! \mainpage Lista Jednokierunkowa
 *  Projekt zaliczeniowy Zaawansowane C++
 *  autor: Michał Mogiła
 */

namespace uj { 
	/**
	 * Lista jednokierunkowa
	 */
	template <typename T>
	class list {
	private:
		/**
		 * Pojedynczy element listy
		 */
		class Node {
			T value;
			Node * next;
			Node(Node * next) : next(next) {}
			Node(T val, Node * next) : value(val), next(next) {}
			friend class list;
			friend class iterator;
			friend class const_iterator;
		};
		Node * head;
		Node * tail;
		int count;
	public:
		class iterator;
		class const_iterator;
		typedef T value_type;
		typedef T& reference;
		typedef const T& const_reference;
		typedef iterator iterator;
		typedef int size_type;
		/**
		 * Iterator dla listy jednokierunkowej
		 */
		class iterator {
			Node * current;
			/**
		      Konstruktor iteratora
		      @param node Element listy wskazywany przez iterator
			  */
			iterator(Node * node) : current(node) {}
			friend class list;
		public:
			typedef std::forward_iterator_tag iterator_category;
			typedef T value_type;
			typedef std::ptrdiff_t difference_type;
			typedef T * pointer;
			typedef T & reference;
			/**
			   Operator wyłuskania
			 */
			T & operator*() {
				return current->next->value;
			}
			/**
			   Operator strzałki
			 */
			T * operator->() {
            	return &(current->next->value);
        	}
			/**
			   Operator preinkrementacji
			 */
			iterator & operator++() {
				current = current->next;
				return *this;
			}
			/**
			   Operator postinkrementacji
			 */
			iterator operator++(int) {
				iterator tmp = *this;
				operator++();
				return tmp;
			}
			/**
			   Operator porównania
			 */
			friend bool operator==(const iterator &it1, const iterator &it2) {
				return (it1.current == it2.current);
			}
			friend bool operator!=(const iterator &it1, const iterator &it2) {
				return (it1.current != it2.current);
			}

		};
		
		class const_iterator {
			Node * current;
			/**
		      Konstruktor const iteratora
		      @param node Element listy wskazywany przez iterator
			  */
			const_iterator(Node * node) : current(node) {}
			friend class list;
		public:
			typedef std::forward_iterator_tag iterator_category;
			typedef T value_type;
			typedef std::ptrdiff_t difference_type;
			typedef T * pointer;
			typedef T & reference;
			/**
			   Operator wyłuskania
			 */
			const T & operator*() {
				return current->next->value;
			}
			/**
			   Operator strzałki
			 */
			const T * operator->() {
            	return &(current->next->value);
        	}
			/**
			   Operator preinkrementacji
			 */
			const_iterator & operator++() {
				current = current->next;
				return *this;
			}
			/**
			   Operator postinkrementacji
			 */
			const_iterator operator++(int) {
				const_iterator tmp = *this;
				operator++();
				return tmp;
			}
			/**
			   Operator porównania
			 */
			friend bool operator==(const const_iterator &it1, const const_iterator &it2) {
				return (it1.current == it2.current);
			}
			friend bool operator!=(const const_iterator &it1, const const_iterator &it2) {
				return (it1.current != it2.current);
			}

		};
		
		/** Domyślny konstruktor
        	 */
		list() : count(0) {
			head = new Node(nullptr);
			tail = head;
		};
		/** Konstruktor kopiujący
        	 */
		list(const list & other) {
			head = new Node(nullptr);
			tail = head;
			count = 0;
			for (auto it = other.begin(); it != other.end(); ++it) {
				push_back(*it);
			}
		}
		/** Destruktor
        	 */
		~list() {
			while (head != nullptr) {
				Node * node = head;
				head = head->next;
				delete node;
			}
		}
		/** Operator kopiowania
        	 */
		list & operator=(const list & other) {
			clear();
			for (auto it = other.begin(); it != other.end(); ++it) {
				push_back(*it);
			}
			return *this;
		}
		/**
		   Zwraca iterator na początku listy
		 */
		iterator begin() {
			return iterator(head);
		}
		/**
		   Zwraca iterator wskazujący na miejsce za ostatnim elementem listy
		 */
		iterator end() {
			return iterator(tail);
		}
		/**
		   Zwraca const iterator na początku listy
		 */
		const_iterator begin() const {
			return const_iterator(head);
		}
		/**
		   Zwraca const iterator wskazujący na miejsce za ostatnim elementem listy
		 */
		const_iterator end() const {
			return const_iterator(tail);
		}
		/**
		   Wstawia element przed wskazywany element i zwraca iterator wskazujący na nowy element
		   @param[in] pos Iterator wskazujący na element przed którym zostanie wstawiona nowa wartość
		   @param[in] value Wartość do nowego elementu
		 */
		iterator insert(iterator pos, const T & value) {
			Node * node = new Node(value, pos.current->next);
			pos.current->next = node;
			if (pos == end()) {
				tail = node;
			}
			++count;
			return node;
		}
		/**
		   Usuwa element wskazywany przez iterator i zwraca iterator wskazujący na na następny element
		   @param[in] pos Iterator wskazujący na element do usunięcia
		 */
		iterator erase(iterator pos) {
			Node * node = pos.current->next;
			pos.current->next = node->next;
			if (node == tail) {
				tail = pos.current;
			}
			delete node;
			--count;
			return pos.current->next;
		}

		void push_front(T val) {
			insert(begin(), val);
		}

		void push_back(T val) {
			insert(end(), val);
		}

		//pop back O(n)

		bool empty() const {
			return (count == 0);
		}

		size_type size() const {
			return count;
		}

		void clear() {
			for (auto it = begin(); it != end();) {
				erase(it);
			}
		}
	};

}
