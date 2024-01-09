import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        HuffmanCodingOnFiles huffmanCodingOnFiles = new HuffmanCodingOnFiles();
        boolean restart = true;
        while (restart) {
            System.out.print("Enter c for compress or d for decompress or q for quit : ");
            String input = scanner.nextLine();
            String filePath;
            switch (input) {
            case "c":
                System.out.print("Enter file path:");
                filePath = scanner.nextLine();
                long start = System.currentTimeMillis();
                huffmanCodingOnFiles.compressFile(filePath);
                long end = System.currentTimeMillis();
                System.out.println("Compressed in ".concat(end - start + "ms"));
                break;
            case "d":
                System.out.print("Enter file path:");
                filePath = scanner.nextLine();
                start = System.currentTimeMillis();
                huffmanCodingOnFiles.decompressFile(filePath);
                end = System.currentTimeMillis();
                System.out.println("Decompressed in ".concat(end - start + "ms"));
                break;

            case "q":
                restart = false;
                scanner.close();
                break;
            default:
                start = System.currentTimeMillis();
                huffmanCodingOnFiles.compressFile("input.txt");
                end = System.currentTimeMillis();
                System.out.println("Compressed in ".concat(end - start + "ms"));
                start = System.currentTimeMillis();
                huffmanCodingOnFiles.decompressFile("input.compressed");
                end = System.currentTimeMillis();
                System.out.println("Decompressed in ".concat(end - start + "ms"));
                break;
            }
        }
    }

}