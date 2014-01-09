/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Abgabe;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author if12b061
 */
public class UrlClass {
    private final String url;
    private String plugin;
    private final ArrayList<String> tokens;

    public UrlClass(String url) {
        this.tokens = new ArrayList<>();
        this.url = url;
        divUrl();
    }

    private void divUrl()
    {
        StringTokenizer st = new StringTokenizer(this.url, "/?=");
        int count = st.countTokens();
        System.out.println(count);
        while(st.hasMoreTokens())
        {
            tokens.add(st.nextToken());
        }
        System.out.println(tokens);
    }
  
    public ArrayList<String> getTokens()
    {
        return this.tokens;
    }
    
    public String getPlugin()
    {
        this.plugin = "static";
        return this.plugin;
    }

}
