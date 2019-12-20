
//Práctica 3 de la asignatura Protocolos de Transporte.
//Realizado por Juan Fontiveros Sánchez y Alejandro Trujillo Moya.

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                //se debe de dar soporte a los códigos de estado 400, 404, 405  y 505.
                //El 400 corresponde a petición incorrecta.
                //El 404 corresponde a que no existe el recurso solicitado (cualquiera diferente de los idex.html, image.jpg o style.css).
                //El 405 corresponde a que el método usado no está permitido (método diferente de GET).
                //El 505 corresponde a que la versión HTTP especificada por el cliente no está soportada.
                 
                     
                    if(partes[0]=="GET"){
                    //Como se ve, si se analiza la primera parte y el método es GET, ya cabe analizar el resto, pero
                    //sabemos que no va a haber un error del tipo 405. Se analiza la versión de HHTP.
                        if(partes[2]!="HTTP/1.0" || partes[2]!="HTTP/1.1"){//Se añade soporte al código de estado 500
                        
                            dos.write(("HTTP/1.1 505 Bad Version\r\n").getBytes());//Después de cada código de estado
                            //añadimos sus correspondientes cabeceras
                            dos.write(("Connection:Close\r\n").getBytes());
                            dos.write(("Server:Padre Poveda\r\n").getBytes());
                            dos.write(("Allow: GET\r\n").getBytes());
                            dos.write(("\r\n").getBytes());
                
                            dos.flush();
                            
                        }
                    
                    }else{
                            //Se añade el soporte al código de estado 405. El método es diferente de GET.
                            dos.write(("HTTP/1.1 405 Method Not Allowed\r\n").getBytes());
                            dos.write(("Connection:Close\r\n").getBytes());
                            dos.write(("Server:Padre Poveda\r\n").getBytes());
                            dos.write(("Allow: GET\r\n").getBytes());
                            dos.write(("\r\n").getBytes());
                
                            dos.flush();
                    }
                    
                    //
                    //dos.write(("HTTP/1.1 400 Bad Request\r\n").getBytes());
                    
                    
                String resource = getDefaultResource(partes[1]);
                try{
                File archivo = new File(resource);
                FileInputStream fis = null;
                fis = new FileInputStream(archivo);
                byte[] data = new byte[(int)archivo.length()];
                fis.read(data);
                    
                
                int length = data.length;
                    
                String type = getType(partes[1]);
                
              
            
                dos.write(("HTTP/1.1 200 OK\r\n").getBytes());
                
                dos.write(("Connection:Close\r\n").getBytes());
                dos.write(("Content-type:"+type+"\r\n").getBytes());
                //dos.write(("Date:").getBytes()); No se incluye tal cabecera por indicación del profesor.
                dos.write(("Server:Padre Poveda\r\n").getBytes());//Llamamos al servidor Padre Poveda, dado que su nombre
                //es indiferente a efectos de resultado en la práctica.
                dos.write(("Allow: GET\r\n").getBytes()); //Sólo se permite el método GET en el guión de la práctica.
                dos.write(("Content-length:"+length+"\r\n").getBytes());
                
                dos.write(("\r\n").getBytes());
                
                dos.flush();
                dos.write(data);
                dos.flush();
                
                    }catch(FileNotFoundException fex){
                    //Se añade el c´digo de estado  404 cuando no se encuentre el recurso solicitado.
                        
                    dos.write(("HTTP/1.1 404 File Not Found\r\n").getBytes());
                    dos.write(("Connection:Close\r\n").getBytes());
                    dos.write(("Server:Padre Poveda\r\n").getBytes());
                    dos.write(("Allow: GET\r\n").getBytes());
                    dos.write(("\r\n").getBytes());
                
                    dos.flush();
                    
                    }
                       
                }
            }else{
                //Si las partes de la primera línea no son tres, estamos seguros de que es una petición incorrecta, luego
                //en este apartado añadimos el control de el error 400. 
                dos.write(("HTTP/1.1 400 Bad Request\r\n").getBytes());
                dos.write(("Connection:Close\r\n").getBytes());
                dos.write(("Server:Padre Poveda\r\n").getBytes());
                dos.write(("Allow: GET\r\n").getBytes());
                dos.write(("\r\n").getBytes());
                
                dos.flush();
            }
            
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
