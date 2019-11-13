package com.company;

import java.security.cert.TrustAnchor;
import java.util.*;

public class MyMap {
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
               if(!this.links.containsKey(node)) {
                   this.linkNames.add(node.name);
                   node.linkNames.add(this.name);
               }
               this.links.put(node, weight);
               node.links.put(this, weight);
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
            start = null;
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
            while(tmp.getName().compareTo(name) != 0){
                if(!used.contains(tmp.name)) {
                    for (Node i : tmp.getLinks().keySet())
                        childs.push(i);
                    used.add(tmp.name);
                }
                tmp = childs.pop();
            }
            return tmp;
        }

        public void addLink(String firstPoint, String secondPoint, int weight){
            if(!has(firstPoint) && !has(secondPoint)) {
                if (vertices.size() == 0) {
                    vertices.add(firstPoint);
                    vertices.add(secondPoint);
                    start = new Node(firstPoint);
                    start.addLink(new Node(secondPoint), weight);
                }
            }
            else if(has(firstPoint) && !has(secondPoint)){
                Node tmp = find(firstPoint);
                tmp.addLink(new Node(secondPoint), weight);
                vertices.add(secondPoint);
            }
            else if(!has(firstPoint) && has(secondPoint)){
                Node tmp = find(secondPoint);
                tmp.addLink(new Node(firstPoint), weight);
                vertices.add(firstPoint);
            }
            else{
                find(firstPoint).addLink(find(secondPoint), weight);
            }
        }

        public ArrayList<String> path(String from, String to){
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
                        if(!visited.contains(i.name) && !childs.contains(i))
                            childs.push(i);
                        if(minPaths.containsKey(i.name)){
                            if(minPaths.get(i.name).totalWeight > minPaths.get(tmp.name).totalWeight + tmp.links.get(i))
                                minPaths.put(i.name,
                                        new MetaInf(tmp,minPaths.get(tmp.name).totalWeight + tmp.links.get(i)));
                        }else{
                            minPaths.put(i.name,new MetaInf(tmp, tmp.links.get(i)));
                        }
                    }
                    visited.add(tmp.name);
                }
            }
            ArrayList<String> ans = new ArrayList<String>(0);
            ans.add(to);
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
    ArrayList<Graph> graphs;
    public MyMap(){
        graphs = new ArrayList<Graph>(0);
    }

    private int find(String nodeName){
        for(int i = 0; i < graphs.size(); ++i)
            if(graphs.get(i).has(nodeName))
                return i;
        return -1;
    }

    public void addPath(String from, String to, int weight){
        int indFirst = find(from);
        int indSecond = find(to);
        if( indFirst == -1 && indSecond == -1){
            Graph tmp = new Graph();
            tmp.addLink(from, to, weight);
            graphs.add(tmp);
        }
        else if(indFirst == -1 || indFirst == indSecond){
            graphs.get(indSecond).addLink(to, from, weight);
        }
        else if(indSecond == -1){
            graphs.get(indFirst).addLink(from, to, weight);
        }
        else{
            graphs.get(indFirst).merge(graphs.get(indSecond), from, to, weight);
            graphs.remove(indSecond);
        }
    }

    public ArrayList<String> getAllPlaces(){
        ArrayList<String> ans = new ArrayList<String>(0);
        for(Graph g: graphs){
            ans.addAll(g.vertices);
        }
        return ans;
    }

    public ArrayList<String> generateShortWay(String from, String to){
        int ind1 = find(from), ind2 = find(to);
        if(ind1 != find(to) || ind1 == -1 || ind2 == -1)
            return null;
        return graphs.get(ind1).path(from, to);
    }
}
