package pl.krakow.vlo.simplechecker;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ComparerTest
{
	String		a;
	String		b;

	Reader		input;
	Reader		correct;

	Comparer	comparer;

	@Before
	public void setUp() throws Exception
	{
		a = "abc";
		b = "abc";
		input = new StringReader(a);
		correct = new StringReader(b);
		comparer = new Comparer();
	}

	@Test
	public void twoIdenticalStreamAreOk() throws IOException
	{
		assertTrue(comparer.compare(input, correct));
	}

	@Test
	public void twoDiffrentStreamsAreBad() throws Exception
	{

		input = new StringReader(a.replace('b', 'c'));
		assertFalse(comparer.compare(input, correct));
	}

	@Test
	public void whiteSpaceDosentMatter() throws Exception
	{
		input = new StringReader(a.replace("ab", "ab  "));
		assertTrue(comparer.compare(input, correct));
	}
}
