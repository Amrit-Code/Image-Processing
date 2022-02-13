import java.io.*;
import java.util.TreeSet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
public class Lab extends Component implements ActionListener {
    

 
    int opIndex;  //option index for 
    int lastOp;
    ArrayList<int[][][]> previous; 
    private BufferedImage bi, biFiltered, bi2, mask;   // the input image saved as bi;//
    int w, h;
    ImageProcesser ip = new ImageProcesser();
     
    public Lab() {
        try {
            bi = ImageIO.read(new File("C:/Users/Amrit Singh/Documents/Y3S2/Image Processing/Labs/Images/BaboonRGB.bmp"));
            //bi = ImageIO.read(new File("C:/Users/Amrit Singh/Documents/Y3S2/Image Processing/Labs/Images/Baboon.bmp"));
            bi2 = ImageIO.read(new File("C:/Users/Amrit Singh/Documents/Y3S2/Image Processing/Labs/Images/Peppers.bmp"));
            mask = ImageIO.read(new File("C:/Users/Amrit Singh/Documents/Y3S2/Image Processing/Labs/Images/mask.jpg"));
            previous = new ArrayList<>();
            w = bi.getWidth(null);
            h = bi.getHeight(null);
            System.out.println(bi.getType());
            if (bi.getType() != BufferedImage.TYPE_INT_RGB) {
                BufferedImage bi2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics big = bi2.getGraphics();
                big.drawImage(bi, 0, 0, null);
                biFiltered = bi = bi2;
            }
        } catch (IOException e) {      // deal with the situation that th image has problem;/
            System.out.println("Image could not be read");

            System.exit(1);
        }
    }                         
 
    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }
 

    String[] getDescriptions() {
        return ip.descs;
    }

    // Return the formats sorted alphabetically and in lower case
    public String[] getFormats() {
        String[] formats = {"bmp","gif","jpeg","jpg","png"};
        TreeSet<String> formatSet = new TreeSet<String>();
        for (String s : formats) {
            formatSet.add(s.toLowerCase());
        }
        return formatSet.toArray(new String[0]);
    }

    void setOpIndex(int i) {
        opIndex = i;
    }
 
    public void paint(Graphics g) { //  Repaint will call this function so the image will change.
        filterImage();      

        g.drawImage(biFiltered, 0, 0, null);
    }

    //************************************
    //  You need to register your functioin here
    //************************************
    public void filterImage() {
        if (opIndex == -1) {
            if(previous.size() == 0){return;}
            biFiltered = ip.convertToBimage(previous.get(previous.size() -1));
            previous.remove(previous.size() -1);
            return;
        }
        previous.add(ip.convertToArray(biFiltered));

        if((lastOp == opIndex)&&(opIndex == 2)){
            return;
        }else if(lastOp == 2){
            previous.remove(previous.size() -1);
            biFiltered = ip.convertToBimage(previous.get(previous.size() -1));
        }

        lastOp = opIndex;
        switch (opIndex) {
        case 0: biFiltered = bi; /* original */
                return; 
        case 1: biFiltered = ip.ImageNegative(bi); /* Image Negative */
                return;
        case 2:
            biFiltered = ip.compare(bi, biFiltered);
            return;
        case 3:
            biFiltered = ip.reScale(biFiltered);
            return;
        case 4:
            biFiltered = ip.Shift(biFiltered);
            return;
        case 5:
            biFiltered = ip.rndShiftReScale(biFiltered);
            return;
        case 6:
            biFiltered = ip.Addition(biFiltered,bi2);
            return;
        case 7:
            biFiltered = ip.Subtraction(biFiltered,bi2);
            return;
        case 8:
            biFiltered = ip.Multiplication(biFiltered,bi2);
            return;
        case 9:
            biFiltered = ip.Division(biFiltered,bi2);
            return;
        case 10:
            biFiltered = ip.Not(biFiltered);
            return;
        case 11:
            biFiltered = ip.And(biFiltered,bi2);
            return;
        case 12:
            biFiltered = ip.Or(biFiltered,bi2);
            return;
        case 13:
            biFiltered = ip.XOr(biFiltered,bi2);
            return;
        case 14:
            biFiltered = ip.ROI(biFiltered,mask);
            return;
        case 15:
            biFiltered = ip.Logfn(biFiltered);
            return;
        case 16:
            biFiltered = ip.Power(biFiltered);
            return;
        case 17:
            biFiltered = ip.RandomLookUp(biFiltered);
            return;
        case 18:
            biFiltered = ip.BitPlaneSlice(biFiltered);
            return;
        case 19:
            ip.printHistogramArray(ip.HistogramLS(biFiltered));
            return;
        case 20:
            ip.printHistogramArray(ip.HistogramNormalisedLS(biFiltered));
            return;
        case 21:
            biFiltered = ip.convolution(biFiltered, 0);
            return;
        case 22:
            biFiltered = ip.SaltAndPepper(biFiltered);
            return;
        case 23:
            biFiltered = ip.MinFilter(biFiltered);
            return;
        case 24:
            biFiltered = ip.MaxFilter(biFiltered);
            return;
        case 25: 
            biFiltered = ip.MidpointFilter(biFiltered);
            return;
        case 26:
            biFiltered = ip.MedianFilter(biFiltered);
            return;
        case 27:
            ip.MeanStandered(biFiltered);
            return;
        case 28:
            biFiltered = ip.SThreshold(biFiltered);
            return;
        case 29:
            biFiltered = ip.AThreshold(biFiltered);
            return;
        }
 
    }

 
     public void actionPerformed(ActionEvent e) {
         if(e.getActionCommand().equals("undo")){
            setOpIndex(-1);
            repaint();
         }else{
            JComboBox cb = (JComboBox)e.getSource();
            if (cb.getActionCommand().equals("SetFilter")) {
                setOpIndex(cb.getSelectedIndex());
                repaint();
            } else if (cb.getActionCommand().equals("Formats")) {
                String format = (String)cb.getSelectedItem();
                File saveFile = new File("savedimage."+format);
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(saveFile);
                int rval = chooser.showSaveDialog(cb);
                if (rval == JFileChooser.APPROVE_OPTION) {
                    saveFile = chooser.getSelectedFile();
                    try {
                        ImageIO.write(biFiltered, format, saveFile);
                    } catch (IOException ex) {
                    }
                }
            }
         }
    };
 
    public static void main(String s[]) {
        JFrame f = new JFrame("Image Processing Demo");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        Lab de = new Lab();
        f.add("Center", de);
        JComboBox choices = new JComboBox(de.getDescriptions());
        choices.setActionCommand("SetFilter");
        choices.addActionListener(de);
        JComboBox formats = new JComboBox(de.getFormats());
        formats.setActionCommand("Formats");
        formats.addActionListener(de);
        JPanel panel = new JPanel();

        JButton undo = new JButton();
        undo.setActionCommand("undo");
        undo.setText("Undo");
        undo.addActionListener(de);

        panel.add(choices);
        panel.add(new JLabel("Save As"));
        panel.add(formats);
        panel.add(undo);
        f.add("North", panel);
        f.pack();
        f.setVisible(true);
    }
}