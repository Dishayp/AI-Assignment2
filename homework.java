import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class homework {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	int gridlength;
	String gamemode;
	String me;
	int depth;
	int scorematrix[][];
	int matrix[][];
	int i, j;
	String myplayer;
	String oppplayer;
	int Stake = 1;
	int Raid = 2;
	String abc = null;
	int c, d;

	public void print() {

		if (abc.equals("Raid")) {
			matrix[c][d] = 1;
			if (matrix[c][d + 1] == -1) {
				matrix[c][d + 1] = 1;
			}
			if (matrix[c][d - 1] == -1) {
				matrix[c][d - 1] = 1;
			}
			if (matrix[c - 1][d] == -1) {
				matrix[c - 1][d] = 1;
			}
			if (matrix[c + 1][d] == -1) {
				matrix[c + 1][d] = 1;
			}
		} else {
			matrix[c][d] = 1;
		}
		for (int i = 1; i <= gridlength; i++) {
			for (int j = 1; j < gridlength + 1; j++) {
				if (matrix[i][j] == 1) {
					out.print(myplayer);
					// System.out.print(myplayer);
				} else if (matrix[i][j] == -1) {
					out.print(oppplayer);
					// System.out.print(oppplayer);
				} else {
					out.print(".");
					// System.out.print(".");
				}
			}
			out.println("");
			// System.out.println();
		}
	}

	private int[] alpha(String myplayer2, int depth2, int minValue, int maxValue) {

		// TODO Auto-generated method stub
		List<int[]> allmoves = child2(myplayer2);
		int ascore;
		int q = 0, p = 0;
		int bestRow = -1;
		int bestCol = -1;
		int type = -1;
		if (myplayer2 == myplayer)
			p = 1;
		else
			p = -1;
		if (depth2 == 0 || allmoves.isEmpty()) {
			ascore = evaluate();
			return new int[] { ascore, bestRow, bestCol, type };
		} else {
			for (int[] move : allmoves) {
				ArrayList<Integer> changemoves = new ArrayList<Integer>();
				Decision(p, move, changemoves);
				if (p == 1) {
					ascore = alpha(oppplayer, depth2 - 1, minValue, maxValue)[0];
					if (ascore >= minValue) {
						if ((ascore == minValue && move[2] == 1 && type == 2) || ascore > minValue) {
							minValue = ascore;
							bestRow = move[0];
							bestCol = move[1];
							type = move[2];
						}
					}
				}

				else {
					ascore = alpha(myplayer, depth2 - 1, minValue, maxValue)[0];
					if (ascore <= maxValue) {
						if ((ascore == maxValue && move[2] == 1 && type == 2) || ascore < maxValue) {
							maxValue = ascore;
							bestRow = move[0];
							bestCol = move[1];
							type = move[2];
						}
					}
				}
				UndoMoves(p, changemoves);
				if (minValue >= maxValue)
					break;
			}
		}
		return new int[] { (myplayer2 == myplayer) ? minValue : maxValue, bestRow, bestCol, type };
	}

	private int[] minimax(String me2, int depth) {
		List<int[]> allmoves = child2(me2);
		int currentScore;
		int q = 0, p = 0;
		int type = -1;
		int bestRow = -1;
		int bestCol = -1;
		int bestScore = (me == me2) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		if (me2 == myplayer)
			p = 1;
		else
			p = -1;
		if (depth == 0 || allmoves.isEmpty()) {
			bestScore = evaluate();
			// System.out.println("-- BS -- "+bestScore);
		} else {
			for (int[] move : allmoves) {
				// System.out.println("moves > "+move[2]);
				ArrayList<Integer> changemoves = new ArrayList<Integer>();
				Decision(p, move, changemoves);
				if (p == 1) {
					currentScore = minimax(oppplayer, depth - 1)[0];
					if (currentScore >= bestScore) {
						if (currentScore == bestScore && move[2] == 1 && type == 2 || currentScore > bestScore) {
							bestScore = currentScore;
							bestRow = move[0];
							bestCol = move[1];
							type = move[2];
						}
					}

				} else {
					currentScore = minimax(myplayer, depth - 1)[0];
					if (currentScore <= bestScore) {
						if (currentScore == bestScore && move[2] == 1 && type == 1 || currentScore < bestScore) {
							bestScore = currentScore;
							bestRow = move[0];
							bestCol = move[1];
							type = move[2];
						}
					}
				}
				UndoMoves(p, changemoves);
			}
		}
		return new int[] { bestScore, bestRow, bestCol, type };
	}

	private void Decision(int p, int[] move, ArrayList<Integer> changemoves) {

		if (move[2] == 2) {
			matrix[move[0]][move[1]] = p;
			changemoves.add(move[0]);
			changemoves.add(move[1]);

			if (matrix[move[0]][move[1] + 1] == (-1 * p)) {
				matrix[move[0]][move[1] + 1] = p;
				changemoves.add(move[0]);
				changemoves.add(move[1] + 1);
			}

			if (matrix[move[0]][move[1] - 1] == (-1 * p)) {
				matrix[move[0]][move[1] - 1] = p;
				changemoves.add(move[0]);
				changemoves.add(move[1] - 1);
			}

			if (matrix[move[0] - 1][move[1]] == (-1 * p)) {
				matrix[move[0] - 1][move[1]] = p;
				changemoves.add(move[0] - 1);
				changemoves.add(move[1]);
			}

			if (matrix[move[0] + 1][move[1]] == (-1 * p)) {
				matrix[move[0] + 1][move[1]] = p;
				changemoves.add(move[0] + 1);
				changemoves.add(move[1]);
			}
		} else {
			matrix[move[0]][move[1]] = p;
			changemoves.add(move[0]);
			changemoves.add(move[1]);
		}
	}

	private void UndoMoves(int p, ArrayList<Integer> changemoves) {

		for (int i = 0; i < changemoves.size(); i = i + 2) {
			if (i == 0)
				matrix[changemoves.get(i)][changemoves.get(i + 1)] = 0;
			else
				matrix[changemoves.get(i)][changemoves.get(i + 1)] = -1 * p;
		}
	}

	public List<int[]> child2(String me3) {

		int x;
		if (me3 == myplayer) {
			x = 1;
		} else {
			x = -1;
		}
		// System.out.println("---------------------------"+d);
		List<int[]> stakemoves = new ArrayList<int[]>();
		List<int[]> raidmoves = new ArrayList<int[]>();
		for (int i = 1; i <= gridlength; i++) {
			for (int j = 1; j <= gridlength; j++) {
				if (matrix[i][j] == 0) {
					stakemoves.add(new int[] { i, j, Stake });
					// allmoves.add(new int[] { i, j, Stake });
					// System.out.println(i+" "+j+" Stake");

					if (matrix[i][j + 1] == x) {
						// System.out.println(i+" "+j+" Raid");
						raidmoves.add(new int[] { i, j, Raid });
						// allmoves.add(new int[] { i, j, Raid });
					}

					if (matrix[i][j - 1] == x) {
						// System.out.println(i+" "+j+" Raid");
						raidmoves.add(new int[] { i, j, Raid });
						// allmoves.add(new int[] { i, j, Raid });
					}
					
					if (matrix[i - 1][j] == x) {
						// System.out.println(i+" "+j+" Raid");
						raidmoves.add(new int[] { i, j, Raid });
						// allmoves.add(new int[] { i, j, Raid });
					}
					if (matrix[i + 1][j] == x) {
						// System.out.println(i+" "+j+" Raid");
						raidmoves.add(new int[] { i, j, Raid });
						// allmoves.add(new int[] { i, j, Raid });
					}
				}
			}
		}

		for (int[] move : raidmoves) {
			stakemoves.add(move);
		}
		return stakemoves;
	}

	private int evaluate() {
		// TODO Auto-generated method stub
		int score = 0;
		for (i = 1; i <= gridlength + 1; i++) {
			for (j = 1; j <= gridlength + 1; j++) {
				score = score + (matrix[i][j] * scorematrix[i][j]);
			}
		}
		return score;
	}

	void getInput() throws IOException {

		gridlength = getInt();
		gamemode = getNextWORD();
		me = getNextWORD();
		char[] lrs = me.toCharArray();
		myplayer = me;
		if (myplayer.equals("X"))
			oppplayer = "O";
		else
			oppplayer = "X";
		depth = getInt();
		scorematrix = new int[gridlength + 2][gridlength + 2];
		matrix = new int[gridlength + 2][gridlength + 2];
		for (int i = 0; i <= gridlength + 1; i++) {
			for (int j = 0; j <= gridlength + 1; j++) {
				scorematrix[i][j] = 0;
				matrix[i][j] = 0;
			}
		}

		for (int i = 1; i <= gridlength; i++) {
			for (int j = 1; j <= gridlength; j++) {
				scorematrix[i][j] = getInt();
			}
		}

		for (int i = 1; i <= gridlength; i++) {

			String s = getNextWORD();
			char[] letters = s.toCharArray();
			for (int j = 0; j < letters.length; j++) {
				if (letters[j] == lrs[0]) {
					matrix[i][j + 1] = +1;
				} else if (letters[j] == '.') {
					matrix[i][j + 1] = 0;
				} else {
					matrix[i][j + 1] = -1;
				}
			}
		}
	}

	homework(String file) throws IOException {

		// long startTime = System.currentTimeMillis();
		br = new BufferedReader(new FileReader(file));
		out = new PrintWriter(System.out);
		int[] score;
		getInput();
		out = new PrintWriter("output.txt", "UTF-8");
		switch (gamemode) {
		case "MINIMAX": {
			score = minimax(myplayer, depth);
			d = score[2];
			c = score[1];
			if (score[3] == 2)
				abc = "Raid";
			else
				abc = "Stake";
			out.println(Character.toString((char) (d + 64)) + "" + c + " " + abc);
			print();
			break;
		}
		case "ALPHABETA": {
			score = alpha(myplayer, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			d = score[2];
			c = score[1];
			if (score[3] == 2)
				abc = "Raid";
			else
				abc = "Stake";
			out.println(Character.toString((char) (d + 64)) + "" + c + " " + abc);
			print();
			break;
		}
		case "COMPETITION":
			break;
		default:
			out.println("No Algorithm Mentioned");
		}

		/*
		 * long endTime = System.currentTimeMillis(); long totalTime = endTime -
		 * startTime; System.out.println(); System.out.println(totalTime);
		 */
		out.close();
	}

	public static void main(String[] args) throws IOException {

		String file = "input.txt";

		new homework(file);

	}

	String nextToken() {

		while (st == null || !st.hasMoreTokens()) {
			try {
				st = new StringTokenizer(br.readLine());

			} catch (Exception e) {
				eof = true;
				return null;
			}
		}
		return st.nextToken();
	}

	String getEntireLine() {

		try {
			return br.readLine();
		} catch (IOException e) {
			eof = true;
			return null;
		}
	}

	String getNextWORD() {
		return nextToken();
	}

	int getInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	long getLong() throws IOException {
		return Long.parseLong(nextToken());
	}

	double getDouble() throws IOException {
		return Double.parseDouble(nextToken());
	}
}