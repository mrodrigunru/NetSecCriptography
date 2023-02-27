package practica;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import java.io.*;

public class Clave {

    
    File fKey;
    KeyPairGenerator kpg;
    KeyPair keyP;
    int tam = 0;
    String keyAlg;
    Scanner teclado;
    String nombre;
    
    /**
     * Contructor por defecto de la clase. Instancia tanto el generador de claves como el par de claves, asi como crea el fichero donde se van a almacenar las claves
     */
    public Clave(){
    	try{               	
        	teclado = new Scanner(System.in);        	
        	System.out.println("Introduce el nombre del fichero de claves (sin extensión)");       	
        	nombre= teclado.nextLine();          
        	File archivo = new File(nombre);            
            if(!archivo.exists()){
            	
            	 fKey = new File(nombre+".key");
            }
        }catch(Exception e){
//            System.out.println("Ya existe un fichero de claves");
        }
    }
    

    /**
     * Funcion dedicada a obtener la clave privada
     * @return la clave privada
     */
    public PrivateKey getPriv(){
        return keyP.getPrivate();
    }

    /**
     * Funcion dedicada a obtener la clave publica
     * @return la clave publica
     */
    public PublicKey getPub(){
        return keyP.getPublic();
    }
    
    
    public String getfName() {
		return nombre;
    	
    }
    
    
    
    //TODO Cifrar el fichero de claves

    /**
     * Funcion dedicada a crear las claves. Serialización hacia un fichero
     * @return true si se crean las claves con éxito, false en caso contrario
     */
    public boolean crearClaves(){
    	
    	
    	try {
    		teclado = new Scanner(System.in);
    		System.out.println("Elige un algorimo de creación de claves: ");
 	        System.out.println("1.  RSA");
 	        System.out.println("2.  DSA");
 	        System.out.println("->");
 	       int opcion;
 	       opcion=teclado.nextInt();
	       switch(opcion){
	            case 1:
	                keyAlg="RSA";
	                break;
	            case 2:
	            	keyAlg="DSA";
	                break;
	            
	        }
 	       
 	       System.out.println("Generando claves "+keyAlg);
 	       kpg = KeyPairGenerator.getInstance(keyAlg);
 	       
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
    	
//    	teclado = new Scanner(System.in);
		System.out.println("Elige la longitud de las claves: ");
        System.out.println("1.	512");
        System.out.println("2.	768");
        System.out.println("3.	1024");
	    System.out.println("->");
	    int tamano;
	    tamano=teclado.nextInt();
	    switch(tamano) {
        case 1:
        	tam=512;
            break;
        case 2:
        	tam=768;
            break;
        case 3:
        	tam=1024;
            break;
    }
	    //tam es el tamaño de las claves a generear
	    
//        System.out.println("tam = " + tam);
        kpg.initialize(tam);
        keyP = kpg.generateKeyPair();
        try(FileOutputStream fOut = new FileOutputStream(fKey)){
            ObjectOutputStream oos = new ObjectOutputStream(fOut);
            oos.writeObject(keyP);
            oos.close();
            fOut.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Funcion dedicada a cargar unas claves ya existentes. Serialización hacia un fichero
     * @return true si se cargan las claves con éxito, false en caso contrario
     */
    public boolean cargarClaves(){
        try(FileInputStream fOut = new FileInputStream(fKey)){
            ObjectInputStream oos = new ObjectInputStream(fOut);
            keyP = (KeyPair) oos.readObject();
            oos.close();
            fOut.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
