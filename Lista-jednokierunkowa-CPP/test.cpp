#include<iostream>
#include "uj_list.hpp"

#include <cppunit/ui/text/TestRunner.h>
#include <cppunit/TestFixture.h>
#include <cppunit/TestAssert.h>
#include <cppunit/TestResult.h>
#include <cppunit/TestCase.h>
#include <cppunit/extensions/HelperMacros.h>
//#include <cppunit/TestCaller.h>
//#include <cppunit/CompilerOutputter.h>
//#include <cppunit/XmlOutputter.h>

using namespace std;

class Klasa {
public:
  int x;
  int y;
  Klasa(): x(1), y(2) {}
  Klasa(int a,int b): x(a), y(b) {}
  bool sprawdz(){
    return x==y;
  }
};

class Test : public CppUnit::TestFixture  {
CPPUNIT_TEST_SUITE( Test );
CPPUNIT_TEST( fillTest );
CPPUNIT_TEST( test1 );
CPPUNIT_TEST( clearTest );
CPPUNIT_TEST( testCopy );
CPPUNIT_TEST( eraseTest );
CPPUNIT_TEST( classTest );
CPPUNIT_TEST_SUITE_END();
private:
  uj::list<int> l;
public:
  void setUp() {
    l.push_front(3);
	l.push_front(2);
	l.push_front(1);
	l.push_back(4);
	l.push_back(5);
	l.insert(l.end(), 6);
  }
  void tearDown() {}
  
  void fillTest() {
    int i = 1;
    for (auto it = l.begin(); it != l.end(); it++) {
		CPPUNIT_ASSERT_EQUAL(*it,i++);
	} 
  }
  void test1() {
    *(++l.begin()) = 99;
	int i = 1;
    for (auto it = l.begin(); it != l.end(); it++) {
		if(i != 2){
		  CPPUNIT_ASSERT_EQUAL(*it,i++);
		} else{
		  CPPUNIT_ASSERT_EQUAL(*it,99);
		  ++i;
		}
	}
  }
  void clearTest() {
    l.clear();
	CPPUNIT_ASSERT_EQUAL(l.size(),0);
	CPPUNIT_ASSERT_EQUAL(l.empty(),true);
	l.push_back(1);
	l.push_back(2);
	int i = 1;
    for (auto it = l.begin(); it != l.end(); it++) {
		CPPUNIT_ASSERT_EQUAL(*it,i++);
	}
  }
  void testCopy() {
    uj::list<int> l2(l);
	int i = 1;
    for (auto it = l2.begin(); it != l2.end(); it++) {
		CPPUNIT_ASSERT_EQUAL(*it,i++);
	}
	
	uj::list<int> l3;
	l3 = l;
	i = 1;
    for (auto it = l2.begin(); it != l2.end(); it++) {
		CPPUNIT_ASSERT_EQUAL(*it,i++);
	}
  }
  void eraseTest(){
    while(!l.empty()){
		l.erase(l.begin());
	}
	CPPUNIT_ASSERT_EQUAL(l.size(),0);
	l.push_back(5);
	CPPUNIT_ASSERT_EQUAL(*l.begin(), 5);
  }
  
  void eraseTest2(){
    l.erase(++l.begin());
	CPPUNIT_ASSERT_EQUAL(*l.begin(), 1);
	int i = 3;
    for (auto it = ++l.begin(); it != l.end(); it++) {
		CPPUNIT_ASSERT_EQUAL(*it,i++);
	}
  }
  void classTest(){
     uj::list<Klasa> l2;
	 l2.push_back(Klasa(1,1));
	 l2.push_back(Klasa(3,3));
	 l2.push_back(Klasa(-3,-3));
	 for (auto it = l2.begin(); it != l2.end(); it++) {
		CPPUNIT_ASSERT_EQUAL(it->sprawdz(), true);
	}
  }
  
};

int main() {

  CppUnit::TextUi::TestRunner runner;
  runner.addTest( Test::suite() );
  runner.run();
  return 0;
}
