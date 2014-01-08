/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.net.Socket;

/**
 *
 * @author Angelika Reithmayer
 */
public interface PluginInterface {
    public void load();
    public void start(Socket socket, UrlClass url);
}
