
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eylül
 */
public class FruitBasket extends JPanel implements Runnable{

    int x,y;
    Basket b= new Basket();
    int number=5;
    Fruit fruit[]= new Fruit[5];
    Thread th;
    Graphics2D g;
    
    
  
    Motion mot[]= new Motion[number];
    boolean inside=false;
    Timer timer[] = new Timer[number];
    static int FWIDTH=1000,FHEIGHT=800;
    int in=0;
    int speed=10;
    int life=5;

    
    public void run(){
        try{
            while(true){
                th.sleep(speed);
                if(b.life==0){
                    Sound.BACKGROUND.stop();
                    Sound.GAMEOVER.play();
                    for(int i=0;i<fruit.length;++i){
                                fruit[i].reset();
                                mot[i].stop();
                            }
                           
                            JOptionPane.showMessageDialog(null, "YOUR SCORE: "+b.score, "GAME OVER!", HEIGHT);
                            int a=JOptionPane.showConfirmDialog(null, "RESTART?", "GAME OVER!", JOptionPane.OK_CANCEL_OPTION);
                            
                        if(a==JOptionPane.OK_OPTION){
                            for(int i=0;i<fruit.length;++i){
                               mot[i].setInitialDelay((i+1)*1500);
                                mot[i].move();
                            }
                            b.score=-10;
                            b.life=5;
                            b.updateScore();
                            
                        }
                        if(a==JOptionPane.CANCEL_OPTION){
                            System.exit(0);
                        }
                        }
                repaint();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
  
    
    public FruitBasket(){
        
        b.setx1(30);
        b.sety1(200);
        setOpaque(false);
     

       
        for(int i=0;i<fruit.length;++i){
            fruit[i]= new Fruit();
            fruit[i].sety1(30);
            fruit[i].reset();
        }
        
        for(in=0;in<fruit.length;++in){
            mot[in]= new Motion(fruit[in],b);
            mot[in].setInitialDelay((in+1)*1500);
            mot[in].move();
         
        }
        MouseListener ml = new MouseAdapter(){
            public void mousePressed(MouseEvent me){
                x=(int)me.getPoint().getX();
                if(b.contains(me.getPoint())){
                inside=true;}
                
            }
            public void mouseReleased(MouseEvent me){
                inside=false;
            }
        };
        addMouseListener(ml);
        
        MouseMotionListener mll = new MouseAdapter(){
            public void mouseDragged(MouseEvent me){
                if(inside==true){
                b.setx1((int)me.getPoint().getX());
                }
            }
        };
        addMouseMotionListener(mll);
        
        
        th= new Thread(this);
        th.start();
        Sound.BACKGROUND.loop();
    }
    
    
    public void paint(Graphics g2){
    g=(Graphics2D)g2;
  
    super.paint(g);
  

   
    b.sety1(getHeight()-35);
    
   
   
    b.Draw(g);    
    for(int i=0;i<fruit.length;++i){
                fruit[i].Draw(g);
    }
    }
    
    
    public static void main(String args[]){
    	
        JFrame jfm = new JFrame();
        jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfm.setLayout(new BorderLayout());
        jfm.getContentPane().setBackground(Color.CYAN);
        JLabel l= new JLabel();
        l.setBorder(BorderFactory.createTitledBorder(null, "Fruit Basket", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
        jfm.add(l,BorderLayout.NORTH);
        final FruitBasket fr = new FruitBasket();
        fr.b.setOutputComponent(l);
        KeyListener kl = new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
          if(ke.getKeyCode()==KeyEvent.VK_RIGHT){
              fr.b.setx1(fr.b.X1);
          }
          if(ke.getKeyCode()==KeyEvent.VK_LEFT){
              fr.b.setx1(fr.b.X1);
          }
            }
        };
        jfm.addKeyListener(kl);
        jfm.add(fr);
        jfm.setSize(FWIDTH, FHEIGHT);
        jfm.setResizable(false);
        jfm.setVisible(true);
        Sound.BACKGROUND.play();
    }
}

class Motion{
    
    ActionListener al;
    Timer tm;
    Fruit fruit;
    Basket b;
    boolean allowed=true;
    
    public Motion(Fruit fr,Basket bs){
    fruit=fr;
    b=bs;
    al=new ActionListener(){
        public void actionPerformed(ActionEvent ae){
            fruit.sety1(fruit.Y1+1);
            if(isAllowed()){
                if(fruit.dropsInBasket(b)){
                    Sound.BALL.play();
                    fruit.reset();
                    b.updateScore();
                }
            }
                if(fruit.r.y+fruit.r.height>b.r.y+b.r.height/2&&!fruit.dropsInBasket(b)){
                    allowed=false;
                    if(fruit.r.y+fruit.r.height*2>=275){
                        fruit.reset();
                        b.life-=1;
                        b.score-=10;
                        b.updateLife();
                    }
                }
            
                else{
                    allowed=true;
               }
        }
    };
    tm= new Timer(10,al);
    }
    void setInitialDelay(int i){
        tm.setInitialDelay(i);
    }
    
    boolean isAllowed(){
        return allowed;
    }
    
    void move(){
        tm.start();
    }
    
    void stop(){
        tm.stop();
    }
}

class  Fruit{

    int X1,Y1;
    Rectangle r;
    Random rn= new Random();

    
    Fruit(){
        
    }
    
    boolean dropsInBasket(Basket b){
        return r.intersects(b.r);
    }
    
    void setx1(int x){
       X1=x; 
    }
    
    void sety1(int y){
        Y1=y;
    }
    
    public void reset(){
        sety1(30);
        setx1(30+rn.nextInt(FruitBasket.FWIDTH-40));
    }
    
    void Draw(Graphics2D g){
   
        
        g.fillOval(X1, Y1, 10, 10);
        g.setColor(Color.RED);
        
      
        r=new Rectangle(X1,Y1,10,15/2);
    }
}

class Basket{

    int X1,Y1;
    Rectangle r;
    JLabel l;
    int score=0,life=5;
    Basket(){
    }
    
    void setx1(int x){
        X1=x;
    }
    
    void setOutputComponent(JLabel lb){
        l=lb;
    }
    
    void updateScore(){
      l.setText("SCORE: "+ (score+=10)+"          LIFE:"+(life));
    }
    
    void updateLife(){
        l.setText("SCORE: "+ (score+=10)+"          LIFE:"+(life));
    }
    
    void sety1(int y){
        Y1=y;
    }
    
    boolean contains(Point p){
        return r.contains(p);
    }
    
    void Draw(Graphics2D g){
    	
    	
    	GradientPaint gp1= new GradientPaint(X1-15,Y1-10,new Color(102,51,0),X1-15+30/2,Y1-10+30/2,new Color(102,51,0),true);
        g.setPaint(gp1);
     
        g.fillRect(X1-15, Y1, 40, 20);
        GradientPaint gp2= new GradientPaint(X1,Y1,new Color(200,142,83),X1,Y1+10,Color.YELLOW.darker());
        g.setPaint(gp2);
        g.fillOval(X1-15, Y1-10, 40, 20);
        r=new Rectangle(X1-15,Y1,40,20);
        g.setColor(Color.YELLOW);
    }
}
