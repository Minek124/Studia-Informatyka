#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main()
{
  vector<int> v;
  while(true){
    int x;
    cin >> x;
    if(x != 0)
      v.push_back(x);
    else
      break;
  }
  auto mm = minmax_element(v.begin(), v.end());
  cout << "min:" << *(mm.first) << " max:" << *(mm.second) << endl;
  return 0;
}
