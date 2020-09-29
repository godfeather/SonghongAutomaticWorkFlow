package Collection;

import java.util.ArrayList;

public class FinalProcess<K,V> {
	private ArrayList<K> key;
	private ArrayList<V> value;
	public void put(K key,V value) {
		this.key.add(key);
		this.value.add(value);
	}
	public FinalProcess() {
		key=new ArrayList<K>();
		value=new ArrayList<V>();
	}
	public K getKey(int i) {
		return key.get(i);
	}
	public V getValue(int i) {
		return value.get(i);
	}
	public void remove(int i) {
		value.remove(i);
		key.remove(i);
	}
	public int size() {
		return key.size();
	}
	public void put(int i,K key,V value) {
		this.key.add(i, key);
		this.value.add(i,value);
	}
}
