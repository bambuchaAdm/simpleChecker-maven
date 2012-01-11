package pl.krakow.vlo.simplechecker;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Ładuje testy do wykonania
 * 
 * @author bambucha
 * 
 */
public class TestLoader
{
	private File	root;

	/**
	 * Tworzy nowy ładowacz testów
	 * 
	 * @param root
	 *            główny katalog
	 */
	public TestLoader(File root)
	{
		this.root = root;
	}

	/**
	 * Zwraca listę plików do wejścia
	 * 
	 * @return lista plików na wejście
	 */
	public File[] getTest()
	{
		File inDir = new File(root, "in");
		return inDir.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".in");
			}
		});
	}

}
