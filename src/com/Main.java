package com;

/*
 * Main controller class which maintains a JFrame window, builds a pool of all collectible
 * Hearthstone cards, and then creates random decklists.
 * 
 * The user must choose a Hero, or choose Random for a random hero. There is also a dial
 * which can be adjusted to control the approximate ratio of class cards to neutral cards
 * in the deck. For example, if the dial is set to 60, approximately 60% of the cards in the
 * resulting deck will be class-cards, while the rest will be neutral cards.
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;

public class Main extends JFrame
{
	// Default serial version ID used since JFrame is serializable
	private static final long serialVersionUID = 1L;
	
	// Array of Card objects which holds our randomly generated decklist
	private Card[] deck;
	
	// Main executable method
	public static void main(String[] args)
	{
		new Main();
	}
	
	// Call JFrame's constructor and then initialize to configure the window
	public Main()
	{
		super();
		initialize();
	}
	
	// Set up display to the user and collect pool of all collectible Hearthstone cards from mashape API
	private void initialize()
	{
		Collector.collectCards();
		
		this.setSize(250, 250);
		this.setContentPane(getJContentPane());
		this.setTitle("Random Deck");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// Set up content pane. This contains a list of classes to choose from for the random decklist,
	// the "Build" button which builds and then displays a random decklist, and a JSlider which lets
	// the user select the approximate percentage of class-specific cards that will be in the final decklist.
	private JPanel getJContentPane()
	{
		JPanel jContentPane = new JPanel();
		
		jContentPane.add(new Screen());
		
		return jContentPane;
	}
	
	// Nested JPanel class which builds the display set-up using Grid Bag Constraints.
	private class Screen extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private JList<String> list;
		private JButton build;
		private JSlider slider;
		
		// Constructor which calls the JPanel constructor and then adds the hero list, action button,
		// and slider using Grid Bag Constraints.
		public Screen()
		{
			super();
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(10,10,10,10);
			setLayout(new GridBagLayout());
			
			list = getList();
			build = getBuildButton();
			slider = getSlider();
			
			c.gridx = 0;
			c.anchor = GridBagConstraints.WEST;
			add(list, c);
			
			c.gridx = 1;
			c.anchor = GridBagConstraints.EAST;
			add(build, c);
			
			c.gridx = 2;
			add(slider, c);
		}
		
		// Assemble the list of 9 hero choices plus "Random" choice
		private JList<String> getList()
		{
			String[] heroes = {"Random","Mage","Warrior","Paladin","Priest","Hunter","Shaman","Warlock","Rogue","Druid"};
			JList<String> l = new JList<String>(heroes);
			
			l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			return l;
		}
		
		// Create JButton with Action Listener to actually build a random decklist.
		private JButton getBuildButton()
		{
			JButton b = new JButton("Build");
			
			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String playerClass = list.getSelectedValue();
					int percent = slider.getValue();
					
					// If no hero is selected, do nothing
					if (playerClass == null) return;
					
					deck = Collector.buildRandomDeck(playerClass, percent-1);
					
					// After we have our random decklist, we display it in a new separate window.
					JFrame newWindow = new JFrame(playerClass);
					
					String[] names = new String[30];
					
					for (int i=0; i<30; i++)
					{
						names[i] = deck[i].name;
					}
					
					newWindow.getContentPane().add(new JList<String>(names), BorderLayout.CENTER);
					
					newWindow.setSize(250, 600);
					newWindow.setResizable(false);
					newWindow.setVisible(true);
				}
			});
			
			return b;
		}
		
		// Initialize the slider for selecting approximate percentage of class-specific cards
		private JSlider getSlider()
		{
			JSlider s = new JSlider(JSlider.VERTICAL, 0, 100, 30);
			
			s.setMajorTickSpacing(10);
			s.setMinorTickSpacing(1);
			s.setPaintTicks(true);
			s.setPaintLabels(true);
			
			return s;
		}
	}
}
