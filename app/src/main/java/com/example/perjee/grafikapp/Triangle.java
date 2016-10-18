package com.example.perjee.grafikapp;

import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by perjee on 10/6/16.
 */

public class Triangle {

    static final int VERTEX_POS_SIZE = 4;
    static final int COLOR_SIZE = 4;

    static final int VERTEX_ATTRIB_SIZE = VERTEX_POS_SIZE;
    static final int COLOR_ATTRIB_SIZE = COLOR_SIZE;

    private final int VERTEX_COUNT;

    private FloatBuffer vertexDataBuffer;
    private FloatBuffer colorDataBuffer;


    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    private final int mProgram;

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private int positionHandle;
    private int colorHandle;

    public Triangle(float[] triangleData, float[] colorData, int shaderProgram) {
        VERTEX_COUNT = triangleData.length / VERTEX_ATTRIB_SIZE;
        mProgram = shaderProgram;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bbv = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleData.length * 4);
        // use the device hardware's native byte order
        bbv.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexDataBuffer = bbv.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexDataBuffer.put(triangleData);
        // set the buffer to read the first coordinate
        vertexDataBuffer.position(0);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bbc = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                colorData.length * 4);
        // use the device hardware's native byte order
        bbc.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        colorDataBuffer = bbc.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        colorDataBuffer.put(colorData);
        // set the buffer to read the first coordinate
        colorDataBuffer.position(0);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, VERTEX_POS_SIZE,
                GLES20.GL_FLOAT, false,
                VERTEX_ATTRIB_SIZE * 4, vertexDataBuffer);

        colorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle, COLOR_SIZE,
                GLES20.GL_FLOAT, false,
                COLOR_ATTRIB_SIZE * 4, colorDataBuffer);

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

    }
}
