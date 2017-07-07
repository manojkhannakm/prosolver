package com.manojkhannakm.prosolver.file;

import java.io.File;

/**
 * @author Manoj Khanna
 */

public abstract class JsonFile<T> {

    protected final File file;

    private T t;

    protected JsonFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object obj) {
        //noinspection FileEqualsUsage
        return obj instanceof JsonFile && ((JsonFile) obj).file.equals(file);
    }

    public abstract boolean read();

    public abstract boolean write();

    public abstract void clear();

    public File getFile() {
        return file;
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }

}
