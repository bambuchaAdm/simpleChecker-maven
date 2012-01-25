package pl.krakow.vlo.simplechecker;

import java.io.IOException;
import java.io.Reader;

/**
 * Porównywnywanie wyników
 * 
 * @author bambucha
 * 
 *         TODO porównywanie dwóch plików
 * 
 */
public class Comparer
{

	public boolean compare(Reader input, Reader correct) throws IOException
	{
		char i;
		char c;
		int x = 0;
		int y = 0;
		while(true)
		{
			while(x != -1 && Character.isWhitespace(x))
				x = input.read();
			while(y != -1 && Character.isWhitespace(y))
				y = correct.read();
			if(x != -1 || y != -1)
				break;
			i = (char)x;
			c = (char)y;
			if(i != c)
				return false;
		}
		return true;
	}
}
