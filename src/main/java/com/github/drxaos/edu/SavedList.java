package com.github.drxaos.edu;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> implements Reloadable {

	private File file;
	private List list;
	
    public SavedList(File file) {
    	this.list=new ArrayList();
    	
    	if (file==null)
    		this.file = new File("savedList.dat");
    	else
    	{
    		this.file = file;
    		reload();
    	}
    }

    public void reload() throws FileOperationException {
    	this.list.clear();
    	try{
    		FileInputStream fileInStream = new FileInputStream(this.file);
    		ObjectInputStream objectInStream=new ObjectInputStream(fileInStream);
    		list = (List) objectInStream.readObject();
    		fileInStream.close();
    		objectInStream.close();
    	}
    	catch(Exception ex){}
    }

    @Override
    public E get(int index) {
        return (E) list.get(index);
    }

    @Override
    public E set(int index, E element) {
    	list.set(index, element);
    	updateFile();
        return this.get(index);
    }

    private void updateFile()
    {
    	try
    	{
    	ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
        outStream.writeObject(list);
        outStream.close();
    	}
    	catch (Exception ex){}
    }
    
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(int index, E element) {
    	list.add(index, element);
    	updateFile();
    }

    @Override
    public E remove(int index) {
    	E obj=(E) this.list.remove(index);
    	updateFile();
        return obj;
    }
}
