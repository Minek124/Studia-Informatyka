#include <iostream>
#include <set>
#include <fstream>


using namespace std;

int main()
{
  set<string> s;
  
  ifstream myfile;
  myfile.open ("slowa.txt");
  while (!myfile.eof()) {
    string slowo;
    myfile >> slowo;
    s.insert(slowo);
  }
  myfile.close();

  
  for(string i : s){
    cout << i << endl;
  }

  return 0;
}
