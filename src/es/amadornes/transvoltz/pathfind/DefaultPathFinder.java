package es.amadornes.transvoltz.pathfind;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeDirection;

/**
 * This pathfinder searches only in empty blocks and with a set max path length
 */
public class DefaultPathFinder extends PathFinder {
	
	private int maxPathLength = 16;
	
	public DefaultPathFinder(Vector3 start, Vector3 finish, int maxPathLength) {
		super(start, finish);
		this.maxPathLength = maxPathLength;
	}

	@Override
	public PathFinder pathfind() {
		List<Vector3> p = new ArrayList<Vector3>();
		p.add(start);
		pathfind(start, new Path(p));
		return this;
	}
	
	private void pathfind(Vector3 loc, Path p){
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			Vector3 rel = loc.getRelative(dir);
			if(!p.visited.contains(rel)){
				Path path = p.clone();
				path.addStep(rel);
				if(rel.equals(finish)){
					paths.add(path);
					return;
				}
				if(rel.isBlock(null)){//If it's air
					if(path.getLength() <= maxPathLength)
						pathfind(rel, path);
				}
			}
		}
	}

}
