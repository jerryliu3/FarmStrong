package com.example.jerry.FarmStrong;

/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

import android.graphics.Bitmap;
import android.graphics.RectF;
import java.util.List;
import java.util.Vector;

/**
 * Generic interface for interacting with different recognition engines.
 */
public interface Classifier {
    /**
     * An immutable result returned by a Classifier describing what was recognized.
     */
    public class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private final String id;

        /**
         * Display name for the recognition.
         */
        private final String title1;
        private  String title2;
        private  String title3;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private Float confidence1;
        private Float confidence2;
        private Float confidence3;



        /** Optional location within the source image for the location of the recognized object. */

        private float[] outputs;
        private Vector<String> labels;
        private RectF location;

        public Recognition(
                final String id, final String title1, final String title2, final String title3, final Float confidence1, final Float confidence2, final Float confidence3, final RectF location) {
            this.id = id;
            this.title1 = title1;
            this.title2 = title2;
            this.title3 = title3;
            this.confidence1 = confidence1;
            this.confidence2 = confidence2;
            this.confidence3 = confidence3;
            this.location = location;


        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title1;
        }

        public Float getConfidence() {
            return confidence1;
        }

        public RectF getLocation() {
            return new RectF(location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        @Override
        public String toString() {
           /* String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title1 != null) {
                resultString += title1 + " ";
            }

            if (confidence1 != null) {
                resultString += String.format("(%.1f%%) ", confidence1 * 100.0f);
            }

            if (location != null) {
                resultString += location + " ";
            }

            return resultString.trim();*/
            return "1. "+ title1 + " " + confidence1 + "\n2. "+ title2 + " " + confidence2 +"\n" +"3. "+ title3 + " " + confidence3 +" ";
        }


    }

    List<Recognition> recognizeImage(Bitmap bitmap);

    void enableStatLogging(final boolean debug);

    String getStatString();

    void close();
}
