package com.example.perjee.grafikapp;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Miran on 17/10/2016.
 */

public class CubeTriangleStrip {
    static final int VERTEX_POS_SIZE = 3;

    private final int VERTEX_COUNT = triangleStripData.length;

    private FloatBuffer vertexDataBuffer;
    private FloatBuffer colorDataBuffer;

    static float triangleStripData[] =
            {
                    0, 0,  0,
                    1, 0,  0,
                    1, 0, -1,
                    0, 0, -1,
                    0, 1,  0,
                    1, 1,  0,
                    1, 1, -1,
                    0, 1, -1
            };

    static float colorData[] = {   // in counterclockwise order:
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 1.0f
    };

    static short cubeElements[] = {
            // front
            0, 1, 2,
            2, 3, 0,
            // top
            3, 2, 6,
            6, 7, 3,
            // back
            7, 6, 5,
            5, 4, 7,
            // bottom
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
    int [] buffers = new int[3];
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
        GLES20.glGenBuffers(3, buffers, 0);
        // initialize vertex byte buffer for shape coordinates
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleStripData.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexDataBuffer = bb.asFloatBuffer();
        vertexDataBuffer.put(triangleStripData);
        vertexDataBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4, vertexDataBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        ByteBuffer bbcolor = ByteBuffer.allocateDirect(colorData.length * 4);
        bbcolor.order(ByteOrder.nativeOrder());
        colorDataBuffer = bbcolor.asFloatBuffer();
        colorDataBuffer.put(colorData);
        colorDataBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorDataBuffer.capacity() * 4, colorDataBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[2]);
        ByteBuffer bbelem = ByteBuffer.allocateDirect(cubeElements.length * 2);
        bbelem.order(ByteOrder.nativeOrder());
        ShortBuffer elemBuffer = bbelem.asShortBuffer();
        elemBuffer.put(cubeElements);
        elemBuffer.position(0);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, elemBuffer.capacity() * 2, elemBuffer, GLES20.GL_STATIC_DRAW);

        int vertexShader = CGRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = CGRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the shader to program
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glVertexAttribPointer(positionHandle, VERTEX_POS_SIZE, GLES20.GL_FLOAT, false, 0, 0);

        colorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        GLES20.glVertexAttribPointer(colorHandle, VERTEX_POS_SIZE, GLES20.GL_FLOAT, false, 0, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, cubeElements.length, GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

    }
}
