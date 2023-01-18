package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, KeyListener {

    public static FlappyBird flappyBird;

    public final int WIDTH = 800, HEIGHT = 800;

    public Renderer renderer;

    public Rectangle bird;

    public int ticks, sideMotion, score;

    public boolean gameOver, started ;

    public ArrayList<Rectangle> pipe;

    public Random rand;

    public FlappyBird(){
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();


        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH,HEIGHT);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setTitle("Flappy Bird");
        jframe.setVisible(true);

        rand = new Random();

        bird = new Rectangle(WIDTH/2 - 10,HEIGHT/2 - 10, 20, 20);
        pipe = new ArrayList<Rectangle>();
        addPipe(true);
        addPipe(true);
        addPipe(true);
        addPipe(true);

        timer.start();
    }

    public void addPipe(boolean start){
        int space = 250;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start) {
            pipe.add(new Rectangle(WIDTH + width + pipe.size() * 300, HEIGHT - height - 120, width, height));
            pipe.add(new Rectangle(WIDTH + width + (pipe.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else{
            pipe.add(new Rectangle(pipe.get(pipe.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            pipe.add(new Rectangle(pipe.get(pipe.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void paintPipes(Graphics g, Rectangle pipe){
        g.setColor(Color.green);
        g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
    }

    public void jump(){
        if(gameOver){
            bird = new Rectangle(WIDTH/2 - 10,HEIGHT/2 - 10, 20, 20);
            pipe.clear();

            addPipe(true);
            addPipe(true);
            addPipe(true);
            addPipe(true);

            gameOver = false;
        }


        if(!started){
            started = true;

        }
        else if(!gameOver){
            if(sideMotion > 0){
                sideMotion = 0;
            }
            sideMotion -= 10;

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ticks ++;

        int speed = 10;

        if(started){

        }

        for (int i = 0; i < pipe.size(); i++) {
            Rectangle pipes = pipe.get(i);
            pipes.x -= 10;
        }

        if(ticks % 2 == 0 && sideMotion < 15){
            sideMotion += 2;
        }

        for (int i = 0; i < pipe.size(); i++) {
            Rectangle pipes = pipe.get(i);

            if(pipes.x + pipes.width < 0){
                pipe.remove(pipes);

                if(pipes.y == 0){
                    addPipe(false);
                }

            }

        }

        bird.y += sideMotion;

        for (Rectangle pipes:pipe){

            if(pipes.y == 0 && bird.x + bird.width/2 > pipes.x + pipes.width/2 - 5 && bird.x + bird.width/2 < pipes.x+ pipes.width/2 + 10){
                score ++;
            }

            if(pipes.intersects(bird)){
                gameOver = true;
                bird.x = pipes.x - bird.width;
            }
        }

        if(bird.y > HEIGHT - 120 || bird.y < 0){
            gameOver = true;
        }
        if(bird.y + sideMotion >= HEIGHT -120){
            bird.y = HEIGHT - 120 - bird.height ;
        }

        renderer.repaint();
    }

    public void repaint(Graphics g) {
        /* Background */
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        /* Ground */
        g.setColor(Color.RED);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.white);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        /* Bird */
        g.setColor(Color.cyan);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for(Rectangle pipes : pipe){
            paintPipes(g, pipes);
        }

        g.setColor(Color.magenta);
        g.setFont(new Font("Arial", 1, 100));

        if (!started){
            g.drawString("Click To Start",120, HEIGHT/2 - 50);
        }

        if (gameOver){
            g.drawString("Game Over",120, HEIGHT/2 - 50);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if( gameOver && started){
            g.drawString(String.valueOf(score), WIDTH/2 - 25, 100);
        }



    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }

    }

    public static void main(String[] args){
        flappyBird = new FlappyBird();
    }

}
