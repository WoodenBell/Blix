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
	 * HashMap containing all URL codes and their respective characters.
	 */
	
	public static HashMap<String, Character> urlEncoded = new HashMap<>();
	
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
		urlEncoded.put("%2F", '/');
		urlEncoded.put("%30", '0');
		urlEncoded.put("%31", '1');
		urlEncoded.put("%32", '2');
		urlEncoded.put("%33", '3');
		urlEncoded.put("%34", '4');
		urlEncoded.put("%35", '5');
		urlEncoded.put("%36", '6');
		urlEncoded.put("%37", '7');
		urlEncoded.put("%38", '8');
		urlEncoded.put("%39", '9');
		urlEncoded.put("%3A", ':');
		urlEncoded.put("%3B", ';');
		urlEncoded.put("%3C", '<');
		urlEncoded.put("%3D", '=');
		urlEncoded.put("%3E", '>');
		urlEncoded.put("%3F", '?');
		urlEncoded.put("%40", '@');
		urlEncoded.put("%41", 'A');
		urlEncoded.put("%42", 'B');
		urlEncoded.put("%43", 'C');
		urlEncoded.put("%44", 'D');
		urlEncoded.put("%45", 'E');
		urlEncoded.put("%46", 'F');
		urlEncoded.put("%47", 'G');
		urlEncoded.put("%48", 'H');
		urlEncoded.put("%49", 'I');
		urlEncoded.put("%4A", 'J');
		urlEncoded.put("%4B", 'K');
		urlEncoded.put("%4C", 'L');
		urlEncoded.put("%4D", 'M');
		urlEncoded.put("%4E", 'N');
		urlEncoded.put("%4F", 'O');
		urlEncoded.put("%50", 'P');
		urlEncoded.put("%51", 'Q');
		urlEncoded.put("%52", 'R');
		urlEncoded.put("%53", 'S');
		urlEncoded.put("%54", 'T');
		urlEncoded.put("%55", 'U');
		urlEncoded.put("%56", 'V');
		urlEncoded.put("%57", 'W');
		urlEncoded.put("%58", 'X');
		urlEncoded.put("%59", 'Y');
		urlEncoded.put("%5A", 'Z');
		urlEncoded.put("%5B", '[');
		urlEncoded.put("%5C", '\\');
		urlEncoded.put("%5D", ']');
		urlEncoded.put("%5E",'^');
		urlEncoded.put("%5F",'_');
		urlEncoded.put("%60",'`');
		urlEncoded.put("%61",'a');
		urlEncoded.put("%62",'b');
		urlEncoded.put("%63",'c');
		urlEncoded.put("%64",'d');
		urlEncoded.put("%65",'e');
		urlEncoded.put("%66",'f');
		urlEncoded.put("%67",'g');
		urlEncoded.put("%68",'h');
		urlEncoded.put("%69",'i');
		urlEncoded.put("%6A",'j');
		urlEncoded.put("%6B",'k');
		urlEncoded.put("%6C",'l');
		urlEncoded.put("%6D",'m');
		urlEncoded.put("%6E",'n');
		urlEncoded.put("%6F",'o');
		urlEncoded.put("%70",'p');
		urlEncoded.put("%71",'q');
		urlEncoded.put("%72",'r');
		urlEncoded.put("%73",'s');
		urlEncoded.put("%74",'t');
		urlEncoded.put("%75",'u');
		urlEncoded.put("%76",'v');
		urlEncoded.put("%77",'w');
		urlEncoded.put("%78",'x');
		urlEncoded.put("%79",'y');
		urlEncoded.put("%7A",'z');
		urlEncoded.put("%7B",'{');
		urlEncoded.put("%7C",'|');
		urlEncoded.put("%7D",'}');
		urlEncoded.put("%7E",'~');
		urlEncoded.put("%7F",' ');
		urlEncoded.put("%80",'`');
		urlEncoded.put("%81",'Ã');
		urlEncoded.put("%82",'Ã');
		urlEncoded.put("%83",'Æ');
		urlEncoded.put("%84",'Ã');
		urlEncoded.put("%85",'Ã');
		urlEncoded.put("%86",'Ã');
		urlEncoded.put("%87",'Ã');
		urlEncoded.put("%88",'Ã');
		urlEncoded.put("%89",'Ã');
		urlEncoded.put("%8A",'Å');
		urlEncoded.put("%8B",'Ã');
		urlEncoded.put("%8C",'Å');
		urlEncoded.put("%8D",'Ã');
		urlEncoded.put("%8E",'Å');
		urlEncoded.put("%8F",'Ã');
		urlEncoded.put("%90",'Ã');
		urlEncoded.put("%91",'Ã');
		urlEncoded.put("%92",'Ã');
		urlEncoded.put("%93",'Ã');
		urlEncoded.put("%94",'Ã');
		urlEncoded.put("%95",'Ã');
		urlEncoded.put("%96",'Ã');
		urlEncoded.put("%97",'Ã');
		urlEncoded.put("%98",'Ã');
		urlEncoded.put("%99",'Ã');
		urlEncoded.put("%9A",'Ã');
		urlEncoded.put("%9B",'Ã');
		urlEncoded.put("%9C",'Ã');
		urlEncoded.put("%9D",'Ã');
		urlEncoded.put("%9E",'Ã');
		urlEncoded.put("%9F",'Ã');
		urlEncoded.put("%A1",'Ã');
		urlEncoded.put("%A2",'Ã');
		urlEncoded.put("%A3",'Ã');
		urlEncoded.put("%A5",'Ã');
		urlEncoded.put("%A6",'Ã');
		urlEncoded.put("%A7",'Ã');
		urlEncoded.put("%A8",'Ã');
		urlEncoded.put("%A9",'Ã');
		urlEncoded.put("%AA",'Ã');
		urlEncoded.put("%AB",'Ã');
		urlEncoded.put("%AC",'Ã');
		urlEncoded.put("%AD",'Ã');
		urlEncoded.put("%AE",'Ã');
		urlEncoded.put("%AF",'Ã');
	}
}
