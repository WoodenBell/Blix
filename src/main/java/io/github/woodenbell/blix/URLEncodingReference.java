package io.github.woodenbell.blix;

import java.util.HashMap;

/**
 * This class contains all the URL codes for characters, used internally to 
 * decode URLEncoded.
 * @author WoodenBell
 * @version 0.3
 */

public class URLEncodingReference {
	
	/**
	 * HashMap containing the basic URL codes and their respective characters.
	 */
	
	public static HashMap<String, Character> urlEncoded = new HashMap<>();
	
	/**
	 * HashMap containing characters and their respective URL codes (basic).
	 */
	
	public static HashMap<Character, String> urlDecoded;
	
	/**
	 * HashMap containing all the URL codes and their respective characters.
	 */
	
	public static HashMap<Character, String> fullUrlDecoded;
	
	/**
	 * HashMap containing all the characters and their respective URL codes.
	 */
	
	public static HashMap<String, Character> fullUrlEncoded;
	
	static {
		urlEncoded.put("%20", ' ');
		urlEncoded.put("%21", '!');
		urlEncoded.put("%22", '\\');
		urlEncoded.put("%23", '#');
		urlEncoded.put("%24", '$');
		urlEncoded.put("%25", '%');
		urlEncoded.put("%26", '&');
		urlEncoded.put("%27", '\'');
		urlEncoded.put("%28", '(');
		urlEncoded.put("%29", ')');
		urlEncoded.put("%2A", '*');
		urlEncoded.put("%2B", '+');
		urlEncoded.put("%2C", ',');
		urlEncoded.put("%2D", '-');
		urlEncoded.put("%2E", '.');
		urlEncoded.put("%3A", ':');
		urlEncoded.put("%3B", ';');
		urlEncoded.put("%3C", '<');
		urlEncoded.put("%3D", '=');
		urlEncoded.put("%3E", '>');
		urlEncoded.put("%3F", '?');
		urlEncoded.put("%40", '@');
		urlEncoded.put("%5B", '[');
		urlEncoded.put("%5C", '\\');
		urlEncoded.put("%5D", ']');
		urlEncoded.put("%5E",'^');
		urlEncoded.put("%5F",'_');
		urlEncoded.put("%60",'`');
		urlEncoded.put("%7B",'{');
		urlEncoded.put("%7C",'|');
		urlEncoded.put("%7D",'}');
		urlEncoded.put("%7E",'~');
		urlEncoded.put("%7F",' ');
		urlEncoded.put("%80",'`');
		//
		
		fullUrlEncoded = new HashMap<>(urlEncoded);
		fullUrlEncoded.put("%2F", '/');
		fullUrlEncoded.put("%30", '0');
		fullUrlEncoded.put("%31", '1');
		fullUrlEncoded.put("%32", '2');
		fullUrlEncoded.put("%33", '3');
		fullUrlEncoded.put("%34", '4');
		fullUrlEncoded.put("%35", '5');
		fullUrlEncoded.put("%36", '6');
		fullUrlEncoded.put("%37", '7');
		fullUrlEncoded.put("%38", '8');
		fullUrlEncoded.put("%39", '9');
		
		fullUrlEncoded.put("%41", 'A');
		fullUrlEncoded.put("%42", 'B');
		fullUrlEncoded.put("%43", 'C');
		fullUrlEncoded.put("%44", 'D');
		fullUrlEncoded.put("%45", 'E');
		fullUrlEncoded.put("%46", 'F');
		fullUrlEncoded.put("%47", 'G');
		fullUrlEncoded.put("%48", 'H');
		fullUrlEncoded.put("%49", 'I');
		fullUrlEncoded.put("%4A", 'J');
		fullUrlEncoded.put("%4B", 'K');
		fullUrlEncoded.put("%4C", 'L');
		fullUrlEncoded.put("%4D", 'M');
		fullUrlEncoded.put("%4E", 'N');
		fullUrlEncoded.put("%4F", 'O');
		fullUrlEncoded.put("%50", 'P');
		fullUrlEncoded.put("%51", 'Q');
		fullUrlEncoded.put("%52", 'R');
		fullUrlEncoded.put("%53", 'S');
		fullUrlEncoded.put("%54", 'T');
		fullUrlEncoded.put("%55", 'U');
		fullUrlEncoded.put("%56", 'V');
		fullUrlEncoded.put("%57", 'W');
		fullUrlEncoded.put("%58", 'X');
		fullUrlEncoded.put("%59", 'Y');
		fullUrlEncoded.put("%5A", 'Z');
		fullUrlEncoded.put("%61",'a');
		fullUrlEncoded.put("%62",'b');
		fullUrlEncoded.put("%63",'c');
		fullUrlEncoded.put("%64",'d');
		fullUrlEncoded.put("%65",'e');
		fullUrlEncoded.put("%66",'f');
		fullUrlEncoded.put("%67",'g');
		fullUrlEncoded.put("%68",'h');
		fullUrlEncoded.put("%69",'i');
		fullUrlEncoded.put("%6A",'j');
		fullUrlEncoded.put("%6B",'k');
		fullUrlEncoded.put("%6C",'l');
		fullUrlEncoded.put("%6D",'m');
		fullUrlEncoded.put("%6E",'n');
		fullUrlEncoded.put("%6F",'o');
		fullUrlEncoded.put("%70",'p');
		fullUrlEncoded.put("%71",'q');
		fullUrlEncoded.put("%72",'r');
		fullUrlEncoded.put("%73",'s');
		fullUrlEncoded.put("%74",'t');
		fullUrlEncoded.put("%75",'u');
		fullUrlEncoded.put("%76",'v');
		fullUrlEncoded.put("%77",'w');
		fullUrlEncoded.put("%78",'x');
		fullUrlEncoded.put("%79",'y');
		fullUrlEncoded.put("%7A",'z');
		fullUrlEncoded.put("%81",'�');
		fullUrlEncoded.put("%82",'�');
		fullUrlEncoded.put("%83",'�');
		fullUrlEncoded.put("%84",'�');
		fullUrlEncoded.put("%85",'�');
		fullUrlEncoded.put("%86",'�');
		fullUrlEncoded.put("%87",'�');
		fullUrlEncoded.put("%88",'�');
		fullUrlEncoded.put("%89",'�');
		fullUrlEncoded.put("%8A",'�');
		fullUrlEncoded.put("%8B",'�');
		fullUrlEncoded.put("%8C",'�');
		fullUrlEncoded.put("%8D",'�');
		fullUrlEncoded.put("%8E",'�');
		fullUrlEncoded.put("%8F",'�');
		fullUrlEncoded.put("%90",'�');
		fullUrlEncoded.put("%91",'�');
		fullUrlEncoded.put("%92",'�');
		fullUrlEncoded.put("%93",'�');
		fullUrlEncoded.put("%94",'�');
		fullUrlEncoded.put("%95",'�');
		fullUrlEncoded.put("%96",'�');
		fullUrlEncoded.put("%97",'�');
		fullUrlEncoded.put("%98",'�');
		fullUrlEncoded.put("%99",'�');
		fullUrlEncoded.put("%9A",'�');
		fullUrlEncoded.put("%9B",'�');
		fullUrlEncoded.put("%9C",'�');
		fullUrlEncoded.put("%9D",'�');
		fullUrlEncoded.put("%9E",'�');
		fullUrlEncoded.put("%9F",'�');
		fullUrlEncoded.put("%A1",'�');
		fullUrlEncoded.put("%A2",'�');
		fullUrlEncoded.put("%A3",'�');
		fullUrlEncoded.put("%A5",'�');
		fullUrlEncoded.put("%A6",'�');
		fullUrlEncoded.put("%A7",'�');
		fullUrlEncoded.put("%A8",'�');
		fullUrlEncoded.put("%A9",'�');
		fullUrlEncoded.put("%AA",'�');
		fullUrlEncoded.put("%AB",'�');
		fullUrlEncoded.put("%AC",'�');
		fullUrlEncoded.put("%AD",'�');
		fullUrlEncoded.put("%AE",'�');
		fullUrlEncoded.put("%AF",'�');
		
		urlDecoded = Util.reverseKeyVals(urlEncoded);
		fullUrlDecoded = Util.reverseKeyVals(fullUrlEncoded);
	}
}
