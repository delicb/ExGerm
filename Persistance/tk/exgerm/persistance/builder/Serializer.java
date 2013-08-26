package tk.exgerm.persistance.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import tk.exgerm.core.model.IGraph;

/**
 * Klasa koja generiše fajl sa grafom. Deo je Builder obrasca (Director) i može
 * da generiše bilo kakvu reprezentaciju grafa.
 * 
 * @author Tim 2
 */
public class Serializer {

	/**
	 * Čuva prosleđeni graf u zadati file pomoću zadatog buildera.
	 * 
	 * @param graph
	 *            Graf koji se čuva
	 * @param builder
	 *            Builder koji se koristi za pravljenje stringovske
	 *            reprezentacija grafa
	 * @param f
	 *            File u koji se snima
	 */
	public void buildDocument(IGraph graph, Builder builder, File f) {
		builder.buildGraph(graph);
		try {
			PrintStream out = new PrintStream(f);
			out.print(builder.getResult());
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
