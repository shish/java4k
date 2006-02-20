/**
 *   Whack-a-square - Shish's entry to the java 4k contest
 *   Copyright (C) 2003 Shish
 * 
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 * 
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

public class whack extends Applet
			implements Runnable, KeyListener, WindowListener {
	private Graphics g;
	private Image buf;
	private int h,w,x=0,y=0, hits=0, misses=0, max=0, submisses=0;
	private boolean pause = false;

	private boolean up[] = new boolean[9];

	public static void main(String[] args) {
		Frame f = new Frame("test");
		f.setLayout(new BorderLayout());
		whack w = new whack();
		f.add(w, BorderLayout.CENTER);
		f.addWindowListener(w);
		f.pack();
		f.show();
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics gtop) {
		if(g == null) {
			buf = createImage(w=getSize().width, h=getSize().height);
			g = buf.getGraphics();
			addKeyListener(this);
			new Thread(this).start();
		}
		g.setColor(Color.white);
		g.fillRect(0,0,w,h);
		g.setColor(Color.black);
		g.drawRect(5,5,140,140);

		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				int a = (i*3)+j;
				int ran = pause ? 1 : (int)(Math.random()*20);
				if(ran == 0) {
					if(up[a]) {
						up[a] = false;
						submisses++;
						if(submisses >= 10) {
							misses++;
							submisses = 0;
						}
					} else {
						up[a] = true;
					}
				}
				g.fillRect(
						(i*45)+10,
						((j*45)+50)-(up[a] ? 40 : 0),
						40,
						up[a] ? 40 : 0
						);
			}
		}
		if(misses == 10) {
			max = hits;
			hits = 0;
			misses = 0;
		}
		g.drawString("Hits: "+hits, 5, 160);
		g.drawString("Misses: "+misses, 5, 175);
		g.drawString("Max: "+max, 5, 190);
		gtop.drawImage(buf, 0, 0, this);
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(pause ? 3000 : 300-(hits*3)-(misses*10));
				repaint();
			}
			catch(Exception e) {
			}
		}
	}

	public boolean isFocusTraversable() {
		return true;
	}

	public Dimension getPreferredSize() {
		super.getPreferredSize();
		return new Dimension(150,200);
	}

	public void hit(int key) {
		if(pause) key = 0;
		switch(key) {
			case 36: hitmole(0); break;
			case 38: hitmole(3); break;
			case 33: hitmole(6); break;
			case 37: hitmole(1); break;
			case 12: hitmole(4); break;
			case 39: hitmole(7); break;
			case 35: hitmole(2); break;
			case 40: hitmole(5); break;
			case 34: hitmole(8); break;
			default: pause = !pause; break;
		}
	}
	public void hitmole(int i) {
		if(up[i]) {
			hits++;
			up[i] = false;
			repaint();
		} else {
			misses++;
		}
	}

	public void keyPressed(KeyEvent ke) {
		hit(ke.getKeyCode());
	}
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {}

	public void windowOpened(WindowEvent we) {}
	public void windowClosing(WindowEvent we) {System.exit(0);}
	public void windowClosed(WindowEvent we) {}

	public void windowMinimized(WindowEvent we) {}
	public void windowMaximized(WindowEvent we) {}

	public void windowActivated(WindowEvent we) {}
	public void windowDeactivated(WindowEvent we) {}

	public void windowIconified(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
}

