package org.tensorflow.lite.codedirect.dermiscan.ui.main.features.add;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Locale;

public interface Classifier {
    class Recognition {

        private final String id;
        private final String title;
        private final Float confidence;

        public Recognition(final String id, final String title, final Float confidence) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (confidence != null) {
                resultString += String.format(
                        Locale.getDefault(),"(%.1f%%) ", confidence * 100.0f);
            }

            return resultString.trim();
        }
    }


    List<Recognition> recognizeImage(Bitmap bitmap);

    void close();
}
