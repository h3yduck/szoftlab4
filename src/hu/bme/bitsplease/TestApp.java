package hu.bme.bitsplease;

import hu.bme.bitsplease.gameEngine.GameEngine;

import java.io.IOException;

public class TestApp {

	public static GameEngine gameEngine;
	
	public static void main(String[] args) {
		gameEngine = new GameEngine();
    	//while(true){
	    	try{
	    		System.out.println((-360.5151654165415213411) % 360);
	    		throw new IOException();
	    	}
	    	catch(IOException ex){
	    		System.out.println(ex.getMessage());
	    	}
    	//}

	}

}
