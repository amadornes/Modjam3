package es.amadornes.modjam3.pathfind;

import java.util.ArrayList;
import java.util.List;


public class Path {

	private List<Vector3> steps;
	public List<Vector3> visited = new ArrayList<Vector3>();
	
	public Path(List<Vector3> steps) {
		this.steps = steps;
	}
	
	public double getLength(){
		double length = 0;
		Vector3 last = null;
		for(Vector3 step : steps){
			if(last != null){
				length += last.toVec3().distanceTo(step.toVec3());
			}
			last = step;
		}
		return length;
	}
	
	public List<Vector3> getSteps(){
		return steps;
	}
	
	public void addStep(Vector3 vec){
		steps.add(vec);
		visited.add(vec);
	}
	
	public Path clone(){
		List<Vector3> steps = new ArrayList<Vector3>();
		for(Vector3 v : this.steps){
			steps.add(v.clone());
		}
		return new Path(steps);
	}
	
}
