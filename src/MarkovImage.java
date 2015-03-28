import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


class MutableInt {
    int value = 1; 
    public void increment(){ ++value;  }
    public int get(){ return value; }
}

public class MarkovImage
{
    /**
     * 
     */
    
    private static boolean saveFrames=false;
    private static HashMap<Integer, HashMap<Integer,MutableInt>> markovChain = new HashMap<Integer,HashMap<Integer,MutableInt>>();
    
    private static int fuzz;
    private static Random rnd = new Random();
    private static BufferedImage imageIn;
    private static BufferedImage imageOut;
    private static int height;
    private static int width;
    private static int[][] OriginalPixels;

    
    public MarkovImage()
    {
    	fuzz = 0;
    	imageIn = null;
    	imageOut = null;
    	height = 0;
    	width = 0;
    	OriginalPixels = null;
    }
    
    public BufferedImage doTheMarcov(){
         generateFreq();
         BufferedImage toSave = generateImage_accurate();
         return toSave;
        }
        
    public static void generateFreq(){
        //init HashMaps
        OriginalPixels = new int[width][height];
        ArrayList<Integer> nb;
        for(int h=0;h<height;h++){
            for(int w=0;w<width;w++){
                OriginalPixels[w][h]=imageIn.getRGB(w,h);
                markovChain.put(new Integer(imageIn.getRGB(w,h)),new HashMap<Integer,  MutableInt>());
            }
        }
        //fill frequencies 
        for(int h=0;h<height;h++){
            for(int w=0;w<width;w++){
                nb = getNeighbors(w,h,w,h);
                recurseNB(fuzz,nb,w,h);
            }
        }
    }
    
    static ArrayList<Integer> getNeighbors(int w, int h, int w_orig, int h_orig){
        ArrayList<Integer> neighbors= new ArrayList<Integer>();
        if(w==0){
            if(h==0){
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w+1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w+1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w+1);
                neighbors.add(h);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h+1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h+1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h+1);
            }else if(h==height-1){
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h-1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h-1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h-1);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w+1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w+1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w+1);
                neighbors.add(h);
                
            }else{
                
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w+1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w+1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w+1);
                neighbors.add(h);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h+1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h+1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h+1);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h-1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h-1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h-1);
            }
            
        }else if(h==0){
            if(w==width-1){
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w-1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w-1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w-1);
                neighbors.add(h);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h+1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h+1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h+1);
            }else{
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w-1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w-1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w-1);
                neighbors.add(h);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w+1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w+1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w+1);
                neighbors.add(h);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h+1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h+1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h+1);
            }
            
        }else if(w==width-1){
            if(h==height-1){
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h-1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h-1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h-1);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w-1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w-1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w-1);
                neighbors.add(h);
            }else{
                MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w-1][h]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w-1][h]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w-1);
                neighbors.add(h);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h+1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h+1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h+1);
                count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h-1]));
                if (count == null) {
                    markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h-1]), new MutableInt());
                }
                else {
                    count.increment();
                }
                neighbors.add(w);
                neighbors.add(h-1);
            }
            
        }else if(h==height-1){
            MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h-1]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h-1]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w);
            neighbors.add(h-1);
            count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w+1][h]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w+1][h]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w+1);
            neighbors.add(h);
            count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w-1][h]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w-1][h]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w-1);
            neighbors.add(h);
        }else{
            MutableInt count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h+1]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h+1]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w);
            neighbors.add(h+1);
            count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w][h-1]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w][h-1]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w);
            neighbors.add(h-1);
            count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w+1][h]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w+1][h]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w+1);
            neighbors.add(h);
            count = markovChain.get(OriginalPixels[w_orig][h_orig]).get(new Integer(OriginalPixels[w-1][h]));
            if (count == null) {
                markovChain.get(OriginalPixels[w_orig][h_orig]).put(new Integer(OriginalPixels[w-1][h]), new MutableInt());
            }
            else {
                count.increment();
            }
            neighbors.add(w-1);
            neighbors.add(h);
        }
        return neighbors;
    }
    
    public static void recurseNB(int counter, ArrayList<Integer> neigh, int w_orig, int h_orig){
        ArrayList<Integer> temp= new ArrayList<Integer>();
        if(counter==0){
            return;
        }else{
            for(int i=0; i<neigh.size();i+=2){
                temp.addAll(getNeighbors(neigh.get(i),neigh.get(i+1), w_orig, h_orig));
            }
            counter--;
            recurseNB(counter,temp, w_orig, h_orig);
        }   
    }
    
    public static int traverse(HashMap<Integer, MutableInt> possibles){
        Collection<MutableInt>vals=possibles.values();
        int total=0;
        for (MutableInt element : vals) {
            total+=element.get();
        }
        int rand = rnd.nextInt(total)+1; 
        int counter=0;
        for (MutableInt element : vals) {
            counter+=element.get();
            if(counter>=rand){
                for (Entry<Integer, MutableInt> entry : possibles.entrySet()) {
                    if (element.equals(entry.getValue())) {
                        return entry.getKey();
                    }
                }
            }
        }
        System.out.println("FATAL ERROR");
        return -1;
    }
    
    public static BufferedImage generateImage_accurate()
    {
        
        BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageOut.setRGB(0,0,imageIn.getRGB(0,0));
        int currentPixel = imageOut.getRGB(0,0);
        for(int h = 0; h < height; h++){
            if(h != 0){
                HashMap<Integer, MutableInt> possibles = markovChain.get(imageOut.getRGB(0,h-1));
                imageOut.setRGB(0, h,traverse(possibles));
            }
            for(int w = 1; w < width; w++){
                HashMap<Integer, MutableInt> possibles = markovChain.get(currentPixel);
                imageOut.setRGB(w,h, traverse(possibles));                 
                currentPixel = imageIn.getRGB(w,h);
            }
        }
        return imageOut;
    }
    public static BufferedImage generateImage()
    {
        
        BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        imageOut.setRGB(0,0,imageIn.getRGB(0,0));
        int currentPixel = imageOut.getRGB(0,0);
        for(int h = 0; h < height; h++){
            if(h != 0){
                HashMap<Integer, MutableInt> possibles = markovChain.get(imageOut.getRGB(0,h-1));
                imageOut.setRGB(0, h,traverse(possibles));
            }
            for(int w = 1; w < width; w++){
                HashMap<Integer, MutableInt> possibles = markovChain.get(currentPixel);
                imageOut.setRGB(w,h, traverse(possibles));                 
                currentPixel = imageIn.getRGB(w,h);
            }
        }
        return imageOut;
    }
    public void setImageIn(BufferedImage in)
    {
    	imageIn = in;
    }
    public void setImageOut(BufferedImage out)
    {
    	imageOut = out;
    }
    public void setHeight(int h)
    {
    	height = h;
    }
    public void setWidth(int w)
    {
    	width = w;
    }
    public BufferedImage getImageIn()
    {
    	return imageIn;
    }
    public BufferedImage getImageOut()
    {
    	return imageOut;
    }
}
