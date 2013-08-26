package tk.exgerm.console.listeners;

import tk.exgerm.console.parser.Parser;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.IListener;

public class GraphOrNodeRemovedListener implements IListener {

	private Parser parser;

	public GraphOrNodeRemovedListener(Parser parser) {
		this.parser = parser;
	}

	@Override
	public void raise(String event, Object... parameters) {
		if (event.equals(ExGGraphRegister.GRAPH_REMOVED)) {
			IGraph graph = (IGraph) parameters[0];
			if (parser.getActiveGraph() != null
					&& parser.getActiveGraph() == graph) {
				parser.setActiveGraph(null);
				parser.forceNewPrompt();
			}
		} else if (event.equals(IGraph.GRAPH_NODE_REMOVED)) {
			INode node = (INode) parameters[1];

			// ako je obrisan bas taj node koji je u konzoli aktivan
			if (parser.getActiveNode() == node) {
				if (parser.getActiveGraph() == node.getGraph())
					parser.setActiveNode(null);
				else
					parser.setActiveNode(node.getGraph());
				parser.forceNewPrompt();
			} else if (IGraph.class.isInstance(node)
					&& parser.getActiveNode() != null
					&& parser.getActiveNode().isChildOf((IGraph) node)) {
				if (parser.getActiveGraph() == node.getGraph())
					parser.setActiveNode(null);
				else
					parser.setActiveNode(node.getGraph());
				parser.forceNewPrompt();
			}
		}
	}
}
