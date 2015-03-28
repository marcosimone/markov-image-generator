import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class MarkovImageGUI extends JPanel implements ActionListener
{
	private JFrame frame;
	private JLabel originalImage; 
	private JLabel markovImage;
	private JLabel directions;
	private JTextField fuzzInput;
	private JButton markovIt;
	private BufferedImage originalPic;
	private BufferedImage markovedPic;
	private boolean saveFrames;
	private Checkbox save;
	private File selectedFile=null;
	private MarkovImage image;
	
	private int fuzzLevel;
	
	public MarkovImageGUI()
	{
		frame = new JFrame();
		frame.setContentPane(this);
		directions = new JLabel("How many Iterations:");
		originalImage = new JLabel("Original Image: ");
		markovImage = new JLabel("Markoved Image: ");
		fuzzInput = new JTextField("1",10);
		markovIt = new JButton("Markov It!");
		markovIt.addActionListener(this);
		image = new MarkovImage();
		save=new Checkbox("Save Individual Frames?", null, false);
		
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose a file to run through the filter");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "bmp");
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    selectedFile = fileChooser.getSelectedFile();
		}
		
		
		try {
            image.setImageIn(ImageIO.read(selectedFile));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Could not read in file D:");
            e.printStackTrace();
        }
		originalPic = image.getImageIn();
		Container container = frame.getContentPane();
		container.setLayout(null);
		container.add(directions);
		container.add(fuzzInput);
		container.add(markovIt);
		container.add(originalImage);
		container.add(markovImage);
		container.add(save);
		
		
		
		directions.setBounds(20,20,120,20);
		fuzzInput.setBounds(140,20,100,20);
		markovIt.setBounds(originalPic.getWidth()-20,originalPic.getHeight()+110,100,50);
		save.setBounds(originalPic.getWidth()+100, originalPic.getHeight()+120,160,25);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(originalPic.getWidth()*2+60,originalPic.getHeight()+200);
	    frame.setTitle("Markov Image Processing");
	    frame.setVisible(true);
	    frame.setResizable(false);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(originalPic, 20, 100, null);
		g.drawImage(markovedPic, originalPic.getWidth()+40, 100, null);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == markovIt)
		{
			image.setImageIn(originalPic);
			fuzzLevel = Integer.parseInt(fuzzInput.getText());
			saveFrames=save.getState();
	        image.setHeight(originalPic.getHeight());
	        image.setWidth(originalPic.getWidth());
	        image.setImageOut(originalPic); 
	        if(saveFrames){
	        	try {
	    			new File(new java.io.File( "." ).getCanonicalPath()+"/"+selectedFile.getName().substring(0,selectedFile.getName().indexOf('.'))).mkdir();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        	
	        }
	        for(int i=0;i<fuzzLevel;i++)
	        {
	        	BufferedImage output=image.doTheMarcov();
	        	if(saveFrames){
			    	try {
			    		String fileExtension = selectedFile.getName().substring(selectedFile.getName().indexOf('.'));
				        File outputfile = new File(selectedFile.getName().substring(0,selectedFile.getName().indexOf('.'))+"/"+selectedFile.getName().substring(0,selectedFile.getName().indexOf('.')) +"_"+ i +fileExtension);
				        ImageIO.write(output, fileExtension.substring(1), outputfile);
				        System.out.println("Markov Photo Saved");
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
			    }
	            image.setImageIn(output);
	            markovedPic = image.getImageIn();
	            originalPic = image.getImageOut();
	            
	            repaint();
	        }
	        System.out.println("All Done!");
		}
	}
	public int getFuzzLevel()
	{
		return fuzzLevel;
	}
}
