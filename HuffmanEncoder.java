import edu.princeton.cs.algs4.BinaryIn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {

    /**
     * Build a frequency table that maps each char to its frequency in the input char array inputSymbols, and
     * return the table.
     * @param inputSymbols
     * @return Map<Character, Integer> frequency table
     */
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freqTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (freqTable.containsKey(c)) {
                freqTable.replace(c, freqTable.get(c) + 1);
            } else {
                freqTable.put(c, 1);
            }
        }
        return freqTable;
    }

    /**
     * Write a new file with the filename + ".uhf" that contains a huffman encoded version of the original input file.
     * @param filename
     */
    private static void encode(String filename) {
        char[] symbols = binaryReader(filename);     // read the file as 8-bit symbols and returns symbol array
        Map<Character, Integer> freqTable = buildFrequencyTable(symbols);     // build the frequency table
        BinaryTrie binaryTrie = new BinaryTrie(freqTable);     // constructs a binary decoding trie by frequency table

        String encodedFile = filename + ".huf";
        ObjectWriter objectWriter = new ObjectWriter(encodedFile);    // create an ObjectWriter object
        objectWriter.writeObject(binaryTrie);     // write the binary trie to the .huf file
        objectWriter.writeObject(symbols.length);    // write the number of symbols to the .huf file

        Map<Character, BitSequence> lookupTable = binaryTrie.buildLookupTable();    // create a lookup table for encoding
        ArrayList<BitSequence> bitSequences = new ArrayList<>();     // create a list of bitsequences
        for (char symbol : symbols) {      // for every symbol in the symbol array
            bitSequences.add(lookupTable.get(symbol));    // use the lookup table to add corresponding bitsequence
        }
        BitSequence allBitSequences = BitSequence.assemble(bitSequences);    // assemble bitsequences into a single one

        objectWriter.writeObject(allBitSequences);    // write the single huge bitsequence to the .huf file
    }

    /**
     * Read the input file as 8-bit symbols and returns a char[] that contains all the symbols in order.
     * @param filename
     * @return char[] symbols
     */
    private static char[] binaryReader(String filename) {
        BinaryIn in = new BinaryIn(filename);
        ArrayList<Character> characterList = new ArrayList<>();
        while(!in.isEmpty()) {
            characterList.add(in.readChar());
        }

        char[] symbols = new char[characterList.size()];
        for (int i = 0; i < symbols.length; i++) {
            symbols[i] = characterList.get(i);
        }

        return symbols;
    }

    public static void main(String[] args) {
        encode(args[0]);
    }
}
