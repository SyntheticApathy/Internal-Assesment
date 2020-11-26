package sample.logicalgameplay;

import javafx.util.Pair;
import sample.logicalmap.LogicalMap;
import sample.logicalmap.Position;

import java.util.*;

public class Enemy {
    private List<Pair<Integer, Integer>> path;
    private int currentPosition;

    private Set<DijkstraNode> unsettledNodes = new HashSet<>();
    private DijkstraNode[][] allNodes;

    private List<Pair<Integer,Integer>> enemyPath;



    public Enemy(LogicalMap logicalMap, Pair<Integer, Integer> initialPosition) {
        /* calculate path using Dijkstra
          based on https://www.baeldung.com/java-dijkstra */

       allNodes = new DijkstraNode[logicalMap.getWidth()][logicalMap.getHeight()];

        for (int i = 0; i < allNodes.length; i++) {
            for (int j = 0; j < allNodes[i].length; j++) {
                allNodes[i][j] = new DijkstraNode(i, j);
            }
        }

        DijkstraNode initialDijkstraNode = allNodes[initialPosition.getKey()][initialPosition.getValue()];
        initialDijkstraNode.setDistance(0);
        unsettledNodes.add(initialDijkstraNode);
        DijkstraNode targetNode = allNodes[logicalMap.getMiddle().getKey()][logicalMap.getMiddle().getValue()];
        while(!unsettledNodes.isEmpty()) {
            DijkstraNode node = chooseNodeWithLowestDistance();
            Set<DijkstraNode> adjacentNodes = findAdjacentNodes(node, logicalMap);
            for (DijkstraNode adjacentNode : adjacentNodes) {
                if(adjacentNode.isSettled()) {
                    continue;
                }
//                updateAdjacentNode(node /*fromNode*/);
                unsettledNodes.add(adjacentNode);
            }
//            settleNode(node);

            if(node.equals(targetNode)) break;
        }

        enemyPath = targetNode.getShortestPath();
    }

    private Set<DijkstraNode> findAdjacentNodes(DijkstraNode node, LogicalMap logicalMap) {
        int x = node.getX();
        int y = node.getY();
        Position[][] positions = logicalMap.getPositions();
        Set<DijkstraNode> adjacentNodes = new HashSet<>();
        //4 possibilities (x-1,y) (x+1,y) (x,y-1) (x,y+1)

        //(x-1,y)
        if(x-1 >= 0) {
            if(! positions[x-1][y].hasObstacle()) {
                adjacentNodes.add(allNodes[x-1][y]);
            }
        }

        //(x+1,y)
        if(x+1 < logicalMap.getWidth()) {
            if(! positions[x+1][y].hasObstacle()) {
                adjacentNodes.add(allNodes[x+1][y]);
            }
        }

        //(x,y-1)
        if(y-1 >= 0) {
            if(! positions[x][y-1].hasObstacle()) {
                adjacentNodes.add(allNodes[x][y-1]);
            }
        }

        //(x,y+1)
        if(y+1 < logicalMap.getHeight()) {
            if(! positions[x][y+1].hasObstacle()) {
                adjacentNodes.add(allNodes[x][y+1]);
            }
        }
        return adjacentNodes;
    }

    private DijkstraNode chooseNodeWithLowestDistance() {
        DijkstraNode found = null;
        assert ! unsettledNodes.isEmpty();
        for (DijkstraNode node : unsettledNodes) {
            if(found == null) {
                found = node;
                continue;
            }
            if(node.getDistance() < found.getDistance()) {
                found = node;
            }
        }
        return found;
    }
}

class DijkstraNode {
    private int distance = Integer.MAX_VALUE;
    private List<Pair<Integer, Integer>> shortestPath = new ArrayList<>();
    private int x;
    private int y;
    private boolean settled = false;

    public DijkstraNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public List<Pair<Integer, Integer>> getShortestPath() {
        return shortestPath;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isSettled() {
        return settled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DijkstraNode that = (DijkstraNode) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
