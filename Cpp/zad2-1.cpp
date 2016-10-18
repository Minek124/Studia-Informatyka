#include <iostream>
#include <vector>

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
  int max = -2147483647;
  int min = 2147483647;
  for(auto i : v){
    if(i > max)
      max = i;
    if(i < min)
      min = i;
  }
  cout << "min:" << min << " max:" << max << endl;
  return 0;
}
