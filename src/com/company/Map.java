package com.company;

import java.util.*;

public class Map {
   private class Graph{
       private class Node{
           private String name;
           private HashSet<String> linkNames = new HashSet<String>(0);
           private HashMap<Node, Integer> links;

           public Node(){
               name = "";
               links = new HashMap<Node, Integer>(0);
           }

           public Node(String selfName){
               name = selfName;
               links = new HashMap<Node, Integer>(0);
           }

           public String getName() {
               return name;
           }

           public HashMap<Node, Integer> getLinks() {
               return links;
           }

           public void setLinks(HashMap<Node, Integer> links) {
               this.links = links;
           }

           public boolean hasLink(String name){
               return linkNames.contains(name);
           }

           public void addLink(Node node, int weight){
               if(this.links.containsKey(node))
                   return;
               this.links.put(node, weight);
               node.links.put(this, weight);
               this.linkNames.add(node.name);
               node.linkNames.add(this.name);
           }

           public void setName(String name) {
               this.name = name;
           }
       }
       private class MetaInf {
           public Node nodeParrent;
           public int totalWeight;
           MetaInf(Node nodeParrent, int totalWeight){
               this.nodeParrent = nodeParrent;
               this.totalWeight = totalWeight;
           }
       }
       private HashSet<String> vertices;
       private Node start;
       public Graph(){
           vertices = new HashSet<String>(0);
           start = new Node();
       }

       public boolean has(String name){
           return vertices.contains(name);
       }

       private Node find(String name){
           if(!has(name))
               return null;
           HashSet<String> used = new HashSet<String>(0);
           ArrayDeque<Node> childs = new ArrayDeque<Node>(0);
           Node tmp = start;
           while(tmp.getName() != name){
               if(!used.contains(tmp.name)) {
                   for (Node i : tmp.getLinks().keySet())
                       childs.push(i);
                   used.add(tmp.name);
               }
               tmp = childs.pop();
           }
           return tmp;
       }

       public boolean addLink(String firstPoint, String secondPoint, int weight){
           if(!has(firstPoint) && !has(secondPoint))
               return false;
           if(has(firstPoint)){
               Node tmp = find(firstPoint);
               if(tmp.hasLink(secondPoint))
                   return false;
               tmp.addLink(new Node(secondPoint), weight);
               vertices.add(secondPoint);
           }
           else if(has(secondPoint)){
               Node tmp = find(secondPoint);
               if(tmp.hasLink(firstPoint))
                   return false;
               tmp.addLink(new Node(firstPoint), weight);
               vertices.add(firstPoint);
           }
           else{
               find(firstPoint).addLink(find(secondPoint), weight);
           }
           return true;
       }

       public List<String> path(String from, String to){
           HashMap<String, MetaInf> minPaths = new HashMap<String, MetaInf>(0);
           HashSet<String> visited = new HashSet<String>(0);
           ArrayDeque<Node> childs = new ArrayDeque<Node>(0);
           Node tmp = null;
           childs.push(find(from));
           minPaths.put(from, new MetaInf(null, 0));
           while(!childs.isEmpty()){
               tmp = childs.pop();
               if(!visited.contains(tmp.name)){
                   for(Node i: tmp.getLinks().keySet()) {
                       childs.push(i);
                       if(minPaths.containsKey(i.name)){
                           if(minPaths.get(i.name).totalWeight > minPaths.get(tmp.name).totalWeight + tmp.links.get(i))
                                minPaths.put(i.name,
                                        new MetaInf(i,minPaths.get(tmp.name).totalWeight + tmp.links.get(i)));
                       }else{
                           minPaths.put(i.name,new MetaInf(tmp, tmp.links.get(i)));
                       }
                   }
                   visited.add(tmp.name);
               }
           }
           ArrayList<String> ans = new ArrayList<String>(0);
           MetaInf curr = minPaths.get(to);
           while(curr.nodeParrent != null){
               ans.add(curr.nodeParrent.name);
               curr = minPaths.get(curr.nodeParrent.name);
           }
           for(int i = 0; i < ans.size()/2; ++i){
               String temp = ans.get(i);
               ans.set(i, ans.get(ans.size()-1-i));
               ans.set(ans.size()-1-i, temp);
           }
           return ans;
       }

       public void merge(Graph second, String point1, String point2, int weight){
           if(second.has(point2))
               this.find(point1).addLink(second.find(point2), weight);
           else
               this.find(point2).addLink(second.find(point1), weight);
           this.vertices.addAll(second.vertices);
       }
   }

}
