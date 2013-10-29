/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst1;

/**
 *
 * @author if12b061 & if12b052
 */
public class PluginManager {

    private String url;
    private String plugin;

    PluginManager(String url)
    {
      this.url = url;
    }
    
    public String getPlugin()
    {
        switch (url) {
            case "/":
                plugin = "index.html";
                break;
            case "/test":
                plugin = "test.html";
                break;
            case "/test2":
                plugin = "test2.html";
                break;
            default:
                plugin = "error.html";
                break;
        }
        
        return plugin;
    }
    
}
