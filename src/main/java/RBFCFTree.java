import java.util.*;

/**
 * Created by robotcator on 17-5-25.
 */
public class RBFCFTree extends FreeTree {

    List<Integer> level;
    List<Integer> canonicalString; //the canonical string of this tree

    public RBFCFTree(List<Integer> vString) {
        super(-1, vString.get(0));

        int vetexIndex = 2;
        int parentIndex = 1;

        level = new ArrayList<Integer>();
        while(level.size() <= vCount) level.add(0);

        vLabel.set(1, vString.get(1));
        int idx = 3;
        int vl = -1;

        if (vString.get(0) == 1) {
            level.set(1, 1);
            return ;
        }

        while((vl = vString.get(idx)) != Config.ENDOFTREETAG) {
            if (vl == Config.ENDTAG) {
                parentIndex += 1;
                idx += 1;
            }else{
                vLabel.set(vetexIndex, vl);
                parent.set(vetexIndex, parentIndex);
                adj.get(parentIndex).add(vetexIndex);
                vetexIndex += 1;
                idx += 1;
            }
        }
        computeBFCS();
    }

    public  RBFCFTree(RBFCFTree rbfcTree, int tmpV, int pos) {
        super(rbfcTree.tid, rbfcTree.vCount+1);
        for (int i = 1; i <= rbfcTree.vCount; i ++) {
           // System.out.println("the vertex and the child of vertex: " + i + " " + rbfcTree.adj.get(i).size() + " : ");
            for (int j = 0; j < rbfcTree.adj.get(i).size(); j ++) {
               // System.out.print(rbfcTree.adj.get(i).get(j) + " ");
                adj.get(i).add(rbfcTree.adj.get(i).get(j));
            }
          //  System.out.println();
        }

        for (int i = 1; i <= rbfcTree.vCount; i ++) {
            vLabel.set(i, rbfcTree.vLabel.get(i));
        }
        for (int i = 1; i <= rbfcTree.vCount; i ++) {
            parent.set(i, rbfcTree.parent.get(i));
        }

        level = new ArrayList<Integer>();
        while(level.size() <= vCount) level.add(0);

        // add new leg
        vLabel.set(vCount, tmpV);
        adj.get(pos).add(vCount);
        parent.set(vCount, pos);

        computeBFCS();
    }

    void computeBFCS() {
//        System.out.println("begin to compute BFCS:");
        canonicalString = new ArrayList<Integer>();
        int tempCount = vCount;
        int curentLevel = 1;

        //System.out.println("the number of count :" + vCount);

        //Stack<Integer> tempS = new Stack<Integer>();
        // no need to use stack
        Queue<Integer> tempS = new LinkedList<Integer>();
        Queue<Integer> tempQ = new LinkedList<Integer>();

        canonicalString.add(vCount);
        canonicalString.add(vLabel.get(1));
        canonicalString.add(Config.ENDTAG);

//        System.out.println("the begin canonicalString :" + canonicalString);
//        System.out.println("the new tree");
//        for (int i = 1; i <= vCount; i ++) {
//            System.out.print(i + " " + adj.get(i).size() + " : ");
//            for (int j = 0; j < adj.get(i).size(); j ++) {
//                System.out.print(adj.get(i).get(j) + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("the level : " + level);
//        System.out.println("the vLabel : " + vLabel);

        level.set(1, curentLevel);

        tempQ.add(1);
        tempQ.add(-1);
        tempCount -= 1;

        while ( !tempQ.isEmpty() && tempCount > 0) {
            int i = tempQ.poll();
            if (i == -1) {
                curentLevel += 1;
                tempQ.add(-1);
                continue;
            }
           // System.out.println("the vertex is :" + i);
            for (int j = 0; j < adj.get(i).size(); j ++) {
           //     System.out.println("the adj vertex :" + adj.get(i).get(j));
                tempS.add(adj.get(i).get(j));
            }
//            System.out.println();

            while( !tempS.isEmpty() ) {
                canonicalString.add(vLabel.get(tempS.peek()));
                tempQ.add(tempS.peek());
                level.set(tempS.peek(), curentLevel + 1);
                tempS.poll();
                tempCount -= 1;
            }
            if ( tempCount > 0 )
                canonicalString.add(Config.ENDTAG);
        }
        canonicalString.add(Config.ENDOFTREETAG);
//        System.out.println("the computed canonical string :" + canonicalString);
    }

    int possibleLeg(int pos, int minV) {

//        System.out.println("the possible procedure begin");
//        System.out.println(pos);
//        System.out.println("level: " + level);
//        System.out.println("vLabel: " + vLabel);
//        System.out.println("the degree");
//        for (int i = 1; i <= vCount; i ++)
//            System.out.print(adj.get(i).size() + " ");
//        System.out.println();

        minV = Config.MinVertex;
        if ((level.get(pos) != 1 &&  adj.get(pos).size() != 0) ||
                (level.get(pos) == 1 && adj.get(pos).size() != 0)) {
            int prejIndex = adj.get(pos).size()-1;
            // get the immediate left sibling
            minV = vLabel.get(adj.get(pos).get(prejIndex));
        }  //check itselef
       // System.out.println("the minV " + minV);

        System.out.println(minV);
        System.out.println(adj.get(pos).size());
        System.out.println(level.get(pos));

        boolean possible = true;
        int j = pos;
        int p, q;
        while( possible && level.get(j) != 1) {

            p = parent.get(j);
            //System.out.println(p + " p and j " + j);
            if (level.get(p) != 1) { //if p is not the root
                if (adj.get(p).size() <= 1) { //if j has no sibling, nothing to do
                    j = p;
                    continue;
                }
            }

            // find the prev sbling
            int prev = -1;
            for (int i = 0; i < adj.get(p).size(); i ++) {
                if (adj.get(p).get(i) == j) {
                    prev = i-1;
                }
            }

            if (prev == -1) { //if j is the left most sibling
                j = p;
                continue;
            }
            int prevj = adj.get(p).get(prev);
           // System.out.println("the j and prevj " + j + " " + prevj);
            if (vLabel.get(prevj) != vLabel.get(j)) {
                j = p;
                continue;
            }

            Queue<Integer> vQ1 = new LinkedList<Integer>();
            Queue<Integer> vQ2 = new LinkedList<Integer>();
            Queue<Integer> labelK1 = new LinkedList<Integer>();
            Queue<Integer> labelK2 = new LinkedList<Integer>();

            vQ1.add(prevj);
            vQ2.add(j);

            vQ1.add(Config.ENDTAG);
            vQ2.add(Config.ENDTAG);

            int tempv1, tempv2;

            while (true) {
                tempv1 = vQ1.poll();
                tempv2 = vQ2.poll();
                //System.out.println("tempv1 and tempv2 " + tempv1 + " " + tempv2);

                if (tempv2 == Config.ENDTAG) {
                    if (tempv1 != Config.ENDTAG) {
                        j = p;
                        break;
                    }else{
                        continue;
                    }
                }

                //System.out.println("get the child of tempv1");
                for (int i = 0; i < adj.get(tempv1).size(); i ++) {
                    // System.out.print(adj.get(tempv1).get(i) + " ");
                    int tmp = adj.get(tempv1).get(i);
                    vQ1.add(tmp);
                    labelK1.add(vLabel.get(tmp));
                }
               // System.out.println();

               // System.out.println("get the child of tempv2");
//                for (int i = 0; i < adj.get(tempv2).size(); i ++) {
//                    System.out.print(adj.get(tempv2).get(i) + " ");
//                    int tmp = adj.get(tempv2).get(i);
//                    vQ2.add(tmp);
//                    labelK2.add(vLabel.get(tmp));
//                }
//                System.out.println();

                if (tempv2 != pos) {
                   // System.out.println("not reach the position :tempV1 and tempV2 " + tempv1 + " " + tempv2);
                    boolean result = false;
                    while (!labelK1.isEmpty()) {
                        //if labelK1 is empty, so is labelK2
                        if (labelK2.isEmpty()) {
                            result = true;
                            break;
                        }
                        if (labelK1.peek() < labelK2.peek()) {
                            //if the vertices resolve
                            result = true;
                            break;
                        }
                        labelK1.poll();
                        labelK2.poll();
                    }
                    if (result) {
                        j = p;
                        break;
                    }
                }else{
                    if (labelK1.isEmpty()) {
                        possible = false;
                        break;
                    }else {
                        while (!labelK1.isEmpty()) {
                            tempv1 = labelK1.poll();
                           // System.out.println("the labelk1's elment "+ tempv1);
                            if (labelK2.isEmpty()) {
//                                System.out.println(tempv1);
                                if (tempv1 > minV) {
                                    minV = tempv1;
                                }
                            //    System.out.println("the updated minvertex " + minV);
                            } else {
                                tempv2 = labelK2.poll();
                             //   System.out.println("the labelk2's elment "+ tempv2);
                                if (tempv1 != tempv2) {
                                    break;
                                }
                            }
                        }
                        j = p;
                        break;// break the while true loop
                    }
                }

            }
        }
        if (!possible) return -1;
        else return minV;
    }

    void extensionExplore(List<FreeTree> database, List<Occurrence> occList, int support, List<Integer> frequency) {

//        System.out.println("========================start extension======================");
//        System.out.println(canonicalString);
        frequency.set(vCount, frequency.get(vCount) + 1);

        int firstPos = parent.get(vCount);
        int lastPos = vCount;

        Set<Integer> neighbors = new HashSet<Integer>();
        Iterator<Integer> iterator;

        for (int pos = firstPos; pos <= lastPos; pos ++) {
            Integer minV = -1;
            minV = possibleLeg(pos, minV);
//            System.out.println("minVertex");
            System.out.println(minV);
           // System.out.println(canonicalString);
           // System.out.println("the pos and minV ==========================" + pos + " " + minV);
            if (minV == -1) continue;

//            System.out.println("=========start the position :" + pos);

            List<SupportNode> potential = new ArrayList<SupportNode>();
            while(potential.size() < (Config.MaxVertex-Config.MinVertex+1)) potential.add(new SupportNode());

            for (int s = 0; s < occList.size(); s ++) {
//                System.out.println("start from : tid " + occList.get(s).tid + " occ index :" + occList.get(s).nodeIndex);

                neighbors.clear();
                // the pattern tree's neighbor
                for (int i = 0; i < adj.get(pos).size(); i ++) {
                    //System.out.println("pos and neighbor :" + pos + " " + adj.get(pos).get(i));
                    int tmpv = adj.get(pos).get(i);
                    neighbors.add(occList.get(s).nodeIndex.get(tmpv-1));
                }
               // System.out.println("all neighbors: ");
               // iterator = neighbors.iterator();
               // while(iterator.hasNext()) {
               //     System.out.println(iterator.next());
               // }
                //System.out.println("occ tid :" + occList.get(s).tid);
                FreeTree freeTree = database.get(occList.get(s).tid); // tid start from one
                int tmpv = occList.get(s).nodeIndex.get(pos-1);
                // System.out.println("node index :" + tmpv);

                // ? the position to be insert
                for (int i = 0; i < freeTree.adj.get(tmpv).size(); i ++) {
                    int dataTreeVertex = freeTree.adj.get(tmpv).get(i);

//                    if (tmpv > dataTreeVertex) continue;
                    //?
                    if ( !neighbors.contains(dataTreeVertex) ) {
//                        System.out.println("to be insert node's neighbor " + dataTreeVertex);
//                        System.out.println("the tmpv(nodeIndex) and dataTreeVertex " + tmpv + " " + dataTreeVertex);

                        int vl = freeTree.vLabel.get(dataTreeVertex);
                        int location = vl - Config.MinVertex;
//                        System.out.println("===========the new leg info :" + vl + " " + location);
                        // deep copy
                        //Occurrence tempOcc = occList.get(s);
                        if (freeTree.vLabel.get(dataTreeVertex) >= minV) {
                            // restrict
                            Occurrence tempOcc = new Occurrence();
                            tempOcc.tid = occList.get(s).tid;
                            for (int tmpi = 0; tmpi < occList.get(s).nodeIndex.size(); tmpi++) {
                                tempOcc.nodeIndex.add(occList.get(s).nodeIndex.get(tmpi));
                            }

                            tempOcc.nodeIndex.add(dataTreeVertex);
//                            System.out.println("tid " + tempOcc.tid + " tempOcc " + tempOcc.nodeIndex);
                            potential.get(location).occList.add(tempOcc);

                            if (potential.get(location).lastTid != tempOcc.tid) {
                                potential.get(location).lastTid = tempOcc.tid;
                                potential.get(location).support += 1;
                            }
                        }
                    }
                } // end position to be insert
            }
//            System.out.println();
//            System.out.println("output the potential elements " + potential.size());
//            for (int k = 0; k < potential.size(); k ++) {
//                SupportNode temp = potential.get(k);
//                // System.out.println("temp lasttid and support " + temp.lastTid + " " + temp.support);
//                for(int l = 0; l < temp.occList.size(); l ++) {
//                    System.out.println("tid of occ :" + temp.occList.get(l).tid);
//                    System.out.println("node index of occ: " + temp.occList.get(l).nodeIndex);
//                }
//            }
//            System.out.println("end of output");

            for (int k = 0; k < potential.size(); k ++) {
                SupportNode temp = potential.get(k);
                if (temp.support >= support) {
                    int tempV = Config.MinVertex + k;
//                    for (int i = 0; i < temp.occList.size(); i ++) {
//                        System.out.println(temp.occList.get(i).tid);
//                        System.out.println(temp.occList.get(i).nodeIndex);
//                    }
//                    System.out.println("===============================================");
//                    System.out.println("the current cannoical :" + canonicalString);
                    RBFCFTree rbfcTree = new RBFCFTree(this, tempV, pos);
//                    System.out.println("recursive explore : " + rbfcTree.canonicalString);
                    rbfcTree.extensionExplore(database, temp.occList, support, frequency);
                }
            }

        }

    }
}