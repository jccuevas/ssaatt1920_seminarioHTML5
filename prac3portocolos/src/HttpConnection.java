
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
            BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = bis.readLine();
            String[] partes = line.split(" ");
            if(partes != null){ //Se analizan dos parámetros fundamentales para saber que la petición está bien: que exista y que tenga
                //tres partes. Si es así, ya se empieza a mirar lo que hay en cada una de las partes.
                
                if(partes.length ==3){
                
                //Se debe de dar soporte a los códigos de estado 400, 404, 405  y 505.
                //El 400 corresponde a petición incorrecta.
                //El 404 corresponde a que no existe el recurso solicitado (cualquiera diferente de los idex.html, image.jpg o style.css).
                //El 405 corresponde a que el método usado no está permitido (método diferente de GET).
                //El 505 corresponde a que la versión HTTP especificada por el cliente no está soportada.
                 
                    if(partes[0].compareToIgnoreCase("GET")==0){
                    //Como se ve, si se analiza la primera parte y el método es GET, ya cabe analizar el resto, pero
                    //sabemos que no va a haber un error del tipo 405. Se analiza la versión de HHTP.

                        
                        if(partes[2].compareToIgnoreCase("HTTP/1.0")!=0 && partes[2].compareToIgnoreCase("HTTP/1.1")!=0){//Se añade soporte al código de estado 505

                        if(partes[2].compareToIgnoreCase("HTTP/1.0")!=0 && partes[2].compareToIgnoreCase("HTTP/1.1")!=0){//Se añade soporte al código de estado 500

                        
//la comparación tenias un or por lo que si una se cumplía la otra no y al ser un or era verdadera. 
                          
  dos.write(("HTTP/1.1 505 Bad Version\r\n").getBytes());//Después de cada código de estado
                            //añadimos sus correspondientes cabeceras
                            dos.write(("Connection: Close\r\n").getBytes());
                            dos.write(("Server: Padre Poveda\r\n").getBytes());
                            dos.write(("Allow: GET\r\n").getBytes());
                            dos.write(("\r\n").getBytes());
                
                            dos.flush();
                            

                        }else{
                        
                            String resource = getDefaultResource(partes[1]);
                
                            try{
                
                            File archivo = new File(resource);
                            FileInputStream fis = null;
                            fis = new FileInputStream(archivo);
                            byte[] data = new byte [(int)archivo.length()];
                            fis.read(data);
                    
                            int length = data.length;
                    
                            String type = getType(partes[1]);
                
                            dos.write(("HTTP/1.1 200 OK\r\n").getBytes());
                            dos.write(("Connection: Close\r\n").getBytes());
                            //dos.write(("Date:").getBytes()); No se incluye tal cabecera por indicación del profesor.
                            dos.write(("Server: Padre Poveda\r\n").getBytes());//Llamamos al servidor Padre Poveda, dado que su nombre
                            //es indiferente a efectos de resultado en la práctica.
                            dos.write(("Allow: GET\r\n").getBytes()); //Sólo se permite el método GET en el guión de la práctica.
                            dos.write(("Content-Type: "+type+"\r\n").getBytes());
                            dos.write(("Content-Length: "+length+"\r\n").getBytes());
                
                            dos.write(("\r\n").getBytes());
                
                            dos.flush();
                            dos.write(data);
                            dos.flush();
                
                        
                            }catch(FileNotFoundException fex){
                                //Se añade el código de estado  404 cuando no se encuentre el recurso solicitado.
                        
                                dos.write(("HTTP/1.1 404 File Not Found\r\n").getBytes());
                                dos.write(("Connection: Close\r\n").getBytes());
                                dos.write(("Server: Padre Poveda\r\n").getBytes());
                                dos.write(("Allow: GET\r\n").getBytes());
                                dos.write(("\r\n").getBytes());
                
                                dos.flush();
                            }
                        }

                        }else{//aquí es donde debes hacer la lectura del archivo, porque es el caso de ser un get y la versión correcta. 



                         } 

                    
                    }else{
                            //Se añade el soporte al código de estado 405. El método es diferente de GET.
                            dos.write(("HTTP/1.1 405 Method Not Allowed\r\n").getBytes());
                            dos.write(("Connection: Close\r\n").getBytes());
                            dos.write(("Server: Padre Poveda\r\n").getBytes());
                            dos.write(("Allow: GET\r\n").getBytes());
                            dos.write(("\r\n").getBytes());
                
                            dos.flush();
                    }
                     
                }else{
                //Si las partes de la primera línea no son tres, estamos seguros de que es una petición incorrecta, luego
                //en este apartado añadimos el control de el error 400. 
                dos.write(("HTTP/1.1 400 Bad Request\r\n").getBytes());
                dos.write(("Connection: Close\r\n").getBytes());
                dos.write(("Server: Padre Poveda\r\n").getBytes());
                dos.write(("Allow: GET\r\n").getBytes());
                dos.write(("\r\n").getBytes());
                
                dos.flush();
            }
            
            //dos.flush();
          }
           
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
    
    
    protected String getType(String resource){//Método que se encarga de devolver el tipo de recurso solictado para luego
    //incluirlo en la cabecera Content-Type. Como se ve, habrán los tipos text/html, image/jpeg y style/css.
    
    String type = "";
    
    if(resource.endsWith(".html")==true){
        type="text/html";
    }
    if(resource.endsWith(".jpg")==true){
       type="image/jpeg";  
    }
    if(resource.endsWith(".css")==true){
        type="style/css";
    }
    
    return type;
    
    }
    
    protected String getDefaultResource(String path){ //Método que devuelve el recurso pedido por el cliente.
    
        String resource = "";
        
        if (path.equals("/")){
        
            resource="./index.html";
        }else{
            resource="."+path;
        }
    
        return resource;
    }
    
}
