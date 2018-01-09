/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial2tcp;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author felipe
 */
public abstract class ClientConnection {
    Socket socket;
    Thread thread;
    public ClientConnection(Socket socket){
        this.socket = socket;
        
        this.thread = new Thread(() -> {
            boolean run = true;
            while(run){
                try{
                    int v = socket.getInputStream().read();
                    if(v != -1)
                        Received(socket.getInetAddress().getHostAddress(), v);
                    else
                        run = false;
                } catch(IOException ex) { run = false; }
            }
        });
    }
    
    public void Start(){
        thread.start();
    }
    
    public void Send(int v) throws IOException{
        this.socket.getOutputStream().write(v);
    }
    
    public void Send(byte[] v) throws IOException{
        this.socket.getOutputStream().write(v);
    }
    
    public String getHostAddress(){
        return socket.getInetAddress().getHostAddress();
    }
    
    public void Close(){
        try{ socket.close(); }
        catch(IOException ex){ ErrorLog.Log(ex); }
    }
    
    public abstract void Received(String hostAddress, int v);
}
