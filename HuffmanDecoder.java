import edu.princeton.cs.algs4.BinaryOut;

public class HuffmanDecoder {

    private static void decode(String encodeFilename, String decodeFilename) {
        ObjectReader reader = new ObjectReader(encodeFilename);    // create an ObjectReader object
        BinaryTrie binaryTrie = (BinaryTrie) reader.readObject();    // read the Huffman encoding trie from encoded file
        int numSymbols = (int) reader.readObject();    // read the number of symbols
        BitSequence allBitsequences = (BitSequence) reader.readObject();    // read the single huge encoded Bitsequence

        char[] chars = new char[numSymbols];    // create a char array to store all the decoded symbols
        int readBits = 0;    // count the read bits
        for (int i = 0; i < numSymbols; i++) {     // repeat until there are no symbols
            // create a new Bitsequence containing the remaining unmatched bits
            BitSequence remainingBits = allBitsequences.allButFirstNBits(readBits);
            Match m = binaryTrie.longestPrefixMatch(remainingBits);    // find a longest prefix match on the Bitsequence
            chars[i] = m.getSymbol();      // add the matched symbol to the chars array
            readBits += m.getSequence().length();
        }

        writer(decodeFilename, chars);    // write the symbol array into the given file decodeFilename
    }

    /**
     * Write the given char array as 8-bit symbols to the given file
     * @param filename output file
     * @param chars char array
     */
    private static void writer(String filename, char[] chars) {
        BinaryOut binaryOut = new BinaryOut(filename);

        for (char c : chars) {
            binaryOut.write(c);
        }

        binaryOut.close();
    }

    public static void main(String[] args) {
        decode(args[0], args[1]);
    }
}
