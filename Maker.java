import java.io.BufferedReader;
  import java.io.FileReader;
  import java.io.IOException;
  import java.util.*;
  import java.util.regex.Matcher;
  import java.util.regex.Pattern;
  import java.util.ArrayList;
  import java.util.Scanner;
public class Maker{
 private ArrayList<String> dictionary;
 private ArrayList<String>[] wordsLeftList;
 private ArrayList<String> wordsLeft1;
 private ArrayList<String> wordsLeft2;
 private ArrayList<String> wordsLeft3;
 private ArrayList<String> wordsLeft4;
 private String[] regExPattern;
 public Maker(int letters, int numWords) {
  String[] tmp = null;
  try{
    tmp = readLines("dictionary.txt");
  }
  catch(IOException e){
    System.out.println("Unable to access "+e.getMessage());             
  }
  dictionary = new ArrayList<String>(Arrays.asList(tmp));
	wordsLeftList = new ArrayList[numWords];
	for(int i=0; i<numWords; i++){
		wordsLeftList[i] = new ArrayList<String>(Arrays.asList(tmp));
	}
	wordsLeft1 = new ArrayList<String>(Arrays.asList(tmp));
	wordsLeft2 = new ArrayList<String>(Arrays.asList(tmp));
	wordsLeft3 = new ArrayList<String>(Arrays.asList(tmp));
	wordsLeft4 = new ArrayList<String>(Arrays.asList(tmp));
	for(int i=0; i<dictionary.size(); i++){
		if(dictionary.get(i).length()!=letters){
			dictionary.remove(i);
			for(int j=0; j<numWords; j++){
				wordsLeftList[j].remove(i);
			}
			i--;
		}
	}
	regExPattern = new String[letters]; 
	for(int i=0; i<regExPattern.length; i++){
		regExPattern[i] = ".";
	}
 }
  public String[] readLines(String filename) throws IOException {
   FileReader fileReader = new FileReader(filename);
   BufferedReader bufferedReader = new BufferedReader(fileReader);
   List<String> lines = new ArrayList<String>();
   String line = null;    
   while ((line = bufferedReader.readLine()) != null) {
     lines.add(line);
   }    
   bufferedReader.close();    
   return lines.toArray(new String[lines.size()]);
  }
  public String getPattern(String guess, String answer){
    String[] guess4y = guess.split("");
		String[] answer4y = answer.split("");
    int[] pattern = new int[guess.length()];
    for(int i=0; i<guess.length(); i++){
      if(String.valueOf(guess.charAt(i)).equals(String.valueOf(answer.charAt(i)))){
        pattern[i]=2;
        guess4y[i] = "9";
        answer4y[i] = "9";
      }
      else if(!answer.contains(String.valueOf(guess.charAt(i)))){
        pattern[i] = 0;
        guess4y[i] = "9";
      }
      else{
        pattern[i] = 9;
      }
    }
    String yellowGuess = ArrayToString(guess4y);
		String yellowAnswer = ArrayToString(answer4y);

		for(int i=0; i<yellowGuess.length(); i++){
			if(!yellowGuess.substring(i,i+1).equals("9") && yellowAnswer.contains(yellowGuess.substring(i,i+1))){
			  pattern[i] = 1;
				yellowAnswer= yellowAnswer.replaceFirst(yellowGuess.substring(i,i+1), "9");
			}
		}
		return Arrays.toString(pattern).substring(1, pattern.length*3-1).replaceAll(", ", "").replaceAll("9","0");
  }
	public double printCombos(String secret){
		ArrayList<Double> averageLeft = new ArrayList<Double>();
		for(int q=0; q<wordsLeftList.length; q++){
			int[] numCombos = new int[(int)Math.pow(3,secret.length())];
		  for(int i=0; i<wordsLeftList[q].size(); i++){
			  int temp = Integer.parseInt(getPattern(wordsLeftList[q].get(i), secret),3);
			  numCombos[temp] = numCombos[temp]+1;
		  }
		  int numerator = 0;
		  for(int i=0; i<numCombos.length; i++){
			  numerator+= (numCombos[i] * numCombos[i]);
		  }
		  averageLeft.add( (double)(numerator)/(double)(wordsLeftList[q].size()));
		}
		
		double sumavgs = 0;
		for(int i=0; i<averageLeft.size(); i++){
			sumavgs = sumavgs+averageLeft.get(i);
		}
		return sumavgs;
	}
	public String findBestWord(){
		String bestWord = "";
		double bestWordLeft = 100000;
		for(int i=0; i<dictionary.size(); i++){
				String word = dictionary.get(i);
			  double left = printCombos(word);
			  if(bestWordLeft>=left){
				  bestWord = word;
				  bestWordLeft = left;
			  }
		}
		System.out.println("You should guess:" + bestWord + " because it leaves " + bestWordLeft + " words left on average");
		return bestWord;
	}
  public String ArrayToString(String[] input){
    String ret = "";
    for(String a : input){
      ret = ret + a;
    }
    return ret;
	}
	public int howManyWord(int n){
		int count = 0;
		for(int i=0; i<dictionary.size(); i++){
			if(dictionary.get(i).length()==n){
				count++;
			}
		}
		return count;
	}
	public void fix(String guess, String[] results){
		for(int j=0; j<wordsLeftList.length; j++){
		  for(int i=0; i<wordsLeftList[j].size(); i++){
			  if(!getPattern(guess, wordsLeftList[j].get(i)).equals(results[j])){
				  wordsLeftList[j].remove(i);
				  i--;
			  }
			}
		}
	}
	public void playNdle(){
		int q=0;
		String[] results = new String[wordsLeftList.length];
		for(int i=0; i<results.length; i++){
			results[i] = "";
		}
		String guess = "";
		while(true){
			q++;
			Scanner scan = new Scanner(System.in);
			System.out.println("Type the word you guessed:");
			guess = scan.nextLine();
			for(int i=0; i<results.length; i++){
				System.out.println("Type the " + (i+1) + "th result:");
				results[i] = scan.nextLine();
			}
			fix(guess, results);
			for(int r=0; r<results.length; r++){
				System.out.println(wordsLeftList[r].toString());
			}
			findBestWord();
			for(int i=0; i<results.length; i++){
				if(wordsLeftList[i].size()==1){
					System.out.println("The " + (i+1) + "th one is definitely " + wordsLeftList[i].get(0));
				}
			}
		}
	}
	public String color(String word, String pattern){
		String ret = "";
		for(int i=0; i<word.length(); i++){
			if(pattern.substring(i,i+1).equals("0")){
				ret = ret + "\u001B[37m" + word.substring(i,i+1);
			}
			else if(pattern.substring(i,i+1).equals("1")){
				ret = ret + "\u001B[33m" + word.substring(i,i+1);
			}
			else{
				ret = ret + "\u001B[32m" + word.substring(i,i+1);
			}
		}
		return ret;
	}
}
