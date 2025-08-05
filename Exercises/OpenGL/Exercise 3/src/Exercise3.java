

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;

//below static import instead of using GL2.GL_NICEST etc. in the code

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.util.ImmModeSink.GL_QUADS;

import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;


/**
 * Exercise 1
 */
@SuppressWarnings("serial")
public class Exercise3 extends GLCanvas implements GLEventListener, KeyListener {
    // Define constants for the top-level container
    private static String TITLE = "Exercise 3";
    private static final int CANVAS_WIDTH = 320;  // width of the drawable
    private static final int CANVAS_HEIGHT = 240; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second

    /** The entry main() method to setup the top-level container and animator */
    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the OpenGL rendering canvas
                GLCanvas canvas = new Exercise3();
                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

                // Create a animator that drives canvas' display() at the specified FPS.
                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                // Create the top-level container
                final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
                frame.getContentPane().add(canvas);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Use a dedicate thread to run the stop() to ensure that the
                        // animator stops before program exits.
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) animator.stop();
                                System.exit(0);
                            }
                        }.start();
                    }
                });
                frame.setTitle(TITLE);
                frame.pack();
                frame.setVisible(true);
                animator.start(); // start the animation loop
            }
        });
    }

    // Setup OpenGL Graphics Renderer

    private GLU glu;  // for the GL Utility

    /** Constructor to setup the GUI for this Component */
    public Exercise3() {
        this.addGLEventListener(this);
        this.addKeyListener(this); // for Handling KeyEvents
    }

    // ------ Implement methods declared in GLEventListener ------

    /**
     * Called back immediately after the OpenGL context is initialized. Can be used
     * to perform one-time initialization. Run only once.
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
        glu = new GLU();                         // get GL Utilities
        gl.glClearColor(0.1f, 0.1f, 0.1f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     *
     */
    int width,height;//fjern
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) height = 1;   // prevent divide by zero

        this.width= width;//fjern
        this.height=height;

        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        //gl.glViewport(0, 0, width/2, height/2);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
        glu.gluLookAt(-10.0, 10.0f, 10.0f, 0, 0, 0, 0, 1, 0);

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    private float[] speed = {0,0,0,0};

    private int[][] angles = {
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
    };

    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers


        // Top front left (1)
        gl.glLoadIdentity();
        gl.glRotatef(angles[0][0], 1, 0, 0);
        gl.glRotatef(angles[0][1], 0, 1, 0);
        gl.glRotatef(angles[0][2], 0, 0, 1);
        gl.glTranslatef(-1.1f, 1.1f, 1.1f);
        drawCube1(gl);

        // Top front right (2)
        gl.glLoadIdentity();
        gl.glRotatef(angles[1][0], 1, 0, 0);
        gl.glRotatef(angles[1][1], 0, 1, 0);
        gl.glRotatef(angles[1][2], 0, 0, 1);
        gl.glTranslatef(1.1f, 1.1f, 1.1f);
        drawCube2(gl);

        // Bottom front right (3)
        gl.glLoadIdentity();
        gl.glRotatef(angles[2][0], 1, 0, 0);
        gl.glRotatef(angles[2][1], 0, 1, 0);
        gl.glRotatef(angles[2][2], 0, 0, 1);
        gl.glTranslatef(1.1f, -1.1f, 1.1f);
        drawCube3(gl);

        // Bottom front left (4)
        gl.glLoadIdentity();
        gl.glRotatef(angles[3][0], 1, 0, 0);
        gl.glRotatef(angles[3][1], 0, 1, 0);
        gl.glRotatef(angles[3][2], 0, 0, 1);
        gl.glTranslatef(-1.1f, -1.1f, 1.1f);
        drawCube4(gl);

        // Bottom rear left (5)
        gl.glLoadIdentity();
        gl.glRotatef(angles[4][0], 1, 0, 0);
        gl.glRotatef(angles[4][1], 0, 1, 0);
        gl.glRotatef(angles[4][2], 0, 0, 1);
        gl.glTranslatef(-1.1f, -1.1f, -1.1f);
        drawCube5(gl);

        // Bottom rear right (6)
        gl.glLoadIdentity();
        gl.glRotatef(angles[5][0], 1, 0, 0);
        gl.glRotatef(angles[5][1], 0, 1, 0);
        gl.glRotatef(angles[5][2], 0, 0, 1);
        gl.glTranslatef(1.1f, -1.1f, -1.1f);
        drawCube6(gl);

        // Bottom top right (7)
        gl.glLoadIdentity();
        gl.glRotatef(angles[6][0], 1, 0, 0);
        gl.glRotatef(angles[6][1], 0, 1, 0);
        gl.glRotatef(angles[6][2], 0, 0, 1);
        gl.glTranslatef(1.1f, 1.1f, -1.1f);
        drawCube7(gl);

        // Bottom top left (8)
        gl.glLoadIdentity();
        gl.glRotatef(angles[7][0], 1, 0, 0);
        gl.glRotatef(angles[7][1], 0, 1, 0);
        gl.glRotatef(angles[7][2], 0, 0, 1);
        gl.glTranslatef(-1.1f, 1.1f, -1.1f);
        drawCube8(gl);



        angles[0][0] = angles[3][0] = angles[4][0] = angles[7][0] += speed[0];
        angles[0][2] = angles[1][2] = angles[2][2] = angles[3][2] += speed[1];
        angles[0][1] = angles[1][1] = angles[6][1] = angles[7][1] += speed[2];
        angles[2][1] = angles[3][1] = angles[4][1] = angles[5][1] += speed[3];
    }


    /**
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    @Override
    public void dispose(GLAutoDrawable drawable) { }

    private void drawCube1(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(1.0f, 0.0f, 0.0f); // red
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBottom(gl);

        // Front-face
        gl.glColor3f(1.0f, 1.0f, 1.0f); // white
        drawFront(gl);

        // Back-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }

    private void drawCube2(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(1.0f, 0.0f, 0.0f); // red
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBottom(gl);

        // Front-face
        gl.glColor3f(1.0f, 1.0f, 1.0f); // white
        drawFront(gl);

        // Back-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 1.0f, 0.0f); // green
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }

    private void drawCube3(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(1.0f, 0.5f, 0.0f); // orange
        drawBottom(gl);

        // Front-face
        gl.glColor3f(1.0f, 1.0f, 1.0f); // white
        drawFront(gl);

        // Back-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 1.0f, 0.0f); // green
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }

    private void drawCube4(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(1.0f, 0.5f, 0.0f); // orange
        drawBottom(gl);

        // Front-face
        gl.glColor3f(1.0f, 1.0f, 1.0f); // white
        drawFront(gl);

        // Back-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }

    private void drawCube5(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(1.0f, 0.5f, 0.0f); // orange
        drawBottom(gl);

        // Front-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawFront(gl);

        // Back-face
        gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }
    private void drawCube6(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(1.0f, 0.5f, 0.0f); // orange
        drawBottom(gl);

        // Front-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawFront(gl);

        // Back-face
        gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 1.0f, 0.0f); // green
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }

    private void drawCube7(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(1.0f, 0.0f, 0.0f); // red
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBottom(gl);

        // Front-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawFront(gl);

        // Back-face
        gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 1.0f, 0.0f); // green
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }
    private void drawCube8(GL2 gl) {
        // ----- Render the Color Cube -----


        gl.glBegin(GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(1.0f, 0.0f, 0.0f); // red
        drawTop(gl);

        // Bottom-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawBottom(gl);

        // Front-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawFront(gl);

        // Back-face
        gl.glColor3f(1.0f, 1.0f, 0.0f); // yellow
        drawBack(gl);

        // Left-face
        gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
        drawLeft(gl);

        // Right-face
        gl.glColor3f(0.0f, 0.0f, 0.0f); // black
        drawRight(gl);

        gl.glEnd(); // of the color cube
    }


    private void drawTop(GL2 gl) {
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
    }

    private void drawBottom(GL2 gl) {
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
    }

    private void drawFront(GL2 gl) {
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
    }

    private void drawBack(GL2 gl) {
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
    }

    private void drawLeft(GL2 gl) {
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
    }

    private void drawRight(GL2 gl) {
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case 'W' -> speed[0] = 1;
            case 'S' -> speed[0] = 0;
            case 'D' -> speed[1] = 1;
            case 'A' -> speed[1] = 0;
            case 'E' -> speed[2] = 1;
            case 'Q' -> speed[2] = 0;
            case 'X' -> speed[3] = 1;
            case 'Z' -> speed[3] = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
