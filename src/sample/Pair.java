package sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pair<X, Y> {
    List<Resource> xResources = new ArrayList<>();
    List<Resource> yResources = new ArrayList<>();

    public Pair() {

    }

    public void addXReources(Resource xResource, Resource yResource) {
        xResources.add(xResource);
        yResources.add(yResource);
    }

    public void addXReources(Y xresources) {
    }

    public void addYResources(Y yresources) {
    }
}
