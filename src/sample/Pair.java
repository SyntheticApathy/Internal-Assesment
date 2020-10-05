package sample;

import java.util.ArrayList;
import java.util.List;

public class Pair {
    List<Resource> xResources = new ArrayList<>();
    List<Resource> yResources = new ArrayList<>();

    public Pair() {

    }

    public void addXReources(Resource xResource, Resource yResource){
        xResources.add(xResource);
        yResources.add(yResource);
    }
}
