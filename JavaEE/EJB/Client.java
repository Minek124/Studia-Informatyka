/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import dddd.fasolkaRemote;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
/**
 *
 * @author minek
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NamingException {
        // TODO code application logic here
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "remote://127.0.0.1:4447");
        properties.put("jboss.naming.client.ejb.context",true);
        Context context = new InitialContext(properties);
        fasolkaRemote myBean = (fasolkaRemote)context.lookup("EJBModule1/fasolka!dddd.fasolkaRemote");
        System.out.println("XXXXXXXXXXXXXXX");
        myBean.setFirstName("Janek");
        System.out.println(myBean.getFirstName());
        
    }
    
}
