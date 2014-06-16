/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abgabe;

import Swe2Plugins.MicroERPService;
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
        this.plugins = new HashMap<>();
    }
    
    public void loadPlugins()
    {
//        MicroERPService np = new MicroERPService();
//        if(!plugins.containsKey("np"))
//        {
//            np.load();
//            plugins.put("np", np);
//        }
            
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
        ArrayList<String> parameters = url.getParameters();
        System.out.println("parameters: " + parameters);
        String plugin = url.getPlugin();
        
        if(plugin == null) {
            plugin = "static";
        }
        System.out.println("plugin: " + plugin);
        
        switch(plugin.toLowerCase())
        {
            case "erp":
                MicroERPService me = new MicroERPService();
                me.getData(socket, url);
                break;
            case "gettemperature":
                TempPlugin tp = new TempPlugin();
                tp.getTemp(socket, url);
                break;
//            case "navi":
//                PluginClass np = plugins.get("np");
//                System.out.println(plugins.get("np").getClass().toString());
//                np.start(socket, url);
//                break;
            case "static":
            default:
                StaticPlugin sp = new StaticPlugin(socket, plugin.toLowerCase());
                sp.start();
                break;
        }
    }
}
