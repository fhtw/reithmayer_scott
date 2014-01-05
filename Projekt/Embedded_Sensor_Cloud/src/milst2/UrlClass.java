/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package milst2;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author if12b061
 */
public class UrlClass {
    private String url, plugin;
    private ArrayList<String> tokens;

    public UrlClass(String url) {
        this.tokens = new ArrayList();
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
  
    public ArrayList getTokens()
    {
        return this.tokens;
    }
    
    public String getPlugin()
    {
        this.plugin = "static";
        return this.plugin;
    }

}
