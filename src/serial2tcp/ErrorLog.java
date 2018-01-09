/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serial2tcp;

/**
 *
 * @author felipe
 */
public class ErrorLog {
    public static void Log(Exception ex){
        System.err.printf("E: %s\n", ex.getMessage());
    }
}
