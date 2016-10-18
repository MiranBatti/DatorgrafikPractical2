package com.example.perjee.grafikapp;

import android.opengl.GLES20;

/**
 * Created by Miran on 13/10/2016.
 */

public class Cube
{
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "attribute vec4 vPosition; \n" +
                    "attribute vec4 vColor; \n" +
                    "uniform mat4 uMVPMatrix;\n" +
                    "varying vec4 c; \n" +
                    "void main() { \n" +
                    "  c = vColor; \n" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;\n" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 c;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = c;\n" +
                    "}";

    private int mProgram;

    private Triangle triangle1;
    private Triangle triangle2;
    private Triangle triangle3;
    private Triangle triangle4;
    private Triangle triangle5;
    private Triangle triangle6;
    private Triangle triangle7;
    private Triangle triangle8;
    private Triangle triangle9;
    private Triangle triangle10;
    private Triangle triangle11;
    private Triangle triangle12;

    static float colorData[] = {   // in counterclockwise order:
            1.0f, 0.0f, 0.0f, 1.0f, // Red
            0.0f, 1.0f, 0.0f, 1.0f, // Green
            0.0f, 0.0f, 1.0f, 1.0f// Blue
    };

    public Cube()
    {
        createShaderProgram();

        float[] triangleFace1 = {0.0f, 0.0f, 0.0f, 1.0f,      1.0f, 0.0f, 0.0f, 1.0f,      0.0f, 1.0f, 0.0f, 1.0f};//front face
        float[] triangleFace2 = {1.0f, 1.0f, 0.0f, 1.0f,      1.0f, 0.0f, 0.0f, 1.0f,      0.0f, 1.0f, 0.0f, 1.0f};

        float[] triangleFace3 = {0.0f, 0.0f, 0.0f, 1.0f,      0.0f, 0.0f, -1.0f, 1.0f,     1.0f, 0.0f, 0.0f, 1.0f};//bottom
        float[] triangleFace4 = {1.0f, 0.0f, -1.0f, 1.0f,     0.0f, 0.0f, -1.0f, 1.0f,     1.0f, 0.0f, 0.0f, 1.0f};

        float[] triangleFace5 = {0.0f, 1.0f, 0.0f, 1.0f,      1.0f, 1.0f, 0.0f, 1.0f,      0.0f, 1.0f, -1.0f, 1.0f};//top
        float[] triangleFace6 = {1.0f, 1.0f, -1.0f, 1.0f,     1.0f, 1.0f, 0.0f, 1.0f,      0.0f, 1.0f, -1.0f, 1.0f};

        float[] triangleFace7 = {1.0f, 0.0f, 0.0f, 1.0f,      1.0f, 1.0f, 0.0f, 1.0f,      1.0f, 0.0f, -1.0f, 1.0f};//right
        float[] triangleFace8 = {1.0f, 1.0f, -1.0f, 1.0f,     1.0f, 1.0f, 0.0f, 1.0f,      1.0f, 0.0f, -1.0f, 1.0f};

        float[] triangleFace9 = {0.0f, 0.0f, 0.0f, 1.0f,      0.0f, 0.0f, -1.0f, 1.0f,     0.0f, 1.0f, 0.0f, 1.0f};//left
        float[] triangleFace10 = {0.0f, 1.0f, -1.0f, 1.0f,    0.0f, 0.0f, -1.0f, 1.0f,     0.0f, 1.0f, 0.0f, 1.0f};

        float[] triangleFace11 = {0.0f, 0.0f, -1.0f, 1.0f,    1.0f, 0.0f, -1.0f, 1.0f,     0.0f, 1.0f, -1.0f, 1.0f};//back
        float[] triangleFace12 = {1.0f, 1.0f, -1.0f, 1.0f,    1.0f, 0.0f, -1.0f, 1.0f,     0.0f, 1.0f, -1.0f, 1.0f};

        colorData = new float[]{   // in counterclockwise order:
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Green
                1.0f, 0.0f, 0.0f, 1.0f// Blue
        };
        triangle1 = new Triangle(triangleFace1, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.0f, 1.0f, 0.0f, 1.0f, // Red
                0.0f, 1.0f, 0.0f, 1.0f, // Green
                0.0f, 1.0f, 0.0f, 1.0f// Blue
        };
        triangle2 = new Triangle(triangleFace2, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.0f, 0.0f, 1.0f, 1.0f, // Red
                0.0f, 0.0f, 1.0f, 1.0f, // Green
                0.0f, 0.0f, 1.0f, 1.0f// Blue
        };
        triangle3 = new Triangle(triangleFace3, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.0f, 1.0f, 1.0f, 1.0f, // Red
                0.0f, 1.0f, 1.0f, 1.0f, // Green
                0.0f, 1.0f, 1.0f, 1.0f// Blue
        };
        triangle4 = new Triangle(triangleFace4, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.0f, 1.0f, 1.0f, 1.0f, // Red
                0.0f, 1.0f, 1.0f, 1.0f, // Green
                0.0f, 1.0f, 1.0f, 1.0f// Blue
        };
        triangle5 = new Triangle(triangleFace5, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.0f, 0.0f, 0.0f, 1.0f, // Red
                0.0f, 0.0f, 0.0f, 1.0f, // Green
                0.0f, 0.0f, 0.0f, 1.0f// Blue
        };
        triangle6 = new Triangle(triangleFace6, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                1.0f, 0.0f, 0.0f, 1.0f, // Red
                1.0f, 0.0f, 0.0f, 1.0f, // Green
                1.0f, 0.0f, 0.0f, 1.0f// Blue
        };
        triangle7 = new Triangle(triangleFace7, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.0f, 0.5f, 1.0f, 1.0f, // Red
                0.0f, 0.5f, 1.0f, 1.0f, // Green
                0.0f, 0.5f, 1.0f, 1.0f// Blue
        };
        triangle8 = new Triangle(triangleFace8, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.5f, 0.2f, 0.2f, 1.0f, // Red
                0.5f, 0.2f, 0.2f, 1.0f, // Green
                0.5f, 0.2f, 0.2f, 1.0f// Blue
        };
        triangle9 = new Triangle(triangleFace9, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.2f, 1.0f, 0.2f, 1.0f, // Red
                0.2f, 1.0f, 0.2f, 1.0f, // Green
                0.2f, 1.0f, 0.2f, 1.0f// Blue
        };
        triangle10 = new Triangle(triangleFace10, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                0.5f, 1.0f, 1.0f, 1.0f, // Red
                0.5f, 1.0f, 1.0f, 1.0f, // Green
                0.5f, 1.0f, 1.0f, 1.0f// Blue
        };
        triangle11 = new Triangle(triangleFace11, colorData, mProgram);

        colorData = new float[]{   // in counterclockwise order:
                1.0f, 0.5f, 1.0f, 1.0f, // Red
                1.0f, 0.5f, 1.0f, 1.0f, // Green
                1.0f, 0.5f, 1.0f, 1.0f// Blue
        };
        triangle12 = new Triangle(triangleFace12, colorData, mProgram);
    }

    public void draw(float[] CTM)
    {
        triangle1.draw(CTM);
        triangle2.draw(CTM);
        triangle3.draw(CTM);
        triangle4.draw(CTM);
        triangle5.draw(CTM);
        triangle6.draw(CTM);
        triangle7.draw(CTM);
        triangle8.draw(CTM);
        triangle9.draw(CTM);
        triangle10.draw(CTM);
        triangle11.draw(CTM);
        triangle12.draw(CTM);
    }

    private void createShaderProgram()
    {
        int vertexShader = CGRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = CGRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }
}
