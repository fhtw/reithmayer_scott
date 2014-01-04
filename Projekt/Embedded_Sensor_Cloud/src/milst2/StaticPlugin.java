/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.net.Socket;

/**
 *
 * @author if12b061 & if12b052
 */
public class StaticPlugin{

    private String url, file;
    private Socket socket;

    StaticPlugin(Socket s, String url)
    {
      this.url = url;
      this.socket = s;
    }
    
    public void start()
    {
        switch (url) {
            case "[]":
                file = "index.html";
                break;
            case "[test]":
                file = "test.html";
                break;
            case "[test2]":
                file = "test2.html";
                break;
            default:
                file = "error.html";
                break;
        }
        
//        System.out.println(url);
        new Response(socket, file).sendResponse();
        
    }
    
}
