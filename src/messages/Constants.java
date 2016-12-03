package messages;

import java.awt.Point;

public class Constants {
	
	public static final char LEFTMOUSEDOWN = 'a';
	public static final char LEFTMOUSEUP = 'b';
	
	public static final char RIGHTMOUSEDOWN = 'c';
	public static final char RIGHTMOUSEUP = 'd';
	
	public static final char LEFTCLICK = 'e';
	
	public static final char SCROLLUP = 'h';
	public static final char SCROLLDOWN = 'i';
	
	public static final char KEYBOARD = 'k';
	public static final char KEYCODE = 'l';
	
	public static final char ARROW = 'm';
	public static final char BRUSH = 'n';
	public static final char ERASER = 'o';

	public static final char BEFOREPAGE = 'q';
	public static final char NEXTPAGE = 'r';
	
	public static final char DELIMITER = '/';
	
	public static final char MOVEMOUSE = 'p';
	
	public static final char REQUESTIMAGE = 'I';
	public static final char PROVIDEIMAGE = 'P';
	
	/*
	 *  Returns a string in the format that can be laster parsed
	 *  format: MOVEMOUSEintxDELIMITERinty
	 *  ex: 	p5/6
	 */
	public static String createMoveMouseMessage(float x, float y){
		int intx = Math.round(x);
		int inty = Math.round(y);
		return ""+MOVEMOUSE + intx + DELIMITER + inty;
	}
	
	public static Point parseMoveMouseMessage(String message){
		String[] tokens = message.substring(1).split(""+Constants.DELIMITER );
		return new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
	}
}
