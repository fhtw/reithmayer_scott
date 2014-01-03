/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

/**
 *
 * @author if12b061 & if12b052
 */
public class StaticPlugin {

    private String url;
    private String file;

    StaticPlugin(String url)
    {
      this.url = url;
    }
    
    public String getSite()
    {
        switch (url) {
            case "/":
                file = "index.html";
                break;
            case "/test":
                file = "test.html";
                break;
            case "/test2":
                file = "test2.html";
                break;
            default:
                file = "error.html";
                break;
        }
        
        return file;
    }
    
}
