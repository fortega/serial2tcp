/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial2tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author felipe
 */
public abstract class InputReceiver implements Runnable {
    private InputStream in;
    private boolean running;
    
    public InputReceiver(InputStream out) throws IOException{
        this.in = in;
    }
    
    @Override
    public void run() {
        running = true;
        while(running){
            try {
                int v = in.read();
                Received(v);
            } catch(Exception ex){ running = false; }
        }
    }
    
    public abstract void Received(int v);
    
}
