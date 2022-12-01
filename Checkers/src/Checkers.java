// Name: Stanley Lin
// Date: Wednesday, January 27, 2021
// Program Description: This program allows two players to play checkers against each other.
// It includes a main menu screen, an instructions screen with 3 pages, a stats screen (that uses
// text file streaming), and the game screen. The main menu has an option to choose who goes first. 
// The stats screen, instructions, and game screen allow the player to return to the main menu. 
// The stats screen has a button to reset the text file. The game screen includes a button to start a new
// game, and if applicable, a button for canceling an additional jump and one for forfeiting the match. 
// In the main game screen, to play, click on the piece. Click on where you would like to move/jump.

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.awt.event.*;

public class Checkers extends JPanel implements MouseListener
{
	// Checker Pieces
	// Stores the coordinates of the piece that the player wants to move.
	public static int[] firstSelection = new int[2]; 
	// Stores the coordinates of where that the player wants the piece to move.
	public static int[] secondSelection = new int[2];
	// Keeps track of the location of pieces on the board. 
	// B is a black piece (BK would be a black king piece).
	// R is a red piece (RK would be a red king piece).
	// "" is an empty space. " " is a square that cannot be moved/jumped to.
	public static String[][] boardPieces = { 		 
			{" ", "B", " ", "B", " ", "B", " ", "B"},
			{"B", " ", "B", " ", "B", " ", "B", " "},
			{" ", "B", " ", "B", " ", "B", " ", "B"},
			{"", " ", "", " ", "", " ", "", " "},
			{" ", "", " ", "", " ", "", " ", ""},
			{"R", " ", "R", " ", "R", " ", "R", " "},
			{" ", "R", " ", "R", " ", "R", " ", "R"},
			{"R", " ", "R", " ", "R", " ", "R", " "}
	};
	
	// blackCaptured and redCaptured captures the pieces captured of black and red respectively.
	public static int blackCaptured = 0;
	public static int redCaptured = 0;
	
	// Game Screens
	// instructions is an int data type because there are multiple instruction screens.
	public static boolean mainMenu = true;
	public static boolean game = false;
	public static boolean stats = false;
	public static int instructions = 0; 
	
	// Game states
	// If selectionFirst is true, the MouseListener captures the next click as the piece the player wants to move.
	public static boolean selectionFirst = true;
	// If makeFirst is true, white indicators are drawn on the piece selected in the paint component.
	public static boolean makeFirst = false;
	// If selectionSecond is true, the MouseListener captures the next click as a jump or a move.
	public static boolean selectionSecond = false;
	// If additionalJump is true, the MouseListener will capture the next additional jump.
	public static boolean additionalJump = false;
	// if erase is true, erase the first selection indicators or erase the first selection because it is moving/jumping.
	public static boolean erase = false;
	// If isJump equals true, the firstSelection jumps to the second selection and the piece in between is captured.
	public static boolean isJump = false;
	// if isMove is true, the firstSelection moves to the second selection.
	public static boolean isMove = false;
	// If redTurn is true, it is red's turn. If not, it is not red's turn. 
	public static boolean redTurn = true;
	// turnSelected is the turn selected on the main menu. If it is true, red goes first. If not, black goes first.
	public static boolean turnSelected = true;
	// If the end is true, the game ends. This is also important so that additional jumps are not checked for if 
	// the last piece of either red or black is captured.
	public static boolean end = false;
	// If forfeit is true, the game also ends.
	public static boolean forfeit = false;
	
	// Game images
	public static Image redPiece, blackPiece, redKing, blackKing, fill, checkerBoard, startNewGame, redWin, blackWin;
	public static Image redMove, blackMove, redsTurn, blacksTurn, redSelected, blackSelected, redDeselected, blackDeselected, score;
	
	// Object Constructor
	public Checkers()
	{
		// Panels Default Settings
		addMouseListener(this);
		setPreferredSize(new Dimension(600,400));
		setBackground(new Color(246, 255, 255));
		
		// This try catch statement retrieves the images.
		try
		{
			redPiece = ImageIO.read(new File("res/redPiece.png"));
			blackPiece = ImageIO.read(new File("res/blackPiece.png"));
			redKing = ImageIO.read(new File("res/redKing.png"));
			blackKing = ImageIO.read(new File("res/blackKing.png"));
			fill = ImageIO.read(new File("res/fill.png"));
			redsTurn = ImageIO.read(new File("res/redsTurn.png"));
			blacksTurn = ImageIO.read(new File("res/blacksTurn.png"));
			redMove = ImageIO.read(new File("res/redMove.png"));
			blackMove = ImageIO.read(new File("res/blackMove.png"));
			redSelected = ImageIO.read(new File("res/redSelected.png"));
			blackSelected = ImageIO.read(new File("res/blackSelected.png"));
			redDeselected = ImageIO.read(new File("res/redDeselected.png"));
			blackDeselected = ImageIO.read(new File("res/blackDeselected.png"));
			score = ImageIO.read(new File("res/score.png"));
			redWin = ImageIO.read(new File("res/redWin.png"));
			blackWin = ImageIO.read(new File("res/blackWin.png"));
			startNewGame = ImageIO.read(new File("res/startNewGame.png"));
		}
		catch (Exception e)
		{
			System.out.println("Image Not Found");
		}
	}
	
	// newGame resets all the variables for a new game.
	// It has no parameters and does not return anything.
	public static void newGame ()
	{
		String[][] newBoardPieces = {
				{" ", "B", " ", "B", " ", "B", " ", "B"},
				{"B", " ", "B", " ", "B", " ", "B", " "},
				{" ", "B", " ", "B", " ", "B", " ", "B"},
				{"", " ", "", " ", "", " ", "", " "},
				{" ", "", " ", "", " ", "", " ", ""},
				{"R", " ", "R", " ", "R", " ", "R", " "},
				{" ", "R", " ", "R", " ", "R", " ", "R"},
				{"R", " ", "R", " ", "R", " ", "R", " "}
		};
		boardPieces = newBoardPieces;
		game = true;
		end = false;
		blackCaptured = 0;
		redCaptured = 0;
	
		selectionFirst = true;
		selectionSecond = false;
		additionalJump = false;
		isJump = false;
		isMove = false;
		redTurn = turnSelected;
		makeFirst = false;
		erase = false;
		forfeit = false;
	}
	
	// makeBoard creates the main game screen, including the checker board, checker pieces, 
	// and side screen, which includes the pieces captured on the score board and the message for
	// whose turn it is.
	// It takes Graphics g and does not return anything.
	public void makeBoard (Graphics g)
	{
		newGame();
		super.paintComponent(g);
		try
		{
			BufferedImage sideScreen = ImageIO.read(new File("res/sideScreen.png"));
			BufferedImage checkerBoard = ImageIO.read(new File("res/checkerboard.png"));
			g.drawImage(sideScreen, 400, 0, null);
			g.drawImage(checkerBoard, 0, 0, 400, 400, null);
			
		}
		catch (Exception e)
		{
			System.out.println("Image Not Found");
		}
		
		for(int i = 0; i < 400; i += 50)
		{
			for(int k = 50; k < 400; k += 100)
			{
				if (i == 0 || i == 100)
				{
					g.drawImage(blackPiece, k+5, i+5, 40, 40, null);
				}
				else if (i == 50)
				{
					g.drawImage(blackPiece, k-50+5, i+5, 40, 40, null);
				}
				else if (i == 250 || i == 350)
				{
					g.drawImage(redPiece, k-50+5, i+5, 40, 40, null);
				}
				else if (i == 300)
				{
					g.drawImage(redPiece, k+5, i+5, 40, 40, null);
				}	
			}
		}
		g.setFont(new Font("Lucida Grande Regular", Font.PLAIN, 35));
		g.setColor(Color.black);
		g.drawString(String.valueOf(blackCaptured), 521, 125);
		g.setColor(Color.red);
		g.drawString(String.valueOf(redCaptured), 521, 70);
		
		if (turnSelected)
		{
			g.drawImage(redsTurn, 400, 0, null);
		}
		else
		{
			g.drawImage(blacksTurn, 400, 0, null);
		}
	}
	
	// firstSelection: this method checks if the coordinates of a first selection is a valid piece to
	// move. It ensures that it is a piece that corresponds to whose turn it is.
	// It takes two parameters: the x coordinate and y coordinate.
	// It returns true if the coordinates of the board piece selected are a valid piece to select.
	public static boolean firstSelection (int xValue, int yValue)
	{
		if (redTurn)
		{
			return (boardPieces[yValue][xValue].equals("R") || boardPieces[yValue][xValue].equals("RK")) ? true : false;
		}
		else
		{
			return (boardPieces[yValue][xValue].equals("B") || boardPieces[yValue][xValue].equals("BK")) ? true : false;
		}
	}
	
	// isMove: this method checks if the coordinates of a second selection is a valid move. It checks if
	// the square is diagonally forward by 50 (or backwards if the piece is a king) and is empty.
	// It takes two parameters: the x coordinate and y coordinate.
	// It returns true if the coordinates of the board piece selected is a valid square to move to.
	// It also sets isMove to true so the paintComponent can draw the move.
	public static boolean isMove (int xValue, int yValue)
	{
		if (redTurn) 
		{

			if (boardPieces[yValue][xValue].equals("") && yValue * 50 == firstSelection[1] - 50 && (xValue * 50 == firstSelection[0] - 50  || xValue * 50 == firstSelection[0] + 50))
			{
				isMove = true;
				return true;
			}
			else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "RK" && boardPieces[yValue][xValue].equals("") && (yValue * 50 == firstSelection[1] - 50 || yValue * 50 == firstSelection[1] + 50) && (xValue * 50 == firstSelection[0] - 50  || xValue * 50 == firstSelection[0] + 50))
			{
				isMove = true;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (boardPieces[yValue][xValue].equals("") && yValue * 50 == firstSelection[1] + 50 && (xValue * 50 == firstSelection[0] - 50 || xValue * 50 == firstSelection[0] + 50))
			{
				isMove = true;
				return true;	
			}
			else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "BK" && boardPieces[yValue][xValue].equals("") && (yValue * 50 == firstSelection[1] - 50 || yValue * 50 == firstSelection[1] + 50) && (xValue * 50 == firstSelection[0] - 50  || xValue * 50 == firstSelection[0] + 50))
			{
				isMove = true;
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	// isJump: this method checks if the coordinates of a second selection is a valid jump. This will check
	// if the piece is a king (which allows it to jump backwards), if the space is two spaces diagonal and is empty,
	// and if there if an opponent piece in between (we cannot just use the midpoint because the not all pieces can
	// jump forward and backwards.) 
	// It takes two parameters: the x coordinate and y coordinate.
	// It returns true if the coordinates of the board piece selected is a valid square to move to. 
	// It also sets isJump to true so the paintComponent can draw the jump.
	public static boolean isJump (int xValue, int yValue)
	{
		if (redTurn && boardPieces[yValue][xValue].equals(""))
		{
			if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("RK") && xValue * 50 == firstSelection[0] - 100 && yValue * 50 == firstSelection[1] + 100 && (boardPieces[yValue-1][xValue+1].equals("B") || boardPieces[yValue-1][xValue+1].equals("BK")))
			{
				isJump = true;
				return true;
			}
			if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("RK") && xValue * 50 == firstSelection[0] + 100 && yValue * 50 == firstSelection[1] + 100 && (boardPieces[yValue-1][xValue-1].equals("B") || boardPieces[yValue-1][xValue-1].equals("BK")))
			{
				isJump = true;
				return true;
			}
			else if (xValue * 50 == firstSelection[0] + 100 && yValue * 50 == firstSelection[1] - 100 && (boardPieces[yValue+1][xValue-1].equals("B") || boardPieces[yValue+1][xValue-1].equals("BK")))
			{
				isJump = true;
				return true;	
			}
			else if (xValue * 50 == firstSelection[0] - 100 && yValue * 50 == firstSelection[1] - 100 && (boardPieces[yValue+1][xValue+1].equals("B") || boardPieces[yValue+1][xValue+1].equals("BK")))
			{
				isJump = true;
				return true;
			}
			else
			{
				isJump = false;
				return false;
			}
		}
		else if (!redTurn && boardPieces[yValue][xValue].equals(""))
		{ 
			if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("BK") && xValue * 50 == firstSelection[0] + 100 && yValue * 50 == firstSelection[1] - 100 && (boardPieces[yValue+1][xValue-1].equals("R") || boardPieces[yValue+1][xValue-1].equals("RK")))
			{
				isJump = true;
				return true;	
			}
			else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("BK") && xValue * 50 ==firstSelection[0] - 100 && yValue * 50 == firstSelection[1] - 100 && (boardPieces[yValue+1][xValue+1].equals("R") || boardPieces[yValue+1][xValue+1].equals("RK")))
			{
				isJump = true;
				return true;
			}
			else if (xValue * 50 == firstSelection[0] - 100 && yValue * 50 == firstSelection[1] + 100 && (boardPieces[yValue-1][xValue+1].equals("R") || boardPieces[yValue-1][xValue+1].equals("RK")))
			{
				isJump = true;
				return true;
			}
			else if (xValue * 50 == firstSelection[0] + 100 && yValue * 50 == firstSelection[1] + 100 && (boardPieces[yValue-1][xValue-1].equals("R") || boardPieces[yValue-1][xValue-1].equals("RK")))
			{
				isJump = true;
				return true;
			}
			else
			{
				isJump = false;
				return false;
			}
		}
		else
		{
			isJump = false;
			return false;
		}
	}
	
	// checkForJumps: this method checks if there are any more possible jumps from the coordinates
	// of a piece after it has jumped. Each case where there is a jump is unique because we also want to ensure
	// that the jump is not beyond the game board (which would crash the program) but also the unique placement
	// of the opponent's piece in between and whether the piece is a king or not.
	// It takes two parameters: the x coordinate and y coordinate.
	// It returns true if there are possible jumps from those coordinates.
	public static boolean checkForJumps (int xValue, int yValue) 
	{
		xValue /= 50;
		yValue /= 50;
		
		if (redTurn)
		{
			if(yValue - 2 >= 0 && xValue + 2 <= 7 && boardPieces[yValue-2][xValue+2] == "" && (boardPieces[yValue-1][xValue+1] == "B" || boardPieces[yValue-1][xValue+1] == "BK"))
			{
				return true;
			}
			else if(yValue - 2 >= 0 && xValue - 2 >= 0 && boardPieces[yValue-2][xValue-2] == "" && (boardPieces[yValue-1][xValue-1] == "B" || boardPieces[yValue-1][xValue-1] == "BK"))
			{
				return true;
			}
			else if(yValue + 2 <= 7 && xValue + 2 <= 7 && boardPieces[yValue][xValue] == "RK" && boardPieces[yValue+2][xValue+2] == "" && (boardPieces[yValue+1][xValue+1] == "B" || boardPieces[yValue+1][xValue+1] == "BK"))
			{
				return true;
			}
			else if(yValue + 2 <= 7 && xValue - 2 >= 0 && boardPieces[yValue][xValue] == "RK" && boardPieces[yValue+2][xValue-2] == "" && (boardPieces[yValue+1][xValue-1] == "B" || boardPieces[yValue+1][xValue-1] == "BK"))
			{
				return true;
			}
		}
		else if (!redTurn)
		{
			if(yValue - 2 >= 0 && xValue + 2 <= 7 && boardPieces[yValue][xValue] == "BK" && boardPieces[yValue-2][xValue+2] == "" && (boardPieces[yValue-1][xValue+1] == "R" || boardPieces[yValue-1][xValue+1] == "RK"))
			{
				return true;
			}
			else if(yValue - 2 >= 0 && xValue - 2 >= 0 && boardPieces[yValue][xValue] == "BK" && boardPieces[yValue-2][xValue-2] == "" && (boardPieces[yValue-1][xValue-1] == "R" || boardPieces[yValue-1][xValue-1] == "RK") && yValue - 2 >= 0 && xValue + 2 >= 0)
			{
				return true;
			}
			else if(yValue + 2 <= 7 && xValue + 2 <= 7 && boardPieces[yValue+2][xValue+2] == "" && (boardPieces[yValue+1][xValue+1] == "R" || boardPieces[yValue+1][xValue+1] == "RK") && yValue - 2 <= 7 && xValue + 2 <= 7)
			{
				return true;
			}
			else if(yValue + 2 <= 7 && xValue - 2 >= 0 && boardPieces[yValue+2][xValue-2] == "" && (boardPieces[yValue+1][xValue-1] == "R" || boardPieces[yValue+1][xValue-1] == "RK") && yValue - 2 <= 7 && xValue + 2 >= 0)
			{
				return true;
			}
		}
		return false;
	}
	
	// isKing: this method checks if a piece is a king after a move/jump. It checks if a piece has reached
	// the opposite side of the board. It changes the piece to a king piece and updates the array.
	// It takes Graphics g to draw the new king piece.
	// It does not return anything (void).
	public void isKing (Graphics g)
	{
		if (secondSelection[1]/50 == 0 && boardPieces[secondSelection[1]/50][secondSelection[0]/50].equals("R"))
		{
			boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "RK";
			g.drawImage(fill, secondSelection[0], secondSelection[1], 50, 50, null);
			g.drawImage(redKing, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
		}
		else if (secondSelection[1]/50 == 7 && boardPieces[secondSelection[1]/50][secondSelection[0]/50].equals("B"))
		{
			boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "BK";
			g.drawImage(fill, secondSelection[0], secondSelection[1], 50, 50, null);
			g.drawImage(blackKing, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
		}
	}
	
	// isWinner: this method checks if all the red pieces or all the black pieces are captured OR 
	// if a forfeit is called. If all the red pieces are captured, black wins. If all the black pieces
	// are captured. If a forfeit is called, red loses if it is red's turn and vice versa for black. 
	// The stats will be updated (text file streaming).
	// This takes Graphics g to draw the end screen.
	// It does not return anything.
	public static void isWinner (Graphics g) throws IOException
	{	
		if (redCaptured == 12 || blackCaptured == 12 || forfeit == true)
		{
			Scanner inputFile = new Scanner(new File("res/checkersStats.txt"));
			int gamesPlayed = 0;
			int redWon = 0;
			int blackWon = 0;
			inputFile.nextLine();
			
			while(inputFile.hasNextLine())
			{
				String line = inputFile.nextLine();
				String[] lineElements = line.split(" ");
				gamesPlayed = Integer.parseInt(lineElements[1]);
				redWon = Integer.parseInt(lineElements[3]);
				blackWon = Integer.parseInt(lineElements[5]);
			}
			
			end = true;
			gamesPlayed++;
			
			if (redCaptured == 12)
			{
				blackWon++;
				g.drawImage(blackWin, 400, 10, null);
				g.drawImage(startNewGame, 0, 0, null);
			}
			else if (blackCaptured == 12)
			{
				redWon++;
				g.drawImage(redWin, 400, 10, null);
				g.drawImage(startNewGame, 0, 0, null);
			}
			else if (forfeit == true)
			{
				selectionFirst = false;
				selectionSecond = false;
				if (redTurn)
				{
					blackWon++;
					g.drawImage(blackWin, 400, 10, null);
					g.drawImage(startNewGame, 0, 0, null);
				}
				else if (!redTurn)
				{
					redWon++;
					g.drawImage(redWin, 400, 10, null);
					g.drawImage(startNewGame, 0, 0, null);
				}
			}
			inputFile.close();
			
			PrintWriter outputFile = new PrintWriter(new FileWriter("res/checkersStats.txt"));
			outputFile.println("Checkers Stats");
			outputFile.printf("GamesWon: %d redWon: %d blackWon: %d", gamesPlayed, redWon, blackWon);
			outputFile.close();
		}
	}
	// stats loads the statistics from the checkersStats.txt text file. 
	// It takes Graphics g to draw the numbers.
	// It does not return anything.
	public static void stats (Graphics g) throws IOException
	{
		Scanner inputFile = new Scanner(new File("res/checkersStats.txt"));
		int gamesPlayed = 0;
		int redWon = 0;
		int blackWon = 0;
		
		inputFile.nextLine();
		while(inputFile.hasNext())
		{
			String line = inputFile.nextLine();
			String[] lineElements = line.split(" ");
			gamesPlayed += Integer.parseInt(lineElements[1]);
			redWon += Integer.parseInt(lineElements[3]);
			blackWon += Integer.parseInt(lineElements[5]);
		}
		inputFile.close();
		g.setFont(new Font("Lucida Grande Regular", Font.PLAIN, 45));
		g.setColor(Color.white);
		
		g.drawString(String.valueOf(gamesPlayed), 133, 210);
		g.drawString(String.valueOf(redWon), 286, 210);
		g.drawString(String.valueOf(blackWon), 440, 210);
	}
	// resetStats resets the statistics so that the games won, red wins, and black wins are all 0 
	// in the checkersStats.txt text file.
	public static void resetStats () throws IOException
	{
		int gamesPlayed = 0;
		int redWon = 0;
		int blackWon = 0;
		PrintWriter outputFile = new PrintWriter(new FileWriter("res/checkersStats.txt"));
		
		outputFile.println("Checkers Stats");
		outputFile.printf("GamesWon: %d redWon: %d blackWon: %d", gamesPlayed, redWon, blackWon);
		outputFile.close();
	}
	
	// Mouse Listener Methods
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e)
	{
		int column = e.getX();
		int row = e.getY();
		
		if (mainMenu)
		{
			// Button to set turn to red's turn.
			if (column > 459 && column < 506 && row > 340 && row < 388)
			{
				if (!turnSelected)
				{
					turnSelected = true;
				}
				paintComponent(this.getGraphics());
			}
			// Button to set turn to black's turn.
			else if (column > 529 && column < 576 && row > 340 && row < 388)
			{
				if (turnSelected)
				{
					turnSelected = false;
				}
				paintComponent(this.getGraphics());
			}
			// Button to start game
			else if (column > 215 && column < 377 && row > 129 && row < 185)
			{
				mainMenu = false;
				game = true;
				paintComponent(this.getGraphics());
			}
			// Button to go to the instructions.
			else if (column > 215 && column < 377 && row > 190 && row < 246)
			{
				mainMenu = false;
				instructions = 1;
				paintComponent(this.getGraphics());
			}	
			// Button to go to stats.
			else if (column > 215 && column < 377 && row > 260 && row < 315)
			{
				mainMenu = false;
				stats = true;
				paintComponent(this.getGraphics());
			}	
		}
		else if (instructions != 0)
		{
			// Button to return to main menu.
			if (column > 18 && column < 65 && row > 18 && row < 65)
			{
				instructions = 0;
				mainMenu = true;
				paintComponent(this.getGraphics());
			}
			// Button to go the first page of instructions.
			else if (column > 28 && column < 211 && row > 100 && row < 157)
			{
				instructions = 1;
				paintComponent(this.getGraphics());
			}
			// Button to go the second page of instructions.
			else if (column > 213 && column < 395 && row > 100 && row < 157)
			{
				instructions = 2;
				paintComponent(this.getGraphics());
			}
			// Button to go the third page of instructions.
			else if (column > 397 && column < 579 && row > 100 && row < 157)
			{
				instructions = 3;
				paintComponent(this.getGraphics());
			}
		}
		else if (stats)
		{
			// Button to return to main menu.
			if (column > 18 && column < 65 && row > 18 && row < 65)
			{
				stats = false;
				mainMenu = true;
				paintComponent(this.getGraphics());
			}
			// Button to reset the stats.
			else if (column > 540 && column < 582 && row > 18 && row < 63)
			{
				try 
				{
					resetStats();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				paintComponent(this.getGraphics());
			}
			
		}
		else
		{
			// If the mouse click is on the side screen on the main game screen, the actual game board is 
			// not affected. 
			if (column >= 400 || row >= 400)
			{
				// If there is an option to make an additional jump, this button cancels the additional
				// jump.
				if (additionalJump && column > 488 && column < 512 && row > 223 && row < 247)
				{
					erase = true;
					paintComponent(this.getGraphics());
				}
				// Button to end the game and return to the main menu.
				else if (column > 450 && column < 560 && row > 268 && row < 317)
				{
					mainMenu = true;
					game = false;
					selectionFirst = false;
					paintComponent(this.getGraphics());		
				}
				// Button to start a new game.
				else if (column > 450 && column < 560 && row > 323 && row < 370)
				{
					newGame();
					paintComponent(this.getGraphics());
				}
				// If a person has no moves left (and thus loses), this button is to forfeit the match.
				else if (!end && !additionalJump && column > 465 && column < 535 && row > 223 && row < 243)
				{
					forfeit = true;
					paintComponent(this.getGraphics());
				}
			}
			// If selectionFirst is true, the next mouse click is captured as the piece that the player
			// wants to move. It will check if it is a valid first selection.
			else if (selectionFirst)
			{
				row /= 50; // turns coordinate into 0 to 7 for array
				column /= 50; // 0 to 7
				
				if (firstSelection(column, row))
				{
					row *= 50; // 0, 50, 100, etc. for coordinates of each square
					column *= 50; // 0, 50, 100, etc.
					firstSelection[0] = column;
					firstSelection[1] = row;
					makeFirst = true;
				}
				paintComponent(this.getGraphics());
			}
			// If selectionFirst is true, the next mouse click is captured as where the player
			// wants to move the first selection to. It will check if it is a valid selection to move/jump.
			// Otherwise, the pieces are reset and the player has to make a first selection again.
			else if (selectionSecond)
			{
				row /= 50; // 0 to 7
				column /= 50; // 0 to 7
				if ((redTurn && (boardPieces[row][column].equals("R") || boardPieces[row][column].equals("RK")) || (!redTurn && (boardPieces[row][column].equals("B") || boardPieces[row][column].equals("BK")))))
				{
					erase = true;
					paintComponent(this.getGraphics());
				}
				else if (isMove(column, row))
				{
					row *= 50; // 0, 50, 100, etc.
					column *= 50; // 0, 50, 100, etc.
					secondSelection[0] = column;
					secondSelection[1] = row;
					paintComponent(this.getGraphics());
				}
				else if (isJump(column, row))
				{
					row *= 50; // 0, 50, 100, etc.
					column *= 50; // 0, 50, 100, etc.
					secondSelection[0] = column;
					secondSelection[1] = row;
					paintComponent(this.getGraphics());
				}
				else
				{
					erase = true;
					paintComponent(this.getGraphics());
				}
			}
			// If additionalJump is true, the next mouse click will only be checked if its a valid jump
			// for an additional jump. 
			else if (additionalJump)
			{
				row /= 50; // 0 to 7
				column /= 50; // 0 to 7
				if (isJump(column, row))
				{
					row *= 50; // 0, 50, 100, etc.
					column *= 50; // 0, 50, 100, etc.
					secondSelection[0] = column;
					secondSelection[1] = row;
					paintComponent(this.getGraphics());
				}
			}
		}
	}
	
	// This is the paintComponent and it takes Graphics g. It does not return anything.
	public void paintComponent(Graphics g)
	{
		// If mainMenu is true, the mainMenu is drawn. It indicates who goes first in the bottom right.
		if (mainMenu) 
		{
			try
			{
				Image mainMenu = ImageIO.read(new File("res/mainScreen.png"));	
				g.drawImage(mainMenu, 0, 0, null);
			}
			catch (Exception e)
			{
				System.out.println("Image Not Found");
			}
			
			if (turnSelected)
			{
				g.drawImage(redSelected, 460, 340, 50, 50, null);
				g.drawImage(blackDeselected, 525, 340, 50, 50, null);
			}
			else
			{
				g.drawImage(redDeselected, 460, 340, 50, 50, null);
				g.drawImage(blackSelected, 525, 340, 50, 50, null);
			}
			
		}
		// If instructions does not equal 0 (false), it will draw the appropriate instruction screen page.
		else if (instructions != 0)
		{
			try
			{
				if (instructions == 1)
				{
					Image instructions1 = ImageIO.read(new File("res/instructions1.png"));
					g.drawImage(instructions1, 0, 0, null);
				}
				else if (instructions == 2)
				{
					Image instructions2 = ImageIO.read(new File("res/instructions2.png"));
					g.drawImage(instructions2, 0, 0, null);
				}
				else if (instructions == 3)
				{
					Image instructions3 = ImageIO.read(new File("res/instructions3.png"));
					g.drawImage(instructions3, 0, 0, null);
				}
			}
			catch (Exception e)
			{
				System.out.println("Image Not Found");
			}
		}
		// If stats is true, the stats screen is drawn.
		else if (stats)
		{
			try
			{
				Image statsScreen = ImageIO.read(new File("res/stats.png"));
				g.drawImage(statsScreen, 0, 0, null);
				
			}
			catch (Exception e)
			{
				System.out.println("Image Not Found");
			}
			
			try 
			{
				stats(g);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		// If forfeit is true, in the main game screen, the game will end.
		else if (forfeit)
		{
			try 
			{
				isWinner(g);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			forfeit = false;
		}
		// If game is true, the main game screen is drawn.
		else if (game)
		{
			makeBoard(g);
			game = false;
		}
		// If erase is true, the indicators of the first selection piece will be erased. If erase is true
		// because an additional jump was cancelled, it will set the turn to the other player and draw
		// the appropriate message on the middle of the side screen.
		else if (erase)
		{
			g.drawImage(fill, firstSelection[0], firstSelection[1], 50, 50, null);
			if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("B"))
			{
				g.drawImage(blackPiece, firstSelection[0]+5, firstSelection[1]+5, 40, 40, null);

			}
			else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("BK"))
			{
				g.drawImage(blackKing, firstSelection[0]+5, firstSelection[1]+5, 40, 40, null);
			}
			else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("R"))
			{
				g.drawImage(redPiece, firstSelection[0]+5, firstSelection[1]+5, 40, 40, null);
			}
			else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50].equals("RK"))
			{
				g.drawImage(redKing, firstSelection[0]+5, firstSelection[1]+5, 40, 40, null);
			}
			
			if (additionalJump)
			{
				if (redTurn)
				{
					g.drawImage(blacksTurn, 400, 0, null);
					redTurn = false;
				}
				else
				{
					g.drawImage(redsTurn, 400, 0, null);
					redTurn = true;
				}
				additionalJump = false;
			}
			
			selectionFirst = true;
			selectionSecond = false;
			erase = false;
			
		}
		// If makeFirst is true, white indicators will be drawn on the piece selected, indicating that it is
		// the piece that the player is moving. It will set selectionFirst to false and selectionSecond to true
		// so that the mouse listener captures the next click as the second selection (jump or move).
		else if (makeFirst)
		{
			g.setColor(Color.white);
			g.drawLine(firstSelection[0] + 5, firstSelection[1] + 5, firstSelection[0] + 5, firstSelection[1] + 15);
			g.drawLine(firstSelection[0] + 5, firstSelection[1] + 5, firstSelection[0] + 15, firstSelection[1] + 5);
			g.drawLine(firstSelection[0] + 35, firstSelection[1] + 45, firstSelection[0] + 45, firstSelection[1] + 45);
			g.drawLine(firstSelection[0] + 45, firstSelection[1] + 45, firstSelection[0] + 45, firstSelection[1] + 35);
			makeFirst = false;
			selectionFirst = false;
			selectionSecond = true;
		}
		/* If isMove is true, the firstSelection piece is moved to the secondSelection. It will be checked if it is 
		   a king piece (drawn accordingly) or will become a king piece. The array will also be updated.
		   After this, it will update the message on the side screen to indicate and make it so that 
		   it is the other player's turn. */
		else if (isMove)
		{
			g.drawImage(fill, firstSelection[0], firstSelection[1], 50, 50, null);
			if(redTurn)
			{
				if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "R")
				{
					g.drawImage(redPiece, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "R";
				}
				else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "RK")
				{
					g.drawImage(redKing, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "RK";
				}
				isKing(g);
				redTurn = false;
				g.drawImage(blacksTurn, 400, 0, null);
			}
			else
			{
				if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "B")
				{
					g.drawImage(blackPiece, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "B";
				}
				else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "BK")
				{
					g.drawImage(blackKing, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "BK";
				}
				isKing(g);
				redTurn = true;
				g.drawImage(redsTurn, 400, 0, null);
			}
			boardPieces[firstSelection[1]/50][firstSelection[0]/50] = "";
			selectionFirst = true;
			selectionSecond = false;
			isMove = false;
		}
		/* If isJump is true, the firstSelection will jump to the secondSelection and capture the piece in between.
		   It will be checked if it is a king piece (drawn accordingly) or will become a king piece. The array will 
		   also be updated. The pieces captured will be updated on the board on the side screen and the program
		   will check if there is a winner (if all pieces of a player are captured). If not, additional jumps will be
		   checked. If there are, the next mouse click will be checked until it is a valid additional jump.
		   If there are none, it makes it the other player's turn and updates the side screen. */
		else if (isJump)
		{
			g.drawImage(fill, firstSelection[0], firstSelection[1], 50, 50, null);
			g.drawImage(fill, (secondSelection[0]+firstSelection[0])/2, (secondSelection[1]+firstSelection[1])/2, 50, 50, null);
			boardPieces[(secondSelection[1]+firstSelection[1])/100][(secondSelection[0]+firstSelection[0])/100] = "";
			g.drawImage(fill, secondSelection[0], secondSelection[1], 50, 50, null);
			
			if (redTurn)
			{
				if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "R")
				{
					g.drawImage(redPiece, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "R";	
				}
				else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "RK")
				{
					g.drawImage(redKing, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "RK";	
				}
				boardPieces[firstSelection[1]/50][firstSelection[0]/50] = "";
				blackCaptured++;
			}
			else if (!redTurn)
			{
				if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "B")
				{
					g.drawImage(blackPiece, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "B";	
				}
				else if (boardPieces[firstSelection[1]/50][firstSelection[0]/50] == "BK")
				{
					g.drawImage(blackKing, secondSelection[0]+5, secondSelection[1]+5, 40, 40, null);
					boardPieces[secondSelection[1]/50][secondSelection[0]/50] = "BK";	
				}
				boardPieces[firstSelection[1]/50][firstSelection[0]/50] = "";
				redCaptured++;
			}
			additionalJump = false;
			selectionSecond = false;
			isJump = false;
			isKing(g);
			
			g.drawImage(score, 400, 0, null);
			g.setFont(new Font("Lucida Grande Regular", Font.PLAIN, 35));
			g.setColor(Color.black);
			g.drawString(String.valueOf(blackCaptured), 521, 125);
			g.setColor(Color.red);
			g.drawString(String.valueOf(redCaptured), 521, 70);
			
			try 
			{
				isWinner(g);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			// If end is false, it means that a player has not won yet.
			if(!end)
			{
				// If checkForJumps returns true, there are additional jumps and the piece is selected.
				// additionalJump is set to true so that the mouse listener only checks for jumps from that
				// selected piece.
				if (checkForJumps(secondSelection[0], secondSelection[1]))
				{
					if (redTurn)
					{
						g.drawImage(redMove, 400, 5, null);
					}
					else if (!redTurn)
					{
						g.drawImage(blackMove, 400, 5, null);
					}
					additionalJump = true;
					firstSelection[0] = secondSelection[0];
					firstSelection[1] = secondSelection[1];
					g.setColor(Color.white);
					g.drawLine(firstSelection[0] + 5, firstSelection[1] + 5, firstSelection[0] + 5, firstSelection[1] + 15);
					g.drawLine(firstSelection[0] + 5, firstSelection[1] + 5, firstSelection[0] + 15, firstSelection[1] + 5);
					g.drawLine(firstSelection[0] + 35, firstSelection[1] + 45, firstSelection[0] + 45, firstSelection[1] + 45);
					g.drawLine(firstSelection[0] + 45, firstSelection[1] + 45, firstSelection[0] + 45, firstSelection[1] + 35);
				}
				// Otherwise, the turn ends.
				else
				{
					selectionFirst = true;
					if (redTurn)
					{
						g.drawImage(blacksTurn, 400, 0, null);
						redTurn = false;
					}
					else
					{
						g.drawImage(redsTurn, 400, 0, null);
						redTurn = true;
					}
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		// Create a frame
		JFrame myFrame = new JFrame("Checkers");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a panel to put inside the frame
		Checkers myPanel = new Checkers(); 
		myFrame.add(myPanel);
		
		// Maximize your frame to the size of the panel
		myFrame.pack();
		
		// Set the visibility of the frame to visible
		myFrame.setVisible(true);
	   
	}
}
