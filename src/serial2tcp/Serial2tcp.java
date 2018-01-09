/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial2tcp;

import java.io.IOException;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author felipe
 */
public class Serial2tcp {

    /**
     * @param args the command line arguments
     */
    static final int PORT_NUM = 9600;
    private static Thread tServer;
    public static Server server;
    public static SerialPort port;
    
    private static void showPortAndExit(){
        System.out.println("Available ports:");
        for(String port : SerialPortList.getPortNames())
            System.out.println(port);
        System.exit(0);
    }
    
    private static void initPort(String name){
        port = new SerialPort(name);
        try{
            port.openPort();

            port.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            port.addEventListener((SerialPortEvent event) -> {
                if(event.isRXCHAR()){
                    try{
                        byte[] data = port.readBytes();
                        server.Send(data);
                    }catch(SerialPortException ex){
                        ErrorLog.Log(ex);
                    }
                }
            });
        }catch(SerialPortException ex){
            ErrorLog.Log(ex);
            System.exit(1);
        }
    }
    
    private static void initServer(){
        try{
            server = new Server(PORT_NUM);
        }catch(IOException ex){
            ErrorLog.Log(ex);
            System.exit(1);
        }
        tServer = new Thread(server);
        tServer.start();
    }
    
    public static void main(String[] args){
        //se define puerto?
        if(args.length != 1)
            showPortAndExit();
        else{
            initServer();
            initPort(args[0]);
        }
        
        try { System.in.read(); } //Esperamos
        catch(IOException ex){ ErrorLog.Log(ex); }
        
        server.stop();
        System.exit(0);
    }
}
