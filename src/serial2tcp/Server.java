/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial2tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import jssc.SerialPortException;

/**
 *
 * @author felipe
 */
public class Server implements Runnable {
    ServerSocket ssocket;
    boolean running;
    ArrayList<ClientConnection> clients;
    public Server(int port) throws IOException{
        ssocket = new ServerSocket(port);
    }
    
    private void cleanClients(){
        if(clients != null){
            for(ClientConnection c : clients)
                c.Close();
        }
        clients = new ArrayList<>();
    }
    
    private void removeClient(ClientConnection client){
        client.Close();
        clients.remove(client);
        System.out.printf("%s disconnected (%d)\n", client.getHostAddress(), clients.size());
    }
    
    public void Send(String t){
        Send(t.getBytes());
    }
    public void Send(int b){
        Send(new byte[] { (byte)b });
    }
    public void Send(byte[] b){
        if(clients != null){
            for(int i = 0; i < clients.size(); i++){
                ClientConnection client = clients.get(i);
                try{
                    client.Send(b);
                }
                catch(IOException ex){
                    removeClient(client);
                }
            }
        }
    }

    @Override
    public void run() {
        cleanClients();
        this.running = true;
        
        while(running){
            try {
                Socket sclient = ssocket.accept();
                ClientConnection client = new ClientConnection(sclient) {
                    @Override
                    public void Received(String hostAddress, int v) {
                        //Serial2tcp.server.Send(v);
                        try{ Serial2tcp.port.writeInt(v); }
                        catch(SerialPortException ex){ ErrorLog.Log(ex); }
                        System.out.printf("%s: %d\n", hostAddress, v);
                    }
                };
                client.Start();
                clients.add(client);
                
                System.out.printf("%s connected (%d)\n", client.getHostAddress(), clients.size());
            } catch (IOException ex) { ErrorLog.Log(ex); }
        }
    }
    
    public void stop(){
        running = false;
    }
    
}
