package javaapplication7;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class JavaApplication7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        List<List<double[]>> rm05 = countdata(0.5);
        Draw draw1 = new Draw(rm05.get(0).get(0),rm05.get(1),"Графік для x, k/m=1 r/m=0.5", 500, 300,"%.1f", 10,10);
        Draw draw2 = new Draw(rm05.get(0).get(0),rm05.get(2),"Графік для енергій, k/m=1 r/m=0.5", 500, 300,"%.1f", 10,310);
        Draw draw3 = new Draw(rm05.get(1).get(0),rm05.get(3),"Фазовий портрет, k/m=1 r/m=0.5", 400, 400,"%.1f", 10,610);
        
       
        
        List<List<double[]>> rm005 = countdata(0.05);
        Draw draw1_1 = new Draw(rm005.get(0).get(0),rm005.get(1),"Графік для x, k/m=1 r/m=0.05", 500, 300,"%.1f", 510,10);
        Draw draw2_1 = new Draw(rm005.get(0).get(0),rm005.get(2),"Графік для енергій, k/m=1 r/m=0.05", 500, 300,"%.1f", 510,310);
        Draw draw3_1 = new Draw(rm005.get(1).get(0),rm005.get(3),"Фазовий портрет, k/m=1 r/m=0.05", 400, 400,"%.1f", 510,610);
        
        
        
        List<List<double[]>> rm1 = countdata(1);
        Draw draw1_2 = new Draw(rm1.get(0).get(0),rm1.get(1),"Графік для x, k/m=1 r/m=1", 500, 300,"%.1f", 1010,10);
        Draw draw2_2 = new Draw(rm1.get(0).get(0),rm1.get(2),"Графік для енергій, k/m=1 r/m=1", 500, 300,"%.1f", 1010,310);
        Draw draw3_2 = new Draw(rm1.get(1).get(0),rm1.get(3),"Фазовий портрет, k/m=1 r/m=1", 400, 400,"%.1f", 1010,610);
    }
    
    private static final double k=6;
    private static final double m=6;
    private static final double x0=11;
    private static final double dt=0.001;
    
    private static List<List<double[]>> countdata(double rm){
        double w0=Math.sqrt(k/m);
        double T=2*Math.PI*Math.sqrt(m/k)/dt;
        double[] t = new double[(int)Math.round(3*T)];
        double[] x = new double[(int)Math.round(3*T)];
        double[] v = new double[(int)Math.round(3*T)];
        double[] Ek = new double[(int)Math.round(3*T)];
        double[] Ep = new double[(int)Math.round(3*T)];
        double[] Et = new double[(int)Math.round(3*T)];
        t[0]=0;
        x[0]=x0;
        v[0]=0;
        Ek[0]=0;
        Ep[0]=x0*x0*k/2;
        Et[0]=Ep[0];
        for (int i=1;i<t.length;i++){
            t[i]=t[i-1]+dt;
            x[i]=x[i-1]+dt*v[i-1];
            v[i]=v[i-1]+(-w0*w0)*dt*x[i-1]-rm*dt*v[i-1];
            Ek[i]=v[i]*v[i]*m/2;
            Ep[i]=x[i]*x[i]*k/2;
            Et[i]=Ek[i]+Ep[i];
        }
        List <double[]> time = new ArrayList<>();
        time.add(t);
        List <double[]> xv = new ArrayList<>();
        xv.add(x);
        List <double[]> EEE = new ArrayList<>();
        EEE.add(Ek);
        EEE.add(Ep);
        EEE.add(Et);
        List<double[]> Phase = new ArrayList<>();
        Phase.add(v);
        List<List<double[]>> res = new ArrayList<>();
        res.add(time);
        res.add(xv);
        res.add(EEE);
        res.add(Phase);
        return res;
    }
}

class Draw extends javax.swing.JFrame {
 
    private double[] xn;
    private double y_min,y_max;
    private double x_min,x_max;
    private double[][] y;
    private int[] xInt;
    private int[][] yInt;
    //private int colwidth;
    private final Dimension size; 
    private final Dimension startPointXoY;
    private double scale, x_scale;
    String title;
    String form;
    int lx,ly;
    private static final Color[] colors = {Color.RED,Color.GREEN,Color.BLUE,Color.MAGENTA,Color.ORANGE,Color.PINK};
 
    public Draw(double[] xn, List<double[]> y_list, String title,int width, int height,String form, int lx, int ly) {
        this.xn = xn;
        this.y=new double[y_list.size()][y_list.get(0).length];
        this.y = y_list.toArray(this.y);
        this.title=title;
        this.form=form;
        this.lx=lx;
        this.ly=ly;
        x_min=xn[0];
        x_max=xn[0];
        for (int i=0;i<xn.length;i++){
            if (xn[i]>x_max) x_max=xn[i];
            if (xn[i]<x_min) x_min=xn[i];
        }
        y_min=y[0][0];
        y_max=y[0][0];
        for (int i=0;i<y.length;i++)
            for (int j=0;j<y[i].length;j++){
                if (y[i][j]>y_max) y_max=y[i][j];
                if (y[i][j]<y_min) y_min=y[i][j];
            }
        size=new Dimension(width,height);
        startPointXoY=new Dimension(40,size.height-30);
        this.scale=((double)y_max-y_min)/(size.height-60);
        this.x_scale = ((double)x_max-x_min)/(size.width-70);
        yInt = new int[y.length][y[0].length];
        xInt = new int[xn.length];
        reBuildArreys();
        initInterface();
    }
 
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,size.width,size.height);
        
        g.setColor(Color.BLACK);
        g.drawLine((int)Math.round(-x_min/x_scale)+startPointXoY.width, startPointXoY.height, (int)Math.round(-x_min/x_scale)+startPointXoY.width, 30);
        g.drawLine(startPointXoY.width, startPointXoY.height-(int)Math.round(-y_min/scale), size.width-30, startPointXoY.height-(int)Math.round(-y_min/scale));
        
        int x_tick_step=xn.length/10;
        
        for (int i = 0; i < 11; i++){
            int curx=(int)Math.round((x_max-x_min)/10*i/x_scale)+startPointXoY.width;
            g.drawLine(curx,startPointXoY.height-(int)Math.round(-y_min/scale),curx,startPointXoY.height - (int)Math.round(-y_min/scale) - 3);
            g.drawString(String.format(form, x_min+(x_max-x_min)/10*i),curx,startPointXoY.height-(int)Math.round(-y_min/scale) + 15);
        }
        for (int i = 0; i < 11; i++) 
            g.drawString(String.format(form, (y_min+(y_max-y_min)/10*i)),startPointXoY.width -35,startPointXoY.height + 5 - (int)Math.round((double)(size.height-60)/10*i));
        for (int i=0;i<y.length;i++){
            g.setColor(colors[i]);
            for (int j=0;j<y[i].length-1;j++)
                g.drawLine(xInt[j], yInt[i][j], xInt[j+1], yInt[i][j+1]);
        }
    }
 
    private void initInterface() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(size);
        setResizable(false);
        setTitle(title);
        setLocation(lx,ly);
        setVisible(true);
    }
 
    private void reBuildArreys() {
        for (int i = 0; i < y.length; i++)
            for (int j = 0; j < y[0].length;j++){
                yInt[i][j] = (int)Math.round((y[i][j]-y_min)/scale);
                yInt[i][j] = startPointXoY.height - (yInt[i][j]);
            }
        for (int i=0;i<xn.length;i++) 
            xInt[i]=(int)Math.round((xn[i]-x_min)/x_scale)+startPointXoY.width;
    }
}