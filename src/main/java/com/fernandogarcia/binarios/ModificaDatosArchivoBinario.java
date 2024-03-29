package com.fernandogarcia.binarios;

import java.io.*;
import java.util.Scanner;

public class ModificaDatosArchivoBinario {

    static Scanner lector= new Scanner(System.in);

    static final int TAMANYO_REGISTRO=92;
    static RandomAccessFile randomAccessFile;

    static File file = new File("C:\\Users\\usuario\\Desktop\\textoEjer5b.txt");
    static {
        try {
            randomAccessFile = new RandomAccessFile(file,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) throws IOException {
        if (file.exists()){
            file.delete();
        }
        int posicion = 1;
        int menu;
        do {
            menu();
            menu= lector.nextInt();

            switch (menu) {
                case 1:
                    char caracter = 's';
                    do {
                        posicion= Integer.parseInt(""+randomAccessFile.length()/TAMANYO_REGISTRO);
                        guardarInfoAlumno(posicion+1, randomAccessFile.getFilePointer());
                        System.out.println("Desea anyadir otro alumno? s/n");
                        caracter = lector.nextLine().charAt(0);
                        posicion++;
                    } while (caracter != 'n');

                    break;
                case 2:
                    System.out.println("Dime el numero del alumno, registro que quieres actualizar");
                    int registro = lector.nextInt();
                    guardarInfoAlumno(registro, (registro-1) * TAMANYO_REGISTRO);
                    break;
                case 3:
                    System.out.println("Dime el campo de registro que quieres actualizar");
                    registro= lector.nextInt();
                    modificarCampo(registro);
                    break;
                case 4:
                    listarAlumnos();
                    break;
                case 5:
                    borrarRegistro();
                    break;
                case 0:
                    System.out.println("fin");
                    lector.nextLine();
                    break;
            }
        }while(menu!=0);
    }


    private static void menu(){

        System.out.println("REGISTROS ALUMNOS");
        System.out.println("----------------------------");
        System.out.println("1.Anyadir alumnos");
        System.out.println("2.Modificar alumno Registro");
        System.out.println("3.Modificar un campo del registro alumno");
        System.out.println("4.Listar alumnos con sus Registros");
        System.out.println("5.Borrar alumnos con sus registro");
        System.out.println("0.Salir");
    }


    private static boolean guardarInfoAlumno(int pos,long registro){
        int posicion= pos;
        System.out.println("Dime el expediente");
        long expediente= lector.nextLong();
        lector.nextLine();
        System.out.println("Dime el nombre del alumno (máximo 20 caracteres");
        String nombre= lector.nextLine();
        System.out.println("Dime la convocatoria ordinaria");
        String convocatoriaOrdinaria= lector.nextLine();
        System.out.println("Dime la nota");
        double nota= lector.nextDouble();
        lector.nextLine();
        System.out.println("Dime la convocatoria extraordinaria");
        String convocatoriaExtraordianaria = lector.nextLine();

        escribeRegistro(posicion,expediente,nombre,convocatoriaOrdinaria,convocatoriaExtraordianaria,nota,registro);

        return true;
    }

    private static void escribeRegistro(int posicion,long expediente, String nombre,String convocatoriaOrdinaria,String convocatoriaExtraordinaria,double nota,long registro){
        try {

            randomAccessFile.writeInt(posicion);
            randomAccessFile.writeLong(expediente);
            for(int i=0;i<20;i++){
                try{
                    randomAccessFile.writeChar(nombre.charAt(i));
                }catch(IndexOutOfBoundsException ioob){
                    randomAccessFile.writeChar(' ');
                }
            }
            for(int i=0;i<8;i++){
                try{
                    randomAccessFile.writeChar(convocatoriaOrdinaria.charAt(i));
                }catch(IndexOutOfBoundsException ioob){
                    randomAccessFile.writeChar(' ');
                }
            }
            for(int i=0;i<8;i++){
                try{
                    randomAccessFile.writeChar(convocatoriaExtraordinaria.charAt(i));
                }catch(IndexOutOfBoundsException ioob){
                    randomAccessFile.writeChar(' ');
                }
            }

            randomAccessFile.writeDouble(nota);
            randomAccessFile.seek(registro);


        }catch (IOException ioe){
            System.out.println("IOE");
        }
    }

    private static void listarAlumnos() throws IOException{
        randomAccessFile.seek(0);

        while(randomAccessFile.getFilePointer()<randomAccessFile.length()){
            int posicion= randomAccessFile.readInt();
            long expediente= randomAccessFile.readLong();
            StringBuilder aux=new StringBuilder();
            for(int i=0;i<20;i++){
                aux.append(randomAccessFile.readChar());
            }
            String nombre=aux.toString();
            aux=new StringBuilder();
            for(int i=0;i<8;i++){
                aux.append(randomAccessFile.readChar());
            }
            String convocatoriaOrdinaria=aux.toString();
            aux=new StringBuilder();
            for(int i=0;i<8;i++){
                aux.append(randomAccessFile.readChar());
            }
            String convocatoriaExtraordianaria=aux.toString();
            double nota= randomAccessFile.readDouble();

            System.out.printf("%d %8d %20s %8s %8s %.1f \n", posicion,expediente,nombre,convocatoriaOrdinaria,
                    convocatoriaExtraordianaria,nota);

        }
    }
    private static void modificarCampo(int registro) throws IOException{
        System.out.println("Campos");
        System.out.println("------------");
        System.out.println("1.Expediente");
        System.out.println("2.Nombre");
        System.out.println("3.Convocatoria Ordinaria");
        System.out.println("4.Convocatoria Extraordinaria");
        System.out.println("5.Nota");

        int opcion= lector.nextInt();
        lector.nextLine();
        int puntero=(registro-1)*TAMANYO_REGISTRO;
        switch (opcion){
            case 1:
                System.out.println("Dime el expediente");
                long expediente= lector.nextLong();
                lector.nextLine();
                randomAccessFile.seek(puntero+4);
                randomAccessFile.writeLong(expediente);
                break;
            case 2:
                System.out.println("Dime el nombre");
                String nombre= lector.nextLine();
                randomAccessFile.seek(puntero+12);

                for(int i=0;i<20;i++){
                    try{
                        randomAccessFile.writeChar(nombre.charAt(i));
                    }catch (IndexOutOfBoundsException ioob){
                        randomAccessFile.writeChar(' ');
                    }
                }
                break;
            case 3:
                System.out.println("Dime la convocatoria ordinaria");
                String convocatoriaOrdinaria= lector.nextLine();
                randomAccessFile.seek(puntero+52);
                for(int i=0;i<8;i++){
                    try{
                        randomAccessFile.writeChar(convocatoriaOrdinaria.charAt(i));
                    }catch (IndexOutOfBoundsException ioob){
                        randomAccessFile.writeChar(' ');
                    }
                }
                break;
            case 4:
                System.out.println("Dime la convocatoria ordinaria");
                String convocatoriaExtraordinaria= lector.nextLine();
                randomAccessFile.seek(puntero+68);
                for(int i=0;i<8;i++){
                    try{
                        randomAccessFile.writeChar(convocatoriaExtraordinaria.charAt(i));
                    }catch (IndexOutOfBoundsException ioob){
                        randomAccessFile.writeChar(' ');
                    }
                }
                break;
            case 5:
                System.out.println("Dime la nota");
                double nota = lector.nextDouble();
                lector.nextLine();
                randomAccessFile.seek(puntero+84);
                break;
            default:
                System.out.println("Opcion incorrecta");
                lector.nextLine();
        }
    }

    private static void borrarRegistro() throws IOException{
        System.out.println("Dime el registro");
        int registro= lector.nextInt();
        lector.nextLine();
        File aux= new File("aux.txt");
        if (aux.exists()){
            aux.delete();
        }
        RandomAccessFile racAux=new RandomAccessFile(aux,"rw");
        randomAccessFile.seek(0);
        int contador=1;
        while (randomAccessFile.getFilePointer()< randomAccessFile.length()){

            if(randomAccessFile.getFilePointer()==((registro-1)*TAMANYO_REGISTRO)){
                randomAccessFile.seek(randomAccessFile.getFilePointer()+TAMANYO_REGISTRO);
            }else{
                randomAccessFile.readInt();
                racAux.writeInt(contador);
                contador++;
                racAux.writeLong(randomAccessFile.readLong());
                for(int i=0;i<36;i++){
                    racAux.writeChar(randomAccessFile.readChar());
                }
                racAux.writeDouble(randomAccessFile.readDouble());
            }
        }
        randomAccessFile.close();
        System.out.println(file.delete());
        randomAccessFile=new RandomAccessFile(file,"rw");
        try {
            InputStream in = new FileInputStream(aux);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }

}
