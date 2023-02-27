package practica;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;

import utility.Header;
import utility.Options;

public class Cifrado {
	 Clave clave;
	 int blockSize;
	 String algoritmo = "RSA/ECB/PKCS1Padding";
	 InputStream file;
	    

	 /**
	  * Constructor parametrizado de la clase
	  * @param file fichero sobre el que se van a realizar las operaciones de cifrado y descifrado
	  * @param clave clave a utilizar en las operaciones de cifrado y descifrado
	  */
	 public Cifrado(InputStream file, Clave clave) {
		 this.file = file;
	     this.clave = clave;
	 }
	 
	 public void cifrar(String nameFile, int modo){
	        if(!clave.getPriv().getAlgorithm().equals("DSA")) {	        	
	        	try(OutputStream out = new FileOutputStream( nameFile.substring(0, nameFile.lastIndexOf(".")) + ".cif" )){
	    	        Cipher cipher = Cipher.getInstance(algoritmo);
	    	        cipher.init(Cipher.ENCRYPT_MODE,clave.getPub());
	    	        byte[] salt = new byte[] {};
	    	        Header h = new Header(Options.OP_PUBLIC_CIPHER,algoritmo,Options.OP_NONE_ALGORITHM,salt);	    	    
	                h.save(out);
	                
	                /*
	                 * 512 -> (en octetos) 64 -11 = 53
	                 * 768 -> 512 + 256 -> (en octetos) 64 + 32 -11 = 85
	                 * 1024 -> 512 +512 -> (en octetos) 64 + 64 -11 = 117
	                 */
	               
	                /*
	                 * Harcodeado. Los caracteres 20 a 23 indican el tamaño de la clave en el toString
	                 * (solo valido para las claves de 512 y 768)
	                 * Las claves de 1024 se seleccionan cuando el resultado sea 102
	                 */
	                int tamClave = Integer.parseInt(clave.getPub().toString().substring(20, 23));
	                
//	                System.out.println(clave.getPub().toString());
//	                Thread.sleep(3000);
	                
	                switch(tamClave) {
		                case 512:
		                	blockSize=53;
			                break;
			            case 768:
			            	blockSize=85;
			                break;
			            case 102:
			            	blockSize=117;
			                break;
	                }
	                final byte[] b = new byte[blockSize];
	                int i;
	                System.out.print("\n");
	                while( (i=file.read(b)) != -1){
	                    System.out.print(blockSize + " ");
	                    out.write(cipher.doFinal(b,0,i));
	                }
	                file.close();
	                out.flush();
	    		    out.close();
	                System.out.println("\nFichero cifrado correctamente");
    	        }
    	        catch(Exception e){
    	            System.out.println("Se ha producido un error al intentar cifrar el archivo");
    	        }
	        }
	        else System.out.println("No se puede realizar esta operación con una clave DSA");
	    }
	 
	 /**
	  * Funcion dedicada a descifrar un fichero cifrado con anterioridad
	  * @param nameFile Nomnre del fichero a descifrar. El fichero deberá tener extensión .cif y generará un .cla
	  */
	   public void descifrar(String nameFile, int modo){
		   
		   
		   if(!clave.getPriv().getAlgorithm().equals("DSA")) {
		        try(OutputStream out = new FileOutputStream( nameFile.substring(0, nameFile.lastIndexOf(".")) + ".cla" )){
	
		        Header h = new Header();
		        h.load(file);
		        algoritmo = h.getAlgorithm1();
		        Cipher cipher = Cipher.getInstance(algoritmo);
		        cipher.init(Cipher.DECRYPT_MODE,clave.getPriv());
		        
		        /*
                 * 512 -> (en octetos) 64 
                 * 768 -> 512 + 256 -> (en octetos) 64 + 32 = 96
                 * 1024 -> 512 +512 -> (en octetos) 64 + 64 = 128
                 */
               
                /*
                 * Harcodeado. Los caracteres 32 a 35 indican el tamaño de la clave en el toString
                 * (solo valido para las claves de 512 y 768)
                 * Las claves de 1024 se seleccionan cuando el resultado sea 102
                 */
                int tamClave = Integer.parseInt(clave.getPriv().toString().substring(32, 35));
                
//                System.out.println(tamClave);
//                Thread.sleep(3000);
                   
                switch(tamClave) {
	                case 512:
	                	blockSize=64;
		                break;
		            case 768:
		            	blockSize=96;
		                break;
		            case 102:
		            	blockSize=128;
		                break;
                }
	
		            final byte[] b = new byte[blockSize];
		            int i;
		            System.out.print("\n");
		            while( (i=file.read(b)) != -1){
		                System.out.print(blockSize + " ");
		                //Si se hace directamente con doFinal(b) no recorre bien el fichero
		                out.write(cipher.doFinal(b,0,i));
		            }
	
		            file.close();
		            out.flush();
		            out.close();
		            System.out.println("\nFichero descifrado correctamente");
		        }
		        catch(Exception e){
		            System.out.println("Se ha producido un error al intentar descifrar el archivo");
//		        	e.printStackTrace();
		        }
		   }
		   else System.out.println("No se puede realizar esta operación con una clave DSA");
	    }
	 
}
