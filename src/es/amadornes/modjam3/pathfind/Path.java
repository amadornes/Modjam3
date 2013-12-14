package es.amadornes.modjam3.pathfind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Path {

	private List<Vector3> steps;
	
	public Path(List<Vector3> steps) {
		this.steps = steps;
	}
	
	public int getLength(){
		return steps.size();
	}
	
	public List<Vector3> getSteps(){
		return steps;
	}
	
	public void addStep(Vector3 vec){
		steps.add(vec);
	}
	
	public Path clone(){
		List<Vector3> steps = new ArrayList<Vector3>();
		Collections.copy(steps, this.steps);;
		return new Path(steps);
	}
	
}
