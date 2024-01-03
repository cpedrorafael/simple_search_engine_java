package search;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var dataArg = args[0];
        File dataFile = new File(dataArg);
        Scanner fileScanner = new Scanner(dataFile);
        var inputScanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        Map<String, List<Integer>> invertedIndex = new HashMap<>();


        while(fileScanner.hasNextLine()){
            lines.add(fileScanner.nextLine());
        }

        for(String line : lines){
            String[] words = line.split(" ");
            for(String word : words){
                if(invertedIndex.containsKey(word)){
                    invertedIndex.get(word).add(lines.indexOf(line));
                }else{
                    List<Integer> indices = new ArrayList<>();
                    indices.add(lines.indexOf(line));
                    invertedIndex.put(word, indices);
                }
            }
        }

        fileScanner.close();

        showMenu(lines, invertedIndex, inputScanner);

        inputScanner.close();
    }

    private static void showMenu(List<String> lines,  Map<String, List<Integer>> invertedIndex, Scanner scanner){
        System.out.println("\n\n\n");
        System.out.println("=== Menu ===");
        System.out.println("1. Find a word");
        System.out.println("2. Print all words);
        System.out.println("0. Exit");
        int selected = Integer.parseInt(scanner.nextLine());
        switch (selected) {
            case 0 -> System.out.println("Bye!");
            case 1 -> findPerson(lines, invertedIndex, scanner);
            case 2 -> showAllPersons(lines, invertedIndex, scanner);
            default -> showMenu(lines, invertedIndex, scanner);
        }
    }

    private static void showAllPersons(List<String> lines, Map<String, List<Integer>> invertedIndex, Scanner scanner){
        for(String line: lines){
            System.out.println(line);
        }

        showMenu(lines, invertedIndex, scanner);
    }

    private static void findPerson(List<String> lines, Map<String, List<Integer>> invertedIndex, Scanner scanner){
        System.out.println("Select a matching strategy: \n ALL (find all lines where query matches exactly), " +
                "\n ANY (find all lines where query matches at least once), NONE");
        String strategyInput = scanner.nextLine();
        Strategy strategy = getStrategy(strategyInput);
        System.out.println("Enter words to search.");
        String query = scanner.nextLine();
        List<String> matches = new ArrayList<String>();

        for (String line : lines) {
            if (strategy.match(query, line)) {
                matches.add(line);
            }
        }

        if(!matches.isEmpty()){
            for(String line : matches){
                System.out.println(line);
            }
        }else{
            System.out.println("No matching people found.");
        }

        showMenu(lines, invertedIndex, scanner);
    }

    private static Strategy getStrategy(String strategy){
        return switch (strategy) {
            case "ALL" -> new ALLStrategy();
            case "ANY" -> new ANYStrategy();
            default -> new NONEStrategy();
        };
    }
}

abstract class Strategy {
    public abstract boolean match(String query, String line);
}

class ALLStrategy extends Strategy {
    @Override
    public boolean match(String query, String line) {
        String[] words = query.split(" ");
        for(String word : words){
            if(!line.toLowerCase().contains(word.toLowerCase())){
                return false;
            }
        }
        return true;
    }
}

class ANYStrategy extends Strategy {
    @Override
    public boolean match(String query, String line) {
        String[] words = query.split(" ");
        for(String word : words){
            if(line.toLowerCase().contains(word.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}

class NONEStrategy extends Strategy {
    @Override
    public boolean match(String query, String line) {
        String[] words = query.split(" ");
        for(String word : words){
            if(line.toLowerCase().contains(word.toLowerCase())){
                return false;
            }
        }
        return true;
    }
}