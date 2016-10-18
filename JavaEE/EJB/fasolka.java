/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dddd;

import javax.ejb.Stateless;

/**
 *
 * @author minek
 */
@Stateless
public class fasolka implements fasolkaRemote {
     private String firstName = null;
  
     @Override
   public String getFirstName(){
      return firstName;
   }
   public void setFirstName(String x){
       firstName = (x+"Changed");
   }
}
