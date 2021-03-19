package battleship;

import java.util.LinkedList;
import java.util.Random;

public class Map{
	public static final int DIM_MAP = 10;
	private final char NULLA = '0', SHIP = 'X', WATER = 'A', HIT = 'C';
	private char[][] map;
	private LinkedList<Ship> shipList;

	public Map() {
		shipList = new LinkedList<Ship>();
		map = new char[DIM_MAP][DIM_MAP];
		for (int i = 0; i < DIM_MAP; i++)
			for (int j = 0; j < DIM_MAP; j++)
				map[i][j] = NULLA;
	}

	public void fillMapRandom() {
		clear();
		Random r = new Random();
		insertRandomShip(r, 4);
		insertRandomShip(r, 3);
		insertRandomShip(r, 3);
		insertRandomShip(r, 2);
		insertRandomShip(r, 2);
		insertRandomShip(r, 2);
		insertRandomShip(r, 1);
		insertRandomShip(r, 1);
		insertRandomShip(r, 1);
		insertRandomShip(r, 1);
	}

	private void clear() {
		for (int i = 0; i < DIM_MAP; i++)
			for (int j = 0; j < DIM_MAP; j++)
				map[i][j] = NULLA;

	}

	public boolean insertShip(int x, int y, int dim, int dir) {
		if (dir == 1 && x + dim > DIM_MAP) {
			return false;
		} // vertical
		if (dir == 0 && y + dim > DIM_MAP) {
			return false;
		} // horizontal
		boolean insert;

		if (dir == 0)
			insert = horVerification(x, y, dim);
		else
			insert = vertVerification(x, y, dim);

		if (!insert)
			return false;
		if (dir == 0) {
			Ship n = new Ship(x, y, x, y + dim - 1);
			shipList.add(n);
		} else {
			Ship n = new Ship(x, y, x + dim - 1, y);
			shipList.add(n);
		}
		for (int i = 0; i < dim; i++) {
			if (dir == 0) {
				map[x][y + i] = SHIP;
			} else
				map[x + i][y] = SHIP;
		}
		return true;
	}

	public int[] insertRandomShip(Random random, int dimensione) {
		boolean insert;
		int[] data = new int[4];
		int direction, line, column;
		do {
			insert = true;
			direction = random.nextInt(2); // 0=Orizzontale, 1=Verticale
			if (direction == 0) {
				column = random.nextInt(DIM_MAP - dimensione + 1);
				line = random.nextInt(DIM_MAP);
			} else {
				column = random.nextInt(DIM_MAP);
				line = random.nextInt(DIM_MAP - dimensione + 1);
			}
			if (direction == 0)
				insert = horVerification(line, column, dimensione);
			else
				insert = vertVerification(line, column, dimensione);
		} while (!insert);
		if (direction == 0) {
			Ship n = new Ship(line, column, line, column + dimensione - 1);
			shipList.add(n);
		} else {
			Ship n = new Ship(line, column, line + dimensione - 1, column);
			shipList.add(n);
		}
		for (int i = 0; i < dimensione; i++) {
			if (direction == 0) {
				map[line][column + i] = SHIP;
			} else
				map[line + i][column] = SHIP;
		}
		data[0] = line;
		data[1] = column;
		data[2] = dimensione;
		data[3] = direction;
		return data;
	}

	public boolean vertVerification(int line, int column, int dimensione) {
		if (line != 0)
			if (map[line - 1][column] == SHIP)
				return false;
		if (line != DIM_MAP - dimensione)// the ship finish on the edge
			if (map[line + dimensione][column] == SHIP)
				return false;
		for (int i = 0; i < dimensione; i++) {
			if (column != 0)
				if (map[line + i][column - 1] == SHIP)
					return false;
			if (column != DIM_MAP - 1)
				if (map[line + i][column + 1] == SHIP)
					return false;
			if (map[line + i][column] == SHIP)
				return false;
		}
		return true;
	}

	public boolean horVerification(int line, int column, int dimensione) {
		if (column != 0)
			if (map[line][column - 1] == SHIP)
				return false;
		if (column != DIM_MAP - dimensione)
			if (map[line][column + dimensione] == SHIP)
				return false;
		for (int i = 0; i < dimensione; i++) {
			if (line != 0)
				if (map[line - 1][column + i] == SHIP)
					return false;
			if (line != DIM_MAP - 1)
				if (map[line + 1][column + i] == SHIP)
					return false;
			if (map[line][column + i] == SHIP)
				return false;
		}
		return true;
	}

	public boolean shoot(Position p) {
		int line = p.getCoordX();
		int column = p.getCoordY();
		if (map[line][column] == SHIP) {
			map[line][column] = HIT;
			return true;
		}
		map[line][column] = WATER;
		return false;
	}

	public Ship sink(Position p) {
		int line = p.getCoordX();
		int col = p.getCoordY();
		Ship ship = null;
		for (int i = 0; i < shipList.size(); i++) {
			if (shipList.get(i).equal(line, col)) {
				ship = shipList.get(i);
				break;
			}
		}
		for (int i = ship.getXin(); i <= ship.getXfin(); i++) {
			for (int j = ship.getYin(); j <= ship.getYfin(); j++) {
				if (map[i][j] != HIT) {
					return null;
				}
			}
		}
		shipList.remove(ship);
		return ship;
	}

	public void setWater(Position p) {
		map[p.getCoordX()][p.getCoordY()] = WATER;
	}

	public boolean water(Position p) {
		return map[p.getCoordX()][p.getCoordY()] == WATER;
	}

	public boolean hit(Position p) {
		return map[p.getCoordX()][p.getCoordY()] == HIT;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < DIM_MAP; i++) {
			for (int j = 0; j < DIM_MAP; j++) {
				sb.append(map[i][j] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	// method for receiving the opponent's list of ships
	public void setAdvShips(LinkedList<int[]> advShips) {
		shipList.clear();
		for (int[] a : advShips) {
			insertShip(a[0], a[1], a[2], a[3]);
			System.out.println("entering" + a[0] + a[1] + a[2] + a[3]);
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.print(map[i][j]);
			System.out.println("");
		}
	}
}
