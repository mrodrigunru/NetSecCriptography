package practica;

import java.io.*;

import java.util.Scanner;

public class Practica4 {
	
	Clave clave;
	Cifrado CifradoClaves;
    Firma firma;
    Scanner teclado;
    File fKey;
	    
	public Practica4() {	
	
	}

	/**
	 * Funcion principal que ejecuta la funcionalidad del programa
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Practica4 practica4 = new Practica4();
		System.out.println("SRT - Practica 4");	
		
		while (true) {
			if (practica4.mainMenu() == true) {
				System.out.println("Saliendo del programa");
				break;
			}
		}
		
	}
	
	/**
	 * Menu principal del programa. Desde el se puede navegar por toda la funcionalidad del mismo
	 */
	 public boolean mainMenu(){
	        teclado = new Scanner(System.in);
	        int opcion;
	        boolean exit = false;

	        System.out.println("Elija una opcion:");
	        System.out.println("1. Operaciones con claves (Generacion/vista de claves)");
	        System.out.println("2. Firmar / Cifrar / Descifrar fichero");
	        System.out.println("0. Salir");
	        System.out.println("->");

	        if((opcion=teclado.nextInt())==1){
	            menuClaves();
	        }
	        else if(opcion==2){
	            menuFicheros();
	        }
	        else if(opcion==0) {
	        	exit=true;
	        }
			return exit;
	    }

	 /**
	  * Menu relativo a trabajar con operaciones sobre claves. Estas operaciones son generar claves y ver las mismas
	  */
	    public void menuClaves(){
	    	clave = new Clave();
		    fKey = new File(clave.getfName()+".key");
	        int opcion;
	        System.out.println("Selecciona una operacion:");
	        System.out.println("1.  Generar nuevo par de claves");
	        System.out.println("2.  Ver claves");
	        System.out.println("->");
	        
	      

	        if((opcion=teclado.nextInt())==1){

	            if(!fKey.exists()){
	            	
	            	clave.crearClaves();
	                System.out.println("Claves generadas");
	             
	            }

	            else{
	                System.out.println("Ya existen claves");
	            }

	        }
	        else if(opcion==2){

	            if(fKey.exists()){
	                clave.cargarClaves();
	                System.out.println("Clave Privada " + clave.getPriv().toString());
	                System.out.println("Clave Publica " + clave.getPub().toString());
	            }

	            else{
	                System.out.println("No existen claves. Genera un par");
	            }
	        }

	    }

	   /**
	    * Menu relativo a trabajar con operaciones sobre ficheros. Estas operaciones son cifrar, descifrar, firmar o verificar la firma de un fichero
	    */
	    public void menuFicheros(){
	    	clave = new Clave();
	        int opcion;
	        String fichero;
	        System.out.println("Selecciona el archivo con el que trabajar (el archivo desde encontrarse en el directorio del proyecto): ");
	        fichero=teclado.next();
	        File file = new File(fichero);
	        try(InputStream in = new FileInputStream(file)){

	            System.out.println("Selecciona una operacion:");
	            System.out.println("1. Cifrar");
	            System.out.println("2. Descifrar");
	            System.out.println("3. Firmar");
	            System.out.println("4. Verificar Firma");
	            System.out.println("->");
	            opcion=teclado.nextInt();
	            
	            fKey = new File(clave.getfName()+".key");
	            if(fKey.exists()){
	                clave.cargarClaves();
	            }
	            else{
	                System.out.println("Debes crear un par de claves para realizar esta operación");
	            }

	            switch(opcion){
	                case 1:
	                	CifradoClaves = new Cifrado(in, clave);
	                	CifradoClaves.cifrar(fichero, opcion);
	                    break;
	                case 2:
	                	CifradoClaves = new Cifrado(in, clave);
	                	CifradoClaves.descifrar(fichero, opcion);
	                    break;
	                case 3:
	                    menuFirma(fichero,in);
	                    break;
	                case 4:
	                    firma = new Firma(in, clave);
	                    firma.verificarFirma(fichero);
	                    break;
	            }
	        }catch(Exception e){
	            e.printStackTrace();
	        }

	    }
	    
	    /**
	     * Menu dedicado a elegir el algoritmo de firma que se quiere utilizar
	     * @param fichero Fichero a firmar
	     * @param in Flujo de entrada abierto sobre el fichero
	     */
	    public void menuFirma(String fichero,InputStream in){
	        int opcion;
	        String algoritmo="";
	        System.out.println("Selecciona un algoritmo de firma:");
	        System.out.println("1.  SHA1withRSA");
	        System.out.println("2.  MD2withRSA");
	        System.out.println("3.  MD5withRSA");
	        System.out.println("4.  SHA1withDSA");
	        System.out.println("->");
	        opcion=teclado.nextInt();
	        switch(opcion){
	            case 1:
	                algoritmo="SHA1withRSA";
	                break;
	            case 2:
	                algoritmo="MD2withRSA";
	                break;
	            case 3:
	                algoritmo="MD5withRSA";
	                break;
	            case 4:
	                algoritmo="SHA1withDSA";
	                break;
	        }
	        firma = new Firma(in, clave);
	        firma.firmar(algoritmo, fichero);
	    }

}
