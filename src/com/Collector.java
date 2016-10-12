package com;

/*
 * Helper class which contains methods to gather all collectible cards from the mashape API,
 * and build and sort random decklists.
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Collector
{
	// Maintain separate lists of each type of card, categorized into a specific hero class, or else neutral.
	static ArrayList<Card> neutral = new ArrayList<Card>();
	static ArrayList<Card> mage = new ArrayList<Card>();
	static ArrayList<Card> warrior = new ArrayList<Card>();
	static ArrayList<Card> shaman = new ArrayList<Card>();
	static ArrayList<Card> priest = new ArrayList<Card>();
	static ArrayList<Card> paladin = new ArrayList<Card>();
	static ArrayList<Card> rogue = new ArrayList<Card>();
	static ArrayList<Card> warlock = new ArrayList<Card>();
	static ArrayList<Card> druid = new ArrayList<Card>();
	static ArrayList<Card> hunter = new ArrayList<Card>();
	
	// This method makes a call to the mashape API to retrieve all collectible Hearthstone
	// cards in JSON format, then loops through the results and adds each Card into
	// our maintained lists one at a time.
	public static void collectCards()
	{
		String s = "https://omgvamp-hearthstone-v1.p.mashape.com/cards?collectible=1";
		URL url = null;
		
		// Check URL is valid
		try {url = new URL(s);}
		catch (MalformedURLException e)
		{
			System.out.println("Invalid URL! System exiting...");
			System.exit(0);
		}
		
		// Check connection is valid
		HttpURLConnection con = null;
		try {con = (HttpURLConnection) url.openConnection();}
		catch (IOException e)
		{
			System.out.println("Connection failed! System exiting...");
			System.exit(0);
		}
		
		// Add request header
		con.setRequestProperty("X-Mashape-Key", "9qUq7YSrhimshKYxAwPnrjjxkmMbp1b39hDjsntTtu7oa1D1Nn");
		
		JSONParser p = new JSONParser();
		JSONObject o = null;
		try { o = (JSONObject) p.parse(new InputStreamReader(con.getInputStream()));}
		catch (IOException e)
		{
			System.out.println("Error opening input stream! System exiting...");
			System.exit(0);
		}
		catch (ParseException pe)
		{
			System.out.println("Error parsing JSON! System exiting...");
			System.exit(0);
		}
		
		// Loop through each Hearthstone set in this result set, and add each card
		// to our maintained collection
		JSONArray basics = (JSONArray) o.get("Basic");
		
		for (int i = 0; i < basics.size(); i++)
		{
			JSONObject tmp = (JSONObject) basics.get(i);
			addCard(tmp);
		}
		
		JSONArray classics = (JSONArray) o.get("Classic");
		
		for (int i = 0; i < classics.size(); i++)
		{
			JSONObject tmp = (JSONObject) classics.get(i);
			addCard(tmp);
		}
		
		JSONArray naxx = (JSONArray) o.get("Naxxramas");
		
		for (int i = 0; i < naxx.size(); i++)
		{
			JSONObject tmp = (JSONObject) naxx.get(i);
			addCard(tmp);
		}
		
		JSONArray gvg = (JSONArray) o.get("Goblins vs Gnomes");
		
		for (int i = 0; i < gvg.size(); i++)
		{
			JSONObject tmp = (JSONObject) gvg.get(i);
			addCard(tmp);
		}
		
		// This category currently has cards like Elite Tauren Chieftain and Gelbin Mekkatorque
		JSONArray prom = (JSONArray) o.get("Promo");
		
		for (int i = 0; i < prom.size(); i++)
		{
			JSONObject tmp = (JSONObject) prom.get(i);
			addCard(tmp);
		}
		
		// This category currently has cards like Captain's Parrot and Old Murk-Eye
		JSONArray rewards = (JSONArray) o.get("Reward");
		
		for (int i = 0; i < rewards.size(); i++)
		{
			JSONObject tmp = (JSONObject) rewards.get(i);
			addCard(tmp);
		}
		
		JSONArray black = (JSONArray) o.get("Blackrock Mountain");
		
		for (int i = 0; i < black.size(); i++)
		{
			JSONObject tmp = (JSONObject) black.get(i);
			addCard(tmp);
		}
		
		JSONArray tourn = (JSONArray) o.get("The Grand Tournament");
		
		for (int i = 0; i < tourn.size(); i++)
		{
			JSONObject tmp = (JSONObject) tourn.get(i);
			addCard(tmp);
		}
		
		JSONArray league = (JSONArray) o.get("The League of Explorers");
		
		for (int i = 0; i < league.size(); i++)
		{
			JSONObject tmp = (JSONObject) league.get(i);
			addCard(tmp);
		}
		
		JSONArray gods = (JSONArray) o.get("Whispers of the Old Gods");
		
		for (int i = 0; i < gods.size(); i++)
		{
			JSONObject tmp = (JSONObject) gods.get(i);
			addCard(tmp);
		}
		
		JSONArray kara = (JSONArray) o.get("Karazhan");
		
		for (int i = 0; i < kara.size(); i++)
		{
			JSONObject tmp = (JSONObject) kara.get(i);
			addCard(tmp);
		}
	}
	
	// This method takes as argument a JSON object which represents a Hearthstone card from
	// the mashape API, and creates a new Card object and adds it to the appropriate ArrayList
	// based on the player class.
	private static void addCard(JSONObject tmp)
	{
		boolean legendary = ((String) tmp.get("rarity")).equals("Legendary");
		
		// Here, we check the "type" of the card represented. The mashape API maintains objects to represent the actual heroes such as Malfurion,
		// and these are obviously not card options that we want for our deck. So, we are looking for spell, minion, and weapon cards. If we find
		// a "hero" card, we return without doing anything.
		String type = (String) tmp.get("type");
		if (type.equals("Hero")) return;
		
		else if (type.equals("Spell") || type.equals("Minion") || type.equals("Weapon"))
		{
			// Sort into class-specific ArrayList
			String pc = (String) tmp.get("playerClass");
			
			if (pc == null) neutral.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Warrior")) warrior.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Mage")) mage.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Druid")) druid.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Warlock")) warlock.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Paladin")) paladin.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Rogue")) rogue.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Priest")) priest.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Hunter")) hunter.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			else if (pc.equals("Shaman")) shaman.add(new Card((String)tmp.get("name"), (String)tmp.get("playerClass"),(String)tmp.get("img"), legendary,(Long)tmp.get("cost")));
			
			// This is not expected to ever happen unless something happens like a new hero is introduced in Hearthstone, or else
			// if mashape makes some kind of change to their infrastructure such as changing "Hunter" to "HunterClass" or something like that
			else
			{
				System.out.println("Hero class: '" + pc + "' not found! System exiting...");
				System.exit(0);
			}
		}
		
		// As we mentioned, we are looking to include spells, minions, and weapons. We are looking to exclude "hero" cards.
		// We should only ever encounter this below scenario if a new type of card is added to Hearthstone.
		else
		{
			System.out.println("New type encountered: " + type);
			System.exit(0);
		}
	}
	
	// By this point, we have our collection of all collectible Hearthstone cards maintained in our 10 class-specific
	// lists (9 hero classes + neutral). This method takes as argument the desired class for the random deck, as well as
	// the percentage of class cards to be in the random deck. We then build, sort, and return a random decklist
	// in the form of an array of 30 Card objects.
	public static Card[] buildRandomDeck(String playerClass, int percent)
	{
		// If the user chooses Random for the hero, generate a random hero class from the 9 available
		if (playerClass.equals("Random")) playerClass = getRandomHero();
		
		// Get the pool of cards to be used for this deck. This will consist of two of our class-specific lists:
		// the neutral card list, and the class-specific card list which corresponds to the chosen hero.
		ArrayList<Card> classPool = getClassPool(playerClass);
		
		// While we are building our decklist of 30 cards, we maintain our cards in two separate lists, one of
		// neutral cards, and one of class-specific cards. This makes it easier to sort and then display the final
		// result in an order that makes it easy for the user to then build the deck.
		ArrayList<Card> classDeckList = new ArrayList<Card>();
		ArrayList<Card> neutralDeckList = new ArrayList<Card>();
		
		// Object from the Random class. We use this in 2 places: determining whether each card for the deck will be
		// either class-specific or neutral, and picking a random card from our pool to add to the decklist.
		Random r = new Random();
		
		// Add 30 random cards to our decklist, following deck-building rules.
		for(int i=0; i<30; i++)
		{
			Card c;
			
			int rand = r.nextInt(100);
			
			boolean isClass;
			
			// Here we are applying our approximate percentage of class-specific cards. For each card, we roll a
			// random number from 0-99. If that number is below our target percentage, we choose a random
			// class-specific card, otherwise we choose a random neutral card.
			if (rand < percent)
			{
				c = classPool.get(r.nextInt(classPool.size()));
				isClass = true;
			}
			else
			{
				c = neutral.get(r.nextInt(neutral.size()));
				isClass = false;
			}
			
			boolean success;
			
			// Attempt to add the card, either to the class-specific list or neutral list, following deck-building rules
			if (isClass) success = addCard(c, classDeckList);
			else success = addCard(c, neutralDeckList);
			
			// If the card was not added, decrement "i" in order to add 1 iteration to this loop, so the final
			// card count is 30.
			if (!success) i--;
		}
		
		// Here we convert our lists from ArrayLists to Arrays, to facilitate sorting / general manipulating.
		Card[] classDeck = new Card[classDeckList.size()];
		Card[] neutralDeck = new Card[neutralDeckList.size()];
		
		int i = 0;
		for (Card c : classDeckList)
		{
			classDeck[i] = c;
			i++;
		}
		i=0;
		for (Card c : neutralDeckList)
		{
			neutralDeck[i] = c;
			i++;
		}
		
		// Cards in a final decklist are sorted first by class-specific/neutral, then by cost, then alphabetically.
		sortByCost(classDeck);
		sortByAlpha(classDeck);
		sortByCost(neutralDeck);
		sortByAlpha(neutralDeck);
		
		// Create final result array and populate it with sorted results from class-specific cards, then neutral cards
		Card[] finalDeck = new Card[30];
		
		for (i = 0; i < classDeck.length; i++)
		{
			finalDeck[i] = classDeck[i];
		}
		
		for (int j = 0; j < neutralDeck.length; j++)
		{
			finalDeck[i] = neutralDeck[j];
			i++;
		}
		
		return finalDeck;
	}
	
	// Initial method to recursively sort by cost using quick sort.
	private static void sortByCost(Card[] inputArr) {
        
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        
        int length = inputArr.length;
        quickSortByCost(0, length - 1, inputArr);
    }
	
	// Initial method to recursively sort alphabetically using quick sort.
	private static void sortByAlpha(Card[] deck)
	{
		int limit = deck.length;
		// This may not be ideal, but we'll just go through each cost-tier and sort
		int i=0;
		while (i < limit && deck[i].cost == 0) i++;
		if (i >= 2) quickSortByAlpha(0, i-1, deck);
		
		int j=i;
		while (j < limit && deck[j].cost == 1) j++;
		if (j-i >= 2) quickSortByAlpha(i, j-1, deck);
		
		int k=j;
		while (k < limit && deck[k].cost == 2) k++;
		if (k-j >= 2) quickSortByAlpha(j, k-1, deck);
		
		int l=k;
		while (l < limit && deck[l].cost ==3) l++;
		if (l-k >= 2) quickSortByAlpha(k, l-1, deck);
		
		int m=l;
		while (m < limit && deck[m].cost == 4) m++;
		if (m-l >= 2) quickSortByAlpha(l, m-1, deck);
		
		int n=m;
		while (n < limit && deck[n].cost == 5) n++;
		if (n-m >= 2) quickSortByAlpha(m, n-1, deck);
		
		int o=n;
		while (o < limit && deck[o].cost == 6) o++;
		if (o-n >= 2) quickSortByAlpha(n, o-1, deck);
		
		int p=o;
		while (p < limit && deck[p].cost == 7) p++;
		if (p-o >= 2) quickSortByAlpha(o, p-1, deck);
		
		int q=p;
		while (q < limit && deck[q].cost == 8) q++;
		if (q-p >= 2) quickSortByAlpha(p, q-1, deck);
		
		int r=q;
		while (r < limit && deck[r].cost == 9) r++;
		if (r-q >= 2) quickSortByAlpha(q, r-1, deck);
		
		int s=r;
		while (s < limit && deck[s].cost == 10) s++;
		if (s-r >= 2) quickSortByAlpha(r, s-1, deck);
		
		int t=s;
		while (t < limit && deck[t].cost == 12) t++;
		if (t-s >= 2) quickSortByAlpha(s, t-1, deck);
		
		// If, in the future, more costs of cards are introduced such as a 13-mana card, this will need to
		// be edited appropriately. Currently, molten giants (cost 25) are not included since that is the
		// only 25-mana card and hence doesn't need to be sorted.
    }
 
	// Recursive method to sort by cost using quick sort.
    private static void quickSortByCost(int lowerIndex, int higherIndex, Card[] array) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // Calculate pivot number, taking pivot as middle index number
        Card pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
        	
            while (array[i].cost < pivot.cost) {
                i++;
            }
            while (array[j].cost > pivot.cost) {
                j--;
            }
            if (i <= j) {
                exchangeCards(i, j, array);
                // Move index to next position on both sides
                i++;
                j--;
            }
        }
        // Call quickSort() method recursively
        if (lowerIndex < j)
            quickSortByCost(lowerIndex, j, array);
        if (i < higherIndex)
            quickSortByCost(i, higherIndex, array);
    }
    
    // Recursive method to sort alphabetically using quick sort.
    private static void quickSortByAlpha(int lowerIndex, int higherIndex, Card[] array) {
        
        int i = lowerIndex;
        int j = higherIndex;
        // Calculate pivot number, taking pivot as middle index number
        Card pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
        	
            while (array[i].name.compareTo(pivot.name) < 0) {
                i++;
            }
            while (array[j].name.compareTo(pivot.name) > 0) {
                j--;
            }
            if (i <= j) {
                exchangeCards(i, j, array);
                // Move index to next position on both sides
                i++;
                j--;
            }
        }
        // Call quickSort() method recursively
        if (lowerIndex < j)
            quickSortByAlpha(lowerIndex, j, array);
        if (i < higherIndex)
            quickSortByAlpha(i, higherIndex, array);
    }
 
    // Helper method to swap the positions of two cards in an array
    private static void exchangeCards(int i, int j, Card[] array) {
        Card temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
	
    // Helper method to generate a random selection from the 9 heroes currently available
	private static String getRandomHero()
	{
		Random r = new Random();
		
		int i = r.nextInt(9);
		
		if (i==0) return "Mage";
		else if (i==1) return "Warrior";
		else if (i==2) return "Shaman";
		else if (i==3) return "Priest";
		else if (i==4) return "Warlock";
		else if (i==5) return "Paladin";
		else if (i==6) return "Rogue";
		else if (i==7) return "Hunter";
		else return "Druid";
	}
	
	// Helper method that returns a copy of one of our class-specific lists of all collectible cards of that class
	private static ArrayList<Card> getClassPool(String playerClass)
	{
		ArrayList<Card> pool = new ArrayList<Card>();
		
		if (playerClass.equals("Mage")) for (Card c : mage) pool.add(c);
		else if (playerClass.equals("Warrior")) for (Card c : warrior) pool.add(c);
		else if (playerClass.equals("Shaman")) for (Card c : shaman) pool.add(c);
		else if (playerClass.equals("Priest")) for (Card c : priest) pool.add(c);
		else if (playerClass.equals("Warlock")) for (Card c : warlock) pool.add(c);
		else if (playerClass.equals("Paladin")) for (Card c : paladin) pool.add(c);
		else if (playerClass.equals("Rogue")) for (Card c : rogue) pool.add(c);
		else if (playerClass.equals("Hunter")) for (Card c : hunter) pool.add(c);
		else for (Card c : druid) pool.add(c);
		
		return pool;
	}
	
	// Attempt to add the specified card "c" to the current decklist "deck", following Hearthstone deck-building rules:
	// max 2 of any card, max 1 if that card is legendary. Returns true if the card is added successfully, returns false
	// if unable to add the card.
	private static boolean addCard(Card c, ArrayList<Card> deck)
	{
		int count = 0;
		
		for (Card d : deck)
		{
			if (c.equals(d)) count++;
		}
		
		if (count==2) return false;
		if (count==1 && c.legendary==true) return false;
		
		deck.add(c);
		
		return true;
	}
}


