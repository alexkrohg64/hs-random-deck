package com;

public class Card
{
	String name;
	String playerClass;
	String img;
	int cost;
	boolean legendary;
	int count = 0;
	
	public Card(String name, String playerClass, String img, boolean legendary, long cost)
	{
		if (playerClass == null) playerClass = "Neutral";
		
		this.name = name;
		this.playerClass = playerClass;
		this.img = img;
		this.legendary = legendary;
		this.cost = Integer.parseInt(cost+"");
	}
	
	public String toString()
	{
		return this.name + " " + this.cost;
	}
	
	public boolean equals(Object o)
	{
		if (o == null) return false;
		
		return this.name.equals(((Card) o).name);
	}
}
