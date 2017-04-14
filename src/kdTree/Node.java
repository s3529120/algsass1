package kdTree;

import java.util.List;

import nearestNeigh.Point;

public class Node
{
   //Related Nodes
   private Node parent=null;
   private Node leftChild=null;
   private Node rightChild=null;
   private Point data;
   private Split split;
   
   //Constructor
   public Node(Point point){
      this.data=point;
   }
   
   //Accessors
   public Node getParent(){
      return parent;
   }
   public Node getLeftChild(){
      return leftChild;
   }
   public Node getRightChild(){
      return rightChild;
   }
   public Point getData(){
      return data;
   }
   public Split getSplit(){
      return split;
   }
   
   //Mutators
   public Boolean setParent(Node node){
      this.parent=node;
      return true;
   }
   public Boolean setLeftChild(Node node){
      this.leftChild=node;
      node.setParent(node);
      return true;
   }
   public Boolean setRightChild(Node node){
      rightChild=node;
      node.setParent(node);
      return true;
   }
   public Boolean setData(Point point){
      this.data=point;
      return true;
   }
   public Boolean setSplit(Split split){
      this.split=split;
      return true;
   }
   
   //Methods
   public void recursion(Point point, Point[] currBest,List<Node> checked,Split split,int k){
      Node focus=this;
      Boolean bool=true;

      while(bool){
         if(focus.getData().cat==point.cat){
            bool=Point.addTo(focus, point, currBest, k);
         }
         checked.add(focus);
         focus=focus.getParent();
         if(focus.getLeftChild() != null && !checked.contains(focus.getLeftChild())){
            focus.getLeftChild().getLeaf(point, split).recursion(point, currBest, checked, split, k);
         }if(focus.getRightChild() != null && !checked.contains(focus.getRightChild())){
            focus.getRightChild().getLeaf(point, split).recursion(point, currBest, checked, split, k);
         }
      }
      
   }
   
   /**Finds leaf closest to given point starting from called node.
    * @param point Point to check closest to.
    * @split Dimension to start splitting on.
    * @return Closest leaf node
    */
   public Node getLeaf(Point point,Split split){
      Node focus=this;
      while(focus.getLeftChild()!=null || focus.getRightChild()!=null){
         if(split==Split.lon){
            if(point.lon<focus.getData().lon){
               if(focus.getLeftChild()!=null){
                  focus=focus.getLeftChild();
               }else{
                  focus=focus.getRightChild();
               }
               split=Split.lat;
            }else{
               if(focus.getRightChild()!=null){
                  focus=focus.getRightChild();
               }else{
                  focus=focus.getLeftChild();
               }
               split=Split.lat;
            }
         }else{
            if(point.lat<focus.getData().lat){
               if(focus.getLeftChild()!=null){
                  focus=focus.getLeftChild();
               }else{
                  focus=focus.getRightChild();
               }
               split=Split.lon;
            }else{
               if(focus.getRightChild()!=null){
                  focus=focus.getRightChild();
               }else{
                  focus=focus.getLeftChild();
               }
               split=Split.lon;
            }
         }
      }
      return focus;
   }
   
   public Split getSplit(Node node){
      int count=0;
      while(node.getParent()!=null){
         count++;
      }
      if(count%2==1){
         return Split.lat;
      }else{
         return Split.lon;
      }
   }
   
   /**Retrieves all children of given node
    * @return all childrens data as ArrayList<Point>
    */
   public static void getChildren(Node focus,List<Point> children){
      if(focus.getLeftChild()!=null){
         children.add(focus.getLeftChild().getData());
         getChildren(focus.getLeftChild(),children);
      }if(focus.getRightChild()!=null){
         children.add(focus.getRightChild().getData());
         getChildren(focus,children);
      }
   }
}
