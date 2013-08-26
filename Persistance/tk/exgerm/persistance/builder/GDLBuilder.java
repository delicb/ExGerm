package tk.exgerm.persistance.builder;

import java.util.Map;

import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;

public class GDLBuilder implements Builder {

	private IGraph graphToBuild;

	public GDLBuilder() {

	}

	@Override
	public void buildGraph(IGraph graph) {
		this.graphToBuild = graph;
	}

	@Override
	public String getResult() {
		// TODO: Dodati neki komentar na početku, kako je ovo generisan file
		// i da ga ne treba ručno menjati... bla, bla, bla... 
		return serializeGraph(graphToBuild, 0);
	}

	private String serializeGraph(IGraph graph, int indentLevel) {
		StringBuffer rez = new StringBuffer();
		String indent = indent(indentLevel);
		rez.append("\n" + indent + "graph ");
		rez.append(sanitize(graph.getName()) + " ");
		rez.append("{\n");
		rez.append(serializeAttributes(graph.getAllAttributes(),
				indentLevel + 1));

		for (INode n : graph.getAllNodes()) {
			if (n instanceof IGraph) {
				rez.append(serializeGraph((IGraph) n, indentLevel + 1));
			} else
				rez.append(serializeNode(n, indentLevel + 1));
		}

		for (IEdge e : graph.getAllEdges()) {
			rez.append(serializeEdge(e, indentLevel + 1));
		}

		rez.append(indent + "}\n");
		return rez.toString();
	}

	private StringBuffer serializeAttributes(Map<String, Object> attributes,
			int indentLevel) {
		StringBuffer rez = new StringBuffer();
		String indent = indent(indentLevel);
		if (attributes.size() > 0) {
			rez.append("\n" + indent + "[\n");

			rez.append(serializeAttributeList(attributes, indentLevel + 1));

			rez.append(indent);
			rez.append("]");
		}
		return rez;
	}

	private StringBuffer serializeAttributeList(Map<String, Object> attributes,
			int indentLevel) {
		StringBuffer rez = new StringBuffer();
		String indent = indent(indentLevel);

		int attCount = attributes.size();
		int counter = 0;
		for (String key : attributes.keySet()) {
			counter++;
			rez.append(indent);
			rez.append(sanitize(key));
			rez.append("=");
			rez.append(sanitize(attributes.get(key)));
			if (counter != attCount)
				rez.append(",");
			rez.append("\n");
		}

		return rez;
	}

	private StringBuffer serializeNode(INode node, int indentLevel) {
		StringBuffer rez = new StringBuffer();
		String indent = indent(indentLevel);

		rez.append("\n" + indent + "node " + sanitize(node.getName()));
		Map<String, Object> allAttrubutes = node.getAllAttributes();
		if (allAttrubutes.size() > 0)
			rez
					.append(serializeAttributes(node.getAllAttributes(),
							indentLevel));
		rez.append(";\n");

		return rez;
	}

	private StringBuffer serializeEdge(IEdge edge, int indentLevel) {
		StringBuffer rez = new StringBuffer();
		String indent = indent(indentLevel);

		rez.append("\n" + indent + "edge " + sanitize(edge.getFrom().getName())
				+ (edge.isDirected() ? " -> " : " -- ")
				+ sanitize(edge.getTo().getName()));
		Map<String, Object> allAttributes = edge.getAllAttributes();
		if (allAttributes.size() > 0) {
			rez.append(serializeAttributes(allAttributes, indentLevel + 1));
		}
		rez.append(";\n");
		return rez;
	}

	private String indent(int level) {
		StringBuffer rez = new StringBuffer();
		for (int i = 0; i < level; i++) {
			rez.append("\t");
		}
		return rez.toString();
	}

	private String sanitize(Object o) {
		String rez = null;
		if (o instanceof String) {
			rez = (String) o;
			if (rez.indexOf(' ') != -1 || rez.indexOf('"') != -1) {
				rez = rez.replaceAll("\\\"", "\\\\\"");
				rez = "\"" + rez + "\"";
			}
		}
		return rez;
	}
}
