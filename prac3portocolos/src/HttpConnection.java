
//Práctica 3 de la asignatura Protocolos de Transporte.
//Realizado por Juan Fontiveros Sánchez y Alejandro Trujillo Moya.

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpConnection implements Runnable{

    Socket socket = null;
    
    public HttpConnection (Socket s){
        socket=s;
    }
    
    @Override
    public void run() {
        DataOutputStream dos = null;
        try {
            System.out.println("Starting new HTTP connection with "+socket.getInetAddress().toString());
            dos = new DataOutputStream(socket.getOutputStream());
            dos.write("200 OK".getBytes());
            dos.flush();
            BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = bis.readLine();
            String[]partes = line.split(" ");
            if(partes != null){
                if(partes.length ==3){
                    
                File archivo = new File("C:\Users\Fonti\Desktop\protocolos",);
                DataInputStream dis = null;
                 dis = new DataInputStream(archivo);
                
                    
                byte[] data = null;
                int length = data.length;
                    
                String type = getType(partes[1]);
                
              
            
                dos.write(("200 OK\r\n\r\n"+line).getBytes());
                
                dos.write(("Connection:close").getBytes());
                dos.write(("Content-type:"+type+"\r\n").getBytes());
                dos.write(("Date:").getBytes());
                dos.write(("Server:").getBytes());
                dos.write(("Allow:").getBytes());
                dos.write(("Content-length:"+length+"\r\n").getBytes());
                
                dos.write(("\r\n").getBytes());
                
                dos.flush();
                dos.write(data);
                dos.flush();
                
                
                }
            }
            while((line= bis.readLine())!=null){
            System.out.println("HTTP HEADER: "+line);
            }
            
            
            dos.write(("ECO "+line).getBytes());
            dos.flush();
        } catch (IOException ex) {
            Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dos.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    
    protected String getType(String resource){
    
    String type = "";
    
    if(resource.endsWith(".html")){
        type="text/html";
    }
    if(resource.endsWith(".jpeg")){
       type="image/jpeg";  
    }
    if(resource.endsWith(".css")){
        type="style/css";
    }
    
    return type;
    
    }
    
    protected String getDefaultResource(String path){
    
        String resource = "";
        
        if (path.equals("/")){
        
            resource="index.html";
        }else{
            resource="." +path;
        }
    
        return resource;
    }
    
}
