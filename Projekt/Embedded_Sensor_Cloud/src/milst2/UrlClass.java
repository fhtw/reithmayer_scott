/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package milst2;

/**
 *
 * @author if12b061
 */
public class UrlClass {
    private String url, plugin;

    public UrlClass(String url) {
        this.url = url;
    }

    public void divUrl(String url)
    {
        this.url = url;

    }

    public String getUrl()
    {
        return this.url;

    }
    
    public String getPlugin()
    {
        this.plugin = "static";
        return this.plugin;
    }

}
