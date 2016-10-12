package com;

/*
 * Defines Card object.
 */

public class Card
{
	// Name of the card e.g. "Call of the Wild"
	String name;
	
	// Player class e.g. "Mage"
	String playerClass;
	
	// Link to image location
	String img;
	
	// Mana cost
	int cost;
	
	// Boolean to determine whether this card is legendary. This is used when
	// determining if a deck can have 1 or 2 copies of a card (only 1 for legendary).
	boolean legendary;
	
	// Constructor method
	public Card(String name, String playerClass, String img, boolean legendary, long cost)
	{
		if (playerClass == null) playerClass = "Neutral";
		
		this.name = name;
		this.playerClass = playerClass;
		this.img = img;
		this.legendary = legendary;
		this.cost = Integer.parseInt(cost+"");
	}
	
	// Override of basic toString method for displaying this card to the user
	public String toString()
	{
		return this.name + " " + this.cost;
	}
	
	// Override of basic equals method for comparing two cards for equality
	public boolean equals(Object o)
	{
		if (o == null) return false;
		
		return this.name.equals(((Card) o).name);
	}
}
