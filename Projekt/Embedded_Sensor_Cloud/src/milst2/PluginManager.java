/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Angelika Reithmayer
 */
public class PluginManager {
    Collection<String> plugins;

    public PluginManager() {
        this.plugins = new ArrayList<>();
    }
    
    public void loadPlugin(UrlClass url) throws ClassNotFoundException
    {
        String plugin = url.getPlugin();
        if(!plugins.contains(plugin))
            plugins.add(plugin);
        
        
        
        
Class MyObj = PluginManager.class;
String className = MyObj.getSimpleName();
System.out.println(className);

Class MyObj2 = this.getClass();
      
Method[] methods = MyObj2.getMethods();

for(Method method : methods){
    System.out.println("method = " + method.getName());
}
        /*        switch (plugin)
         * {
         * case "static":
         * StaticPlugin pm = new StaticPlugin(url.getUrl());
         * String file = pm.getSite();
         * }*/        
    }
    
}
