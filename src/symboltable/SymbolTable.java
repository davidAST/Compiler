package symboltable;

import ast.definitions.Definition;

import java.util.*;


public class SymbolTable {
	
	private int scope=0;
	private List<Map<String, Definition>> table;
	public SymbolTable()  {
		table = new ArrayList<>();
		table.add(new HashMap<>());
	}

	/**
	 * Increases the scope and creates a new Hash Map for the current scope
	 */
	public void set() {
		scope++;
		table.add(new HashMap<>());
	}

	/**
	 * Decreases the scope and removes the Hash Map for the previous scope (before
	 * decreasing the scope)
	 */
	public void reset() {
		table.remove(scope);
		scope--;
	}

	/**
	 * Inserts a Definition into the symbol table with the current scope
	 * @param definition Definition to be added to the current scope
	 * @return True if the definition was added, false if the definition was already in that scope
	 * (If it returns false, the Identification Visitor will handle the error)
	 */
	public boolean insert(Definition definition) {
		Map<String, Definition> map = table.get(scope);
		if (map.get(definition.getName()) == null) {
			map.put(definition.getName(), definition);
			definition.setScope(scope);
			return true;
		}
		return false;
	}

	/**
	 * Finds a definition in the symbol table. If it doesn't find it in the current scope,
	 * it keeps searching through all the scopes.
	 * @param id ID of the Definition to be found
	 * @return The definition with that ID or null if it was not found (The IdentificationVisitor
	 * throws the error if it returns null)
	 */
	public Definition find(String id) {
		int iterator = scope;
		while (iterator >= 0) {
			Definition def = table.get(iterator).get(id);
			if (def != null) {
				return def;
			}
			iterator--;
		}
		return null;
	}

	/**
	 * Finds a definition given their name (id) in the current scope
	 * @param id ID (Name) of the Definition
	 * @return The definition found or null if it couldn't be found.
	 */
	public Definition findInCurrentScope(String id) {
		return table.get(scope).get(id);
	}
}
