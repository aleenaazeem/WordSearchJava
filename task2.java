package Assignment;
import java.io.*;
import java.util.*;
class Node {
    Map<Character, Node> childNodes; // Store child nodes (characters)
    boolean isWordComplete; // Flag to indicate end of a word

    public Node() {
        childNodes = new HashMap<>();
        isWordComplete = false;
    }
}

class PrefixTree {
	//this is the prefixtree for our trie
    private Node rootNode;

    public PrefixTree() {
        rootNode = new Node();
    }

    // Adds a word to the Trie
public void addWord(String word) {
        Node current = rootNode;
    for (char letter : word.toCharArray()) {
    // If letter is not present, create a new node
    current.childNodes.putIfAbsent(letter, new Node());
    // Move to the next node
    current = current.childNodes.get(letter);
    }
    // Mark the end of the word
    current.isWordComplete = true;
    }

    // Helper function to collect words with a given starting sequence
    private void gatherWords(Node node, String prefix, List<String> suggestions) {
        if (node.isWordComplete) {
            suggestions.add(prefix);
        }
        for (char letter : node.childNodes.keySet()) {
            gatherWords(node.childNodes.get(letter), prefix + letter, suggestions);
        }
    }

    // Finds words that start with the provided prefix
    public List<String> getSuggestions(String prefix) {
        Node current = rootNode;
        for (char letter : prefix.toCharArray()) {
            if (!current.childNodes.containsKey(letter)) {
                return new ArrayList<>(); // No matching words found
            }
            current = current.childNodes.get(letter);
        }
        List<String> matchingWords = new ArrayList<>();
        gatherWords(current, prefix, matchingWords);
        return matchingWords;
    }
}

public class TrieWord {
	//here we are running our main program
    public static void main(String[] args) {
        PrefixTree autoCompleteSystem = new PrefixTree();

        // Reading words from file (make sure dropbox_words.txt is in the same directory)
        String fileName = "dropbox_words.txt";
        //this function is used to read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String word; //we have created a string word
            while ((word = reader.readLine()) != null) {
            	//where we are reading the file linee by line and storing it in the string
                autoCompleteSystem.addWord(word.trim());
            }
        } catch (IOException e) {
        	//if the above code doesnt work it will throw and error
            System.err.println("Oops! Something went wrong while reading the file: " + e.getMessage());
            return;
        }

        //Here we will ask the user for their input and search the word
        Scanner user_input = new Scanner(System.in); //the word is stored
        System.out.print("Start typing a word: ");
        String userPrefix = user_input.nextLine();

        // Fetching words that start with the given prefix
        //here we will getting the word suggestions and then storing them in a list
        List<String> wordSuggestions = autoCompleteSystem.getSuggestions(userPrefix);

        //After filling up the list we will display it. 
        if (wordSuggestions.isEmpty()) {
            System.out.println("Sorry, no words found starting with: " + userPrefix);
        } else {
            System.out.println("Words that start with'" + userPrefix + "': " + wordSuggestions);
        }

        user_input.close();
    }
}
