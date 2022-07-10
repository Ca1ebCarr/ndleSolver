import java.io.*;
import java.util.*;
class Main {
  public static void main(String[] args) {
    //enter the number of letters per word in the first argument. The second argument is the number of words to be guessed.
    //For example: Maker(5,4) would mean that there are 4, 5 letter words to be guessed/ 
		Maker player = new Maker(5,4);
		player.playNdle();
  }
}
