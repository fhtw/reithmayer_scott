/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abgabe;

import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Angelika Reithmayer
 */
public class PluginManager {
    HashMap<String, PluginClass> plugins;
    Socket socket;

    public PluginManager() {
        this.plugins = new HashMap();
    }
    
    public void loadPlugins()
    {
        NavPlugin np = new NavPlugin();
        if(!plugins.containsKey("np"))
        {
            np.load();
            plugins.put("np", np);
        }
            
        //PluginManager sollte nachsehen ob benötigtes Plugin bereits geladen ist, falls nicht suchen ob es zum laden vorhanden ist?
        //mittels jar file???
        
        
        
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
     
    }
    
    public void startPlugin(UrlClass url, Socket s) throws IOException, ParseException
    {
        this.socket = s;
        ArrayList<String> urlParts = url.getTokens();
        String plugin;
        
        if(!urlParts.isEmpty())
        {
            plugin = urlParts.get(0).toLowerCase();
        }else{
            plugin = "static";
        }
        System.out.println(plugin);
        
        switch(plugin)
        {
            case "gettemperature":
                TempPlugin tp = new TempPlugin();
                tp.getTemp(socket, url);
                break;
            case "navi":
                
                PluginClass np = plugins.get("np");
                System.out.println(plugins.get("np").getClass().toString());
                np.start(socket, url);
                break;
            case "static":
            default:
                StaticPlugin sp = new StaticPlugin(socket, urlParts.toString().toLowerCase());
                sp.start();
                break;
        }
    }
}
