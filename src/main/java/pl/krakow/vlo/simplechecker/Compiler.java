package pl.krakow.vlo.simplechecker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa kompilująca testowany program przy pomocy gcc używając -O2 -static -lm
 * 
 * @author bambucha
 * 
 * 
 */
public class Compiler
{
	private static final Logger	log	= Logger.getLogger(Compiler.class.getName());

	private File				root;

	public Compiler(File root)
	{
		super();
		this.root = root;
	}

	/**
	 * Kompluje pliki przy pomocy
	 * 
	 * @param what
	 *            plik źródłowy
	 * @return binarka
	 */
	public File compile()
	{
		File output = new File(root, "out");
		File[] toCompile = root.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".cpp");
			}
		});
		if(toCompile.length > 1 || toCompile.length == 0)
			throw new IllegalStateException(
					"Dwa lub więcej || nie ma plików plików *.cpp. Jestem głupi i nie wiem co robić :(");
		File what = toCompile[0];
		log.log(Level.INFO,
				"Przygotowania do kompilacji pliku " + what.getName());
		String command = getCompilerCommand() + " " + getCompilerFlags() + " "
				+ what.getAbsolutePath() + " " + getOutputSwitch() + " "
				+ output.getAbsolutePath();
		log.info(command);
		BufferedReader input = null;
		try
		{
			Process commpilation = Runtime.getRuntime().exec(command);
			commpilation.waitFor();
			StringBuilder builder = new StringBuilder();
			input = new BufferedReader(new InputStreamReader(
					commpilation.getInputStream()));
			while(input.ready())
				builder.append(input.readLine());
			if(commpilation.exitValue() != 0)
				throw new IllegalStateException("\n" + builder.toString());
		}
		catch(IOException e)
		{
			log.log(Level.SEVERE, "Kompilacja się nie powiodła", e);
		}
		catch(InterruptedException e)
		{
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "Wyjątek", e);
		}
		finally
		{
			try
			{
				if(input != null)
					input.close();
			}
			catch(IOException e)
			{
				// TODO Auto-generated catch block
				log.log(Level.SEVERE, "Wyjątek", e);
			}
		}
		log.log(Level.INFO, "skompilowałem plik " + what.toString()
				+ " do pliku " + output.getAbsolutePath());
		return output;
	}

	private String getCompilerFlags()
	{
		return "-O2 -static -lm";
	}

	protected String getCompilerCommand()
	{
		return "g++";
	}

	protected String getOutputSwitch()
	{
		return "-o";
	}

}
