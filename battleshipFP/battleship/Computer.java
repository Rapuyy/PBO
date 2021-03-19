package battleship;

import java.util.LinkedList;
import java.util.Random;

public class Computer {
	private LinkedList<Position> listHit;
	private Random r;
	private int hit;
	private LinkedList<String> possibility;
	private Position finalShot;
	private String direction;
	private Map plMap;
	private Position firstHit;// position in which they hit the ship for the first time

	public Computer(Map opponentMap) {
		listHit = new LinkedList<Position>();
		this.plMap = opponentMap;
		for (int i = 0; i < Map.DIM_MAP; i++) {
			for (int j = 0; j < Map.DIM_MAP; j++) {
				Position p = new Position(i, j);
				listHit.add(p);// Initialize possible hits
			}
		}
		r = new Random();
		hit = 0;
	}

	public Report myTurn() {

		Report rep = new Report();
		if (hit == 0) {
			boolean blow = randomShoot();
			rep.setP(finalShot);
			rep.setHit(blow);
			Ship sunk;
			if (blow) {
				hit++;
				sunk = plMap.sink(finalShot);
				if (sunk != null) {
					rep.setSunk(true);
					removeCon(sunk);
					hit = 0;
					direction = null;
				} else {
					firstHit = finalShot;
					possibility = new LinkedList<String>();
					initializesList();
				}
			}
			return rep;
		} // Shoot randomly
		if (hit == 1) {
			boolean blow = shootTarget1();
			Ship sunk;
			rep.setP(finalShot);
			rep.setHit(blow);
			rep.setSunk(false);
			if (blow) {
				hit++;
				possibility = null;
				sunk = plMap.sink(finalShot);
				if (sunk != null) {
					rep.setSunk(true);
					removeCon(sunk);
					hit = 0;
					direction = null;
				}
			}
			return rep;
		}
		if (hit >= 2) {
			boolean blow = shootTarget2();
			Ship sunk;
			rep.setP(finalShot);
			rep.setHit(blow);
			rep.setSunk(false);
			if (blow) {
				hit++;
				sunk = plMap.sink(finalShot);
				if (sunk != null) {
					rep.setSunk(true);
					removeCon(sunk);
					hit = 0;
					direction = null;
				}
			} else {
				reverseDirection();
			}
			return rep;
		}
		return null;// unattainable
	}

	private boolean randomShoot() {
		int shot = r.nextInt(listHit.size());
		Position p = listHit.remove(shot);
		finalShot = p;
		boolean blow = plMap.shoot(p);
		return blow;
	}

	private boolean shootTarget1() {
		boolean errore = true;
		Position p = null;
		do {
			int shot = r.nextInt(possibility.size());
			String dove = possibility.remove(shot);
			p = new Position(firstHit);
			p.move(dove.charAt(0));
			direction = dove;
			if (!plMap.water(p)) {
				listHit.remove(p);
				errore = false;
			}
		} while (errore);// make sure you are not trying to shoot on a already hit position
		finalShot = p;
		return plMap.shoot(p);
	}

	private boolean shootTarget2() {
		boolean colpibile = false;
		Position p = new Position(finalShot);
		do {
			p.move(direction.charAt(0));

			if (p.offMap() || plMap.water(p)) {
				reverseDirection();
			} else {
				if (!plMap.hit(p)) {
					colpibile = true;
				}

			}
		} while (!colpibile);
		listHit.remove(p);
		finalShot = p;
		return plMap.shoot(p);
	}

	//

	private void removeCon(Ship sunk) {
		int Xin = sunk.getXin();
		int Xfin = sunk.getXfin();
		int Yin = sunk.getYin();
		int Yfin = sunk.getYfin();
		if (Xin == Xfin) {// horizontal
			if (Yin != 0) {
				Position p = new Position(Xin, Yin - 1);
				if (!plMap.water(p)) {
					listHit.remove(p);
					plMap.setWater(p);

				}
			}
			if (Yfin != Map.DIM_MAP - 1) {
				Position p = new Position(Xin, Yfin + 1);
				if (!plMap.water(p)) {
					listHit.remove(p);
					plMap.setWater(p);
				}
			}
			if (Xin != 0) {
				for (int i = 0; i <= Yfin - Yin; i++) {
					Position p = new Position(Xin - 1, Yin + i);
					if (!plMap.water(p)) {
						listHit.remove(p);
						plMap.setWater(p);
					}
				}

			}
			if (Xin != Map.DIM_MAP - 1) {
				for (int i = 0; i <= Yfin - Yin; i++) {
					Position p = new Position(Xin + 1, Yin + i);
					if (!plMap.water(p)) {
						listHit.remove(p);
						plMap.setWater(p);
					}
				}
			}
		} else {// vertical
			if (Xin != 0) {
				Position p = new Position(Xin - 1, Yin);
				if (!plMap.water(p)) {
					listHit.remove(p);
					plMap.setWater(p);
				}
			}
			if (Xfin != Map.DIM_MAP - 1) {
				Position p = new Position(Xfin + 1, Yin);
				if (!plMap.water(p)) {
					listHit.remove(p);
					plMap.setWater(p);
				}
			}
			if (Yin != 0) {
				for (int i = 0; i <= Xfin - Xin; i++) {
					Position p = new Position(Xin + i, Yin - 1);
					if (!plMap.water(p)) {
						listHit.remove(p);
						plMap.setWater(p);
					}
				}

			}
			if (Yfin != Map.DIM_MAP - 1) {
				for (int i = 0; i <= Xfin - Xin; i++) {
					Position p = new Position(Xin + i, Yin + 1);
					if (!plMap.water(p)) {
						listHit.remove(p);
						plMap.setWater(p);
					}
				}
			}
		}
	}

	private void initializesList() {
		if (finalShot.getCoordX() != 0) {
			possibility.add("N");
		}
		if (finalShot.getCoordX() != Map.DIM_MAP - 1) {
			possibility.add("S");
		}
		if (finalShot.getCoordY() != 0) {
			possibility.add("O");
		}
		if (finalShot.getCoordY() != Map.DIM_MAP - 1) {
			possibility.add("E");
		}
	}

	private void reverseDirection() {
		if (direction.equals("N")) {
			direction = "S";
		} else if (direction.equals("S")) {
			direction = "N";
		} else if (direction.equals("E")) {
			direction = "O";
		} else if (direction.equals("O")) {
			direction = "E";
		}
	}

}
