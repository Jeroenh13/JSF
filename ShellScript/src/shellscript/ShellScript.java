/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shellscript;

/**
 *
 * @author jeroen
 */
public class ShellScript {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(String env: args)
        {
            String value = System.getenv(env);
            if(value != null)
            {
                System.out.format("%s=%s%n",env,value);
            }
            else
            {
                System.out.format("%s is" + " not assigned.%n",env);
            }
        }
    }
}
