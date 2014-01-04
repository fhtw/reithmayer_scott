/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.io.IOException;
//import java.lang.reflect.Method;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Angelika Reithmayer
 */
public class PluginManager {
    Collection<String> plugins;
    Socket socket;

    public PluginManager(Socket s) {
        this.plugins = new ArrayList<>();
        this.socket = s;
    }
    
    public void loadPlugin(UrlClass url) throws ClassNotFoundException
    {
        String plugin = url.getPlugin();
        if(!plugins.contains(plugin))
            plugins.add(plugin);
        
        
        
        //  Reflections - Experiment
/*
        Class MyObj = PluginManager.class;
        String className = MyObj.getSimpleName();
        System.out.println(className);

        Class MyObj2 = this.getClass();

        Method[] methods = MyObj2.getMethods();

        for (Method method : methods) {
            System.out.println("method = " + method.getName());
        }
*/
        
        
        
        
        /*        switch (plugin)
         * {
         * case "static":
         * StaticPlugin pm = new StaticPlugin(url.getUrl());
         * String file = pm.getSite();
         * }*/        
    }
    
    public void startPlugin(UrlClass url) throws IOException, ParseException
    {
        ArrayList<String> urlParts = url.getTokens();
        
        if(!urlParts.isEmpty()&& urlParts.get(0).matches("GetTemperature"))
        {
            TempPlugin tp = new TempPlugin();
            tp.getTemp(socket, url);
        }else{
            StaticPlugin sp = new StaticPlugin(socket, urlParts.toString());
            sp.start();
        }

    }
}
