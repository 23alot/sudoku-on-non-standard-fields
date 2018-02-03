package sudoku.newgame.dancinglinks;
import sudoku.newgame.dancinglinks.*;
import sudoku.newgame.sudoku.Board;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by boscatov on 06.12.2017.
 */
public class StupidTest {
    public static void main(String[] args) {


//        byte[] prpr = {0,0,0,0,0,0,0,0,0,
//                1,1,1,1,1,1,1,1,1,
//                2,2,2,2,2,2,2,2,2,
//                3,3,3,3,3,3,3,3,3,
//                4,4,4,4,4,4,4,4,4,
//                5,5,5,5,5,5,5,5,5,
//                6,6,6,6,6,6,6,6,6,
//                7,7,7,7,7,7,7,7,7,
//                8,8,8,8,8,8,8,8,8};
        byte[] prpr = {0,0,0,1,1,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                3,3,3,4,4,4,5,5,5,
                3,3,3,4,4,4,5,5,5,
                3,3,3,4,4,4,5,5,5,
                6,6,6,7,7,7,8,8,8,
                6,6,6,7,7,7,8,8,8,
                6,6,6,7,7,7,8,8,8};
        int[] input = {0,6,0,0,1,0,5,0,0,
                1,0,0,0,0,5,0,7,0,
                0,9,8,0,0,4,0,0,0,
                8,0,0,0,0,0,0,0,0,
                0,4,0,0,0,0,0,0,6,
                0,0,0,0,0,3,0,9,1,
                9,0,0,3,0,2,0,0,0,
                0,0,7,5,0,8,0,0,3,
                0,0,0,1,0,0,2,0,4};
//        byte[] prpr = {0,0,1,1,
//                0,0,1,1,
//                2,2,3,3,
//                2,2,3,3
//        };
        int r = 0;
        int t = 0;
        for(int i = 0; i < 81;++i){
            r += (i*(i/9+1));
            t += ((80-i)*(i/9+1));
        }
        System.out.println(r + " " + t);
        List<Long> time = new ArrayList<>();
        for(int e = 0; e < 100_000;++e) {
            long start = System.currentTimeMillis();
            byte n = 9;
            Algorithm algo = new Algorithm(new Structure((byte) 9, prpr));
            Board bd = algo.create(55, 81, prpr,0);
            long finish = System.currentTimeMillis();
            System.out.println("time: " + (finish - start));
            time.add(finish - start);
            for (int i = 0; i < n; ++i) {
                for (int z = 0; z < n; ++z) {
                    System.out.print(bd.cells[i][z].value + " ");
                }
                System.out.println();
            }
            System.out.println();
            for (int i = 0; i < n; ++i) {
                for (int z = 0; z < n; ++z) {
                    System.out.print(bd.cells[i][z].correctValue + " ");
                }
                System.out.println();
            }
        }
        long finito = 0;

        for(int i = 0; i < 100_000;++i)
            if(time.get(i) > finito)
            finito = time.get(i);
        System.out.println("Max time: " + finito);
    }
    static void find(Node nde, Structure st){
        Node temp = st.root;
        temp.upHead = (HeadNode)temp;
        int i = 0;
        do{
            if(!temp.upHead.deleted) {
                Node nd = temp;
                do {
                    if(nd.down==nde){
                        System.out.println("up"+nd.upHead.position);
                    }
                    if(nd.up==nde){
                        System.out.println("down"+nd.upHead.position);
                    }
                    if(nd.left==nde){
                        System.out.println("right"+nd.leftHead.position);
                    }
                    if(nd.right==nde){
                        System.out.println("left"+nd.leftHead.position);
                    }
                    nd = nd.down;
                }while(nd!=temp);
            }
            i++;
            temp= temp.right;
        }while(temp!=st.root);
    }
    static void toTest(Link link){
        link = link.prev;
    }
}
class Link{
    Link prev;
    int value;
    Link(int value){
        this.value = value;
    }
    Link(Link link, int value){
        this.prev = link;
        this.value = value;
    }
}
