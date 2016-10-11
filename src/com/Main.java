package com;

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
	private static final long serialVersionUID = 1L;
	private Card[] deck;
	
	public static void main(String[] args)
	{
		new Main();
	}
	
	public Main()
	{
		super();
		initialize();
	}
	
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
	
	private JPanel getJContentPane()
	{
		JPanel jContentPane = new JPanel();
		
		jContentPane.add(new Screen());
		
		return jContentPane;
	}
	
	private class Screen extends JPanel
	{
		private static final long serialVersionUID = 1L;
		
		private JList<String> list;
		private JButton build;
		private JSlider slider;
		
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
		
		private JList<String> getList()
		{
			String[] heroes = {"Random","Mage","Warrior","Paladin","Priest","Hunter","Shaman","Warlock","Rogue","Druid"};
			JList<String> l = new JList<String>(heroes);
			
			l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			return l;
		}
		
		private JButton getBuildButton()
		{
			JButton b = new JButton("Build");
			
			b.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String playerClass = list.getSelectedValue();
					int percent = slider.getValue();
					
					//If nothing is selected, do nothing
					if (playerClass == null) return;
					
					deck = Collector.buildRandomDeck(playerClass, percent-1);
					
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
