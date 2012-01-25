package pl.krakow.vlo.simplechecker;

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

	private File findFileToCompile(File root)
	{
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
		return toCompile[0];
	}

	private String generateCommand(File what, File output)
	{
		return getCompilerCommand() + " " + getCompilerFlags() + " "
				+ what.getAbsolutePath() + " " + getOutputSwitch() + " "
				+ output.getAbsolutePath();
	}

	private void printProcessOutput(Process commpilation)
	{
		StringBuilder builder = new StringBuilder();
		try (BufferedReader input = new BufferedReader(new InputStreamReader(
				commpilation.getInputStream())))
		{
			while(input.ready())
				builder.append(input.readLine());
			if(commpilation.exitValue() != 0)
				throw new IllegalStateException("\n" + builder.toString());
			log.info(builder.toString());
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "Wyjątek", e);
		}
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
		File what = findFileToCompile(root);
		log.log(Level.INFO, "Przygotowania do kompilacji pliku {}",
				what.getName());
		String command = generateCommand(what, output);
		log.info(command);
		try
		{
			Process commpilation = Runtime.getRuntime().exec(command);
			commpilation.waitFor();
			printProcessOutput(commpilation);
		}
		catch(IOException e)
		{
			log.log(Level.SEVERE, "Kompilacja się nie powiodła", e);
			throw new IllegalStateException(e);
		}
		catch(InterruptedException e)
		{
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "Wyjątek", e);
		}
		log.log(Level.INFO, "skompilowałem plik {} do pliku {} ", new Object[] {
				what, output });
		return output;
	}

	protected String getCompilerFlags()
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
