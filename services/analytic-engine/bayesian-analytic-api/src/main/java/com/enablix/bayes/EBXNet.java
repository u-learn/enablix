package com.enablix.bayes;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import lombok.NonNull;
import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.Streamer;


/**
 * 
 * The main design concern was to make as much of the network immutable as
 * possible, but make it easy to swap CPT tables to recompile
 *
 */
public class EBXNet {

	public static class States {
		public static final String TRUE = "true";
		public static final String FALSE = "false";
		public static final String[] BOOLS = { TRUE, FALSE };
		
		public static final String HIGH = "high";
		public static final String LOW = "low";
		public static final String NO = "no";
		public static final String[] HIGH_LOW_STATES = {
				HIGH, LOW, NO
		};
	}
		
	private static Environ env;

	static {
		try {
			Node.setConstructorClass(Node.class.getCanonicalName());
			env = new Environ(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	final Map<String, EBXNode> nodes = new HashMap<>();
	final Net net;
	// TODO: Immutable?

	private EBXBoolNode target;

	class EdgeKey {
		// Node names
		public final String from;
		public final String to;

		public EdgeKey(@NonNull String from, @NonNull String to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof EdgeKey) {
				EdgeKey key = (EdgeKey) obj;
				return key.from.equals(this.from) && key.to.equals(this.to);
			}
			return super.equals(obj);
		}

		@Override
		public String toString() {
			return from + "-->" + to;
		}
	}

	final Map<EdgeKey, EBXLink> edges = new HashMap<>();

	public EBXLink getEdge(String fromName, String toName) {
		return edges.get(new EdgeKey(fromName, toName));
	}

	public Net getNet() {
		return net;
	}

	public EBXNet() {
		try {
			net = new Net();
		} catch (NeticaException e) {
			throw new RuntimeException(e);
		}
	}

	public EBXLink link(final EBXNode from, final EBXNode to) {
		EBXLink link = new EBXLink(from, to);
		EdgeKey key = new EdgeKey(from.getName(), to.getName());
		edges.put(key, link);
		return link;
	}

	public EBXNet chainLink(final EBXNode from, final EBXNode to) {
		link(from, to);
		return this;
	}

	public class EBXLink {

		final EBXNode from;
		final EBXNode to;
		Object[][] cpt;

		public EBXLink(final EBXNode from, final EBXNode to) {
			this.from = from;
			this.to = to;
			try {
				to.getNode().addLink(from.getNode());
			} catch (NeticaException e) {
				throw new RuntimeException(e);
			}
		}

		public String toString() {
			return from.getTitle() + "-->" + to.getTitle();
		}
	}

	public class EBXBoolNode extends EBXCategoricalNode {

		public EBXBoolNode(final String name, final String title) {
			super(name, title, States.BOOLS);
		}

		public float getProbability() {
			try {
				return n.getBelief(States.TRUE);
			} catch (NeticaException e) {
				throw new RuntimeException(e);
			}
		}

		public EBXBoolNode setPriors(float trueProbability) {
			float[] probabilities = { trueProbability, 1 - trueProbability };
			try {
				n.setCPTable(probabilities);
			} catch (NeticaException e) {
				throw new RuntimeException(e);
			}
			return this;
		}
	}
	
	public class EBXHighLowNode extends EBXCategoricalNode {
		
		public EBXHighLowNode(final String name, final String title) {
			super(name, title, States.HIGH_LOW_STATES);
		}
		
	}

	public EBXBoolNode createBoolNode(String name, String title) {
		EBXBoolNode node = new EBXBoolNode(name, title);
		nodes.put(name, node);
		return node;
	}
	
	public EBXHighLowNode createHighLowNode(String name, String title) {
		EBXHighLowNode node = new EBXHighLowNode(name, title);
		nodes.put(name, node);
		return node;
	}

	static String join(Object[] arr) {
		return arrayToCommaDelimitedString(arr);
	}

	public class EBXCategoricalNode implements EBXNode {

		final String name;
		final String title;
		final Node n;
		// states should probably be immutable
		final Set<String> states;

		public EBXCategoricalNode(final String name, final String title, final String[] states) {
			try {
				this.states = Sets.newHashSet(states);
				this.name = name;
				this.title = title;
				this.n = new Node(name, join(states), net);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public String getTitle() {
			return title;
		}

		public String getName() {
			return name;
		}

		public Node getNode() {
			return n;
		}

		/**
		 * @param The "to" this node
		 */
		@Override
		public void linkTo(EBXNode node) {
			link(this, node);
		}

		@Override
		public void linkFrom(EBXNode node) {
			link(node, this);
		}

		@Override
		public String toString() {
			String dne = asDNE();
			int start = dne.indexOf("node " + getName() + " {");
			String chunk1 = dne.substring(start);
			int end = chunk1.indexOf('}');
			return chunk1.substring(0, end + 1);
		}

		@Override
		public void setCPTable(float[] props) {
			try {
				getNode().setCPTable(props);
			} catch (NeticaException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public float getBelief(String state) {
			try {
				return n.getBelief(state);
			} catch (NeticaException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Collection<String> getStates() {
			return states;
		}

	}

	public EBXNet compile() throws Exception {
		net.compile();
		return this;
	}

	public EBXNet target(EBXBoolNode target) {
		this.target = target;
		return this;
	}

	public EBXBoolNode target() {
		return target;
	}

	public float getProbability() {
		return target.getProbability();
	}

	/**
	 * More opaque reference for inner class usage
	 * 
	 * @return
	 */
	private String asDNE() {
		return this.toString();
	}

	/**
	 * Returns DNE representation of network
	 */
	@Override
	public String toString() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			Streamer steamer = new Streamer(output, "EBXNet.dne", env);
			net.write(steamer);
			return output.toString("UTF8");
		} catch (NeticaException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

	}

	public Collection<String> getNodeNames() {
		return nodes.keySet();
	}
	
	public Collection<EBXNode> getNodes() {
		return nodes.values();
	}

}
