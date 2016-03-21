package com.thrashplay.saltar.editor.model;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class IdGenerator {
    private int currentId = 1;

    public synchronized String getId() {
        return getId("gameObject");
    }

    public synchronized String getId(String prefix) {
        return prefix + "-" + currentId++;
    }
}
