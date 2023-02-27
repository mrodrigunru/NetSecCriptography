package practica;

import java.io.*;
import java.security.*;

import utility.Header;
import utility.Options;

public class Firma {

    Clave clave;
    int blockSize = 64;
    Signature dsa;
    InputStream file;

    public Firma(InputStream file, Clave clave){

        this.file=file;
        this.clave=clave;
    }
    
    /**
     * Funcion dedicada a la firma de un fichero
     * @param algoritmo Algoritmo elegido para realizar la firma
     * @param nameFile	Sobre del fichero que se desea firmar
     */
    public void firmar(String algoritmo,String nameFile){
        try {

            dsa = Signature.getInstance(algoritmo);
            dsa.initSign(clave.getPriv());
            OutputStream out = new FileOutputStream( nameFile.substring(0, nameFile.lastIndexOf(".")) + ".fir" );
            byte[] b = new byte[blockSize];

            int i;
            while( (i=file.read(b)) != -1 ){   
                dsa.update(b);
            }

            Header h = new Header(Options.OP_HASH_MAC,Options.OP_NONE_ALGORITHM,algoritmo,dsa.sign());
            h.save(out);
            file.close();
            InputStream file2= new FileInputStream(nameFile);
            
            while((i=file2.read()) != -1){
                out.write((byte)i);
            }
            file2.close();
            //out.flush();
            out.close();
            System.out.println("El archivo se ha firmado correctamente.");
            
        } catch (Exception e) {
            System.out.println("Se ha producido un error al intentar firmar el archivo.");
        }
    }
    
    /**
     * Funcion dedicada a verificar la firma de un fichero. En caso de que el fichero haya sido modificado tras firmarlo, devolverá un error.
     * @param nameFile Nombre del fichero sobre el que se desea verificar la firma
     */
    public void verificarFirma(String nameFile){
        try {

            Header h = new Header();
            //Se ha modificado el fichero Options.java para añadir el algoritmo SHA1withDSA a 
            //los algoritmos de autenticación
            h.load(file);
            String algoritmo = h.getAlgorithm2();

            dsa = Signature.getInstance(algoritmo);
            dsa.initVerify(clave.getPub());

            OutputStream out = new FileOutputStream( nameFile.substring(0, nameFile.lastIndexOf(".")) + ".cla" );
            byte[] b = new byte[blockSize];
            int i;
            while( (i=file.read(b)) != -1 ){
                out.write(b,0,i);
                dsa.update(b);
            }

            if(dsa.verify(h.getData())){
                System.out.println("La firma ha sido verificada y es correcta.");
            }
            else{
                System.out.println("Se ha producido un error al intentar verificar la firma.");
            }
           // out.flush();
            out.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
