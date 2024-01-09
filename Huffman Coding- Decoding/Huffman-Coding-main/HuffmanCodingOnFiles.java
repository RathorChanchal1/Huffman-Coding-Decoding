import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.BitSet;

public class HuffmanCodingOnFiles extends HuffmanCoding {

    /**
     * creates a compressed file of the target file
     * 
     * @param filePath the path of the target file
     * @throws IOException if it cant access the file this occurs if the file not
     *                     found or there is no enough permissions
     */
    public void compressFile(String filePath) throws IOException {
        String input = Files.readString(Paths.get(filePath));
        String data = compress(input);
        String header = getLastOperationHeader();
        writeBinaryFile(filePath.substring(0, filePath.indexOf(".")).concat(".compressed"), header, data, true);
    }

    /**
     * creates a decompressed file of the target file
     * 
     * @param filePath the path of the target file
     * @throws IOException if it cant access the file this occurs if the file not
     *                     found or there is no enough permissions
     */
    public void decompressFile(String filePath) throws IOException {
        String[] headerData = readBinaryFile(filePath);
        String decompressed = decompress(headerData[0], headerData[1]);
        writeBinaryFile(filePath.substring(0, filePath.indexOf(".")).concat("_decompressed.txt"), decompressed, "",
                false);
    }

    /**
     * writes data and raw in a file of a target path. data string will be writen as
     * its, raw string will be written as binary
     * 
     * @param filePath  target path
     * @param data      string will be writen as its
     * @param raw       raw string will be written as binary
     * @param wrtieSize whether to write the size of the raw string. True if the raw
     *                  string isn't empty
     */
    public static void writeBinaryFile(String filePath, String data, String raw, boolean wrtieSize) {
        if (wrtieSize) {
            // adding the size of the raw input to the data to help in file reading
            data = raw.length() + SPILT_ELEMENT + data;
        }
        byte[] dataBytes = data.getBytes();
        byte[] rawBytes = stringToBitset(raw).toByteArray();
        try {
            Files.write(Paths.get(filePath), dataBytes);
            // added append option to prevent it from over write the previous data
            Files.write(Paths.get(filePath), rawBytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * convert string to binary
     * 
     * @param binary string to be converted
     * @return converted binary data
     */
    public static BitSet stringToBitset(String binary) {
        BitSet bitset = new BitSet(binary.length());
        int len = binary.length();
        for (int i = len - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                bitset.set(len - i - 1);
            }
        }
        return bitset;
    }

    /**
     * convert binary data to string
     * 
     * @param input binary data to be converted
     * @param size  the size of binary data
     * @return converted string
     */
    public static String bitsetToString(BitSet input, int size) {
        StringBuilder output = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            if (input.get(i)) {
                output.append("1");
            } else {
                output.append("0");
            }
        }
        return output.toString();
    }

    /**
     * reads header and string data from a target file
     * 
     * @param filePath target file path
     * @return array of strings the first element is the header and the second is
     *         the data
     * @throws IOException if it cant access the file this occurs if the file not
     *                     found or there is no enough permissions
     */
    public static String[] readBinaryFile(String filePath) throws IOException {
        String[] headerDataStrings = new String[2];
        // read the file into bytes array
        byte[] allBytes = Files.readAllBytes(Paths.get(filePath));
        // convert the bytes array to string to get the indices of start and end of
        // header string
        String allBytesString = new String(allBytes);
        // get the index of the size writen in the file
        int endSizeIndex = allBytesString.indexOf(SPILT_ELEMENT);
        // convert the size form string to integer
        int size = Integer.parseInt(allBytesString.substring(0, endSizeIndex));
        // get the start header index
        int startHeaderIndex = endSizeIndex + SPILT_ELEMENT.length();
        // get the end header index
        int endHeaderIndex = allBytesString.indexOf(TABLE_END_ELEMENT) + TABLE_END_ELEMENT.length();
        // get the header from the file
        headerDataStrings[0] = allBytesString.substring(startHeaderIndex, endHeaderIndex);
        // add the binary data to a separate bytes array for converting it to string
        byte[] dataBytes = new byte[allBytes.length - endHeaderIndex];
        System.arraycopy(allBytes, endHeaderIndex, dataBytes, 0, dataBytes.length);
        // converting the binary data to string
        headerDataStrings[1] = bitsetToString(BitSet.valueOf(dataBytes), size);
        // return the array of header and data strings
        return headerDataStrings;
    }
}
