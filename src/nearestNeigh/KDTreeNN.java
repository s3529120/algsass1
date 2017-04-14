package nearestNeigh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kdTree.Node;
import kdTree.Split;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * @author Jeffrey, Youhan
 */
public class KDTreeNN implements NearestNeigh{

   private Node head;

    @Override
    public void buildIndex(List<Point> points) {
       if (points.isEmpty()){
       return;
       }

   // treeRoot is member attribute storing root of kd-tree
          head = buildTree(points, Split.lon);
}


    //recursively builds the tree

    //bXDim is boolean indicating which dimension we are splitting on and points is the list of points to split/partition on
    private Node buildTree(List<Point> points, Split bXDim){
       Point[] copyArr;
       Node newleft,newright;
       if(points.size()==1){
          copyArr=points.toArray(new Point[1]);
          return new Node(copyArr[0]);
       }
       Point[] sortedPoints = Point.sortPoints(points, bXDim);
       // find the median from sorted points
       int median = Point.getMedian(sortedPoints);
       // construct a node for the median point
       System.out.println(sortedPoints[median].id+"--"+median+"--"+points.size());
       Node currParent = new Node(sortedPoints[median]);
       newleft=null;
       newright=null;
       // Check if there is a left partition (indexing starts at 0).  If so, recursively partition it
       if (median > 0){
          // flip() inverts the boolean value (effectively changing the dimension we split on next)
          copyArr =  new Point[median];
          for(int i=0;i<median;i++){
             copyArr[i]=sortedPoints[i];
          }
             newleft=buildTree(new ArrayList<Point>(Arrays.asList(copyArr)), flip(bXDim));
             System.out.println("=l="+newleft.getData().id);
       // check if there is a right partition
       }if (median < points.size()){
          // flip() inverts the boolean value (effectively changing the dimension we split on next)
          copyArr= new Point[points.size()-median];
          int i=0;
          for(int j=median;j<points.size();j++){
             copyArr[i]=sortedPoints[j];
             i++;
          }
          newright=buildTree(new ArrayList<Point>(Arrays.asList(copyArr)), flip(bXDim));
          System.out.println("=r="+newright.getData().id);
       }
       if(newleft!=null){
          currParent.setLeftChild(newleft);
       }if(newright!=null){
       currParent.setRightChild(newright);
       }
       return currParent;
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
       Point[] currentBest=new Point[k];
       Node focus;
       List<Node> checked = new ArrayList<Node>(); 
       
       //Find closest leaf
       focus=head;
       focus=head.getLeaf(searchTerm, Split.lon);
       focus.recursion(searchTerm, currentBest,checked,focus.getSplit(),k);
       if(searchTerm.lat<head.getData().lat){
          focus=head.getRightChild().getLeaf(searchTerm, Split.lat);
          focus.recursion(searchTerm, currentBest, checked, focus.getSplit(),k);
       }else{
          focus=head.getLeftChild().getLeaf(searchTerm, Split.lat);
          focus.recursion(searchTerm, currentBest, checked, focus.getSplit(),k);
       }
       List<Point> ret = new ArrayList<Point>();
       for(int i=0;i<currentBest.length;i++){
          ret.add(currentBest[i]);
       }
        return ret;
    }

    @Override
    public boolean addPoint(Point point) {
       Node focus=head;
       
       if(isPointIn(point)){
          return false;
       }
       while(focus!=null){
          if(focus.getSplit()==Split.lon){
             if(point.lat<focus.getData().lat){
                if(focus.getLeftChild()==null){
                   focus.getLeftChild().setData(point);
                   return true;
                }
                focus=focus.getLeftChild();
             }else{
                if(focus.getRightChild()==null){
                   focus.getRightChild().setData(point);
                   return true;
                }
                focus=focus.getRightChild();
             }
          }else{
             if(point.lon<focus.getData().lon){
                if(focus.getLeftChild()==null){
                   focus.getLeftChild().setData(point);
                   return true;
                }
                focus=focus.getLeftChild();
             }else{
                if(focus.getRightChild()==null){
                   focus.getRightChild().setData(point);
                   return true;
                }
                focus=focus.getRightChild();
             }
          }
       }
       return false;
    }

    @Override
    public boolean deletePoint(Point point) {
       Node focus,prev;
       Split split=Split.lon;
       List<Point> children=new ArrayList<Point>();
        if(isPointIn(point)==false){
           return false;
        }
        
        //Find node
        focus=head;
        while(!focus.getData().equals(point)){
           if(split==Split.lon){
              if(point.lon<focus.getData().lon){
                 focus=focus.getLeftChild();
              }else{
                 focus=focus.getRightChild();
              }
              split=Split.lat;
           }else{
              if(point.lon<focus.getData().lat){
                 focus=focus.getLeftChild();
              }else{
                 focus=focus.getRightChild();
              }
              split=Split.lon;
           }
        }
        prev=focus.getParent();
        Node.getChildren(focus,children);
        if(prev.getLeftChild().getData().equals(focus.getData())){
           prev.setLeftChild(buildTree(children,split));
        }else{
           prev.setRightChild(buildTree(children,split));
        }
        return true;
    }

    @Override
    public boolean isPointIn(Point point) {
       Node focus=head;
       while(focus!=null){
          if(focus.getData().equals(point)){
             return true;
          }
          if(focus.getSplit()==Split.lon){
             if(point.lat<focus.getData().lat){
                focus=focus.getLeftChild();
             }else{
                focus=focus.getRightChild();
             }
          }else{
             if(point.lon<focus.getData().lon){
                focus=focus.getLeftChild();
             }else{
                focus=focus.getRightChild();
             }
          }
       }
        return false;
    }

    public static Split flip(Split split){
       if(split==Split.lon){
          return Split.lat;
       }else{
          return Split.lon;
       }
    }
}
