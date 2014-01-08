/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

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
    //ArrayList<PluginClass> pluginList;
    HashMap<String, PluginClass> plugins;
    Socket socket;

    public PluginManager() {
        //this.pluginList = new ArrayList<>();
        this.plugins = new HashMap();
    }
    
    public void loadPlugins()
    {
/*        String plugin = url.getPlugin();
        if(!pluginList.contains(plugin))
            pluginList.add(plugin);
*/
        NavPlugin np = new NavPlugin();
        if(!plugins.containsKey("np"))
        {
            np.load();
            plugins.put("np", np);
        }
            
/*        
        if(!pluginList.contains(np))
        {
            np.load();
            pluginList.add(np);
        }
*/        
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
