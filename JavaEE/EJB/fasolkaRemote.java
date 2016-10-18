/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dddd;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 *
 * @author minek
 */
@Remote
public interface fasolkaRemote {
   public void setFirstName(String x);
   public String getFirstName();
}
