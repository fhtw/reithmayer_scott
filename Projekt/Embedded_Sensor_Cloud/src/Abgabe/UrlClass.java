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
    private final ArrayList<String> parameters;

    public UrlClass(String url) {
        this.parameters = new ArrayList<>();
        this.url = url;
        divUrl();
    }

    private void divUrl() {
        StringTokenizer st = new StringTokenizer(this.url, "/?=");
        System.out.println("urlparts: " + st.countTokens());
        if (st.hasMoreTokens()) {
            this.plugin = st.nextToken();
        }
        while (st.hasMoreTokens()) {
            parameters.add(st.nextToken());
        }
        System.out.println(parameters);
    }

    public ArrayList<String> getParameters() {
        return this.parameters;
    }

    public String getPlugin() {
        return this.plugin;
    }

    //wird bei POST request ausgeführt (da bei POST Parameter nicht in URL sind)
    public void addParameters(String line) {
        //ev. erst über '&' teilen und dann über '=' um leere values zu erkennen..
        StringTokenizer strTok = new StringTokenizer(line, "=&");
        while (strTok.hasMoreTokens()) {
            this.parameters.add(strTok.nextToken());
        }
    }
    /*
     public String getPlugin()
     {
     this.plugin = "static";
     return this.plugin;
     }
    
     */
}
