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
public abstract class PluginClass {
    public abstract void load();
    public abstract void start(Socket socket, UrlClass url);
}
