import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Scanner;

import javax.crypto.*;
import javax.crypto.spec.*;

public class lock {
    private static byte[] salt ="Enter a random word here(not passowrd)".getBytes(); //"".getBytes(); /* "April72020".getBytes();*/
    public static void encrypt() throws IllegalBlockSizeException, BadPaddingException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String folderPath = "enter folder desitination to be encrypted"; // change this to your folder path
        
        try {
            Path folder = Paths.get(folderPath);
            if (!Files.isDirectory(folder)) {
                System.out.println("Not a valid folder path!");
                scanner.close();
                return;
            }
            
            
            
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            // encrypt all files in the folder
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
            Files.walk(folder).filter(Files::isRegularFile).forEach(file -> {
                try {
                    if (file.getFileName().toString().endsWith(".nb")) {
                        System.out.println("files already encrypted!");
                        System.exit(-1);
                    }

                    byte[] input = Files.readAllBytes(file);
                    byte[] output = cipher.doFinal(input);
                    Path encryptedFile = folder.resolve(file.getFileName().toString() + ".nb");
                    Files.write(encryptedFile, output);
                    Files.delete(file);
                } catch (IOException | GeneralSecurityException e) {
                   // e.printStackTrace();
                }
            });
            
            System.out.println("Files encrypted!");
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
           // e.printStackTrace();
        }
        scanner.close();
    }
    public static void decrypt() throws IllegalBlockSizeException, BadPaddingException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String folderPath = "N:/program_files/vault"; // change this to your folder path
        
        try {
            Path folder = Paths.get(folderPath);
            if (!Files.isDirectory(folder)) {
                System.out.println("Not a valid folder path!");
                scanner.close();
                return;
            }
            
            // generate a secret key from the password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            
            // decrypt all files in the folder
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(new byte[16]));
            Files.walk(folder).filter(file -> file.toString().endsWith(".nb")).forEach(file -> {
                try {
                    byte[] input = Files.readAllBytes(file);
                    byte[] output = cipher.doFinal(input);
                    Path decryptedFile = folder.resolve(file.getFileName().toString().replace(".nb", ""));
                    Files.write(decryptedFile, output);
                    Files.delete(file);
                } catch (IOException | GeneralSecurityException e) {
                   // e.printStackTrace();
                }
            });
            
            System.out.println("Folder will be open if the password is correct");
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            //e.printStackTrace();
        }
        scanner.close();
    }
    public static void main(String[] args) {
        try{
            //decrypt();
            //encrypt();
        }
        catch(Exception e){
            //do nothing;
        }
        
    }
}
