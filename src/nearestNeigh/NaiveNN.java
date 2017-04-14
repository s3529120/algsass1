package nearestNeigh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * @author Jeffrey, Youhan
 */
public class NaiveNN implements NearestNeigh{
   private List<Point> list;

    @Override
    public void buildIndex(List<Point> points) {
        // To be implemented.
       //Assign points to NaiveNN list variable
       this.list = points;
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        // To be implemented.
       int largest=0,x=0;
       Point[] stored;
       Point[] matches = new Point[k];
       
       stored=list.toArray(new Point[list.size()]);
       
       for(int i=0;i<list.size();i++){
          if(stored[i].cat==searchTerm.cat){
             matches[x]=stored[i];
             System.out.println(stored[i].cat.toString() + searchTerm.cat.toString());
             x++;
          }
          if(x==k){
             break;
          }
          
       }
       if(matches.length<k){
          return Arrays.asList(matches);
       }
          for(int i=0;i<stored.length;i++){
             largest=0;
             for(int j=0;j<k;j++){
                if(matches[j].distTo(searchTerm)>matches[largest].distTo(searchTerm)){
                   largest=j;
                }
             }
             if(stored[i].distTo(searchTerm)<matches[largest].distTo(searchTerm)&&searchTerm.cat==stored[i].cat){
                matches[largest]=stored[i];
                System.out.println(stored[i].cat.toString() + searchTerm.cat.toString());
             }
       }
        return Arrays.asList(matches);
    }

    @Override
    public boolean addPoint(Point point) {
        // To be implemented.
       if(isPointIn(point)){
        return false;
       }else{
          list.add(point);
          return true;
       }
    }

    @Override
    public boolean deletePoint(Point point) {
        // To be implemented.
       Point[] stored;
       stored=(Point[]) list.toArray();
       for(int i=0;i<list.size();i++){
          if(stored[i].equals(point)){
             list.remove(stored[i]);
             return true;
          }
       }
          return true;
       
    }

    @Override
    public boolean isPointIn(Point point) {
        Boolean contains=false;
        Point[] stored;
        stored=(Point[]) list.toArray();
        for(int i=0;i<list.size();i++){
           if(stored[i].equals(point)){
              contains=true;
              break;
           }
        }
       if(contains){
          return true;
       }else{
          return false;
       }
    }
    

}
