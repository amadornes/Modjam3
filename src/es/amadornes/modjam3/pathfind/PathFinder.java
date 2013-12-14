package es.amadornes.modjam3.pathfind;

import java.util.ArrayList;
import java.util.List;

public abstract class PathFinder {

	protected List<Path> paths = new ArrayList<Path>();
	protected Vector3 start, finish;
	
	public PathFinder(Vector3 start, Vector3 finish) {
		this.start = start;
		this.finish = finish;
	}
	
	public abstract void pathfind();
	
	public Path getShortestPath(){
		Path shortest = null;
		
		for(Path p : paths){
			if(shortest == null || shortest.getLength() > p.getLength()){
				shortest = p;
			}
		}
		
		return shortest;
	}
	
}
