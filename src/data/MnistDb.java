package data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MnistDb {

    private List<int[]> imagesList;
    private List<String> labelsList;

    public List<int[]> getImagesList() {
        return imagesList;
    }
    public List<String> getLabelsList() {
        return labelsList;
    }

    public List<String> getLabels (InputStream fileInput, int labelQty)
    {
        int nRead;
        int offset = 8;
        labelsList = new ArrayList<String>();
        byte [] labels = new byte [labelQty];

        try {
            while (true) {
                fileInput.skip(offset);
                nRead = fileInput.read(labels, 0, labels.length);
                if (nRead==-1) break;
            }
        }

        catch (IOException io)
        {
            System.out.println("Something went wrong while loading...");
        }

        finally {
            try {
                fileInput.close();
            } catch (IOException e) {
                System.out.println("Cannot close the stream...");
            }
        }

        for (int i = 0; i < labelQty; i++)
        {
            labelsList.add(String.valueOf(labels[i]));
        }

        return labelsList;
    }

    public List<int[]> getImages (InputStream fileInput, int imgQty)
    {
        int nRead;
        int offset = 16;
        imagesList = new ArrayList<int[]>();
        byte[][] images1 = new byte[imgQty][784];
        int[][] images2 = new int[imgQty][784];

        try
        {
            fileInput.skip(offset);
                for (int i=0; i<imgQty; i++) {
                    nRead = fileInput.read(images1[i], 0, 784);

                    for (int j=0; j<images1[i].length; j++){
                    images2[i][j] = images1[i][j]&0xff;
                    }
                    imagesList.add(images2[i]);
                    if (nRead==-1) break;
                }
        }
            catch (IOException io)
            {
                System.out.println("Something went wrong while loading...");
            }

            finally {
            try {
                fileInput.close();
            } catch (IOException e) {
                System.out.println("Cannot close the stream...");
            }
        }

        return imagesList;
    }
}
