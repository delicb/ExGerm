package tk.exgerm.core.impl.service;

import java.util.List;
import java.util.Map;

import tk.exgerm.core.Core;
import tk.exgerm.core.config.Configuration;
import tk.exgerm.core.event.EventDispatcher;
import tk.exgerm.core.exception.ExGCommandAlreadyExistException;
import tk.exgerm.core.exception.ExGGraphDoesNotExsistException;
import tk.exgerm.core.exception.ExGGraphExsistException;
import tk.exgerm.core.exception.ExGIteratorAlreadyExsistException;
import tk.exgerm.core.gui.ActionManager;
import tk.exgerm.core.gui.StatusBar;
import tk.exgerm.core.gui.tabmanager.TabManager;
import tk.exgerm.core.model.IEdge;
import tk.exgerm.core.model.IGraph;
import tk.exgerm.core.model.INode;
import tk.exgerm.core.plugin.ExGAction;
import tk.exgerm.core.plugin.ExGCommand;
import tk.exgerm.core.plugin.ExGConfigPanel;
import tk.exgerm.core.plugin.ExGGraphRegister;
import tk.exgerm.core.plugin.ExGGraphicalComponent;
import tk.exgerm.core.plugin.ExGHelp;
import tk.exgerm.core.plugin.ExGIterator;
import tk.exgerm.core.plugin.ExGStatusbarContent;
import tk.exgerm.core.plugin.ExGSubmenu;
import tk.exgerm.core.plugin.IListener;
import tk.exgerm.core.register.CommandRegister;
import tk.exgerm.core.register.GraphRegisterProxy;
import tk.exgerm.core.register.HelpRegister;
import tk.exgerm.core.register.IteratorRegister;
import tk.exgerm.core.service.ICoreContext;
import tk.exgerm.core.service.IPipeLine;

public class CoreContext implements ICoreContext {

	/**
	 * Ime komponente kojoj je dodeljen ovaj kontekst.
	 */
	protected String name;
	
	/**
	 * Fabrika koja se koristi za kreiranje grafova, nodova i grana
	 */
	protected IGraphFactory graphFactory;

	/**
	 * Služi da kontekst dobije sve što mu treba :)
	 */
	protected Core core;

	/**
	 * Za operacije koje se tiču akcija
	 */
	protected ActionManager actionManager;

	/**
	 * Za operacije sa tabobima
	 */
	protected TabManager tabManager;

	/**
	 * Za operacija sa cuvanjem grafova
	 */
	protected GraphRegisterProxy graphRegister;

	/**
	 * Za operacija sa komandama.
	 */
	protected CommandRegister commandRegister;

	/**
	 * Za operacije sa status barom
	 */
	protected StatusBar statusbar;

	/**
	 * Za rad sa eventovima
	 */
	protected EventDispatcher eventDispatcher;

	/**
	 * Za rad sa podacima
	 */
	protected DataRegister dataRegister;
	
	/**
	 * Za rad sa iteratorima
	 */
	protected IteratorRegister iteratorRegister;
	
	/**
	 * Za rad sa helpovima
	 */
	protected HelpRegister helpRegister;
	
	/**
	 * Za rad sa konfiguracijom
	 */
	protected Configuration configuratoin;

	/**
	 * Kreira kontekst koji će koristiti sve komponente za komunikaciju sa
	 * Core-om.
	 * 
	 * @param factory
	 *            Fabriku za pravljenje grafova i elemenata grafa.
	 */
	public CoreContext(String componentName, Core core, IGraphFactory factory) {
		this.name = componentName;
		this.graphFactory = factory;
		this.core = core;
		this.actionManager = core.getMainWindow().getActionManager();
		this.tabManager = core.getMainWindow().getTabManager();
		this.graphRegister = core.getServiceRegister().getGraphRegisterProxy();
		this.commandRegister = core.getServiceRegister().getCommandRegister();
		this.statusbar = core.getMainWindow().getStatusBar();
		this.eventDispatcher = core.getEventDispatcher();
		this.dataRegister = core.getDataRegister();
		this.iteratorRegister = core.getServiceRegister().getIteratorRegister();
		this.helpRegister = core.getServiceRegister().getHelpRegister();
		this.configuratoin = core.getConfig();
	}

	@Override
	public IPipeLine createPipeLine() {
		return new PipeLine();
	}

	@Override
	public void listenEvent(String event, IListener listener) {
		eventDispatcher.addListener(event, listener);
	}

	@Override
	public void removeListener(String event, IListener listener) {
		eventDispatcher.removeListener(event, listener);
	}

	@Override
	public void removeListener(IListener listener) {
		eventDispatcher.removeListener(listener);
	}

	@Override
	public void raise(String event, Object... parameters) {
		core.getEventDispatcher().raiseEvent(event, parameters);
	}

	@Override
	public IEdge newEdge(INode from, INode to) {
		return graphFactory.newEdge(from, to);
	}

	@Override
	public IGraph newGraph(String name) {
		return graphFactory.newGraph(name);
	}

	@Override
	public INode newNode(String name) {
		return graphFactory.newNode(name);
	}

	@Override
	public void setStatusbarMessage(String message) {
		core.getMainWindow().setStatusbarMessage(message);
	}

	@Override
	public String getLangage() {
		return "en-US";
	}

	@Override
	public void addAction(ExGAction action) {
		actionManager.addAction(action);
	}

	@Override
	public void addGraphicalComponent(ExGGraphicalComponent component) {
		if (component != null)
			tabManager
					.addTab(component, component.getPosition());
	}

	@Override
	public void addConfigPane(ExGConfigPanel configPane) {
		core.getConfigurationManager().addConfigurationPanel(name, configPane);
	}

	@Override
	public void removeAction(ExGAction action) {
		actionManager.removeAction(action);
	}

	@Override
	public void removeGraphicalComponent(ExGGraphicalComponent component) {
		if (component != null)
			tabManager.removeTab(component);
	}

	@Override
	public void removeConfigPane(ExGConfigPanel configPane) {
		core.getConfigurationManager().removeConfigurationPanel(configPane);
	}

	@Override
	public void registerMenu(String menuCode, String menuName) {
		actionManager.registerMenu(menuCode, menuName);
	}

	@Override
	public void registerToolbar(String toolbarCode) {
		actionManager.registerToolbar(toolbarCode);
	}

	@Override
	public void addStatusbarContent(ExGStatusbarContent statusbarContent) {
		if (statusbarContent != null)
			statusbar.addComponent(statusbarContent);
	}

	@Override
	public void removeStatusbarContent(ExGStatusbarContent statusbarContent) {
		if (statusbarContent != null)
			statusbar.removeComponent(statusbarContent);
	}

	@Override
	public void addSubmenu(ExGSubmenu submenu) {
		actionManager.addSubmenu(submenu);
	}

	@Override
	public void removeSubmenu(ExGSubmenu submenu) {
		actionManager.removeSubmenu(submenu);
	}

	@Override
	public void addGraph(IGraph graph) throws ExGGraphExsistException {
		graphRegister.addGraph(graph);
	}

	@Override
	public List<IGraph> getAllGraphs() {
		return graphRegister.getAllGraphs();
	}

	@Override
	public IGraph getGraph(String name) {
		return graphRegister.getGraph(name);
	}

	@Override
	public void removeGraph(String name) throws ExGGraphDoesNotExsistException {
		graphRegister.removeGraph(name);

	}

	@Override
	public void removeGraph(IGraph graph) throws ExGGraphDoesNotExsistException {
		graphRegister.removeGraph(graph);
	}

	@Override
	public List<ExGCommand> getAllCommands() {
		return commandRegister.getAllCommand();
	}

	@Override
	public void registerCommand(ExGCommand command)
			throws ExGCommandAlreadyExistException {
		commandRegister.addCommand(command);
	}

	@Override
	public void removeCommand(ExGCommand command) {
		commandRegister.removeCommand(command);
	}

	@Override
	public ExGCommand getCommand(String command) {
		return commandRegister.getCommand(command);
	}

	@Override
	public void registerGraphRegister(ExGGraphRegister register) {
		graphRegister.addBackend(register);
	}

	@Override
	public void removeGraphRegister(ExGGraphRegister register) {
		graphRegister.removeBackend(register);
	}

	@Override
	public void addData(String key, Object value) {
		dataRegister.put(key, value);
	}

	@Override
	public Object getData(String key) {
		return dataRegister.get(key);
	}

	@Override
	public void registerIterator(ExGIterator iterator)
			throws ExGIteratorAlreadyExsistException {
		iteratorRegister.addIterator(iterator);
	}

	@Override
	public void removeIterator(String name) {
		iteratorRegister.removeIterator(name);
	}

	@Override
	public String getConfigData(String key) {
		return this.configuratoin.get(name, key);
	}

	@Override
	public void putConfigData(String key, String value) {
		this.configuratoin.put(name, key, value);		
	}

	@Override
	public String removeConfigData(String key) {
		return this.configuratoin.remove(name, key);
	}

	@Override
	public void registerHelp(ExGHelp help) {
		this.helpRegister.addHelp(name, help);
	}

	@Override
	public void removeHelp(ExGHelp help) {
		this.helpRegister.removeHelp(help);
	}

	@Override
	public Map<String, ExGHelp> getAllHelps() {
		return this.helpRegister.getAllHelp();
	}

	@Override
	public ExGHelp getHelp(String name) {
		return this.helpRegister.getHelp(name);
	}

	@Override
	public void preventClosing() {
		this.core.preventClosing();
		
	}

	@Override
	public void hideMainWindow() {
		core.getMainWindow().setVisible(false);
	}

	@Override
	public void showMainWindow() {
		core.getMainWindow().setVisible(true);
	}

	@Override
	public boolean isMainWindowsShown() {
		return core.getMainWindow().isVisible();
	}
	
	@Override
	public int getMainWindowState() {
		return core.getMainWindow().getExtendedState();
	}
	
	@Override
	public void setMainWindowState(int state) {
		core.getMainWindow().setExtendedState(state);
	}
}
