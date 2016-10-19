package com.example.perjee.grafikapp;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.*;

/**
 * Created by Miran on 17/10/2016.
 */

public class CubeTriangleStrip {
    static final int VERTEX_PER_COORD = 3;

    private final int VERTEX_COUNT = cubeData.length;

    private FloatBuffer vertexDataBuffer;
    private FloatBuffer colorDataBuffer;

    static float cubeData[] =
            {
                    0, 0,  0,//0
                    1, 0,  0,//1
                    1, 0, -1,//2
                    0, 0, -1,//3
                    0, 1,  0,//4
                    1, 1,  0,//5
                    1, 1, -1,//6
                    0, 1, -1 //7
            };

    static float colorData[] =
            {
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 1.0f
            };

    static short indexBuffer[] =
            {
                // bottom
                0, 1, 2,
                2, 3, 0,
                // back
                3, 2, 6,
                6, 7, 3,
                // top
                7, 6, 5,
                5, 4, 7,
                // front
                4, 5, 1,
                1, 0, 4,
                // left
                4, 0, 3,
                3, 7, 4,
                // right
                1, 5, 6,
                6, 2, 1
            };

    private final int mProgram;

    private final int [] buffers = new int[3];

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

    private int mMVPMatrixHandle;

    private int positionHandle;
    private int colorHandle;

    public CubeTriangleStrip() {
        glGenBuffers(3, buffers, 0);
        // initialize vertex byte buffer for shape coordinates
        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
        ByteBuffer bb = ByteBuffer.allocateDirect(cubeData.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexDataBuffer = bb.asFloatBuffer();
        vertexDataBuffer.put(cubeData);
        vertexDataBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4, vertexDataBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
        ByteBuffer bbcolor = ByteBuffer.allocateDirect(colorData.length * 4);
        bbcolor.order(ByteOrder.nativeOrder());
        colorDataBuffer = bbcolor.asFloatBuffer();
        colorDataBuffer.put(colorData);
        colorDataBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER, colorDataBuffer.capacity() * 4, colorDataBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[2]);
        ByteBuffer bbelem = ByteBuffer.allocateDirect(indexBuffer.length * 2);
        bbelem.order(ByteOrder.nativeOrder());
        ShortBuffer elemBuffer = bbelem.asShortBuffer();
        elemBuffer.put(indexBuffer);
        elemBuffer.position(0);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elemBuffer.capacity() * 2, elemBuffer, GL_STATIC_DRAW);

        int vertexShader = CGRenderer.loadShader(GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = CGRenderer.loadShader(GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = glCreateProgram();

        // add the shader to program
        glAttachShader(mProgram, vertexShader);
        glAttachShader(mProgram, fragmentShader);
        glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        glUseProgram(mProgram);

        mMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");

        positionHandle = glGetAttribLocation(mProgram, "vPosition");
        glEnableVertexAttribArray(positionHandle);
        glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
        glVertexAttribPointer(positionHandle, VERTEX_PER_COORD, GL_FLOAT, false, 0, 0);

        colorHandle = glGetAttribLocation(mProgram, "vColor");
        glEnableVertexAttribArray(colorHandle);
        glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
        glVertexAttribPointer(colorHandle, VERTEX_PER_COORD, GL_FLOAT, false, 0, 0);

        glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        glDrawElements(GL_TRIANGLE_STRIP, indexBuffer.length, GL_UNSIGNED_SHORT, 0);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }
}
