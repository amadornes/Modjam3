package es.amadornes.modjam3.pathfind;

import java.util.Arrays;

import net.minecraftforge.common.ForgeDirection;

/**
 * This pathfinder searches only in empty blocks and with a set max path length
 */
public class DefaultPathFinder extends PathFinder {
	
	private int maxPathLength = 100;
	
	public DefaultPathFinder(Vector3 start, Vector3 finish, int maxPathLength) {
		super(start, finish);
		this.maxPathLength = maxPathLength;
	}

	@Override
	public void pathfind() {
		pathfind(start, new Path(Arrays.asList(new Vector3[]{start})));
	}
	
	private void pathfind(Vector3 loc, Path p){
		if(p.getLength() > maxPathLength)
			return;
		
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			Vector3 rel = loc.getRelative(dir);
			Path path = p.clone();
			path.addStep(rel);
			if(rel.equals(finish)){
				paths.add(path);
				return;
			}
			if(rel.isBlock(null)){//If it's air
				pathfind(rel, path);
			}
		}
	}

}
