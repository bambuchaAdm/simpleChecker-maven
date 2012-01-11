package pl.krakow.vlo.simplechecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Główna klasa programu
 * 
 * @author bambucha
 * 
 *         TODO Czyszczenie po testach TODO Testowanie aplikacji
 * 
 */
public class Tester
{
	private File		root;
	private File		binary;
	private File		results;
	private File		tests;
	private Compiler	compiler;
	private TestLoader	loader;
	private ResultSaver	saver;

	public Tester(File root, Compiler compiler)
	{
		this.root = root;
		this.results = new File(root, "results");
		this.tests = new File(root, "tests");
		this.compiler = compiler;
		this.binary = compiler.compile();
	}

	private Process getProcess() throws IOException
	{
		ProcessBuilder builder = new ProcessBuilder(binary.getAbsolutePath());
		return builder.start();
	}

	private void writeTest(Process task, File testCase)
	{
		InputStream in = null;
		try
		{
			in = new FileInputStream(testCase);
		}
		catch(FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			Logger.getAnonymousLogger().log(Level.SEVERE, "Wyjątek", e1);
		}
		OutputStream out = task.getOutputStream();
		try
		{
			rewrite(in, out);
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			Logger.getAnonymousLogger().log(Level.SEVERE, "Wyjątek", e);
		}
	}

	public void testFile(File testCase) throws IOException
	{
		Process task = getProcess();
		writeTest(task, testCase);
		saveResult(task, testCase);
	}

	private void saveResult(Process task, File testCase)
			throws FileNotFoundException
	{

		File outputFile = new File(results, testCase.getName());
		OutputStream output = new FileOutputStream(outputFile);
		InputStream input = task.getInputStream();
		try
		{
			rewrite(input, output);
		}
		catch(FileNotFoundException e)
		{
			Logger.getAnonymousLogger().log(Level.SEVERE,
					"Nie mamy wyjścia...", e);
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			Logger.getAnonymousLogger().log(Level.SEVERE, "Wyjątek", e);
		}
	}

	private void rewrite(InputStream in, OutputStream out) throws IOException
	{
		int buff = 0;
		while((buff = in.read()) != -1)
			out.write(buff);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File currentDirectory = new File(System.getProperty("user.dir"));
		Compiler compiler = new Compiler(currentDirectory);
		Tester tester = new Tester(currentDirectory, compiler);
	}
}
