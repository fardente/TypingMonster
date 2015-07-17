import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

import java.util.ArrayList;


public class ManiacInput implements KeyListener{
	
	private String _combinedTypedChars; 
	private ArrayList<ManiacInputListener> _listeners;
		
	public ManiacInput() 
	{
		_combinedTypedChars = new String();
		_listeners = new ArrayList<ManiacInputListener>();
	}

	public void addListener(ManiacInputListener listener) 
	{
		_listeners.add(listener);
	}
	
	public void submitCharacters()
	{	
		for (ManiacInputListener maniacInputListener : _listeners) 
		{
			maniacInputListener.RemoveRequest(_combinedTypedChars);
		}
		_combinedTypedChars = "";
	}
	
	public void typedChars(Character chcaracter)
	{
		_combinedTypedChars = _combinedTypedChars + chcaracter;
		
		for (ManiacInputListener maniacInputListener : _listeners) 
		{
			maniacInputListener.CharRequest(_combinedTypedChars);
		}
	}
	
	public void removeCharacter()
	{
		if(_combinedTypedChars.length() != 0)
		{
			_combinedTypedChars = _combinedTypedChars.substring(0, _combinedTypedChars.length()-1);
			
			for (ManiacInputListener maniacInputListener : _listeners) 
			{
				maniacInputListener.CharRequest(_combinedTypedChars);
			}	
		}
	}
	
	public void requestPause()
	{
		for (ManiacInputListener maniacInputListener : _listeners) 
		{
			maniacInputListener.PauseRequest();
		}
	}

	/*Implemented interface of KeyListener*/
	@Override
	public void inputEnded() {
		
	}

	@Override
	public void inputStarted() {
		
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input arg0) {
		
	}

	@Override
	public void keyPressed(int charCode, char character) {
		
		if(Character.isLetter(character))
			typedChars(character);
		
		if(charCode == Input.KEY_ENTER)
			submitCharacters();
		
		if(charCode == Input.KEY_BACK)
			removeCharacter();
		
		if(charCode == Input.KEY_ESCAPE)
			requestPause();
	}

	@Override
	public void keyReleased(int arg0, char arg1) {
		
	}
}
